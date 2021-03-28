package com.nasa.app.ui

import android.content.Context
import com.google.android.exoplayer2.*

class ExoMediaPlayer {

    companion object {
        private const val TAG = "MediaPlayerTag"
    }

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var context: Context

    fun playPlayer(url: String, time: Long) {
        val mediaItem: MediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.seekTo(time)
        exoPlayer.playWhenReady = true
    }

    fun getPlayer(context: Context): ExoPlayer {
        this.context = context
        exoPlayer = SimpleExoPlayer.Builder(context).build()
        return exoPlayer
    }

    fun getPlayerTime(): Long {
        return exoPlayer.contentPosition
    }

    fun pausePlayer() {
        exoPlayer.pause()
    }

    fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.release()
    }

    fun addListener(listener: Player.EventListener) {
        exoPlayer.addListener(listener)
    }
}

