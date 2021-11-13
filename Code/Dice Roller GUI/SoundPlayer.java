/*
 * Code taken from : https://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples
 * It's not my code
 */
import javax.sound.sampled.*;
import java.io.*;
public class SoundPlayer implements LineListener {
	boolean playCompleted;
	final int maxSoundsPlaying = 200;
	int soundsPlaying;

	public void play(String audioFilePath) {
		File audioFile = new File("F:\\SoundEffects\\" +audioFilePath);
		if (soundsPlaying < maxSoundsPlaying) {
			try {
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				AudioFormat format = audioStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				Clip audioClip = (Clip) AudioSystem.getLine(info);
				audioClip.addLineListener(this);
				audioClip.open(audioStream);
				audioClip.start();

			} catch (UnsupportedAudioFileException ex) {
				System.out.println("The specified audio file is not supported.");
				ex.printStackTrace();
			} catch (LineUnavailableException ex) {
				System.out.println("Audio line for playing back is unavailable.");
				ex.printStackTrace();
			} catch (IOException ex) {
				System.out.println("Error playing the audio file.");
				ex.printStackTrace();
			}

		}
	}

	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();

		if (type == LineEvent.Type.START) {
			System.out.println("Playback started for sound " + soundsPlaying);
			soundsPlaying++;

		} else if (type == LineEvent.Type.STOP) {
			playCompleted = true;
			System.out.println("Playback completed for sound " + soundsPlaying);
			soundsPlaying--;
		}

	}
}