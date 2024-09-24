#include "include/flu_audio_player/flu_audio_player_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "flu_audio_player_plugin.h"

void FluAudioPlayerPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  flu_audio_player::FluAudioPlayerPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
