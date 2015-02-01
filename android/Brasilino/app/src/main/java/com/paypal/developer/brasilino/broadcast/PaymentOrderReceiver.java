package com.paypal.developer.brasilino.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.paypal.developer.brasilino.PermissionActivity;
import com.paypal.developer.brasilino.application.CarrinhoApplication;
import com.paypal.developer.brasilino.database.CandieSQLiteDataSource;
import com.paypal.developer.brasilino.domain.Token;
import com.paypal.developer.brasilino.service.PaymentService;
import com.paypal.developer.brasilino.util.IntentParameters;
import com.paypal.developer.brasilino.util.Util;

/**
 * Created by ricardo on 31/01/2015.
 */
public class PaymentOrderReceiver extends BroadcastReceiver {

    public PaymentOrderReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManagerCompat.from(context).cancel(Util.NOTIFICATION_ID);

        if(intent != null && intent.hasExtra(IntentParameters.UUID)) {
            CandieSQLiteDataSource dataSource = CarrinhoApplication.getDatasource();

            if(dataSource != null) {
                Token token = dataSource.getToken();
                if(token != null) {

                    Util.sendMessage(
                            "/candies/payment",
                            "start"
                    );

                    Intent serviceIntent = new Intent(context, PaymentService.class);
                    serviceIntent.putExtras(intent.getExtras());
                    serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(serviceIntent);
                    return;
                }
            }

            Util.sendMessage(
                    "/candies/payment",
                    "token"
            );

            Intent webIntent = new Intent(context, PermissionActivity.class);
            webIntent.putExtras(intent.getExtras());
            webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(webIntent);
        }
    }
}