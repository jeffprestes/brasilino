package com.example.ricardo.thtpaypalcarrinho;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
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

    private boolean frente = false;
    private boolean tras = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        WebView web = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.loadUrl("http://192.168.1.42:8181/camera.php");
    }

    public void cima(View v) {
            new Assincrono().execute("F");
    }

    public void baixo(View v) {
            new Assincrono().execute("T");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (v == imgEsquerda) {
                new Assincrono().execute("E");
            } else {
                new Assincrono().execute("D");
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            if (v == imgEsquerda) {
                new Assincrono().execute("E");
            } else {
                new Assincrono().execute("D");
            }
        }
        return true;
    }

    class Assincrono extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedWriter escritorLinhas;
            OutputStreamWriter escritorCaracteres;
            OutputStream escritorSocket;

            Socket s;
            try {
                s = new Socket("192.168.1.42", 8282);

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
}
