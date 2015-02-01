package com.example.ricardo.thtpaypalcarrinho.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ricardo.thtpaypalcarrinho.application.CarrinhoApplication;
import com.example.ricardo.thtpaypalcarrinho.domain.NewTransaction;
import com.example.ricardo.thtpaypalcarrinho.domain.Token;
import com.example.ricardo.thtpaypalcarrinho.util.Util;
import com.example.ricardo.thtpaypalcarrinho.util.WebServerHelper;

public class PaymentService extends Service {

    private LocalBinder mBinder = new LocalBinder();
    private PaymentStepsListener paymentStepsListener;
    private Token token = null;


    public interface PaymentStepsListener {
        public void onPaymentStarted(Token token);
        public void onPaymentFinished(Token token, boolean successful, String message);
    }


    public PaymentService() {
        token = CarrinhoApplication.getDatasource().getToken();
    }

    private String getStringToken()    {
        return token.getToken();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Token token = CarrinhoApplication.getDatasource().getToken();

        if(token != null) {
            NotificationManagerCompat.from(getApplicationContext()).cancel(Util.NOTIFICATION_ID);
            WebServerHelper.performNewPayment(
                    this.getStringToken(),
                    intent.getBooleanExtra("five", false)?Util.PRODUCT_DEFAULT_VALUE_FIVE:Util.PRODUCT_DEFAULT_VALUE_TEN,
                    this.getResponsePaymentListener(),
                    this.getErrorPaymentListener()
            );

            if(paymentStepsListener != null) {
                paymentStepsListener.onPaymentStarted(token);
            }

            return START_STICKY;
        } else {
            return START_NOT_STICKY;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setPaymentStepsListener(PaymentStepsListener paymentStepsListener) {
        this.paymentStepsListener = paymentStepsListener;
    }


    public class LocalBinder extends Binder {
        public PaymentService getService() {
            return PaymentService.this;
        }
    }


    /**
     * Return specific Response Payment listener to payment webservice call
     */
    public Response.Listener<NewTransaction> getResponsePaymentListener() {

        return new Response.Listener<NewTransaction>() {

            public void onResponse (NewTransaction response)    {
                if (response.isSuccessfull()) {

                    WebServerHelper.sendMachineOrder(
                            token,
                            new Response.Listener<NewTransaction>() {

                                @Override
                                public void onResponse(NewTransaction response) {
                                    if(response.isSuccessfull()) {
                                        Util.sendMessage(
                                                "/candies/payment",
                                                "success"
                                        );
                                        finishService("Pegar dados do usuário...", true);
                                    } else {
                                        finishService("Erro no comando à maquina.", false);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(PaymentService.class.getSimpleName(), "Erro no envio à máquina", error);
                                    finishService(Log.getStackTraceString(error), false);
                                }
                            }
                    );
                } else {
                    finishService(response.getMessage(), false);
                }
            }

        };
    }

    private void finishService(String message, boolean successful) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

        Util.sendMessage(
                "/candies/payment",
                (successful?"success":"fail")
        );

        if (paymentStepsListener != null)
            paymentStepsListener.onPaymentFinished(token, successful, message);
        stopSelf();
    }

    /**
     * Specific error listener for payment webservice call
     * @return Error Listener for payment webservice call
     */
    public Response.ErrorListener getErrorPaymentListener() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e(CarrinhoApplication.class.getSimpleName(), Log.getStackTraceString(error));
                Toast.makeText(getApplicationContext(), "Erro no pagamento: " + error.getLocalizedMessage() + " | Tempo final: " + (Calendar.getInstance().getTimeInMillis()), Toast.LENGTH_SHORT).show();
                if (paymentStepsListener != null)
                    paymentStepsListener.onPaymentFinished(token, false, error.getMessage());
                stopSelf();
            }
        };
    }
}
