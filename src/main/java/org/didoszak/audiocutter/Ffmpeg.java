package org.didoszak.audiocutter;

import java.io.*;

public class Ffmpeg {
    private String path = new File("ffmpeg\\ffmpeg.exe").getAbsolutePath();
    private static PrintStream out;

    public String convertToWav(String path) {
        System.out.println("converting");

        // check if there is an ffmpeg installed
        if(executeCommandGobble("ffmpeg -version"))
            this.path = "ffmpeg";

        String outputPath = new File("resources").getAbsolutePath() + path.substring(path.lastIndexOf("\\"), path.lastIndexOf(".")) + ".wav";
        System.out.println(outputPath);
        String runCommand = this.path +
                " -i \"" + path +
                "\" -f wav -bitexact -acodec pcm_s16le -ac 2 " +
                "\"" + outputPath + "\"";

        System.out.println(runCommand);
        return executeCommandGobble(runCommand) ? outputPath : "";
    }

    @Deprecated
    private boolean executeCommand(String runCommand) {
        try {
            long t1 = System.currentTimeMillis();
            Process process = Runtime.getRuntime().exec(runCommand);

            InputStream output = process.getInputStream();
            Reader r = new InputStreamReader(output);
            BufferedReader br = new BufferedReader(r);
            BufferedReader errBr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = errBr.readLine()) != null) {
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

    private boolean executeCommandGobble(String runCommand) {
        try {
            long t1 = System.currentTimeMillis();
            Process process = Runtime.getRuntime().exec(runCommand);

            out = new PrintStream(new BufferedOutputStream(process.getOutputStream()));

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream())
            );
            new Thread(new StreamGobbler("in", process.getOutputStream(), process.getInputStream())).start();
            new Thread(new StreamGobbler("err", process.getOutputStream(), process.getErrorStream())).start();


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

    private class StreamGobbler implements Runnable {
        private OutputStream out;
        private String name;
        private BufferedInputStream bfStream;

        public StreamGobbler(String name, OutputStream out, InputStream inStream) {
            this.name = name;
            this.out = out;
            bfStream = new BufferedInputStream(inStream);

        }

        @Override
        public void run() {
            int i = 0;
            String line = "";
            while(true) {
                try {
                    i = bfStream.read();
                    char c = (char)i;

                    //check if char is a newline or carriage
                    if(i == 10)
                        line = "";
                    else if (i == 13)
                        parseLine(line);
                    else if (i == -1) {
                        parsePrompt(line);
                        break;
                    }
                    else
                        line += c;

                    // if its an user prompt, the inputstream may end without a newline
                    if(bfStream.available() == 0)
                        parsePrompt(line);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void parseLine(String line) {
            System.out.println(line);
        }

        private void parsePrompt(String line) throws IOException {
            if(line.endsWith("Overwrite? [y/N] ")) {
                System.out.println("Input needed: \n\t" + line);
                writeResponse("y");
            }
            else if (line.startsWith("ffmpeg version")) {
                System.out.println("ffmpeg available");
            }

        }

        private void writeResponse(String response) throws IOException {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out)
            );
            writer.write(response, 0, response.length());
            writer.newLine();
            writer.close();
        }
    }
}














