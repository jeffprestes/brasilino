package com.paypal.developer.brasilino;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;


public class Home extends ActionBarActivity implements View.OnTouchListener{

    private ImageView imgEsquerda;
    private ImageView imgDireita;
    private ImageView imgCima;
    private ImageView imgBaixo;

    private String ip;

    private BufferedWriter escritorLinhas = null;
    private OutputStreamWriter escritorCaracteres = null;
    private OutputStream escritorSocket = null;
    private Socket s = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        ip = sp.getString("ip", "");

        if (ip.endsWith("tht")){
            ip = ip.substring(0, ip.length() - 3);
        }

        Toast.makeText(this, ip, Toast.LENGTH_LONG).show();

        getSupportActionBar().hide();

        boolean isFive = getIntent().getBooleanExtra("isFive", false);
        Timer timer = new Timer();
        TimerTask tTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Home.this, "Tempo Esgotado!", Toast.LENGTH_LONG).show();
                    }
                });

                finish();
            }
        };
        timer.schedule(tTask, isFive?1000*60*5:1000*60*10);

        imgEsquerda = (ImageView) findViewById(R.id.imgEsquerda);
        imgEsquerda.setOnTouchListener(this);

        imgDireita = (ImageView) findViewById(R.id.imgDireita);
        imgDireita.setOnTouchListener(this);

        imgCima = (ImageView) findViewById(R.id.imgCima);
        imgCima.setOnTouchListener(this);

        imgBaixo = (ImageView) findViewById(R.id.imgBaixo);
        imgBaixo.setOnTouchListener(this);

        imgEsquerda.setEnabled(true);
        imgBaixo.setEnabled(true);
        imgCima.setEnabled(true);
        imgDireita.setEnabled(true);

        WebView web = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.loadUrl("http://"+ip+":8181/camera.php");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (v == imgEsquerda) {
                new Assincrono().execute("EA;");
            } else if (v == imgDireita) {
                new Assincrono().execute("DA;");
            } else if (v == imgCima) {
                new Assincrono().execute("FA;");
            } else if (v == imgBaixo) {
                new Assincrono().execute("TA;");
            }
         } else if (event.getAction() == MotionEvent.ACTION_UP){
            if (v == imgEsquerda) {
                new Assincrono().execute("ED;");
            } else if (v == imgDireita) {
                new Assincrono().execute("DD;");
            } else if (v == imgCima) {
                new Assincrono().execute("FD;");
            } else if (v == imgBaixo) {
                new Assincrono().execute("PP;");
            }
        }
        return true;
    }

    class Assincrono extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params)   {

            Log.d("COMANDO_PARA_CARRINHO", params[0]);

            try {
                s = new Socket(ip, 8282);

                escritorSocket = s.getOutputStream();
                escritorCaracteres = new OutputStreamWriter(escritorSocket);
                escritorLinhas = new BufferedWriter(escritorCaracteres);

                escritorLinhas.write(params[0]);

                escritorLinhas.flush();
                escritorLinhas.close();
                escritorCaracteres.flush();
                escritorCaracteres.close();
                escritorSocket.flush();
                escritorSocket.close();
                s.close();

                s=null;
                escritorLinhas = null;
                escritorCaracteres = null;
                escritorSocket = null;

                return "foi";

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "UnknownHostException: " + e;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "IOException: "+e;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
