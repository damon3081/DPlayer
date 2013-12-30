package com.damon.videoplayer.receiver;

import io.vov.vitamio.VIntent;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MediaScannerReceiver extends BroadcastReceiver{
	private static boolean isScanning = false;
	//	private boolean isScanningStarted = false;
	private IReceiverNotify mNotify;

	public MediaScannerReceiver() {

	}

	public MediaScannerReceiver(IReceiverNotify notify) {
		mNotify = notify;
	}

	
	public static boolean isScanning(Context ctx) {
		return isServiceRunning(ctx, "io.vov.vitamio.MediaScannerService");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		Log.i("MediaScannerReceiver", action);
		if (VIntent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
			isScanning = true;
			//			isScanningStarted = true;
			if (mNotify != null)
				mNotify.receiver(0);
		} else if (VIntent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
			isScanning = false;
			if (mNotify != null) {
				mNotify.receiver(2);
			}
		}
	}

	/** �����Ƿ��������� */
	public static boolean isServiceRunning(Context ctx, String name) {
		ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (name.equals(service.service.getClassName()))
				return true;
		}
		return false;
	}

}
