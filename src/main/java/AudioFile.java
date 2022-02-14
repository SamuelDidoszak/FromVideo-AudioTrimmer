import javafx.scene.media.Media;

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
    /**file length in seconds*/
    private float length;
    private Media audioFile;

    private String tempPath;

    public String getTempPath() {
        return tempPath;
    }

    public Media getAudioFile() {
        return audioFile;
    }

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

    /**
     * Copies the provided file to /target directory
     */
    public void copyToDirectory() {
        String outputDirectory = getClass().getResource("audiowaveform.exe").toString();
        outputDirectory = outputDirectory.substring(0, outputDirectory.lastIndexOf("/") + 1);
        Path outputPath = Paths.get(outputDirectory.substring(6) + fileName);
        try {
            Files.copy(path, outputPath);
        } catch (IOException e) {
            if(e.getClass().equals(FileAlreadyExistsException.class))
                System.out.println("File already exists");
            else
                e.printStackTrace();
        }
        tempPath = outputPath.toString();

//        System.out.println(outputPath);
//        audioFile = new Media(new File(outputPath.toString()).toURI().toString());
//        System.out.println("????????");
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
            audioInputStream = AudioSystem.getAudioInputStream(path.toFile());
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
            AudioSystem.write(trimmedInputStream, AudioSystem.getAudioFileFormat(path.toFile()).getType(), new File(path.getParent().toString() + "\\" + fileName.substring(0, fileName.indexOf(".")) + "Trimmed" + fileName.substring(fileName.indexOf("."))));
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
