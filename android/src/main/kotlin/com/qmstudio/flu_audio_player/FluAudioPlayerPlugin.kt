package com.qmstudio.flu_audio_player

import android.util.Log
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FluAudioPlayerPlugin */
class FluAudioPlayerPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var soundPoolQueue: SoundPoolQueue

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flu_audio_player")
    channel.setMethodCallHandler(this)
    soundPoolQueue = SoundPoolQueue()
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    Log.e("flu_audio_player", "onMethodCall:${call.method} arguments: ${call.arguments} " )
    when(call.method){
      "playAudios"->{
        val arguments = call.arguments as List<*>?
        if(arguments?.isNotEmpty() == true){
          val list = mutableListOf<Audio>()
          for ( e in arguments){
            if(e is Map<*, *>){
              val uuid = e["uuid"] as String? ?: ""
              val asset = e["asset"] as String? ?: ""
              val assetPackage = e["assetPackage"] as String? ?: ""
              val duration = e["duration"]  as Int? ?: 0
              val path = e["path"]  as String? ?: ""
              val speed = e["speed"]  as Double? ?: 1
              list.add(Audio(uuid,asset,assetPackage,path,duration.toLong(),speed.toDouble()))
            }
          }
          soundPoolQueue.addAudios(list)
        }
      }
      "resetSoundPool"->{
        soundPoolQueue.reset()
      }
      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
