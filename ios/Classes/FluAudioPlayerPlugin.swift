import Flutter
import UIKit
import AVFoundation

public class FluAudioPlayerPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flu_audio_player", binaryMessenger: registrar.messenger())
    let instance = FluAudioPlayerPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }
 lazy var queuePlayer: AVQueuePlayer = AVQueuePlayer()

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getPlatformVersion":
      result("iOS " + UIDevice.current.systemVersion)
    case "playAudios":
        var audios:[Audio] = [];
        if let args = call.arguments as? [[String:Any]]{
            for item in args {
                let audio = Audio();
                audio.uuid = item["uuid"] as? String ?? ""
                audio.asset = item["asset"] as? String ?? ""
                audio.assetPackage = item["assetPackage"] as? String ?? ""
                audio.duration = item["duration"] as? Int ?? 0
                audio.path = item["path"] as? String ?? ""
                audio.speed = item["speed"] as? Double ?? 0
                audios.append(audio)
                let path = URL(fileURLWithPath: audio.path)
                let playerItem = AVPlayerItem(url: path)
                queuePlayer.insert(playerItem, after: nil)
            }
        }
        queuePlayer.play()
//        if audios.count > 0{
//            SoundPoolQueue.shared.addAudios(audios: audios)
//        }
    case "resetSoundPool":
        SoundPoolQueue.shared.reset()
    default:
      result(FlutterMethodNotImplemented)
    }
  }
}
