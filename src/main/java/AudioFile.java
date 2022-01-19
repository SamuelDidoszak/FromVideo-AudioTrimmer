import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AudioFile {
    private String fileName;
    private Path path;
    /**file length in seconds*/
    private float length;

    public AudioFile(String fileName) {
        this.fileName = fileName;
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

        System.out.println("length: " + length);
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
            e.printStackTrace();
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
