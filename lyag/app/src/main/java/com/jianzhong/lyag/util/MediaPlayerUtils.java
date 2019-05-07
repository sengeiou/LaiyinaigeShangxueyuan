package com.jianzhong.lyag.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;

public class MediaPlayerUtils {
	
	private final String tag = this.getClass().getSimpleName();

	public static final int RING_DEFAULT = 1; 	// 默认状态
	public static final int RING_PLAYING = 2; 	// 播放状态
	public static final int RING_STOP 	 = 3; 	// 停止状态
	public static final int RING_PAUSE 	 = 4; 	// 暂停状态
	public static final int RING_RESUME  = 5; 	// 继续状态
	public static final int RING_LOADING = 6; 	// 加载状态
	public static final int RING_PLAYING_COMPLETE = 7; // 播放完成状态
	
	public boolean isPlay = false;
	
	private static MediaPlayerUtils mMediaPlayerUtils;
	public static MediaPlayer mMediaPlayer = null;
	private String path ;
	private Thread mPlayerThread;
	private MediaPlayerUtils() {
	}
	
	public static MediaPlayerUtils getInstance(){
		// 单例 MediaPlayerUtils 对象
		if(mMediaPlayerUtils == null){
			mMediaPlayerUtils = new MediaPlayerUtils();
		}
		// 单例 MediaPlayer 对象
		if(mMediaPlayer == null){
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
		return mMediaPlayerUtils;
	}

	public synchronized void initStart(final Context context, final String path, final int progress,
									   final OnPreparedListener preparedListener,
									   final OnCompletionListener completionListener) {
		this.path = path ;
		if (mMediaPlayer.isPlaying()) {
			stop();
		}else{
			mPlayerThread = new Thread(){
				@Override
				public void run() {
					try {
						System.out.println("调用  initStart()");
						long time = System.currentTimeMillis();
//						Uri uri = Uri.parse(Uri.encode(path)); 不要再encode 不然华为手机播放不了
						Uri uri = Uri.parse(path);
						mMediaPlayer.reset();
						mMediaPlayer.setDataSource(context, uri);
						mMediaPlayer.setLooping(false);
						mMediaPlayer.setOnPreparedListener(preparedListener);
						mMediaPlayer.setOnCompletionListener(completionListener);
						mMediaPlayer.prepare();
						mMediaPlayer.seekTo(progress);
						mMediaPlayer.start();
					} catch (Exception e) {
//						Log.e(tag, "path:"+path +" "+mMediaPlayer.getAudioSessionId());
						e.printStackTrace();
						completionListener.onCompletion(mMediaPlayer);
					}
					mPlayerThread = null;
				}
			};
			mPlayerThread.start();
			isPlay = true;
		}
	}


	public void start() {
		if (mMediaPlayer != null) {
			System.out.println("调用  start()");
			mMediaPlayer.start();
			isPlay = true;
		}
	}

	public void pause() {
		if (mMediaPlayer != null) {
			mMediaPlayer.pause();
		}
	}

	public void resume() {
		if (mMediaPlayer != null) {
			mMediaPlayer.start();
		}
	}

	public void stop() {
		if (mMediaPlayer != null) {
			System.out.println("调用  stop()");
			mMediaPlayer.pause();
			mMediaPlayer.stop();
			mMediaPlayer.reset();
			mMediaPlayer = null;
			isPlay = false;
		}
	}

//	public boolean canPlay() {
//		return mPlayerThread == null;
//	}

	public void setRate(float speed){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
		}
	}

	public int getDuration() { // 获取流媒体的总播放时长，单位是毫秒。
		return mMediaPlayer.getDuration();
	}

	public int getCurrentPosition() { // 获取当前流媒体的播放的位置，单位是毫秒。
		return mMediaPlayer.getCurrentPosition();
	}
	
	public boolean isPlaying() { // 获取当前流媒体的播放的位置，单位是毫秒。
		return mMediaPlayer.isPlaying();
	}
	
	public String getUri(){
		return path ;
	}

	public MediaPlayer getMediaPlayer(){
		return mMediaPlayer;
	}

	public String getCurrentTime(){
		String currentTime;
		int currentPosition = getCurrentPosition();
		int currentMinutes = currentPosition / 1000 / 60;
		int currentSeconds = currentPosition / 1000 % 60;
		if (currentSeconds >= 10) {
			currentTime = currentMinutes + ":" + currentSeconds;
		} else {
			currentTime = currentMinutes + ":0" + currentSeconds;
		}
		return currentTime;
	}

	public String getTotalTime(){
		String totalTime;
		int duration = getDuration();
		int totalMinutes = duration / 1000 / 60;
		int totalSeconds = duration / 1000 % 60;
		if (totalSeconds >= 10) {
			totalTime = totalMinutes + ":" + totalSeconds;
		} else {
			totalTime = totalMinutes + ":0" + totalSeconds;
		}
		return totalTime;
	}

}