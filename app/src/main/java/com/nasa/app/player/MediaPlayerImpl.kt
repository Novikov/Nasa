package com.raywenderlich.funtime.device.player

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer


class MediaPlayerImpl : MediaPlayer {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var context: Context

    private fun initializePlayer() {
        exoPlayer = SimpleExoPlayer.Builder(context).build()
    }

    override fun play(url: String) {
        val mediaItem: MediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun getPlayerImpl(context: Context): ExoPlayer {
        this.context = context
        initializePlayer()
        return exoPlayer
    }

    override fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.release()
    }
}