package com.example.ricardo.thtpaypalcarrinho.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.ricardo.thtpaypalcarrinho.database.CandieSQLiteDataSource;
import com.example.ricardo.thtpaypalcarrinho.util.OkHttpStack;

/**
 * Created by ricardo on 30/01/2015.
 */
public class CarrinhoApplication extends Application {
    private static final String UNBIND_FLAG = "UNBIND_FLAG";

    private CandieSQLiteDataSource dataSource;
    private static CarrinhoApplication app;

    private RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        dataSource = new CandieSQLiteDataSource(app);
    }

    public static CandieSQLiteDataSource getDatasource() {
        if(app == null)
            return null;
        else
            return app.dataSource;
    }

    public static CarrinhoApplication get() {
        return app;
    }

    /**
     * Add a simple Volley {@link com.android.volley.Request} to the application request queue
     * @param request   A valid Volley {@link com.android.volley.Request}
     */
    public void addRequestToQueue(Request<?> request) {
        addRequestToQueue(request, CarrinhoApplication.class.getSimpleName());
    }

    /**
     * <p>Add a Volley {@link com.android.volley.Request} to the application request queue.</p>
     * <p>But, first, associate a {@link java.lang.String} tag to the request. So, it can be
     * stopped latter</p>
     * @param request   A valid Volley {@link com.android.volley.Request}
     * @param tag       {@link java.lang.String} that will be associated to the specific request
     */
    public void addRequestToQueue(Request<?> request, String tag) {
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        getQueue().add(request);
    }

    /**
     * Get the application {@link com.android.volley.RequestQueue}. It holds all the application
     * internet request
     * @return  The application {@link com.android.volley.RequestQueue}
     */
    public RequestQueue getQueue() {
        if(queue == null) {
            queue = Volley.newRequestQueue(app, new OkHttpStack());
        }
        return queue;
    }

    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public void setFromUnbind(boolean fromUnbind)
    {
        if(fromUnbind)
            getSharedPreferences().edit().putBoolean(UNBIND_FLAG, true).apply();
        else
            getSharedPreferences().edit().remove(UNBIND_FLAG).apply();
    }

}