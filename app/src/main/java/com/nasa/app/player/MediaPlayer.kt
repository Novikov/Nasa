package com.raywenderlich.funtime.device.player

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer

interface MediaPlayer {
    fun play(url: String)
    fun getPlayerImpl(context: Context): ExoPlayer
    fun releasePlayer()
}