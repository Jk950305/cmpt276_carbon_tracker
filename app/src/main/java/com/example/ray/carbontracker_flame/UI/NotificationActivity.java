package com.example.ray.carbontracker_flame.UI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ray.carbontracker_flame.R;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;

/**
 * NotificationActivity creates all-time time watcher and send notifications at 9pm, and reset today's journey count at 0am
 */
public class NotificationActivity extends AppCompatActivity {

    public static final String SHAREDPREF_NOTI = "Notification SP";
    public static final String SHAREDPREF_TODAYS_JOURNEYCOUNT = "todaysJourneyCount";
    public static final String SHAREDPREF_LAST_BILLDATE = "lastBillDate";
    public static int todaysJourneyCount;
    public static long lastBillDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRunnableNotis();
        startActivity(MainActivity.makeIntent(NotificationActivity.this));
        finish();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, NotificationActivity.class);
    }

    public void setRunnableNotis() {
        Thread myThread = null;
        Runnable myRunnableThread = new NotiImple();
        myThread = new Thread(myRunnableThread);
        myThread.start();
    }

    private class NotiImple implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    liveCalculateTime();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void liveCalculateTime() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isMidnight()) {
                        resetTodaysJourneyCount();
                    }
                    getNotificationSetting();
                    if (isNinePM()) {
                        if (todaysJourneyCount == 0) {
                            setNotification(getString(R.string.add_a_journey), getString(R.string.zero_journey), JourneyActivity.class);
                        } else if (isPassed()) {
                            setNotification(getString(R.string.add_gas_bill), getString(R.string.more_than_month), AddBillActivity.class);
                        } else {
                            setNotification(getString(R.string.add_more_journeys),
                                    getString(R.string.you_entered) + todaysJourneyCount +
                                            getString(R.string.journeys_today),
                                    JourneyActivity.class);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setNotification(String title, String text, Class next) {
        Intent intent = new Intent(this, next);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setTicker("TickerTitle")
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent).getNotification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private void getNotificationSetting() {
        SharedPreferences setting = getSharedPreferences(SHAREDPREF_NOTI, Context.MODE_PRIVATE);
        todaysJourneyCount = setting.getInt(SHAREDPREF_TODAYS_JOURNEYCOUNT, 0);
        lastBillDate = setting.getLong(SHAREDPREF_LAST_BILLDATE, 0);
    }

    private void resetTodaysJourneyCount() {
        SharedPreferences setting = getSharedPreferences(SHAREDPREF_NOTI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt(SHAREDPREF_TODAYS_JOURNEYCOUNT, 0);
        editor.apply();
    }

    public boolean isNinePM() {
        Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int min = date.get(Calendar.MINUTE);
        int sec = date.get(Calendar.SECOND);

        if (hour == 21 && min == 0 && sec == 0) { //21:00:00 = 9pm
//        if(min%2==0 && sec==0){ // send every 2min
            return true;
        } else {
            return false;
        }
    }

    public boolean isMidnight() {
        Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int min = date.get(Calendar.MINUTE);
        int sec = date.get(Calendar.SECOND);
        if (hour == 0 && min == 0 && sec == 0) { //midnight = 0:0:0
//        if(min%5==0&&sec==0){ //reset every 5 min
            return true;
        } else {
            return false;
        }
    }

    public boolean isPassed() {
        if (lastBillDate == 0) {
            return true;
        } else {
            DateTime start = new DateTime(lastBillDate);
            DateTime end = new DateTime(Calendar.getInstance().getTimeInMillis());
            int days = Days.daysBetween(start, end).getDays();
            if (days > 45) {
                return true;
            }
        }
        return false;
    }
}
