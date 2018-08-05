package com.example.tetris;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class BeginAcitivity extends Activity {
	private Button BeginIV;
	private Button ShowHighScore;
	public int logRestart = 0;
	private ImageView OpenVolume;
	private ImageView CloseVolume;
	private ImageView AboutHelp;
	private static SharedPreferences spf;
	private boolean VolumeSwitch;
	private static final String SPF_NAME = "TerisData";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.begin_activity);
		BeginIV = (Button) findViewById(R.id.beginIcon);
		ShowHighScore = (Button) findViewById(R.id.hightestscore);
		OpenVolume = (ImageView) findViewById(R.id.openvolume);
		AboutHelp = (ImageView) findViewById(R.id.abouthelpicon);
		CloseVolume = (ImageView) findViewById(R.id.closevolume);
		spf = getSharedPreferences(SPF_NAME, 0);
		if (spf.getInt("SPFExit", 0) == 0) {
			Editor editor = spf.edit();
			editor.putInt("SPFExit", 1);
			editor.putBoolean("VolumeSwitch", true);
			editor.putInt("HighScore", 0);
			editor.commit();
			ShowHighScore.setText("最高分:" + 0);
			OpenVolume.setVisibility(View.VISIBLE);
			CloseVolume.setVisibility(View.GONE);
		} else {
			VolumeSwitch = spf.getBoolean("VolumeSwitch", true);
			ShowHighScore.setText("最高分:" + spf.getInt("HighScore", 0));
			if (VolumeSwitch) {
				OpenVolume.setVisibility(View.VISIBLE);
				CloseVolume.setVisibility(View.GONE);
			} else {
				CloseVolume.setVisibility(View.VISIBLE);
				OpenVolume.setVisibility(View.GONE);
			}
		}
		Intent i = getIntent();
		logRestart = i.getIntExtra("result", 0);
		if (logRestart == 1) {
			Intent i1 = new Intent(BeginAcitivity.this, HomeActivity.class);
			startActivity(i1);
			BeginAcitivity.this.finish();
		}
		OpenVolume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OpenVolume.setVisibility(View.GONE);
				CloseVolume.setVisibility(View.VISIBLE);
				EditSpf();
			}

		});
		CloseVolume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CloseVolume.setVisibility(View.GONE);
				OpenVolume.setVisibility(View.VISIBLE);
				EditSpf1();
			}

		});
		BeginIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i2 = new Intent(BeginAcitivity.this, HomeActivity.class);
				startActivity(i2);
				BeginAcitivity.this.finish();
			}
		});
		AboutHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogAboutHelp();
			}
		});
		ShowHighScore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowHighScore.setText("最高分:" + 0);
				spf = getSharedPreferences(SPF_NAME, 0);
				Editor e = spf.edit();
				e.putInt("HighScore", 0);
				e.commit();
			}
		});

	}

	private void dialogAboutHelp() {
		AlertDialog.Builder a = new AlertDialog.Builder(this);
		a.setTitle("关于");
		a.setMessage("版本：1.0" + "\n" + "作者：焦琦" + "\n" + "小提示：点击最高分即可清零哦！");
		a.setIcon(R.drawable.abouthelp);
		a.create();
		a.show();
	}

	private void EditSpf() {
		spf = getSharedPreferences(SPF_NAME, 0);
		Editor e1 = spf.edit();
		e1.putBoolean("VolumeSwitch", false);
		e1.commit();
	}

	private void EditSpf1() {
		spf = getSharedPreferences(SPF_NAME, 0);
		Editor e2 = spf.edit();
		e2.putBoolean("VolumeSwitch", true);
		e2.commit();
	}
}
