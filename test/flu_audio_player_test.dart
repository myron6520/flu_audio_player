import 'package:flutter_test/flutter_test.dart';
import 'package:flu_audio_player/flu_audio_player.dart';
import 'package:flu_audio_player/flu_audio_player_platform_interface.dart';
import 'package:flu_audio_player/flu_audio_player_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFluAudioPlayerPlatform
    with MockPlatformInterfaceMixin
    implements FluAudioPlayerPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FluAudioPlayerPlatform initialPlatform = FluAudioPlayerPlatform.instance;

  test('$MethodChannelFluAudioPlayer is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFluAudioPlayer>());
  });

  test('getPlatformVersion', () async {
    FluAudioPlayer fluAudioPlayerPlugin = FluAudioPlayer();
    MockFluAudioPlayerPlatform fakePlatform = MockFluAudioPlayerPlatform();
    FluAudioPlayerPlatform.instance = fakePlatform;

    expect(await fluAudioPlayerPlugin.getPlatformVersion(), '42');
  });
}
