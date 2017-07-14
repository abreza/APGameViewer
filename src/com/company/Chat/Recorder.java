package com.company.Chat;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Recorder {
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private AudioFormat format =
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, true);
    private TargetDataLine microphone;
    private Thread session;

    public synchronized void beginRecording(final File soundFile) throws IOException, LineUnavailableException {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info))
            throw new IOException("Line type not supported: " + info);
        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format, microphone.getBufferSize());
        session = new Thread(() -> {
            AudioInputStream sound = new AudioInputStream(microphone);
            microphone.start();
            try {
                AudioSystem.write(sound, fileType, soundFile);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }
        });
        session.start();
    }

    public synchronized void endRecording() {
        microphone.stop();
        microphone.close();
        if (session != null) {
            try {
                session.join();
            } catch (InterruptedException e) {
            }
            session = null;
        }
        notify();
    }

    public synchronized void waitForEnd() {
        while (session != null)
            try {
                wait();
            } catch (InterruptedException e) {
            }
    }
}
