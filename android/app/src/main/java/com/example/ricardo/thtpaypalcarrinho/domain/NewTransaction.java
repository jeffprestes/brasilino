package com.example.ricardo.thtpaypalcarrinho.domain;

import com.google.gson.annotations.SerializedName;

public class NewTransaction
{
    @SerializedName("status")
    private String status;

    @SerializedName("return")
    private String message;

    public String getMessage() {
        return message;
    }

    public boolean isSuccessfull() {
        try {
            int statusCode = Integer.parseInt(status);
            return statusCode > 0;
        } catch (Exception exception) {
            return false;
        }
    }
}
