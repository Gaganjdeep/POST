package com.ariseden.post.UtillsG;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by gagandeep on 29 Apr 2016.
 */
public class DateUtilsG
{

    public static String SEVER_FORMAT   = "yyyy-MM-dd'T'HH:mm:ss";
    public static String G_FORMAT       = "MMM dd,hh:mm a";
    public static String G_FORMAT_SHORT = "MMM dd";


    private static final SimpleDateFormat sdfServer = new SimpleDateFormat(SEVER_FORMAT);

    private static final SimpleDateFormat sdfServerThemeInfo = new SimpleDateFormat(SEVER_FORMAT);

    private static final SimpleDateFormat sdfG      = new SimpleDateFormat(G_FORMAT);

    public static Date getCurrentDate()
    {
        Calendar c = Calendar.getInstance();

        return new Date(c.getTimeInMillis());

    }


    public static Date dateServer(String date)
    {
//        sdfServer.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date_ = null;
        try
        {
            date_ = sdfServerThemeInfo.parse(date);


//            date_.setTime(date_.getTime() + TimeUnit.HOURS.toMillis(24) - (TimeUnit.SECONDS.toMillis(1)));

            return date_;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return date_;
        }
    }


    public static Date dateServerChat(String date)
    {
        sdfServer.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date_ = null;
        try
        {
            date_ = sdfServer.parse(date);

            return date_;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return date_;
        }
    }


    public static long getRemainingTime(String time)
    {
        return Long.parseLong(time);
    }


    public static long timeLeft(String date)
    {
//        sdfServer.setTimeZone(TimeZone.getTimeZone("UTC"));

        try
        {
            Date     dateServer = sdfServer.parse(date);
            Calendar cServer    = Calendar.getInstance();
            cServer.setTime(dateServer);

            long endDateTime     = cServer.getTimeInMillis();
            long currentDateTime = System.currentTimeMillis();


//            System.out.println((endDateTime - currentDateTime) + "yo yo yo yo yo yo yo");

            return (endDateTime - currentDateTime) + TimeUnit.HOURS.toMillis(24);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return 0;
    }


}
