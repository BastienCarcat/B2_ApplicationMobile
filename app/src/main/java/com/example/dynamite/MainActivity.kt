package com.example.dynamite;

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.activity_main.*
import org.jmusixmatch.MusixMatch


class MainActivity : AppCompatActivity() {

    private val clientId = "111d8a20647a466cabf20bbb9363f347" //Clé API Spotify
    private val redirectUri = "com.example.dynamite://callback" //Url redirect Spotify DEv
    var apiKey = "c31a28b6305998f504e30ac76e2d5354" //Clé API musixmatch
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
            val playlistURI = "spotify:playlist:37i9dQZF1DWVuV87wUBNwc" //URL de la playlist à jouer
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
                        val lyrics = musixMatch.getLyrics(trackMatch.track.trackId) //je recupere les parole à partir des donnée de la recherche d'au dessus
                        runOnUiThread{
                            lyricsDisplay.text = "Paroles: " + lyrics.lyricsBody // Je récupère uniquement les paroles et je les affiche
                            lyricsDisplay.movementMethod = ScrollingMovementMethod() //Je permet de scroller dans le conteneur des paroles
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
            infos.startAnimation(animation) //je démarre mon animation sur le titre de la musique (quand je clique sur le bouton play)
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
