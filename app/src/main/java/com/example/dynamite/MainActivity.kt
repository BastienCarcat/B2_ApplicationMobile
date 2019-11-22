package com.example.dynamite

import android.os.Bundle

import android.util.Log

import androidx.appcompat.app.AppCompatActivity

import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {

                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")

                    // Now you can start interacting with App Remote
                    connected()

                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MyActivity", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }

    private fun connected() {
        // Play a playlist
        mSpotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")

        // Subscribe to PlayerState
        mSpotifyAppRemote!!.playerApi
            .subscribeToPlayerState()
            .setEventCallback { playerState ->
                val track = playerState.track
                if (track != null) {
                    Log.d("MainActivity", track.name + " by " + track.artist.name)
                }
            }
    }

    companion object {

        private val CLIENT_ID = "deed684a8da74f5f8b507cdb4a41832e"
        private val REDIRECT_URI = "com.example.myapp://callback"
    }
}
