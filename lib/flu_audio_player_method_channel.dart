import 'package:flu_audio_player/audio.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flu_audio_player_platform_interface.dart';

/// An implementation of [FluAudioPlayerPlatform] that uses method channels.
class MethodChannelFluAudioPlayer extends FluAudioPlayerPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flu_audio_player');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> playAudios(List<Audio> audio) async {
    await methodChannel.invokeMethod(
        'playAudios', audio.map((e) => e.toMap()).toList());
  }
}
