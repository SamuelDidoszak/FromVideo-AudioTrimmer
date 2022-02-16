package org.didoszak.audiocutter;

import java.io.*;

public class Ffmpeg {
    private String path = new File("ffmpeg\\ffmpeg.exe").getAbsolutePath();

    public String convertToWav(String path) {
        System.out.println("converting");
        String outputPath = new File("resources").getAbsolutePath() + path.substring(path.lastIndexOf("\\"), path.lastIndexOf(".")) + ".wav";
//        String outputPath = new File("resources").getAbsolutePath() + "\\" + Paths.get(path).getFileName().toString().substring(0, Paths.get(path).getFileName().toString().lastIndexOf(".")) + ".wav";
        System.out.println(outputPath);
        String runCommand = this.path +
                " -i \"" + path +
                "\" -f wav -bitexact -acodec pcm_s16le -ar 22050 -ac 1 " +
                "\"" + outputPath + "\"";

        return executeCommand(runCommand) ? outputPath : "";
    }

    private boolean executeCommand(String runCommand) {
        try {
            long t1 = System.currentTimeMillis();
            Process process = Runtime.getRuntime().exec(runCommand);

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
            process.destroy();
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}














