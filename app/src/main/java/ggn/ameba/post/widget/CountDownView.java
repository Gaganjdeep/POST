package ggn.ameba.post.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import ggn.ameba.post.R;

public class CountDownView extends RelativeLayout
{
    private TextView mDays, mHours, mMinutes, mSeconds, mMilliseconds;
    private long mCurrentMillis, mBeginTime;
    private boolean mIsTimerRunning = false, mIsAlarmRunning = false;
    private Intent        mTimerIntent;
    private String        mAlarmSoundPath;
    private TimerListener listener;

//    private static final Calendar      mTime      = Calendar.getInstance();
    private static final DecimalFormat mFormatter = new DecimalFormat("00");

    private Messenger mMessenger = new Messenger(new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            mCurrentMillis = (Long) msg.obj;
            if (mCurrentMillis == 0)
            {
                mIsAlarmRunning = true;
                onCountDownFinished();
            }
            updateUI(mCurrentMillis);
        }
    });

    private void onCountDownFinished()
    {
        listener.timerElapsed();
        mIsTimerRunning = false;
        startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blink));
    }

    public CountDownView(Context context)
    {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mTimerIntent = new Intent(context, TimerService.class);

        LayoutInflater.from(context).inflate(R.layout.countdownview_main, this, true);

        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
//        int        numColorId  = values.getResourceId(R.styleable.CountDownView_numberColor, android.R.attr.textColor);
//        int        unitColorId = values.getResourceId(R.styleable.CountDownView_unitColor, android.R.attr.textColor);

        if (values.getBoolean(R.styleable.CountDownView_showDays, false))
        {
            View v = ((ViewStub) findViewById(R.id.days_stub)).inflate();
            mDays = (TextView) v.findViewById(R.id.days);
//            ((TextView) v.findViewById(R.id.hours_unit));
//            .setTextColor(getResources().getColorStateList(unitColorId));
//            mHours.setTextColor(getResources().getColorStateList(numColorId));
        }


        if (values.getBoolean(R.styleable.CountDownView_showHour, false))
        {
            View v = ((ViewStub) findViewById(R.id.hours_stub)).inflate();
            mHours = (TextView) v.findViewById(R.id.hours);
//            ((TextView) v.findViewById(R.id.hours_unit));
//            .setTextColor(getResources().getColorStateList(unitColorId));
//            mHours.setTextColor(getResources().getColorStateList(numColorId));
        }

        if (values.getBoolean(R.styleable.CountDownView_showMin, false))
        {
            View v = ((ViewStub) findViewById(R.id.minutes_stub)).inflate();
            mMinutes = (TextView) v.findViewById(R.id.minutes);
//            ((TextView) v.findViewById(R.id.minutes_unit)).setTextColor(getResources().getColorStateList(unitColorId));
//            mMinutes.setTextColor(getResources().getColorStateList(numColorId));
        }

        if (values.getBoolean(R.styleable.CountDownView_showSec, false))
        {
            View v = ((ViewStub) findViewById(R.id.seconds_stub)).inflate();
            mSeconds = (TextView) v.findViewById(R.id.seconds);
//            ((TextView) v.findViewById(R.id.seconds_unit)).setTextColor(getResources().getColorStateList(unitColorId));
//            mSeconds.setTextColor(getResources().getColorStateList(numColorId));
        }

       /* if (values.getBoolean(R.styleable.CountDownView_showMilli, false))
        {
            View v = ((ViewStub) findViewById(R.id.milliseconds_stub)).inflate();
            mMilliseconds = (TextView) v.findViewById(R.id.milliseconds);
//            ((TextView) v.findViewById(R.id.milliseconds_unit)).setTextColor(getResources().getColorStateList(unitColorId));
//            mMilliseconds.setTextColor(getResources().getColorStateList(numColorId));
        }*/
    }

    /**
     * Set listner to notify when timer reaches zero
     */
    public void setListener(TimerListener listener)
    {
        this.listener = listener;
    }

    /**
     * Sets the initial time for this countdown. This is fixed and will
     * not change unless a call to {@link #setInitialTime} is made.
     *
     * @param millisInFuture - Time in milliseconds to countdown from.
     */
    public void setInitialTime(long millisInFuture)
    {
        mBeginTime = millisInFuture;
        setCurrentTime(millisInFuture);
    }

    /**
     * Sets the current countdown time. May not necessarily be the same
     * as the initial countdown time.
     *
     * @param millisInFuture - Time in milliseconds to countdown from.
     */
    public void setCurrentTime(long millisInFuture)
    {
        mCurrentMillis = millisInFuture;
        updateUI(millisInFuture);
    }

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR   = 60 * MINUTE;
    private static final int DAY    = 24 * HOUR;

    private void updateUI(long millisInFuture)
    {
//        mTime.setTimeInMillis(millisInFuture);

//        Calendar cal = new GregorianCalendar();
//        cal.setTimeInMillis(millisInFuture);

        String days     = "0";
        String hours    = "0";
        String minutess = "0";
        String secs     = "0";


// TODO: this is the value in ms
        long         ms   = millisInFuture;
        StringBuffer text = new StringBuffer("");
        if (ms > DAY)
        {
            days = (ms / DAY) + "";
//            text.append(ms / DAY).append(" days ");
            ms %= DAY;
        }
        if (ms > HOUR)
        {
            hours = (ms / HOUR) + "";
//            text.append(ms / HOUR).append(" hours ");
            ms %= HOUR;
        }
        if (ms > MINUTE)
        {
            minutess = (ms / MINUTE) + "";
//            text.append(ms / MINUTE).append(" minutes ");
            ms %= MINUTE;
        }
        if (ms > SECOND)
        {
            secs = (ms / SECOND) + "";
//            text.append(ms / SECOND).append(" seconds ");
            ms %= SECOND;
        }
//        text.append(ms + " ms");
//        System.out.println(text.toString());

        if (mDays != null)
            mDays.setText(days);
//            mDays.setText(TimeUnit.MILLISECONDS.toDays(millisInFuture) + "");


        if (mHours != null)
            mHours.setText(hours);
//            mHours.setText(TimeUnit.MILLISECONDS.toHours(millisInFuture) + "");

        if (mMinutes != null)
            mMinutes.setText(minutess);
//            mMinutes.setText(TimeUnit.MILLISECONDS.toMinutes(millisInFuture) + "");

        if (mSeconds != null)
            mSeconds.setText(secs);
//            mSeconds.setText(TimeUnit.MILLISECONDS.toSeconds(millisInFuture) + "");

//        if (mMilliseconds != null)
//            mMilliseconds.setText(millisInFuture + "");
    }

    /**
     * Starts the timer.
     */
    public void start()
    {
        mIsTimerRunning = true;
        mTimerIntent.putExtra("messenger", mMessenger);
        mTimerIntent.putExtra("millis", mCurrentMillis);
        if (mAlarmSoundPath != null)
            mTimerIntent.putExtra("alarm_sound", mAlarmSoundPath);
        getContext().startService(mTimerIntent);
    }

    /**
     * Stops the timer.
     */
    public void stop()
    {
        mIsTimerRunning = false;
        clearAnimation();
        getContext().stopService(mTimerIntent);
    }

    /**
     * Resets the timer.
     */
    public void reset()
    {
        stop();
        mCurrentMillis = mBeginTime;
        updateUI(mCurrentMillis);
    }

    /**
     * Checks if the countdown timer is currently running.
     *
     * @return true if running, false otherwise.
     */
    public boolean isTimerRunning()
    {
        return mIsTimerRunning;
    }

    /**
     * Gets the current time of the countdown timer.
     *
     * @return a long that represents the current time in milliseconds.
     */
    public long getCurrentMillis()
    {
        return mCurrentMillis;
    }

    /**
     * Sets a custom alarm sound to be played when timer goes off.
     *
     * @param assetPath - Relative path under assets directory.
     *                  i.e. "sounds/Timer_Expire.ogg"
     */
    public void setAlarmSound(String assetPath)
    {
        mAlarmSoundPath = assetPath;
    }

    public void onSaveInstanceState(Bundle outState)
    {
        if (!mIsTimerRunning)
        {
            outState.putLong("currentMillis", mCurrentMillis);
        }
        outState.putBoolean("isTimerRunning", mIsTimerRunning);
        outState.putBoolean("isAlarmRunning", mIsAlarmRunning);
    }

    public void onRestoreSavedInstanceState(Bundle savedState)
    {
        mIsTimerRunning = savedState.getBoolean("isTimerRunning");
        mIsAlarmRunning = savedState.getBoolean("isAlarmRunning");

        if (mIsTimerRunning)
        {
            mTimerIntent.putExtra("messenger", mMessenger);
            getContext().startService(mTimerIntent);
        }
        else
        {
            mCurrentMillis = savedState.getLong("currentMillis");
            updateUI(mCurrentMillis);
        }

        if (mIsAlarmRunning)
        {
            onCountDownFinished(); // Resume blinking
        }
    }
}
