package com.example.tetris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class HomeActivity extends Activity implements OnTouchListener,
		OnGestureListener {
	GestureDetector T_gestureDetector;
	private SharedPreferences spf;
	protected static final int aa = 0;
	protected static final int bb = 1;
	private int xx = 0;
	private boolean ThreadRunLog = true;
	public boolean pauseLog = false;
	private float distance;
	public int downTime, i = 0;
	Vibrator v;
	long[] vPattern = { 0, 10, 10, 30 };
	TerisView tv = null;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HomeActivity.bb:
				ThreadRunLog = false;
				gameoverDialog();
				break;
			case HomeActivity.aa:
				tv.invalidate();
			}
			super.handleMessage(msg);
		}
	};

	private void gameoverDialog() {
		AlertDialog.Builder adb = new AlertDialog.Builder(HomeActivity.this);
		adb.setTitle("哇哦");
		adb.setIcon(R.drawable.alert);
		adb.setMessage("Game Over！");
		adb.setCancelable(false);
		adb.setPositiveButton("确定退出", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				spf = getSharedPreferences("TerisData", 0);
				if (tv.Teris_grade > spf.getInt("HighScore", 0)) {
					Editor e = spf.edit();
					e.putInt("HighScore", tv.Teris_grade);
					e.commit();
				}
				TerisMusic.releaseMusic();
				HomeActivity.this.finish();
			}
		});
		adb.setNegativeButton("重新挑战", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(HomeActivity.this, BeginAcitivity.class);
				i.putExtra("result", 1);
				startActivity(i);
				TerisMusic.releaseMusic();
				HomeActivity.this.finish();
			}
		});
		adb.create();
		adb.show();
	}

	public HomeActivity() {
		T_gestureDetector = new GestureDetector((OnGestureListener) this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		tv = new TerisView(HomeActivity.this);
		Init();
		new Thread(new TerisThread()).start();
	}

	class TerisThread implements Runnable {

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				while (ThreadRunLog) {
					Message m = new Message();
					if (xx == 0) {
						m.what = HomeActivity.aa;
					} else {
						m.what = HomeActivity.bb;
					}
					HomeActivity.this.handler.sendMessage(m);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					downTime = ++i * 100;
					if (downTime == (10 - 2 * tv.Teris_level + 1) * 100) {
						downHandle();
						i = 0;
					}
				}
			}

		}

		private void downHandle() {
			if (ThreadRunLog) {
				if (tv.isDown()) {
					tv.down();
				} else {
					spf = getSharedPreferences("TerisData", 0);
					tv.high_score = spf.getInt("HighScore", 0);
					tv.downOverHandle();
					if (tv.Teris_grade > spf.getInt("HighScore", 0)) {
						Editor e = spf.edit();
						e.putInt("HighScore", tv.Teris_grade);
						e.commit();
					}
					if (tv.ifGameOver()) {
						xx = 1;
					}
					if (xx == 0)
						tv.getNewBlock();
					if (tv.Teris_grade >= 100 && tv.Teris_level_Time == 1000) {
						tv.Teris_level_Time = 800;
						tv.Teris_level += 1;
					}
					if (tv.Teris_grade >= 6000 && tv.Teris_level_Time == 800) {
						tv.Teris_level_Time = 500;
						tv.Teris_level += 1;
						tv.log_x = 0;
					}
					if (tv.Teris_grade >= 9000 && tv.Teris_level_Time == 500) {
						tv.Teris_level_Time = 200;
						tv.Teris_level += 1;
						tv.log_x = 0;
					}
				}
			}

		}

	}

	private void Init() {
		setContentView(tv);
		v = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		tv.setOnTouchListener((OnTouchListener) this);
		tv.setClickable(true);
		spf = getSharedPreferences("TerisData", 0);
		tv.high_score = spf.getInt("HighScore", 0);
		TerisMusic.InitMusicPlay(this);
		TerisMusic.getSwitch(this, spf.getBoolean("VolumeSwitch", true));
		TerisMusic.inItSound();
		TerisMusic.BGMPlay();
		TerisMusic.startMusic();
	}

	public boolean onTouch(View v, MotionEvent event) {
		return T_gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		int x = (int) e.getX();
		int y = (int) e.getY();
		if (tv.LeftButton.IsClick(x, y)) {
			v.vibrate(vPattern, -1);
			if (tv.isLeft() && ThreadRunLog) {
				TerisMusic.soundPlay(R.raw.action, 0);
				tv.left();
			}
		}
		if (tv.RightButton.IsClick(x, y)) {
			v.vibrate(vPattern, -1);
			if (tv.isRight() && ThreadRunLog) {
				TerisMusic.soundPlay(R.raw.action, 0);
				tv.right();
			}
		}
		if (tv.ChangeButton.IsClick(x, y)) {
			tv.ButtonPressChange = true;
			v.vibrate(vPattern, -1);
			if (tv.isChange() && ThreadRunLog) {
				TerisMusic.soundPlay(R.raw.change, 0);
				tv.change();
			}
		}
		if (tv.DownButton.IsClick(x, y)) {
			tv.ButtonPressDown = true;
			v.vibrate(vPattern, -1);
			if (tv.isDown() && ThreadRunLog) {
				TerisMusic.soundPlay(R.raw.down, 0);
				tv.down();
			}
		}
		if (tv.FastDownButton.IsClick(x, y)) {
			tv.ButtonPressFastDown = true;
			v.vibrate(vPattern, -1);
			if (tv.isDown() && ThreadRunLog) {
				TerisMusic.soundPlay(R.raw.fastdown, 0);
				tv.fastDown();
			}
		}
		if (tv.PlayButton.IsClick(x, y) || tv.PauseButton.IsClick(x, y)) {
			if (tv.ButtonPressPause) {
				tv.ButtonPressPause = false;
			} else {
				tv.ButtonPressPause = true;
			}
			v.vibrate(vPattern, -1);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(300);
						if (ThreadRunLog) {
							ThreadRunLog = false;
						} else {
							ThreadRunLog = true;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}

		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		tv.keyPress(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			tv.ButtonPressPause = true;
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(300);
						ThreadRunLog = false;

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
			dialogBack();
			return true;
		}
		return true;
	}

	private void dialogBack() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("提示");
		ab.setMessage("确定退出？");
		ab.setCancelable(false);
		ab.setIcon(R.drawable.over);
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				spf = getSharedPreferences("TerisData", 0);
				if (tv.Teris_grade > spf.getInt("HighScore", 0)) {
					Editor e = spf.edit();
					e.putInt("HighScore", tv.Teris_grade);
					e.commit();
				}
				TerisMusic.releaseMusic();
				HomeActivity.this.finish();

			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tv.ButtonPressPause = false;
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(300);
							ThreadRunLog = true;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
		ab.create();
		ab.show();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (tv.Teris_grade > spf.getInt("HighScore", 0)) {
			Editor e = spf.edit();
			e.putInt("HighScore", tv.Teris_grade);
			e.commit();
		}
		TerisMusic.releaseMusic();
	}
}
