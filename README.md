# Dynamite
This repository contains the code of the MobileApp project. Produced in 2019.  
The app is a simple spotify player with lyrics got from MusixMatch API.

## How to Start
```
git clone https://github.com/AugustinRibreau/B2_ApplicationMobile.git
```

## ❗️ More Informations ❗️
- To make this app working correctly you need spotify installed on your device and to be connected to an account on it (premium is better);
- You need to create your own Spotify API Key (Check the useful links), and replace it in the "MainActivity.kt" at <a href="https://github.com/AugustinRibreau/B2_ApplicationMobile/blob/1a1c57649d7399ed10385640a617daac59f854be/app/src/main/java/com/example/dynamite/MainActivity.kt#L19" target="_blank">line 19</a>;
- You need to add "com.example.dynamite://callback" as Redirect URL in Spotify Developper Dashboard (see usefull links);
- The API Keys provided (Spotify and MusixMatch) work on any device, but with MusixMatch you can only make 2000 Api Calls per day and 500 Lyrics display per day;

## ✏️ Explanation of design choices
- Dark background and light writing to see well in full sunlight;
- Button shadow and sound on click for the user feedback;
- Scrollable lyrics to read them easily -> improvable with auto scroll lyrics;
- Responsive content adaptable to any devices;

## Useful Links
- https://developer.musixmatch.com/documentation <- MusixMatch Official Site Documentation
- https://github.com/sachin-handiekar/jMusixMatch <- MusixMatch Community Documentation by sachin-Handiekar
- https://developer.spotify.com/documentation/android/quick-start/ <- Quick start (create an API Key)
- https://spotify.github.io/android-sdk/app-remote-lib/docs/index.html <- Spotify API Documentation
- https://developer.spotify.com/dashboard/ <- Spotify Developper Dashboard to manage you API Keys for your apps

## How to contribute

Just send a pull-request 😉

## Authors
- <b>Augustin Ribreau</b> - <i>Developper</i> - <a href="https://www.linkedin.com/in/augustinribreau/">Linkedin</a>.
- <b>Kévin Gillet</b> - <i>Developper</i> - <a href="https://www.linkedin.com/in/k%C3%A9vin-gillet-50b25b175/">Linkedin</a>.
- <b>Gégoire Barre</b> - <i>Developper</i> - <a href="https://github.com/crolse">Github</a>.
- <b>Bastien Carcat</b> - <i>Developper</i> - <a href="https://www.linkedin.com/in/bastien-carcat-1a8707175/">Linkedin</a>.
