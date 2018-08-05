package com.example.tetris;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ImageButton {
	private Bitmap buttonBitmap = null;
	private int imageX = 0;
	private int imageY = 0;
	private int imageWidth = 0;
	private int imageHeight = 0;

	public ImageButton(Context context, int ImageID, int x, int y) {
		buttonBitmap = ReadImage(context, ImageID);
		imageX = x;
		imageY = y;
		imageWidth = buttonBitmap.getWidth();
		imageHeight = buttonBitmap.getHeight();
	}

	public void DrawImageButton(Canvas c, Paint p) {
		c.drawBitmap(buttonBitmap, imageX, imageY, p);
	}

	public boolean IsClick(int x, int y) {
		boolean isClick = false;
		if (x >= imageX && x <= imageX + imageWidth && y >= imageY
				&& y <= imageY + imageHeight) {
			isClick = true;
		}
		return isClick;
	}

	public Bitmap ReadImage(Context context, int imageResId) {
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inPreferredConfig = Bitmap.Config.RGB_565;
		bfo.inPurgeable = true;
		bfo.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(imageResId);
		return BitmapFactory.decodeStream(is, null, bfo);
	}

}
