package org.didoszak.audiocutter;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AudioFile {
    private String fileName;
    private Path path;
    private Path currentPath;
    /**file length in seconds*/
    private float length;
    private Media audioFile;
    private MediaPlayer player;

    public AudioFile(Path path) {
        this.path = path;
        this.fileName = path.getFileName().toString();
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(path.toFile());
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        AudioFormat format = audioInputStream.getFormat();

        length = path.toFile().length() / (format.getFrameSize() * format.getFrameRate());
    }

    public AudioFile(Path path, Path currentPath) {
        this(currentPath);
        this.path = path;
        this.currentPath = currentPath;
    }

    /**
     * Copies the provided file to /target directory
     */
    public void copyToDirectory() {
        Path outputPath = Paths.get(new File("resources").getAbsolutePath() + "\\" + fileName);
        try {
            Files.copy(path, outputPath);
        } catch (IOException e) {
            if(e.getClass().equals(FileAlreadyExistsException.class))
                System.out.println("File already exists");
            else
                e.printStackTrace();
        }
        currentPath = outputPath;
    }

    /**
     *
     * @param start start second
     * @param end if it's the same as the total audioFile length it's omitted
     */
    public void trimAudioFile(double start, double end) {
        AudioInputStream audioInputStream = null;
        AudioInputStream trimmedInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(currentPath.toFile());
            AudioFormat format = audioInputStream.getFormat();

            int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate();
                //  skips in seconds. Decimal part is left
            audioInputStream.skip((long)start * bytesPerSecond);
            long framesOfAudioToCopy;
            if(end != length)
                framesOfAudioToCopy = (long) (end - start) * (int) format.getFrameRate();
            else
                framesOfAudioToCopy = audioInputStream.getFrameLength() - (long) (start * format.getFrameRate());
            trimmedInputStream = new AudioInputStream(audioInputStream, format, framesOfAudioToCopy);

            System.out.println("file output path: " + path.getParent().toString() + "\\" + fileName.substring(0, fileName.indexOf(".")) + "Trimmed" + fileName.substring(fileName.indexOf(".")));
            AudioSystem.write(trimmedInputStream, AudioSystem.getAudioFileFormat(currentPath.toFile()).getType(), new File(path.getParent().toString() + "\\" + fileName.substring(0, fileName.indexOf(".")) + "Trimmed" + fileName.substring(fileName.indexOf("."))));
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (audioInputStream != null) try {
                audioInputStream.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            if (trimmedInputStream != null) try {
                trimmedInputStream.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Plays audio with a custom duration. Option to pause can be enabled inside
     * @param start start position in seconds
     * @param end end position in seconds
     */
    public void playAudio(double start, double end) {
        boolean enablePause = false;

        if(player == null) {
            audioFile = new Media(new File(currentPath.toString()).toURI().toString());
            player = new MediaPlayer(audioFile);
        }

        if(!enablePause) {
            if(player.getStatus() == MediaPlayer.Status.PLAYING && (int) Math.ceil(player.getCurrentTime().toMillis()) < (int) Math.ceil(player.getStopTime().toMillis()) - 5)
                player.stop();
            else {
                player.seek(new Duration(start * 1000));
                player.setStartTime(new Duration(start * 1000));
                player.setStopTime(new Duration(end * 1000));
                player.play();
            }
        } else {
            switch (player.getStatus()) {
                case READY:
                    player.setStartTime(new Duration(start * 1000));
                    player.setStopTime(new Duration(end * 1000));
                    player.play();
                    break;
                case PLAYING:
                    System.out.println((int) player.getCurrentTime().toMillis() + "\t\t" + (int) player.getStopTime().toMillis());
                    if ((int) player.getCurrentTime().toMillis() != (int) player.getStopTime().toMillis()) {
                        player.pause();
                    } else {
                        player.seek(new Duration(start * 1000));
                        player.setStartTime(new Duration(start * 1000));
                        player.setStopTime(new Duration(end * 1000));
                        player.play();
                    }
                    break;
                case PAUSED:
                    player.play();
                    break;
            }
        }
    }

    public void removeFile() {
        // Has to force garbage collection
        System.gc();
        File currentFile = new File(currentPath.toString());
        currentFile.delete();
    }

    public String getFileName() {
        return fileName;
    }

    public Path getFolderPath() {
        return path.getParent();
    }

    public float getLength() {
        return length;
    }
}
