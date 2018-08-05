package com.example.tetris;

import java.util.Random;

import android.R.color;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class TerisView extends View {
	public final static int Teris_height = 24;
	public final static int Teris_width = 10;
	int Teris_grade;
	int Teris_level;
	int Teris_line;
	private int[][] Teris_screen = new int[Teris_height][Teris_width];
	private int[][] Teris_color = new int[Teris_height][Teris_width];
	private int[] logClearColor = new int[Teris_width];
	private int now = 0, next = 0, nowState = 0, nextState = 0;
	private int Teris_x = 0, Teris_y = 0;
	private int Teris_distance_shock = 0;
	public int numAddGrade = 0;
	public int numAddLine = 0;
	public int log_x = 0;
	public int high_score;
	public int Teris_level_Time = 1000;
	private boolean ButtonPressLeft = false;
	private boolean ButtonPressRight = false;
	public boolean ButtonPressChange = false;
	public boolean ButtonPressDown = false;
	public boolean ButtonPressFastDown = false;
	public boolean ButtonPressPause = false;
	ImageButton LeftButton = null;
	ImageButton RightButton = null;
	ImageButton DownButton = null;
	ImageButton FastDownButton = null;
	ImageButton PauseButton = null;
	ImageButton LeftButton_p = null;
	ImageButton RightButton_p = null;
	ImageButton DownButton_p = null;
	ImageButton FastDownButton_p = null;
	ImageButton PlayButton = null;
	ImageButton ChangeButton = null;
	ImageButton ChangeButton_p = null;
	Canvas TerisCanvas = null;
	TerisResources TR;
	Random random = null;
	Handler h = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				numAddGrade = 0;
				numAddLine = 0;
				break;
			case 1:
				log_x = 1;
				break;

			default:
				break;
			}
		};
	};

	public TerisView(Context context) {
		super(context);
		setWillNotDraw(false);
		TR = new TerisResources(context);
		InitButton(context);
		clean();
		random = new Random();
		nowState = Math.abs(random.nextInt() % 7);
		nextState = Math.abs(random.nextInt() % 7);
		now = Math.abs(random.nextInt() % 28);
		next = Math.abs(random.nextInt() % 28);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (BlockState.BS[now][i][j] != 0) {
					Teris_screen[i][j + 3] = BlockState.BS[now][i][j];
					Teris_color[i][j + 3] = nowState;
				}
			}
		}
		Teris_y = 3;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	private void clean() {
		for (int i = 0; i < Teris_height; i++) {
			for (int j = 0; j < Teris_width; j++) {
				Teris_screen[i][j] = 0;
				Teris_color[i][j] = -1;
			}
		}
	}

	public void InitButton(Context context) {
		LeftButton = new ImageButton(context, R.drawable.leftbutton, 0, 1664);
		LeftButton_p = new ImageButton(context, R.drawable.leftbutton, 0, 1664);
		RightButton = new ImageButton(context, R.drawable.rightbutton, 824,
				1664);
		RightButton_p = new ImageButton(context, R.drawable.rightbutton, 824,
				1664);
		ChangeButton = new ImageButton(context, R.drawable.changebutton, 696,
				1400);
		ChangeButton_p = new ImageButton(context, R.drawable.changebutton, 696,
				1400);
		DownButton = new ImageButton(context, R.drawable.downbutton, 128, 1400);
		DownButton_p = new ImageButton(context, R.drawable.downbutton, 128,
				1400);
		FastDownButton = new ImageButton(context, R.drawable.fastdownbutton,
				772, 1072);
		FastDownButton_p = new ImageButton(context, R.drawable.fastdownbutton,
				772, 1072);
		PlayButton = new ImageButton(context, R.drawable.playbutton, 412, 1532);
		PauseButton = new ImageButton(context, R.drawable.pausebutton, 412,
				1532);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		paint.setTextSize(50);
		paint.setColor(Color.RED);
		paint.setAlpha(225);
		this.setKeepScreenOn(true);
		canvas.drawBitmap(TR.Background_Bitmap, 0, 0, null);
		canvas.drawBitmap(TR.Block_Bitmap[7], 0, Teris_distance_shock, null);
		canvas.drawBitmap(TR.Backgroound_grade, 700,
				400 + Teris_distance_shock, null);
		PaintNextBlock(canvas);
		PaintNowBlock(canvas);
		PaintButton(canvas, paint);
		canvas.drawText("" + Teris_level, 870, 770 + Teris_distance_shock,
				paint);
		canvas.drawText("" + Teris_grade, 870, 570 + Teris_distance_shock,
				paint);
		canvas.drawText("" + Teris_line, 870, 970 + Teris_distance_shock, paint);
		Teris_distance_shock = 0;
		TerisCanvas = canvas;
	}

	public void PaintButton(Canvas canvas, Paint paint) {
		if (ButtonPressPause) {
			PlayButton.DrawImageButton(canvas, paint);
		} else {
			PauseButton.DrawImageButton(canvas, paint);
		}
		if (numAddGrade != 0) {
			Paint p1 = new Paint();
			p1.setColor(Color.YELLOW);
			p1.setTextSize(100);
			if (Teris_grade > high_score) {
				canvas.drawText("ÆÆ¼ÍÂ¼À²£¡£¡", 150, 400, p1);
			}
			canvas.drawText("+" + numAddGrade, 800, 570, p1);
			canvas.drawText("+" + numAddLine, 800, 970, p1);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						Message m1 = new Message();
						m1.what = 0;
						TerisView.this.h.sendMessage(m1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();
		}
		if (Teris_level_Time == 800 || Teris_level_Time == 500
				|| Teris_level_Time == 200) {
			if (log_x == 0) {
				canvas.drawBitmap(TR.Image_levelUp, 222, 572, paint);
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(1000);
							Message m = new Message();
							m.what = 1;
							TerisView.this.h.sendMessage(m);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}).start();
			}
		}
		if (ButtonPressLeft) {
			LeftButton_p.DrawImageButton(canvas, paint);
		} else {
			LeftButton.DrawImageButton(canvas, paint);
		}
		ButtonPressLeft = false;
		if (ButtonPressRight) {
			RightButton_p.DrawImageButton(canvas, paint);
		} else {
			RightButton.DrawImageButton(canvas, paint);
		}
		ButtonPressRight = false;
		if (ButtonPressChange) {
			ChangeButton_p.DrawImageButton(canvas, paint);
		} else {
			ChangeButton.DrawImageButton(canvas, paint);
		}
		ButtonPressChange = false;
		if (ButtonPressDown) {
			DownButton_p.DrawImageButton(canvas, paint);
		} else {
			DownButton.DrawImageButton(canvas, paint);
		}
		ButtonPressDown = false;

		if (ButtonPressFastDown) {
			FastDownButton_p.DrawImageButton(canvas, paint);
		} else {
			FastDownButton.DrawImageButton(canvas, paint);
		}
		ButtonPressFastDown = false;

	}

	public void PaintNowBlock(Canvas canvas) {
		for (int i = 0; i < Teris_height; i++) {
			for (int j = 0; j < Teris_width; j++) {
				if (Teris_screen[i][j] != 0 && i > 3) {
					canvas.drawBitmap(TR.Block_Bitmap[Teris_color[i][j]],
							j * 70, (i - 4) * 70 + Teris_distance_shock, null);
				}
			}
		}

	}

	public void PaintNextBlock(Canvas canvas) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (BlockState.BS[next][i][j] != 0) {
					canvas.drawBitmap(TR.Block_Bitmap[nextState], j * 70 + 770,
							i * 70 + 70 + Teris_distance_shock, null);
				}
			}
		}

	}

	public void colorState(int x, int y, int BlockColor) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (BlockState.BS[now][i][j] != 0) {
					Teris_color[x + i][y + j] = BlockColor;
				}
			}
		}
	}

	public void keyPress(int k, KeyEvent ke) {
		if (k == KeyEvent.KEYCODE_DPAD_DOWN && isDown()) {
			down();
			ButtonPressDown = false;
		}
		if (k == KeyEvent.KEYCODE_DPAD_LEFT && isLeft()) {
			left();
			ButtonPressLeft = false;
		}
		if (k == KeyEvent.KEYCODE_DPAD_RIGHT && isRight()) {
			right();
			ButtonPressRight = false;
		}
		if (k == KeyEvent.KEYCODE_DPAD_UP && isChange()) {
			change();
			ButtonPressChange = false;
		}
		if (k == KeyEvent.KEYCODE_DPAD_CENTER && isDown()) {
			fastDown();
			ButtonPressFastDown = false;
		}
	}

	public boolean isBlockChange(int[][] block, int x, int y) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (block[i][j] != 0) {
					if (!isOver(x + i, y + j)) {
						return false;
					}

				}
			}
		}
		return true;
	}

	public boolean isOver(int x, int y) {
		if (x < 0 || x >= Teris_height)
			return false;
		if (y < 0 || y >= Teris_width)
			return false;
		if (Teris_screen[x][y] == 0)
			return true;
		return false;
	}

	public void fastDown() {
		Teris_distance_shock = 5;
		cleanState(fastDown(2), Teris_y);
		move();
	}

	public void change() {
		cleanState(Teris_x, Teris_y);
		move();
	}

	public boolean isChange() {
		int tempX = 0, tempY = 0;
		int tempShape;
		int[][] tempBlock = new int[4][4];
		tempShape = now;
		if (tempShape % 4 > 0) {
			tempShape--;
		} else {
			tempShape += 3;
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				tempBlock[i][j] = BlockState.BS[tempShape][i][j];
			}
		}
		tempX = Teris_x;
		tempY = Teris_y;
		boolean canChange = false;
		cleanState(Teris_x, Teris_y);
		if (isBlockChange(tempBlock, tempX, tempY)) {
			canChange = true;
		} else if (isBlockChange(tempBlock, tempX - 1, tempY)) {
			canChange = true;
			tempX--;
		} else if (isBlockChange(tempBlock, tempX - 2, tempY)) {
			canChange = true;
			tempX -= 2;
		} else if (isBlockChange(tempBlock, tempX + 1, tempY)) {
			canChange = true;
			tempX++;
		} else if (isBlockChange(tempBlock, tempX + 2, tempY)) {
			canChange = true;
			tempX += 2;
		}
		if (canChange) {
			now = tempShape;
			Teris_x = tempX;
			Teris_y = tempY;
			return true;
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (BlockState.BS[now][i][j] != 0) {
					Teris_screen[Teris_x + i][Teris_y + j] = 1;
					Teris_color[Teris_x + i][Teris_y + j] = nowState;
				}
			}
		}
		return false;
	}

	public void cleanState(int teris_x2, int teris_y2) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (BlockState.BS[now][i][j] != 0) {
					Teris_screen[teris_x2 + i][teris_y2 + j] = 0;
					Teris_color[teris_x2 + i][teris_y2 + j] = -1;
				}
			}
		}

	}

	public void right() {
		cleanState(Teris_x, Teris_y);
		Teris_y++;
		move();
	}

	public boolean isRight() {
		ButtonPressRight = true;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (BlockState.BS[now][i][j] != 0) {
					while (j < 3 && BlockState.BS[now][i][j + 1] != 0)
						j++;
					if (!isOver(Teris_x + i, Teris_y + j + 1))
						return false;
				}
			}
		}
		return true;
	}

	public void left() {
		cleanState(Teris_x, Teris_y);
		Teris_y--;
		move();
	}

	public boolean isLeft() {
		ButtonPressLeft = true;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (BlockState.BS[now][i][j] != 0) {
					if (!isOver(Teris_x + i, Teris_y + j - 1))
						return false;
					else
						break;
				}
			}
		}
		return true;
	}

	public int fastDown(int d) {
		int a[] = { 100, 100, 100, 100 };
		int n = 0, s = 0, m = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (BlockState.BS[now][i][j] != 0) {
					m = i;
					while (m < 3 && BlockState.BS[now][m + 1][j] != 0)
						m++;
					if (isOver(Teris_x + m + 1, Teris_y + j)) {
						n = 1;
						while (isOver(Teris_x + m + (++n), Teris_y + j))
							;
						a[s++] = n - 1;
					}
				}
			}
		}
		switch (d) {
		case 2:
			s = Teris_x;
			Teris_x += getMin(getMin(a[0], a[1]), getMin(a[2], a[3]));
			break;
		default:
			break;
		}
		return s;
	}

	public int getMin(int i, int j) {
		if (i >= j)
			return j;
		else
			return i;
	}

	public void down() {
		cleanState(Teris_x, Teris_y);
		Teris_x++;
		move();
	}

	public boolean isDown() {
		int n = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (BlockState.BS[now][i][j] != 0) {
					n = i;
					while (n < 3 && BlockState.BS[now][n + 1][j] != 0)
						n++;
					if (!isOver(Teris_x + n + 1, Teris_y + j))
						return false;
				}
			}
		}
		return true;
	}

	public int clearFullLine() {
		int FullLineNum = 0, n = 0, lineNumber = 0;
		FullLineNum = findFullLine();
		for (int i = Teris_x + 3; i >= Teris_x - 1; i--) {
			if (isFullLine(i)) {
				n = i;
				for (int j = 0; j < Teris_width; j++) {
					logClearColor[j] = Teris_color[n][j];
				}
				lineNumber++;
				for (int r = n; r >= FullLineNum; r--) {
					for (int t = 0; t < Teris_width; t++) {
						if (r == FullLineNum) {
							Teris_screen[r][t] = 0;
							Teris_color[r][t] = -1;
						} else {
							Teris_screen[r][t] = Teris_screen[r - 1][t];
							Teris_color[r][t] = Teris_color[r - 1][t];
						}
					}
				}
				TerisMusic.soundPlay(R.raw.delete1 + lineNumber - 1, 0);
				i = n + 1;
			}
		}
		return lineNumber;
	}

	public boolean isFullLine(int i) {
		int j = 0;
		while (j < Teris_width && Teris_screen[i][j] == 1)
			j++;
		if (j == Teris_width)
			return true;
		return false;
	}

	public int findFullLine() {
		for (int i = Teris_x + 3; i > 0; i--) {
			int j = 0;
			while (j < Teris_width && Teris_screen[i][j] == 0)
				j++;
			if (j == Teris_width)
				return i + 1;
		}
		return 0;
	}

	public void move() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (BlockState.BS[now][i][j] != 0) {
					Teris_screen[Teris_x + i][Teris_y + j] = 1;
					Teris_color[Teris_x + i][Teris_y + j] = nowState;
				}
			}
		}
	}

	public void score(int g) {
		Teris_line += g;
		numAddLine = g;
		switch (g) {
		case 1:
			numAddGrade = 100;
			Teris_grade += 100;
			break;
		case 2:
			numAddGrade = 300;
			Teris_grade += 300;
			break;
		case 3:
			numAddGrade = 500;
			Teris_grade += 500;
			break;
		case 4:
			numAddGrade = 800;
			Teris_grade += 800;
			break;

		default:
			break;
		}
	}

	public boolean ifGameOver() {
		for (int i = 0; i < Teris_width; i++) {
			if (Teris_screen[4][i] != 0) {
				return true;
			}
		}
		return false;
	}

	public void downOverHandle() {
		colorState(Teris_x, Teris_y, nowState);
		score(clearFullLine());
	}

	public void getNewBlock() {
		now = next;
		nowState = nextState;
		next = Math.abs(random.nextInt() % 28);
		nextState = Math.abs(random.nextInt() % 7);
		Teris_y = 3;
		Teris_x = 0;
	}
}
