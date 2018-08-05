package com.example.tetris;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class TerisMusic {
	public static MediaPlayer BGMMP;
	public static SoundPool SP;
	private static boolean musicSwitch = true;;
	private static boolean soundSwitch = true;
	private static Map<Integer, Integer> soundMap;
	private static Context context;

	public static void InitMusicPlay(Context c) {
		context = c;
	}

	public static void getSwitch(Context c, boolean b) {
		musicSwitch = b;
		soundSwitch = b;
	}

	public static void BGMPlay() {
		BGMMP = MediaPlayer.create(context, R.raw.bgm);
		BGMMP.setLooping(true);
	}

	public static void pauseMusic() {
		if (BGMMP.isPlaying()) {
			BGMMP.pause();
		}
	}

	public static void startMusic() {
		if (musicSwitch) {
			BGMMP.start();
		}
	}

	public static void releaseMusic() {
		if (BGMMP != null) {
			BGMMP.release();
		}
	}

	public static void setMusicSwitch(boolean musicSwitch) {
		TerisMusic.musicSwitch = musicSwitch;
		if (TerisMusic.musicSwitch) {
			BGMMP.start();
		} else {
			BGMMP.stop();
		}
	}

	public static void inItSound() {
		SP = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
		soundMap = new HashMap<Integer, Integer>();
		soundMap.put(R.raw.action, SP.load(context, R.raw.action, 1));
		soundMap.put(R.raw.fastdown, SP.load(context, R.raw.fastdown, 1));
		soundMap.put(R.raw.change, SP.load(context, R.raw.change, 1));
		soundMap.put(R.raw.down, SP.load(context, R.raw.down, 1));
		soundMap.put(R.raw.delete1, SP.load(context, R.raw.delete1, 1));
		soundMap.put(R.raw.delete2, SP.load(context, R.raw.delete2, 1));
		soundMap.put(R.raw.delete3, SP.load(context, R.raw.delete3, 1));
		soundMap.put(R.raw.delete4, SP.load(context, R.raw.delete4, 1));
	}

	public static int soundPlay(Integer resID, Integer res) {
		if (!soundSwitch) {
			return 0;
		}
		Integer sound = soundMap.get(resID);
		if (sound != null) {
			return SP.play(sound, 1, 1, 1, res, 1);
		} else {
			return 0;
		}
	}

	public static void releaseSound() {
		if (SP != null) {
			SP.release();
		}
	}

}
