package com.ariseden.post;

/**
 * Created by gagandeep on 26 Apr 2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.ariseden.post.activities.SplashActivity;
import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONObject;
import com.ariseden.post.R;

import com.ariseden.post.UtillsG.GlobalConstantsG;
import com.ariseden.post.activities.ChatActivity;
import com.ariseden.post.adapter.RecentChatModel;


public class GCMIntentService extends GCMBaseIntentService
{

    static int flag = 0;

    Bundle bun;

    public GCMIntentService()
    {
        super(GlobalConstantsG.SENDER_ID);
    }


    private void showNotification(Context context, String message, Intent intent)
    {
        Intent notificationIntent = intent;
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher);

        builder.setContentIntent(pendingIntent);

        builder.setAutoCancel(true);


//        if (!taskData.getImage().isEmpty())
//        {
//
//            try
//            {
//                Uri imageUri = Uri.parse(taskData.getImage());
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
////        }
//                builder.setLargeIcon(bitmap);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        else
//        {
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
//        }

        builder.setContentTitle("POST");
        builder.setContentText(message);

        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(message));
        builder.setPriority(Notification.PRIORITY_MAX);

//        builder.setSubText("Tap to open EasyBeezee and set reminders..!");


        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (defaultSound == null)
        {
            defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (defaultSound == null)
            {
                defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
        }

        builder.setSound(defaultSound);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, builder.build());
    }

    @Override
    protected void onRegistered(Context context, String registrationId)
    {
//		Log.i(TAG, "Device registered: regId = " + registrationId);

    }

    @Override
    protected void onUnregistered(Context context, String registrationId)
    {
//		Log.i(TAG, "Device unregistered");

    }


    @Override
    protected void onMessage(Context context, Intent intent)
    {
        try
        {
            bun = intent.getExtras();
            String     jsonString = intent.getStringExtra("message");
            JSONObject jobj       = new JSONObject(jsonString);
            String     flag       = jobj.getString("flag");
            Intent     notificationIntent;


            //TODO : flags added for GCM notifications.
            //            AdminMessage
            //            NewChatMessage


            if (flag.equals("NewChatMessage"))
            {
                Intent updateUnreadmsg = new Intent(GlobalConstantsG.BROADCAST_GETUNREAD);
                context.sendBroadcast(updateUnreadmsg);

                notificationIntent = new Intent(context, ChatActivity.class);
                RecentChatModel recentChatModel = new RecentChatModel();
                recentChatModel.setCustomerIdTo(jobj.getString("CustomerIdBy"));
                recentChatModel.setCustomerName(jobj.getString("UserName"));
                recentChatModel.setChatContent("");
                recentChatModel.setDateTimeCreated("");
                recentChatModel.setPhotoPath(jobj.optString("PhotoPath"));
                recentChatModel.setIsRead("");

                notificationIntent.putExtra("data", recentChatModel);
            }
            else
            {
                notificationIntent = new Intent(context, SplashActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            String msgShow = jobj.optString("message");

            showNotification(context, msgShow, notificationIntent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDeletedMessages(Context context, int total)
    {

//		String message = "";
        // generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId)
    {

    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId)
    {
        return super.onRecoverableError(context, errorId);
    }
}