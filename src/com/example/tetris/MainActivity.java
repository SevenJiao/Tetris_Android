package com.example.tetris;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView text_welcome;
	private Handler hl = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Intent i = new Intent(MainActivity.this, BeginAcitivity.class);
				int result = 0;
				i.putExtra("result", result);
				startActivity(i);
				MainActivity.this.finish();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FindView();
		Animation textWelcomeScale = AnimationUtils.loadAnimation(
				MainActivity.this, R.anim.scale);
		text_welcome.startAnimation(textWelcomeScale);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(3000);
					Message m = new Message();
					m.what = 1;
					hl.sendMessage(m);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	private void FindView() {
		// TODO Auto-generated method stub
		text_welcome = (TextView) findViewById(R.id.text_welcome);
	}

}
