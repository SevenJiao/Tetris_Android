package com.example.tetris;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class TerisResources {
	Resources Teris_Resources;
	Canvas canvas;
	Bitmap Buffer_Bitmap = null;
	Bitmap Background_Bitmap = null;
	Bitmap[] Block_Bitmap = new Bitmap[8];
	Bitmap Backgroound_grade;
	Bitmap Backgroound_play;
	Bitmap Image_levelUp;

	public TerisResources(Context context) {
		Teris_Resources = context.getResources();
		for (int i = 0; i < 7; i++) {
			Block_Bitmap[i] = createImage(
					Teris_Resources.getDrawable(R.drawable.cube_960_011 + i),
					70, 70);
		}
		Background_Bitmap = createImage(
				Teris_Resources.getDrawable(R.drawable.background), 1080, 1920);
		Block_Bitmap[7] = createImage(
				Teris_Resources.getDrawable(R.drawable.bgmain), 1080, 1400);
		Backgroound_grade = createImage(
				Teris_Resources.getDrawable(R.drawable.grade), 380, 600);
		Image_levelUp = createImage(
				Teris_Resources.getDrawable(R.drawable.levelup), 256, 256);
		Buffer_Bitmap = Bitmap.createBitmap(1080, 1920, Config.ARGB_8888);
		canvas = new Canvas(Buffer_Bitmap);
		showBitmap();
	}

	public void showBitmap() {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setAlpha(100);
		canvas.drawBitmap(Background_Bitmap, 0, 0, null);
	}

	public static Bitmap createImage(Drawable d, int w, int h) {
		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		d.setBounds(0, 0, w, h);
		d.draw(c);
		return bitmap;
	}
}
