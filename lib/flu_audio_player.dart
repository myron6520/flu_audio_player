import 'package:flu_audio_player/audio.dart';

import 'flu_audio_player_platform_interface.dart';

class FluAudioPlayer {
  Future<String?> getPlatformVersion() {
    return FluAudioPlayerPlatform.instance.getPlatformVersion();
  }

  Future<void> playAudios(List<Audio> audios) async {
    return await FluAudioPlayerPlatform.instance.playAudios(audios);
  }
}
