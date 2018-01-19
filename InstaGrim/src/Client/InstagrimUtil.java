package Client;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class InstagrimUtil {
	public static Color blue = new Color(34, 134, 216);
	public static Color black = new Color(0, 0, 0);
	public static Color clear = new Color(255, 0, 0, 0);
	public static Color clearblack = new Color(0, 0, 0, 180);
	public static Color clearivory = new Color(234, 234, 234, 220);
	public static Color ivory = new Color(234, 234, 234);
	public static Color white = new Color(255, 255, 255);

	public static Clip clip;
	public static AudioInputStream ais;
	private static ArrayList<URL> soundURLList = new ArrayList<URL>();
	private static ArrayList<String> soundList = new ArrayList<String>();
	private static int soundIndex = 0;

	public final static String SOUND36_PATH = "36.wav";
	public final static String SOUND116_PATH = "116.wav";
	public final static String SOUND117_PATH = "117.wav";
	public final static String SOUND118_PATH = "118.wav";
	public final static String SOUND119_PATH = "119.wav";
	URL sound36URL = getClass().getClassLoader().getResource(SOUND36_PATH);
	URL sound116URL = getClass().getClassLoader().getResource(SOUND116_PATH);
	URL sound117URL = getClass().getClassLoader().getResource(SOUND117_PATH);
	URL sound118URL = getClass().getClassLoader().getResource(SOUND118_PATH);
	URL sound119URL = getClass().getClassLoader().getResource(SOUND119_PATH);

	public void sound() {
		try {
			// "sound/36.wav"
			soundURLList.add(sound36URL);
			soundURLList.add(sound116URL);
			soundURLList.add(sound117URL);
			soundURLList.add(sound118URL);
			soundURLList.add(sound119URL);
			String[] soundDelimiter;
			for (int i = 0; i < soundURLList.size(); i++) {
				soundDelimiter = soundURLList.get(i).getFile().toString().split("/");
				for (int j = 0; j < soundDelimiter.length; j++) {
					if (soundDelimiter[j].contains(".wav")) {
						soundList.add(soundDelimiter[j]);
						break;
					}
				}
			}

			ais = AudioSystem
					.getAudioInputStream(/*new BufferedInputStream(*/new File("sound/" + soundList.get(soundIndex)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			Thread.sleep(10000);
		} catch (Exception e) {
		}
	}

	public static void soundOn() {
		try {
			if (soundIndex == 4)
				soundIndex = 0;
			ais = AudioSystem.getAudioInputStream(
					new BufferedInputStream(new FileInputStream("sound/" + soundList.get(++soundIndex))));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
		}
	}

	public static void soundOff() {
		clip.stop();
		clip.close();
	}

	// 영어 또는 숫자만 입력 받기
	public static boolean checkInputOnlyNumberAndAlphabet(String string) {
		for (int i = 0; i < string.length(); i++) {
			char chrInput = string.charAt(i);
			if (chrInput >= 0x61 && chrInput <= 0x7A) {
			} else if (chrInput >= 0x41 && chrInput <= 0x5A) {
			} else if (chrInput >= 0x30 && chrInput <= 0x39) {
			} else {
				return false;
			}
		}
		return true;
	}

	// 공백 검사
	public static boolean checkInputBlank(String string) {
		for (int i = 0; i < string.length(); i++) {
			char chrInput = string.charAt(i);
			if (chrInput == 0x0 || chrInput == 0x20) {
				return false;
			}
		}
		return true;
	}

	// 이메일 검사
	public static boolean checkEmailFormat(String string) {
		if (string == null)
			return false;
		boolean result = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", string.trim());
		return result;
	}

}
