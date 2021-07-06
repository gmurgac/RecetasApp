package com.example.recetascocina.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class IngredientesDBOpenHelper extends SQLiteOpenHelper {
    private final String sqlCreate2 = "CREATE TABLE ingredientes("+
            "idIngrediente INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            "nombre TEXT UNIQUE, cantidad FLOAT, unidadMedida TEXT)";
    private final String sqlCreate3 = "CREATE TABLE usuarios(" +
            "id INTEGER PRIMARY KEY NOT NULL," +
            "nickname TEXT UNIQUE)";

    public IngredientesDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.sqlCreate3);
        db.execSQL(this.sqlCreate2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ingredientes");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL(sqlCreate3);
        db.execSQL(sqlCreate2);
    }
}
