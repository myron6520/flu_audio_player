class Audio {
  late String uuid;
  late String asset;
  late String assetPackage;
  late String path;
  late int duration;
  late double speed;
  late String text;
  late String textZh;
  late String textEn;
  late String textVi;
  late String textZhCn;
  late String textZhTw;
  Audio.fromMap(Map info) {
    uuid = "${info['uuid'] ?? ''}";
    path = "${info['path'] ?? ''}";
    asset = "${info['asset'] ?? ''}";
    assetPackage = "${info['assetPackage'] ?? ''}";
    duration = int.tryParse("${info['duration'] ?? ''}") ?? 0;
    speed = double.tryParse("${info['speed'] ?? ''}") ?? 1;
    text = "${info['text'] ?? ''}";
    textZh = "${info['text_zh'] ?? ''}";
    textEn = "${info['text_en'] ?? ''}";
    textVi = "${info['text_vi'] ?? ''}";
    textZhCn = "${info['text_zh-CN'] ?? ''}";
    textZhTw = "${info['text_zh-TW'] ?? ''}";
  }
  Map toMap() => {
        'uuid': uuid,
        'asset': asset,
        'assetPackage': assetPackage,
        'path': path,
        'duration': duration,
        'speed': speed,
        'text': text,
        'text_zh': textZh,
        'text_en': textEn,
        'text_vi': textVi,
        'text_zh-CN': textZhCn,
        'text_zh-TW': textZhTw,
      };
  bool get isAsset => asset.isNotEmpty;
  bool get isLocal => path.isNotEmpty;
  bool get useTts => !isAsset && !isLocal;
}
