package com.example.ricardo.thtpaypalcarrinho.util;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.ricardo.thtpaypalcarrinho.R;
import com.example.ricardo.thtpaypalcarrinho.broadcast.PaymentOrderReceiver;

import java.util.Calendar;

/**
 * Created by ricardo on 31/01/2015.
 */
public class Util
{
    public static final int NOTIFICATION_ID = 123456;
    public static final double PRODUCT_DEFAULT_VALUE_FIVE = 1.0f;
    public static final double PRODUCT_DEFAULT_VALUE_TEN = 2.0f;

    public static void dispatchNotification(Context context, String uuid, String major, String minor, int productImage)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), productImage);
        dispatchNotification(context, uuid, major, minor, bitmap);
        bitmap.recycle();
    }

    public static void dispatchNotification(Context context, String uuid, String major, String minor, Bitmap productImage)
    {
        Bundle infoBundle = new Bundle();
        infoBundle.putString(IntentParameters.UUID,uuid);
        infoBundle.putString(IntentParameters.MAJOR,major);
        infoBundle.putString(IntentParameters.MINOR,minor);

        Intent purchaseIntent = new Intent(context, PaymentOrderReceiver.class);
        purchaseIntent.putExtras(infoBundle);

        PendingIntent purchasePendingIntent = PendingIntent.getBroadcast(
                context,
                RequestCode.PURCHASE,
                purchaseIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLocalOnly(true)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_PROMO)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(context.getString(R.string.notification_generic_title))
                .setContentText(context.getString(R.string.notification_generic_message))
                .addAction(R.drawable.ic_logo_paypal, context.getString(R.string.action_purchase), purchasePendingIntent);

        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_ALL;

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(NOTIFICATION_ID,notification);
    }

    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void sendMessage(String path, String message) {
        Log.e("THTPAYPALCARRINHO", path + " - " + message);
    }

    public static void scheduleReceiver(Context context, Class receiver) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MILLISECOND, 500);

        AlarmManager alarmManager = (AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent receiverIntent = new Intent(context.getApplicationContext(), receiver);
        PendingIntent receiverPendent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, receiverIntent, 0);
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                receiverPendent
        );
    }
}

