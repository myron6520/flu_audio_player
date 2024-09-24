import Flutter
import UIKit

public class FluAudioPlayerPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flu_audio_player", binaryMessenger: registrar.messenger())
    let instance = FluAudioPlayerPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getPlatformVersion":
      result("iOS " + UIDevice.current.systemVersion)
    case "playAudios":
        {
            
        }
    default:
      result(FlutterMethodNotImplemented)
    }
  }
}
