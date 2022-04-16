package com.abysl.assetmanager.services

import com.soywiz.korau.sound.NativeSoundProvider
import com.soywiz.korau.sound.playing
import kotlinx.coroutines.*
import java.io.File

class AudioService {
    val nsp = NativeSoundProvider()

    var audioJob = CoroutineScope(Dispatchers.IO)

    var playing = false

    fun toggleAudio(file: File){
        if(playing){
            stop()
        }else {
            playAudio(file)
        }
    }

    fun playAudio(file: File){
        playing = true
        audioJob = CoroutineScope(Dispatchers.IO)
        audioJob.launch {
            val sound = nsp.createSound(file.readBytes(), name = file.name)
            sound.playAndWait()
            playing = false
        }
    }

    fun stop(){
        playing = false
        audioJob.cancel()
    }
}