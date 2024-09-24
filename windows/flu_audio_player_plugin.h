#ifndef FLUTTER_PLUGIN_FLU_AUDIO_PLAYER_PLUGIN_H_
#define FLUTTER_PLUGIN_FLU_AUDIO_PLAYER_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace flu_audio_player {

class FluAudioPlayerPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  FluAudioPlayerPlugin();

  virtual ~FluAudioPlayerPlugin();

  // Disallow copy and assign.
  FluAudioPlayerPlugin(const FluAudioPlayerPlugin&) = delete;
  FluAudioPlayerPlugin& operator=(const FluAudioPlayerPlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace flu_audio_player

#endif  // FLUTTER_PLUGIN_FLU_AUDIO_PLAYER_PLUGIN_H_
