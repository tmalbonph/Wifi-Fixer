/*Copyright [2010-2011] [David Van de Ven]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.wahtod.wififixer.utility;

import org.wahtod.wififixer.R;
import org.wahtod.wififixer.legacy.HoneyCombNotifUtil;
import org.wahtod.wififixer.legacy.LegacyNotifUtil;
import org.wahtod.wififixer.legacy.VersionedLogFile;
import org.wahtod.wififixer.utility.StatusMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public abstract class NotifUtil {
	public static final int STATNOTIFID = 2392;
	public static final int MAX_SSID_LENGTH = 16;
	public static final int LOGNOTIFID = 2494;
	public static int ssidStatus = 0;
	public static Notification statnotif;
	public static Notification lognotif;
	public static PendingIntent contentIntent;

	/*
	 * for SSID status in status notification
	 */
	public static final int SSID_STATUS_UNMANAGED = 3;
	public static final int SSID_STATUS_MANAGED = 7;
	public static final String NULL_SSID = "empty";
	public static final String SEPARATOR = " : ";

	public static final String ACTION_STATUS_NOTIFICATION = "org.wahtod.wififixer.STATNOTIF";
	public static final String STATUS_DATA_KEY = "STATUS_DATA_KEY";

	/*
	 * Field keys for status bundle
	 */
	public static final String SSID_KEY = "SSID";
	public static final String STATUS_KEY = "STATUS";
	public static final String SIGNAL_KEY = "SIGNAL";

	/*
	 * Icon type for getIconfromSignal()
	 */
	public static final int ICON_SET_SMALL = 0;
	public static final int ICON_SET_LARGE = 1;

	/*
	 * Cache appropriate NotifUtil
	 */
	private static NotifUtil selector;

	/*
	 * API
	 */

	public abstract void vaddStatNotif(Context ctxt, final String ssid,
			String status, final int signal, final boolean flag);

	public abstract void vaddLogNotif(final Context context, final boolean flag);

	public abstract void vshow(final Context context, final String message,
			final String tickerText, final int id, PendingIntent contentIntent);

	public abstract void vsetStatNotifWifiState(final Context ctxt,
			final boolean wifistate);

	public static void setStatNotifWifiState(final Context ctxt,
			final boolean wifistate) {
		cacheSelector();
		selector
				.vsetStatNotifWifiState(ctxt.getApplicationContext(), wifistate);
	}

	public static void setSsidStatus(final int status) {
		ssidStatus = status;
	}

	/*
	 * Exposed API and utility methods
	 */
	public static void addStatNotif(final Context ctxt, final StatusMessage m) {
		cacheSelector();
		/*
		 * Show (or cancel) notification
		 */
		selector.vaddStatNotif(ctxt, m.ssid, m.status, m.signal, m.show);
	}

	public static void broadcastStatNotif(final Context ctxt,
			final StatusMessage m) {
		Intent intent = new Intent(ACTION_STATUS_NOTIFICATION);
		Bundle message = new Bundle();
		message.putString(SSID_KEY, m.ssid);
		message.putString(STATUS_KEY, m.status);
		message.putInt(SIGNAL_KEY, m.signal);
		intent.putExtra(STATUS_DATA_KEY, message);
		ctxt.sendBroadcast(intent);
	}

	public static void addLogNotif(final Context context, final boolean flag) {
		cacheSelector();
		selector.vaddLogNotif(context, flag);
	}

	public static void show(final Context context, final String message,
			final String tickerText, final int id, PendingIntent contentIntent) {
		cacheSelector();
		selector.vshow(context, message, tickerText, id, contentIntent);
	}

	private static void cacheSelector() {
		/*
		 * Instantiate and cache appropriate NotifUtil implementation
		 */
		if (selector == null) {
			if (Build.VERSION.SDK_INT > 10) {
				selector = new HoneyCombNotifUtil();
			} else
				selector = new LegacyNotifUtil();
		}
	}

	public static int getIconfromSignal(int signal, int iconset) {
		switch (signal) {
		case 0:
			if (iconset == ICON_SET_SMALL)
				signal = R.drawable.statsignal0;
			else
				signal = R.drawable.signal0;
			break;
		case 1:
			if (iconset == ICON_SET_SMALL)
				signal = R.drawable.statsignal1;
			else
				signal = R.drawable.signal1;
			break;
		case 2:
			if (iconset == ICON_SET_SMALL)
				signal = R.drawable.statsignal2;
			else
				signal = R.drawable.signal2;
			break;
		case 3:
			if (iconset == ICON_SET_SMALL)
				signal = R.drawable.statsignal3;
			else
				signal = R.drawable.signal3;
			break;
		case 4:
			if (iconset == ICON_SET_SMALL)
				signal = R.drawable.statsignal4;
			else
				signal = R.drawable.signal4;
			break;
		}
		return signal;
	}

	public static StringBuilder getLogString(final Context context) {
		StringBuilder logstring = new StringBuilder(context
				.getString(R.string.writing_to_log));
		logstring.append(NotifUtil.SEPARATOR);
		logstring.append(VersionedLogFile.getLogFile(context).length() / 1024);
		logstring.append(context.getString(R.string.k));
		return logstring;
	}

	public static String truncateSSID(String ssid) {
		if (ssid == null || ssid.length() < 1)
			return NULL_SSID;
		else if (ssid.length() < MAX_SSID_LENGTH)
			return ssid;
		else
			return ssid.substring(0, MAX_SSID_LENGTH);

	}

	public static void cancel(final Context context, final int notif) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(notif);
	}

}