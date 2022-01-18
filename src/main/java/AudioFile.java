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

    public AudioFile(String fileName) {
        this.fileName = fileName;
    }

    public AudioFile(Path path) {
        this.path = path;
        this.fileName = path.getFileName().toString();
    }

    /**
     * Copies the provided file to src/main/audiowaveform directory
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
}
