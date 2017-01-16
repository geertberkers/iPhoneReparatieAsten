package geert.berkers.iphonereparatieasten.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import geert.berkers.iphonereparatieasten.listeners.OnHomePressedListener;

/**
 * Created by Geert.
 */
public class HomeButtonReceiver {

    private Context context;
    private IntentFilter intentFilter;
    private OnHomePressedListener onHomePressedListener;
    private HomeButtonBroadcastReceiver homeButtonBroadcastReceiver;

    public HomeButtonReceiver(Context context) {
        this.context = context;
        intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    public void setOnHomePressedListener(OnHomePressedListener listener) {
        onHomePressedListener = listener;
        homeButtonBroadcastReceiver = new HomeButtonBroadcastReceiver();
    }

    public void startWatch() {
        if (homeButtonBroadcastReceiver != null) {
            context.registerReceiver(homeButtonBroadcastReceiver, intentFilter);
        }
    }

    public void stopWatch() {
        if (homeButtonBroadcastReceiver != null) {
            context.unregisterReceiver(homeButtonBroadcastReceiver);
        }
    }

    class HomeButtonBroadcastReceiver extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (onHomePressedListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            onHomePressedListener.onHomePressed();
                        }
                    }
                }
            }
        }
    }
}