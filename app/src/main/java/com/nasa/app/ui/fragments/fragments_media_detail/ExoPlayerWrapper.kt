package com.nasa.app.ui.fragments.fragments_media_detail

import android.net.Uri
import com.google.android.exoplayer2.*
import com.nasa.app.ui.fragments.di.FragmentScope
import javax.inject.Inject

@FragmentScope
class ExoPlayerWrapper @Inject constructor(private val exoPlayer: ExoPlayer) {

    fun playPlayer(uri: Uri, time: Long) {
        val mediaItem: MediaItem = MediaItem.fromUri(uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.seekTo(time)
        exoPlayer.playWhenReady = true
    }

    fun getPlayer(): ExoPlayer {
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

