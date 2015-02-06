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

    private boolean frente = false;
    private boolean tras = false;

    private String ip;

    private BufferedWriter escritorLinhas;
    private OutputStreamWriter escritorCaracteres;
    private OutputStream escritorSocket;

    private Socket s;

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

        imgEsquerda.setEnabled(false);
        imgBaixo.setEnabled(false);
        imgCima.setEnabled(false);
        imgDireita.setEnabled(false);

        WebView web = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.loadUrl("http://"+ip+":8181/camera.php");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CreateSocket().execute();
    }

    public void cima(View v) {
            new Assincrono().execute("A;");
    }

    public void baixo(View v) {
            new Assincrono().execute("F;");
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
                new Assincrono().execute("TD;");
            }
        }
        return true;
    }

    class Assincrono extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                s = new Socket(ip, 8282);

                escritorSocket = s.getOutputStream();
                escritorCaracteres = new OutputStreamWriter(escritorSocket);
                escritorLinhas = new BufferedWriter(escritorCaracteres);
                escritorLinhas.write(params[0]);
                escritorLinhas.flush();
                escritorLinhas.close();

                s.close();

                return "foi";
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "UnknownHostException: ";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "IOException: "+e;
            }
        }

    }

    class CreateSocket extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pd = ProgressDialog.show(, getString(R.string.aviso), getString(R.string.corpo_aviso));
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                s = new Socket(ip, 8282);

                escritorSocket = s.getOutputStream();
                escritorCaracteres = new OutputStreamWriter(escritorSocket);
                escritorLinhas = new BufferedWriter(escritorCaracteres);
                escritorLinhas.write(params[0]);
                escritorLinhas.flush();
                escritorLinhas.close();

                s.close();

                return "foi";

            } catch (UnknownHostException e) {
                e.printStackTrace();
                return "UnknownHostException: ";
            } catch (IOException e) {
                e.printStackTrace();
                return "IOException: "+e;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pd.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            escritorLinhas.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        super.onDestroy();
    }
}
