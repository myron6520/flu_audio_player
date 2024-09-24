import 'package:flu_audio_player/audio.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flu_audio_player_method_channel.dart';

abstract class FluAudioPlayerPlatform extends PlatformInterface {
  /// Constructs a FluAudioPlayerPlatform.
  FluAudioPlayerPlatform() : super(token: _token);

  static final Object _token = Object();

  static FluAudioPlayerPlatform _instance = MethodChannelFluAudioPlayer();

  /// The default instance of [FluAudioPlayerPlatform] to use.
  ///
  /// Defaults to [MethodChannelFluAudioPlayer].
  static FluAudioPlayerPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FluAudioPlayerPlatform] when
  /// they register themselves.
  static set instance(FluAudioPlayerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> playAudios(List<Audio> audio) {
    throw UnimplementedError('playAudios() has not been implemented.');
  }
}
