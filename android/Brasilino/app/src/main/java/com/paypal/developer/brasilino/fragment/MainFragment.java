package com.paypal.developer.brasilino.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.paypal.developer.brasilino.Home;
import com.paypal.developer.brasilino.PermissionActivity;
import com.paypal.developer.brasilino.R;
import com.paypal.developer.brasilino.application.CarrinhoApplication;
import com.paypal.developer.brasilino.domain.Token;
import com.paypal.developer.brasilino.domain.User;
import com.paypal.developer.brasilino.domain.UserRoot;
import com.paypal.developer.brasilino.service.PaymentService;
import com.paypal.developer.brasilino.util.Util;
import com.paypal.developer.brasilino.util.WebServerHelper;

public class MainFragment extends Fragment {

    private TextView mTvInformation;
    private Button mBtPurchase;
    private PaymentService paymentService;
    private RadioGroup rdbTime;
    private RadioButton rdbFive;
    private boolean lastTimeIsFive;

    public MainFragment() {}

    private SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mTvInformation = ((TextView) rootView.findViewById(R.id.tvInformation));

        rdbTime = (RadioGroup) rootView.findViewById(R.id.rdb);
        rdbFive = (RadioButton) rootView.findViewById(R.id.rdbCinco);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mBtPurchase = ((Button) rootView.findViewById(R.id.btPurchase));
        mBtPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle infoBundle = null;
                //TODO: Refazer a logica de desabilitar a cobrança do paypal para Ricardo
                //Criar outra variavel em SharedPreferences
                if (sp.getString("ip", "").endsWith("tht")){
                    goHome();
                } else if(CarrinhoApplication.getDatasource().getToken() == null) {
                    Intent permissionIntent = new Intent(getActivity(), PermissionActivity.class);
                    if(infoBundle != null)
                        permissionIntent.putExtras(infoBundle);
                    getActivity().startActivity(permissionIntent);
                    Util.sendMessage(
                            "/candies/payment",
                            "token"
                    );
                }else {
                    Intent purchaseIntent = new Intent(getActivity().getApplicationContext(), PaymentService.class);
                    if (infoBundle != null)
                        purchaseIntent.putExtras(infoBundle);
                    purchaseIntent.putExtra("five", rdbFive.isChecked());
                    lastTimeIsFive = rdbFive.isChecked();
                    getActivity().startService(purchaseIntent);
                    Util.sendMessage(
                            "/candies/payment",
                            "start"
                    );
                }

                NotificationManagerCompat.from(getActivity().getApplicationContext()).cancel(Util.NOTIFICATION_ID);
            }
        });

        checkViews();


        mTvInformation.setText(getString(R.string.message_machine_close));
        mBtPurchase.setVisibility(View.VISIBLE);

        return rootView;
    }

    public void checkViews(){

        if (CarrinhoApplication.get().getSharedPreferences().getString("Nome", null) != null){
            mBtPurchase.setText("Comprar Tempo");
            mTvInformation.setText("Conta: "+CarrinhoApplication.get().getSharedPreferences().getString("Nome", ""));
            rdbTime.setVisibility(View.VISIBLE);
        } else {
            rdbTime.setVisibility(View.GONE);
            mBtPurchase.setText("Obter Permissão");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().bindService(
                new Intent(getActivity().getApplicationContext(), PaymentService.class),
                mPaymentConnection,
                Context.BIND_AUTO_CREATE
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        checkViews();
    }

    @Override
    public void onStop() {
        CarrinhoApplication.get().setFromUnbind(true);
        getActivity().unbindService(mPaymentConnection);
        super.onStop();
    }


    private ServiceConnection mPaymentConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PaymentService.LocalBinder binder = (PaymentService.LocalBinder)service;
            paymentService = binder.getService();
            paymentService.setPaymentStepsListener(paymentStepsListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            paymentService = null;
        }
    };

    private PaymentService.PaymentStepsListener paymentStepsListener = new PaymentService.PaymentStepsListener() {
        @Override
        public void onPaymentStarted(Token token) {
            Util.sendMessage("/candies/payment/started", "New payment");
        }

        @Override
        public void onPaymentFinished(Token token, boolean successful, String message) {
            Util.sendMessage("/candies/payment/finished",token.getToken());
            Util.sendMessage("/candies/payment/finished",message);
            Util.sendMessage("/candies/payment/finished",(successful?"true":"false"));

            if (successful) {
                goHome();
            }
        }
    };

    public void goHome(){
        Intent o = new Intent(getActivity(), Home.class);
        o.putExtra("isFive", lastTimeIsFive);
        startActivity(o);
    }
}
