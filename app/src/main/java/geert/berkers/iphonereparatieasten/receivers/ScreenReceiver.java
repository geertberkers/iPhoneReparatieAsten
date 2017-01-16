package geert.berkers.iphonereparatieasten.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Geert.
 */
public class ScreenReceiver extends BroadcastReceiver {

    public boolean screenTurnedOff = false;
    public boolean screenIsOnAgain = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenTurnedOff = true;
            screenIsOnAgain = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if(screenTurnedOff) {
                screenIsOnAgain = true;
            }
        }
    }
}
