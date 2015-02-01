package com.example.ricardo.thtpaypalcarrinho.util;

import com.android.volley.Response;
import com.example.ricardo.thtpaypalcarrinho.application.CarrinhoApplication;
import com.example.ricardo.thtpaypalcarrinho.domain.NewTransaction;
import com.example.ricardo.thtpaypalcarrinho.domain.Token;
import com.example.ricardo.thtpaypalcarrinho.domain.User;
import com.example.ricardo.thtpaypalcarrinho.domain.UserRoot;
import com.example.ricardo.thtpaypalcarrinho.request.GsonRequest;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by ricardo on 31/01/2015.
 */
public class WebServerHelper {
    public static final String SERVER_AUTHORITY = "novatrix.com.br";
    public static final String SERVER_URL = "https://www.novatrix.com.br/gateway";
    public static final String PROJECT_NAME = "smartwatch";

    public static final String GET_TOKEN_PATH = "gettoken.php";
    public static final String GET_USER_PATH = "getuser.php";
    public static final String TRANSACTION_PATH = "transaction.php";
    public static final String OPERATION_SUCCESSFUL_PATH = "ok.php";
    public static final String ORDER_MACHINE_PATH = "broker.php";
    public static final boolean LIVE_ENVIRONMET = false;

    public static String getTokenPost() {
        return String.format("{\"projeto\":\"%s\"}", PROJECT_NAME);
    }

    public static String getUserPost(String token) {
        return String.format("{\"token\":\"%s\"}", token);
    }

    public static String getTransationPost(String token, double value) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt","BR"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return String.format("{\"token\": \"%s\", \"valor\": \"%s\"}",token,numberFormat.format(value));
    }

    public static String getMachineOrderPost(String token) {
        return String.format("{\"token\": \"%s\"}", token);
    }

    public static void performNewPayment(String token, double value, Response.Listener<NewTransaction> responseListener, Response.ErrorListener errorListener) {
        CarrinhoApplication.get().addRequestToQueue(new GsonRequest<>(
                WebServerHelper.TRANSACTION_PATH,
                WebServerHelper.getTransationPost(token,value),
                responseListener,
                errorListener,
                NewTransaction.class
        ));
    }

    public static void sendMachineOrder(Token token, Response.Listener<NewTransaction> successResponse, Response.ErrorListener errorResponse) {
        CarrinhoApplication.get().addRequestToQueue(new GsonRequest<>(
                WebServerHelper.ORDER_MACHINE_PATH,
                WebServerHelper.getMachineOrderPost(token.getToken()),
                successResponse,
                errorResponse,
                NewTransaction.class
        ));
    }

    public static void requestNewToken(Response.Listener<Token> successResponse, Response.ErrorListener errorResponse) {
        CarrinhoApplication.get().addRequestToQueue(new GsonRequest<>(
                WebServerHelper.GET_TOKEN_PATH,
                WebServerHelper.getTokenPost(),
                successResponse,
                errorResponse,
                Token.class));
    }

    public static void requestUser(Token token, Response.Listener<User> successResponse, Response.ErrorListener errorResponse) {
        CarrinhoApplication.get().addRequestToQueue(new GsonRequest<>(
                WebServerHelper.GET_USER_PATH,
                WebServerHelper.getUserPost(token.getToken()),
                successResponse,
                errorResponse,
                User.class));
    }
}

