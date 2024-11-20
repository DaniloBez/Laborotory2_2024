import javax.sound.sampled.*;
import java.io.File;

/**
 * Клас AudioPlayer відповідає за завантаження та відтворення аудіофайлів у форматі WAV.
 */
public class AudioPlayer {
    private Clip audioClip;

    /**
     * Завантажує аудіофайл з вказаного шляху та готує його для відтворення.
     *
     * @param filePath Шлях до аудіофайлу у форматі WAV.
     */
    public void loadAudio(String filePath) {
        try {
            // Зупиняє і закриває попередній аудіокліп, якщо він відкритий.
        	if (audioClip != null && audioClip.isOpen()) {
                audioClip.stop();
                audioClip.close();
            }

            // Завантажує аудіофайл і створює аудіопотік.
            File audioFile = new File(filePath); 
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Налаштовує формат аудіо та ініціалізує Clip для відтворення.
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Відтворює аудіо з початку. Якщо аудіокліп завантажено, він починається спочатку.
     */
    public void playAudio() {
        if (audioClip != null) {
            audioClip.setFramePosition(0);
            audioClip.start();
        }
    }

    /**
     * Зупиняє відтворення аудіо, якщо аудіокліп активний.
     */
    public void stopAudio() {
        if (audioClip != null) {
            audioClip.stop();
        }
    }
}
