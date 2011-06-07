/* Copyright (c) 2011 Ivan Ivanenko <ivan.ivanenko@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. */
package com.foobnix.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.foobnix.model.FModel;
import com.foobnix.model.FModel.DOWNLOAD_STATUS;
import com.foobnix.model.FModelBuilder;
import com.foobnix.service.AlarmSleepService;
import com.foobnix.service.FoobnixNotification;
import com.foobnix.service.LastFmService;
import com.foobnix.util.C;
import com.foobnix.util.LOG;

import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.MemoryCache;

public class FoobnixApplication extends Application {
	private List<FModel> onlineItems = new ArrayList<FModel>();
	private final List<FModel> downloadItems = Collections.synchronizedList(new ArrayList<FModel>());
	private PlayListManager playListManager;
	private FModel nowPlayingSong = FModelBuilder.Empty();
	private LastFmService lastFmService;
	private AlarmSleepService alarmSleepService;
	private FoobnixNotification notification;
	private boolean isPlaying = false;

	@Override
	public void onCreate() {
		super.onCreate();
		notification = new FoobnixNotification(this);
		playListManager = new PlayListManager(this);
		lastFmService = new LastFmService(this);
		alarmSleepService = new AlarmSleepService(this, notification);

		if (isOnline()) {
			lastFmService.isConnectedAndEnable();
		}

		Caller.getInstance().setCache(new MemoryCache());
		C.get().load(this);

		TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tmgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private PhoneStateListener phoneStateListener = new PhoneStateListener() {
		private boolean needResume;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_RINGING || state == TelephonyManager.CALL_STATE_OFFHOOK) {
				needResume = isPlaying;
				FServiceHelper.getInstance().pause(FoobnixApplication.this);
			} else if (state == TelephonyManager.CALL_STATE_IDLE) {
				if (needResume) {
					FServiceHelper.getInstance().playPause(FoobnixApplication.this);
				}
			}
		}
	};

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		LOG.d("Network", netInfo);
		if (netInfo == null) {
			return false;
		}
		LOG.d(netInfo, netInfo.isConnected());
		return netInfo.isConnected();
	}

	public boolean isDownloadFinished() {
		for (FModel model : downloadItems) {
			if (model.getStatus() == DOWNLOAD_STATUS.ACTIVE) {
				return false;
			}
		}
		return true;
	}


	public void playOnAppend() {
		if (!isPlaying()) {
			FServiceHelper.getInstance().playFirst(this);
		}
	}

	public void setOnlineItems(List<FModel> onlineItems) {
		this.onlineItems = onlineItems;
	}

	public List<FModel> getOnlineItems() {
		return onlineItems;
	}

	public PlayListManager getPlayListManager() {
		return playListManager;
	}

	public void addToDownload(FModel item) {
		FServiceHelper.getInstance().addDownload(this, item);
	}

	public List<FModel> getDowloadList() {
		return downloadItems;
	}

	public void setNowPlayingSong(FModel nowPlayingFModel) {
		this.nowPlayingSong = nowPlayingFModel;
	}

	public FModel getNowPlayingSong() {
		return nowPlayingSong;
	}

	public LastFmService getLastFmService() {
		return lastFmService;
	}

	public void setNotificationManager(FoobnixNotification notificationManager) {
		this.notification = notificationManager;
	}

	public FoobnixNotification getNotificationManager() {
		return notification;
	}

	public void setNotification(FoobnixNotification notification) {
		this.notification = notification;
	}

	public FoobnixNotification getNotification() {
		return notification;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public AlarmSleepService getAlarmSleepService() {
		return alarmSleepService;
	}


}