package com.example.dynamite;

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity
import android.util.Log;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.AnimationUtils
import org.jmusixmatch.MusixMatch
import org.jmusixmatch.entity.lyrics.Lyrics
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MotionEvent
import android.view.View
import org.jmusixmatch.MusixMatchException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val clientId = "111d8a20647a466cabf20bbb9363f347"
    private val redirectUri = "com.example.dynamite://callback"
    var apiKey = "c31a28b6305998f504e30ac76e2d5354"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun connected() {
        val musixMatch = MusixMatch(apiKey)
        spotifyAppRemote?.let {
            val playlistURI = "spotify:playlist:1UPPptBf3XiBT2qcTAJzw7"
            it.playerApi.play(playlistURI)
            it.playerApi.toggleShuffle()
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d("MainActivity", track.name + " par " + track.artist.name)
                val infoMusic = track.name + " par " + track.artist.name //concaténation de l'artiste et du titre
                infos.text=infoMusic //J'affiche le nom de la musique à chaque changement

                //je fais une recherche
                try{
                    Thread{
                        val trackMatch = musixMatch.getMatchingTrack(track.name, track.artist.name)
                        //je recupere les parole à partir des donnée de la recherche
                        val lyrics = musixMatch.getLyrics(trackMatch.track.trackId)
                        runOnUiThread{
                            // Je récupère uniquement les paroles et je les affiche
                            lyricsDisplay.text = "Paroles: " + lyrics.lyricsBody
                        }
                    }.start()
                }
                catch(e : Exception){
                    throw  e
                }

            }
        }
        // Gestion des bouton play/pause, next, back
        startButton.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up) //je charge mon animation
            infos.startAnimation(animation) //je démarre mon animation
            spotifyAppRemote?.playerApi?.playerState?.setResultCallback { result ->
                if (result.isPaused) {
                    spotifyAppRemote?.let { it.playerApi.resume() }
                } else
                    spotifyAppRemote?.let { it.playerApi.pause() }
                }
        }
        nextButton.setOnClickListener {
            spotifyAppRemote?.let { it.playerApi.skipNext() }
        }
        backButton.setOnClickListener {
            spotifyAppRemote?.let { it.playerApi.skipPrevious() }
        }

    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }

    }
}
