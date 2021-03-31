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
	public static final String coin = "file:./sounds/coin_pickup.wav";
	public static final String laserFiring = "file:./sounds/laser_fire_lp.wav";
	public static final String laserLoading = "file:./sounds/laser_warning.wav";
	public static final String scientistFainting = "file:./sounds/scientist_faint.wav";
	public static final String barryWalking = "file:./sounds/foot_step.wav";
	public static final String barrySliding = "file:./sounds/fall_slide.wav";

	// used to play sound effects
	// soundToPlay is a string specifying the relative path of the sound effect file

	public static void playSoundEffect(String soundToPlay, int loopNum) {
		URL soundLocation;
		try {
			soundLocation = new URL(soundToPlay);
			Clip clip = null;
			clip = AudioSystem.getClip();
			AudioInputStream inputStream;
			inputStream = AudioSystem.getAudioInputStream(soundLocation);
			clip.open(inputStream);
            clip.loop(loopNum);
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