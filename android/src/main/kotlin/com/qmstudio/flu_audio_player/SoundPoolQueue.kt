package com.qmstudio.flu_audio_player

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlin.math.max
import kotlin.math.min

class SoundPoolQueue {
    companion object{
        const val TAG = "SoundPoolQueue"
    }
    private val soundPool: SoundPool by lazy {
        val pool = SoundPool.Builder()
            .setMaxStreams(30)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build()
            )
            .build()
        return@lazy pool
    }
    private val mHandle = Handler(Looper.myLooper()!!)
    private var isPlaying:Boolean = false
    private val soundQueue:MutableList<Audio> = mutableListOf()
    private val soundsIds = mutableMapOf<String,Int>()
    fun addAudios(audios:List<Audio>){
        soundQueue.addAll(audios)
        if(isPlaying)return
        doPlay()
    }
    fun reset(){
        soundsIds.clear()
        soundQueue.clear()
        isPlaying = false
    }
    private fun doPlay(){
        if(soundQueue.isNotEmpty()){
            isPlaying = true
            val audioInfo = soundQueue.first()
            Log.e(TAG, "doPlay: ${audioInfo.path}" )
            val soundId = soundsIds[audioInfo.uuid];
            if(soundId != null){
                var speed = audioInfo.speed
                speed = min(speed,2.0)
                speed = max(speed,0.5)
                val streamId =  soundPool.play(soundId, 1f, 1f, 1, 0, speed.toFloat())
                if(streamId>0){
                    mHandle.postDelayed({
                        soundQueue.removeFirst()
                        doPlay()
                    }, audioInfo.duration)
                    return
                }
            }
            soundPool.setOnLoadCompleteListener { _, sampleId, _ ->
                run {
                    if (sampleId > 0) {
                        soundsIds[audioInfo.uuid] = sampleId
                        var speed = audioInfo.speed
                        speed = min(speed,2.0)
                        speed = max(speed,0.5)

                        soundPool.play(sampleId, 1f, 1f, 1, 0, speed.toFloat())
                        mHandle.postDelayed({
                            soundQueue.removeFirst()
                            doPlay()
                        }, audioInfo.duration)

                    } else {
                        soundQueue.removeFirst()
                        doPlay()
                    }
                }
            }
            Log.e(TAG, "doPlay: load ${audioInfo.path}")
            soundPool.load(audioInfo.path,1)
        }else{
            isPlaying = false
        }
    }
}