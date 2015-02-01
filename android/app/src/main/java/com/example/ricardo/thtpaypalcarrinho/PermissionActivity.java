package com.example.ricardo.thtpaypalcarrinho;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ricardo.thtpaypalcarrinho.application.CarrinhoApplication;
import com.example.ricardo.thtpaypalcarrinho.database.CandieSQLiteDataSource;

import com.example.ricardo.thtpaypalcarrinho.domain.Token;
import com.example.ricardo.thtpaypalcarrinho.domain.User;
import com.example.ricardo.thtpaypalcarrinho.service.PaymentService;
import com.example.ricardo.thtpaypalcarrinho.util.CandiesWebViewClient;
import com.example.ricardo.thtpaypalcarrinho.util.Util;
import com.example.ricardo.thtpaypalcarrinho.util.WebServerHelper;

public class PermissionActivity extends ActionBarActivity {

    private WebView webContent;
    private ProgressBar progress;
    private Bundle receivedExtras;
    private Token token;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (receivedExtras != null)
            outState.putAll(receivedExtras);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        NotificationManagerCompat.from(getApplicationContext()).cancel(Util.NOTIFICATION_ID);

        if (savedInstanceState != null)
            receivedExtras = savedInstanceState;

        final CandieSQLiteDataSource dataSource = CarrinhoApplication.getDatasource();

        webContent = ((WebView) findViewById(R.id.webContent));
        webContent.setVisibility(View.INVISIBLE);
        CandiesWebViewClient webViewClient = new CandiesWebViewClient(this);
        webViewClient.setOnProcessStepListener(new CandiesWebViewClient.OnProcessStepListener() {
            @Override
            public void onProcessStarted() {
                progress.setVisibility(View.GONE);
                webContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onServerLoading() {
                progress.setVisibility(View.VISIBLE);
                webContent.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onProcessFinished() {
                dataSource.saveToken(token);
                /*Intent paymentIntent = new Intent(getApplicationContext(), PaymentService.class);
                if (receivedExtras != null)
                    paymentIntent.putExtras(receivedExtras);
                getApplicationContext().startService(paymentIntent);
                finish();*/
                WebServerHelper.requestUser(
                        token,
                        new Response.Listener<User>() {
                            @Override
                            public void onResponse(User response) {
                                Toast.makeText(PermissionActivity.this, "Bem vindo " + response.getFirstName() + " " + response.getLastName(), Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = CarrinhoApplication.get().getSharedPreferences().edit();
                                editor.putString("Nome", response.getFirstName() + " " + response.getLastName());
                                editor.commit();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(CarrinhoApplication.class.getSimpleName(), Log.getStackTraceString(error));
                            }
                        });;
            }
        });
        webContent.setWebViewClient(webViewClient);

        //We are enabling Javascript and Cookies for a better experience on the PayPal web site
        webContent.getSettings().setJavaScriptEnabled(true);
        webContent.getSettings().setAppCacheEnabled(true);

        progress = ((ProgressBar) findViewById(R.id.progress));
        progress.setIndeterminate(true);
        progress.setVisibility(View.VISIBLE);

        if (dataSource.getToken() == null) {
            WebServerHelper.requestNewToken(
                    new Response.Listener<Token>() {
                        @Override
                        public void onResponse(Token response) {
                            showWebContentForToken(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(CarrinhoApplication.class.getSimpleName(), Log.getStackTraceString(error));
                            TextView errorMessage = ((TextView) findViewById(R.id.errorMessage));
                            errorMessage.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                        }
                    });
        } else
            showWebContentForToken(dataSource.getToken());
    }

    private void showWebContentForToken(Token token) {
        this.token = token;
        webContent.loadUrl(token.getUrl().toString());

       /* WebServerHelper.requestUser(
                token,
                new Response.Listener<User>() {
                    @Override
                    public void onResponse(User response) {
                        Log.e(CarrinhoApplication.class.getSimpleName(), "Bem vindo " + response.getFirstName() + " " +
                                response.getLastName());
                        Toast.makeText(PermissionActivity.this, "Bem vindo " + response.getFirstName() + " " +
                                response.getLastName(), Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(CarrinhoApplication.class.getSimpleName(), Log.getStackTraceString(error));
                    }
                });;*/
    }
}

