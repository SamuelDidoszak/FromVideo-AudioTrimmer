import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class AudioWaveform {
    private String runCommand;
    private Process process;

    public AudioWaveform(AudioFile audioFile) {
        String audioWaveformPath = getClass().getResource("audiowaveform.exe").toString().substring(6);
        String targetDirPath = audioWaveformPath.substring(0, audioWaveformPath.lastIndexOf("/") + 1);

        runCommand = audioWaveformPath +
                " --input-filename " + targetDirPath + audioFile.getFileName() +
                " --output-filename " + targetDirPath + "full.png" +
                " --background-color 121212" +
                " --waveform-color b39ddb" +
                " --amplitude-scale auto" +
                " --zoom auto" +
                " --border-color 121212" +
                " --no-axis-labels" +
                " --width 30000" +
                " --height 720";


        // Sadly, starting the process via ProcessBuilder doesn't work. It only does, when arguments passed make the program print something and not work

//        audioWaveform = new ProcessBuilder(audioWaveformPath,
//                "--input-filename " + audioFile.getFileName(),
//                "--output-filename " + "full1.png",
//                "--background-color " + "121212");
    }

    /**
     * Starts the audiowaveform process
     * @return True if the process was correctly instantiated or False if not
     */
    public boolean startProcess() {
        try {
            long t1 = System.currentTimeMillis();
            process = Runtime.getRuntime().exec(runCommand);
//            int waitFlag = process.waitFor();
//            if(waitFlag == 0) {
//                System.out.println(process.exitValue());
//            }
            InputStream output = process.getInputStream();
            Reader r = new InputStreamReader(output);
            BufferedReader br = new BufferedReader(r);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
            if(process.exitValue() == 0) {
                long t2 = System.currentTimeMillis();
                System.out.println("execution time: " + (t2 - t1));
            }
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
