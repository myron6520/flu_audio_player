class Audio {
  late String uuid;
  late String asset;
  late String assetPackage;
  late String path;
  late int duration;
  late double speed;
  Audio.fromMap(Map info) {
    uuid = "${info['uuid'] ?? ''}";
    path = "${info['path'] ?? ''}";
    asset = "${info['asset'] ?? ''}";
    assetPackage = "${info['assetPackage'] ?? ''}";
    duration = int.tryParse("${info['duration'] ?? ''}") ?? 0;
    speed = double.tryParse("${info['speed'] ?? ''}") ?? 1;
  }
  Map toMap() => {
        'uuid': uuid,
        'asset': asset,
        'assetPackage': assetPackage,
        'path': path,
        'duration': duration,
        'speed': speed,
      };
}
