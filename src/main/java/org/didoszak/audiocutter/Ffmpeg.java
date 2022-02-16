package org.didoszak.audiocutter;

import java.io.*;

public class Ffmpeg {
    private String path = new File("ffmpeg/ffmpeg.exe").getAbsolutePath();

    public String convertMp4ToWav(String videoPath) {
        System.out.println("converting");
        String outputPath = new File("resources").getAbsolutePath() + "\\" + videoPath.substring(videoPath.lastIndexOf("\\"), videoPath.length() - 4) + ".wav";
        System.out.println(outputPath);
        String runCommand = path +
                " -i " + videoPath +
                " -f wav -bitexact -acodec pcm_s16le -ar 22050 -ac 1 " +
                outputPath;

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
            return outputPath;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "";
        }
    }
}
