//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <flu_audio_player/flu_audio_player_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) flu_audio_player_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "FluAudioPlayerPlugin");
  flu_audio_player_plugin_register_with_registrar(flu_audio_player_registrar);
}
