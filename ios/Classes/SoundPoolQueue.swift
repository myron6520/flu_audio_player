//
//  SoundPoolQueue.swift
//  Pods
//
//  Created by 钟园园 on 2024/10/8.
//
import AudioToolbox
class SoundPoolQueue{
    static let shared: SoundPoolQueue = {
           let instance = SoundPoolQueue()
           return instance
       }()
    private var _audios:[Audio] = []
    private var _soundIds:[String:SystemSoundID] = [:];
    private var isPlaying = false
    func addAudios(audios:[Audio])  {
        _audios.append(contentsOf: audios)
        if isPlaying {
            return
        }
        doPlay()
    }
    func reset() {
        _audios.removeAll()
        isPlaying = false
    }
    private func doPlay(){
        if _audios.isEmpty{
            isPlaying = false
            return
        }
        isPlaying = true
        let audio = _audios.first
        print("doPlay:\(String(describing: audio?.path))")
        let uuid = audio?.uuid ?? ""
        if let soundId = _soundIds[uuid]{
            AudioServicesPlaySystemSound(soundId)
        }else{
            let path = URL(fileURLWithPath: audio?.path ?? "")
            var soundId: SystemSoundID = 0
            let status = AudioServicesCreateSystemSoundID(path as CFURL, &soundId)
            if status == kAudioServicesNoError{
                AudioServicesPlaySystemSound(soundId)
                AudioServicesAddSystemSoundCompletion(soundId, nil, nil, soundCompletionCallback, UnsafeMutableRawPointer(Unmanaged.passUnretained(self).toOpaque()))
                _soundIds[uuid] = soundId
            }
        }
        _audios.remove(at: 0);
    
//        DispatchQueue.main.asyncAfter(deadline: .now()+Double(audio?.duration ?? 0)/1000.0, execute: { [weak self] in
//            self?.doPlay()
//        })
    }
    let soundCompletionCallback: AudioServicesSystemSoundCompletionProc = { (soundID, clientData) in
        
        // 在主线程上执行更新
        DispatchQueue.main.async {
            SoundPoolQueue.shared.doPlay()
        }
    }
}

