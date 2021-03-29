import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {
	public static final String background = "file:./sounds/background_music.wav";

	// used to play sound effects
	// soundToPlay is a string specifying the relative path of the sound effect file

	public static void playSoundEffect(String soundToPlay, boolean loopForever) {
		URL soundLocation;
		try {
			soundLocation = new URL(soundToPlay);
			Clip clip = null;
			clip = AudioSystem.getClip();
			AudioInputStream inputStream;
			inputStream = AudioSystem.getAudioInputStream(soundLocation);
			clip.open(inputStream);
            if(loopForever) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);									// sound does not loop
            } else {
                clip.loop(0);
            }
			clip.start();														// play sound
			
			clip.addLineListener(new LineListener() {							// kill sound thread
				public void update (LineEvent evt) {
					if (evt.getType() == LineEvent.Type.STOP) {
						evt.getLine().close();
					}
				}
			});
			
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

}