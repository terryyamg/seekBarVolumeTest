package tw.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Switch switchVolume; // 開關

	private SeekBar mediaVlmSeekBar = null; // 音樂、影片、遊戲及其他媒體

	private SeekBar ringerVlmSeekBar = null; // 鈴聲

	private SeekBar alarmVlmSeekBar = null; // 鬧鐘

	private SeekBar notifyVlmSeekBar = null; // 通知

	private AudioManager audioManager = null;

	private TextView tv1, tv2, tv3, tv4;

	private boolean setSwitch;
	private int originalMedia, originalRinger, originalAlarm, originalNotify,
			setMedia, setRinger, setAlarm, setNotify;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* 權限 */
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.setVolumeControlStream(AudioManager.STREAM_RING);
		this.setVolumeControlStream(AudioManager.STREAM_ALARM);
		this.setVolumeControlStream(AudioManager.STREAM_NOTIFICATION);

		setContentView(R.layout.activity_main);

		initControls();
	}

	@SuppressLint("InlinedApi")
	private void initControls() {
		// Return the handle to a system-level service - 'AUDIO'.
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		/* 手機原設定音量大小 */
		originalMedia = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		originalRinger = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		originalAlarm = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
		originalNotify = audioManager
				.getStreamVolume(AudioManager.STREAM_NOTIFICATION);

		// 取出最後設定音量大小
		try {
			SharedPreferences preferencesGet = getApplicationContext()
					.getSharedPreferences("vol",
							android.content.Context.MODE_PRIVATE);
			setSwitch = preferencesGet.getBoolean("setSwitch", false);// 預設開關為關閉
			setMedia = preferencesGet.getInt("setMedia", originalMedia); // 預設音量大小
			setRinger = preferencesGet.getInt("setRinger", originalRinger);
			setAlarm = preferencesGet.getInt("setAlarm", originalAlarm);
			setNotify = preferencesGet.getInt("setNotify", originalNotify);

			Log.i("setSwitch", setSwitch + "");
			Log.i("setMedia", setMedia + "");
			Log.i("setRinger", setRinger + "");
			Log.i("setAlarm", setAlarm + "");
			Log.i("setNotify", setNotify + "");
		} catch (Exception e) {
		}

		/* 開關 */
		switchVolume = (Switch) findViewById(R.id.switchVolume);
		switchVolume.setChecked(setSwitch); // 預設開關為關閉

		/* 音樂、影片、遊戲及其他媒體 */
		mediaVlmSeekBar = (SeekBar) findViewById(R.id.seekBar1);
		tv1 = (TextView) findViewById(R.id.tv1);
		mediaVlmSeekBar.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC)); // 最大值

		/* 鈴聲 */
		ringerVlmSeekBar = (SeekBar) findViewById(R.id.seekBar2);
		tv2 = (TextView) findViewById(R.id.tv2);
		ringerVlmSeekBar.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_RING));

		/* 鬧鐘 */
		alarmVlmSeekBar = (SeekBar) findViewById(R.id.seekBar3);
		tv3 = (TextView) findViewById(R.id.tv3);
		alarmVlmSeekBar.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_ALARM));

		/* 通知 */
		notifyVlmSeekBar = (SeekBar) findViewById(R.id.seekBar4);
		tv4 = (TextView) findViewById(R.id.tv4);
		notifyVlmSeekBar.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));

		/* 設定音量大小 */
		mediaVlmSeekBar.setProgress(setMedia);
		ringerVlmSeekBar.setProgress(setRinger);
		alarmVlmSeekBar.setProgress(setAlarm);
		notifyVlmSeekBar.setProgress(setNotify);

		/* 開關動作 */
		if (setSwitch) {
			tv1.setText(audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC) + "");// 顯示大小
		} else {
			tv1.setText("0");// 顯示大小
		}

		switchVolume.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) { // 開啟
					mediaVlmSeekBar.setProgress(audioManager
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC)); // 設到最大
					setMedia = audioManager
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					setSwitch = true;
				} else { // 關閉
					mediaVlmSeekBar.setProgress(0); // 設到最小
					setMedia = 0;
					setSwitch = false;
				}

			}
		});

		try {
			/* 音樂、影片、遊戲及其他媒體 */
			mediaVlmSeekBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						// seekbar停止時動作
						public void onStopTrackingTouch(SeekBar arg0) {
						}

						// seekbar開始前時動作
						public void onStartTrackingTouch(SeekBar arg0) {
						}

						// seekbar改變時動作
						public void onProgressChanged(SeekBar arg0,
								int progress, boolean arg2) {
							tv1.setText(progress + "");// 顯示大小
							audioManager.setStreamVolume(
									AudioManager.STREAM_MUSIC, progress, 0);
							setMedia = progress; // 修改預設值為選定大小
						}
					});

			/* 鈴聲 */
			ringerVlmSeekBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						public void onStopTrackingTouch(SeekBar arg0) {
						}

						public void onStartTrackingTouch(SeekBar arg0) {
						}

						public void onProgressChanged(SeekBar arg0,
								int progress, boolean arg2) {
							tv2.setText(progress + "");// 顯示大小
							audioManager.setStreamVolume(
									AudioManager.STREAM_RING, progress, 0);
							setRinger = progress; // 修改預設值為選定大小
						}
					});

			/* 鬧鐘 */
			alarmVlmSeekBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						public void onStopTrackingTouch(SeekBar arg0) {
						}

						public void onStartTrackingTouch(SeekBar arg0) {
						}

						public void onProgressChanged(SeekBar arg0,
								int progress, boolean arg2) {
							tv3.setText(progress + "");// 顯示大小
							audioManager.setStreamVolume(
									AudioManager.STREAM_ALARM, progress, 0);
							setAlarm = progress; // 修改預設值為選定大小
						}
					});

			/* 通知 */
			notifyVlmSeekBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						public void onStopTrackingTouch(SeekBar arg0) {
						}

						public void onStartTrackingTouch(SeekBar arg0) {
						}

						public void onProgressChanged(SeekBar arg0,
								int progress, boolean arg2) {
							tv4.setText(progress + "");// 顯示大小
							audioManager.setStreamVolume(
									AudioManager.STREAM_NOTIFICATION, progress,
									0);
							setNotify = progress; // 修改預設值為選定大小
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 結束時 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		/* 紀錄設定音量大小 */
		SharedPreferences preferencesSave = getApplicationContext()
				.getSharedPreferences("vol",
						android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferencesSave.edit();
		editor.putBoolean("setSwitch", setSwitch);
		editor.putInt("setMedia", setMedia);
		editor.putInt("setRinger", setRinger);
		editor.putInt("setAlarm", setAlarm);
		editor.putInt("setNotify", setNotify);
		editor.commit();
		/* 還原手機原設定音量大小 */
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalMedia,
				0);
		audioManager.setStreamVolume(AudioManager.STREAM_RING, originalRinger,
				0);
		audioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalAlarm,
				0);
		audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
				originalNotify, 0);
		Log.i("onDestroy", "onDestroy");
	}

}