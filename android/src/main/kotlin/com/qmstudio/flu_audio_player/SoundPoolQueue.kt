package com.qmstudio.flu_audio_player

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import kotlin.math.max
import kotlin.math.min

class SoundPoolQueue {
    companion object {
        const val TAG = "SoundPoolQueue"
        private const val MIN_SPEED = 0.5
        private const val MAX_SPEED = 2.0
        private const val PLAY_RETRY_DELAY_MS = 50L
    }

    private val lock = Any()
    private var generation = 0
    private var isPlaying = false
    private val soundQueue = mutableListOf<Audio>()
    private val soundIdsByPath = mutableMapOf<String, Int>()
    private val pendingLoads = mutableMapOf<Int, Audio>()
    private val activeStreamIds = mutableSetOf<Int>()

    private val schedulerThread = HandlerThread("SoundPoolScheduler").apply { start() }
    private val scheduler = Handler(schedulerThread.looper)
    private var soundPool: SoundPool? = null

    private fun ensureSoundPool(): SoundPool {
        return soundPool ?: SoundPool.Builder()
            .setMaxStreams(30)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .build()
            .also { pool ->
                pool.setOnLoadCompleteListener { _, sampleId, status ->
                    scheduler.post { onLoadComplete(sampleId, status) }
                }
                soundPool = pool
            }
    }

    fun addAudios(audios: List<Audio>) {
        scheduler.post {
            synchronized(lock) {
                soundQueue.addAll(audios)
            }
            if (!isPlaying) {
                doPlay()
            }
        }
    }

    fun reset() {
        scheduler.post { resetOnScheduler() }
    }

    fun release() {
        scheduler.post {
            resetOnScheduler()
            soundPool?.release()
            soundPool = null
            schedulerThread.quitSafely()
        }
    }

    private fun resetOnScheduler() {
        generation++
        scheduler.removeCallbacksAndMessages(null)
        synchronized(lock) {
            soundPool?.let { pool ->
                for (streamId in activeStreamIds) {
                    pool.stop(streamId)
                }
            }
            activeStreamIds.clear()
            pendingLoads.clear()
            soundIdsByPath.clear()
            soundQueue.clear()
            isPlaying = false
        }
    }

    private fun doPlay() {
        val audioInfo: Audio
        val gen: Int
        synchronized(lock) {
            if (soundQueue.isEmpty()) {
                isPlaying = false
                return
            }
            isPlaying = true
            audioInfo = soundQueue.first()
            gen = generation
        }

        val pool = ensureSoundPool()
        Log.d(TAG, "doPlay: ${audioInfo.path}")

        val soundId = synchronized(lock) { soundIdsByPath[audioInfo.path] }
        if (soundId != null) {
            if (playAndScheduleNext(audioInfo, soundId, gen)) {
                return
            }
            scheduler.postDelayed({
                if (gen == generation) doPlay()
            }, PLAY_RETRY_DELAY_MS)
            return
        }

        val loadId = pool.load(audioInfo.path, 1)
        if (loadId == 0) {
            Log.w(TAG, "load failed: ${audioInfo.path}")
            removeHeadAndContinue(gen)
            return
        }
        synchronized(lock) {
            pendingLoads[loadId] = audioInfo
        }
        Log.d(TAG, "loading: ${audioInfo.path}")
    }

    private fun onLoadComplete(sampleId: Int, status: Int) {
        val audioInfo = synchronized(lock) {
            pendingLoads.remove(sampleId)
        } ?: return

        val gen = synchronized(lock) { generation }
        if (status != 0 || sampleId <= 0) {
            Log.w(TAG, "onLoadComplete failed: ${audioInfo.path}")
            removeHeadAndContinue(gen)
            return
        }

        val stillCurrent = synchronized(lock) {
            soundQueue.isNotEmpty() && soundQueue.first().uuid == audioInfo.uuid
        }
        synchronized(lock) {
            soundIdsByPath[audioInfo.path] = sampleId
        }
        if (!stillCurrent) {
            return
        }
        if (!playAndScheduleNext(audioInfo, sampleId, gen)) {
            scheduler.postDelayed({
                if (gen == generation) doPlay()
            }, PLAY_RETRY_DELAY_MS)
        }
    }

    private fun playAndScheduleNext(audioInfo: Audio, soundId: Int, gen: Int): Boolean {
        val pool = soundPool ?: return false
        val speed = clampSpeed(audioInfo.speed)
        val streamId = pool.play(soundId, 1f, 1f, 1, 0, speed.toFloat())
        if (streamId == 0) {
            return false
        }
        synchronized(lock) {
            activeStreamIds.add(streamId)
        }
        val delayMs = playbackDelayMs(audioInfo.duration, speed)
        scheduler.postDelayed({
            synchronized(lock) {
                activeStreamIds.remove(streamId)
            }
            if (gen != generation) return@postDelayed
            removeHeadAndContinue(gen)
        }, delayMs)
        return true
    }

    private fun removeHeadAndContinue(gen: Int) {
        if (gen != generation) return
        synchronized(lock) {
            if (soundQueue.isNotEmpty()) {
                soundQueue.removeAt(0)
            }
        }
        doPlay()
    }

    private fun clampSpeed(speed: Double): Double {
        return max(MIN_SPEED, min(MAX_SPEED, speed))
    }

    private fun playbackDelayMs(duration: Long, speed: Double): Long {
        if (duration <= 0) return 0
        return (duration / speed).toLong().coerceAtLeast(0)
    }
}
