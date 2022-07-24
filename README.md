# FromVideo AudioTrimmer
A GUI tool for trimming audio and converting Video to audio

## Features
- Trims the audio to its desired length
- Supports both audio and video files
- Shows the amplitude plot of the audio.
- The plot can be zoomed and dragged


## Usage
Drop an audio or video file onto the application and click the waveform to set start and end trim position

Converted and trimmed file will automatically save to the folder from which the file was dragged and dropped.


- Left mouse button sets the start position
- Right mouse button sets the end position
- Mouse scroll to zoom
- Drag by clicking and dragging
- Space button to play audio


## Screenshots
<img src="https://user-images.githubusercontent.com/70522994/161356511-4c163f68-5a68-4ac1-8150-eceb6723aa07.png" width="45%"></img> <img src="https://user-images.githubusercontent.com/70522994/161356514-45beb45a-7f91-4783-a80c-ca849fa843d9.png" width="45%"></img> <img src="https://user-images.githubusercontent.com/70522994/161356516-4c29bd54-219f-4034-a706-1f781ee381a2.png" width="45%"></img> <img src="https://user-images.githubusercontent.com/70522994/161360150-8f53a26d-3a3d-4a65-aba0-e6f7f7f568ad.gif" width="45%"></img> 



## Tech
This project uses several tools and libraries:

- [JavaFX 17.0.1]
- Maven
- [FFmpeg]
- [Audiowaveform 1.4.2]
- [JFXUtils]
- [JFoenix]

JFXUtils library was modified to include bounded [zoom] and [dragging] capabilities.











  [JavaFX 17.0.1]: <https://openjfx.io/>
  [FFmpeg]: <https://ffmpeg.org/>
  [Audiowaveform]: <https://github.com/bbc/audiowaveform>
  [JFoenix]: <https://github.com/sshahine/JFoenix>
  [JFXUtils]: <https://github.com/gillius/jfxutils>
  [zoom]: <https://github.com/SamuelDidoszak/AudioCutter/blob/master/src/main/java/org/didoszak/audiocutter/CustomClasses/ChartZoomManagerCustom.java>
  [dragging]: <https://github.com/SamuelDidoszak/AudioCutter/blob/master/src/main/java/org/didoszak/audiocutter/CustomClasses/ChartPanManagerCustom.java>






