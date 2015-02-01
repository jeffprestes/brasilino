package com.example.ricardo.thtpaypalcarrinho.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class Token
{
    @SerializedName("token")
    private String token;
    @SerializedName("url")
    private String url;

    public Token() {}

    public Token(Cursor cursor) {
        this.url = null;
        this.token = cursor.getString(cursor.getColumnIndex(DomainNamespace.TOKEN));
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Uri getUrl() {
        if(url == null) return null;
        return Uri.parse(url);
    }

    public void setUrl(Uri url) {
        this.url = (url == null?null:url.toString());
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DomainNamespace.TOKEN, token);
        return values;
    }

    public static String[] getColumns() {
        return new String[]{DomainNamespace.ID, DomainNamespace.TOKEN};
    }

    public class DomainNamespace {
        public static final String TABLE_NAME = "token";
        public static final String ID = "_id";
        public static final String TOKEN = "token";
    }
}
