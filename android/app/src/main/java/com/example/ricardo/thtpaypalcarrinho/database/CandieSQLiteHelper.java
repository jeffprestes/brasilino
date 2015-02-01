package com.example.ricardo.thtpaypalcarrinho.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ricardo.thtpaypalcarrinho.domain.Token;

public class CandieSQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "candies.db";
    private static final int DB_VERSION = 1;

    private static final String DATABASE_CREATION = "create table " +
            Token.DomainNamespace.TABLE_NAME + "(" +
            Token.DomainNamespace.ID + " integer primary key autoincrement," +
            Token.DomainNamespace.TOKEN + " text not null" +
            ");";

    public CandieSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Token.DomainNamespace.TABLE_NAME);
        db.execSQL(DATABASE_CREATION);
    }
}

