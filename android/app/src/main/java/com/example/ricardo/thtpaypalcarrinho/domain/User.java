package com.example.ricardo.thtpaypalcarrinho.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ricardo on 31/01/2015.
 */
public class User {

    @SerializedName("firstname")
    private String firstName;
    @SerializedName("lastname")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("endereco")
    private String endereco;
    @SerializedName("project")
    private String project;
    @SerializedName("pais")
    private String pais;
    @SerializedName("cidade")
    private String cidade;
    @SerializedName("estado")
    private String estado;

    public User() {}

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

}
