package com.example.ricardo.thtpaypalcarrinho.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

public class CandiesWebViewClient extends WebViewClient {

    private OnProcessStepListener onProcessStepListener;
    private boolean onServer = true;
    private WeakReference<Activity> currentActivity;

    public CandiesWebViewClient(Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        Uri uri = Uri.parse(url);
        if(uri.getAuthority() != null) {
            if(uri.getAuthority().endsWith("paypal.com")) {
                if(onServer) {
                    onServer = false;
                    if(onProcessStepListener != null) onProcessStepListener.onProcessStarted();
                }
                return false;
            } else if(uri.getAuthority().endsWith(WebServerHelper.SERVER_AUTHORITY)) {
                onServer = true;
                if(uri.getPath().endsWith(WebServerHelper.OPERATION_SUCCESSFUL_PATH) && onProcessStepListener != null)
                    onProcessStepListener.onProcessFinished();
                else if(onProcessStepListener != null)
                    onProcessStepListener.onServerLoading();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (Uri.parse(url).getAuthority().endsWith("paypal.com")) {
            if (onServer) {
                onServer = false;
                if(currentActivity.get() != null && onProcessStepListener != null) {
                    currentActivity.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onProcessStepListener.onProcessStarted();
                        }
                    });
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        processUrl(request.getUrl());
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        processUrl(Uri.parse(url));
        return super.shouldInterceptRequest(view, url);
    }

    private void processUrl(Uri uri) {
        if (uri.getAuthority() != null) {
            if(uri.getAuthority().endsWith(WebServerHelper.SERVER_AUTHORITY)) {
                if(currentActivity.get() != null && onProcessStepListener != null) {
                    currentActivity.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onProcessStepListener.onServerLoading();
                        }
                    });
                }
            }
        }
    }

    public void setOnProcessStepListener(OnProcessStepListener onProcessStepListener) {
        this.onProcessStepListener = onProcessStepListener;
    }

    public interface OnProcessStepListener
    {
        public void onProcessStarted();
        public void onServerLoading();
        public void onProcessFinished();
    }

}

