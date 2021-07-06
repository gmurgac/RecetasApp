package com.example.recetascocina.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.recetascocina.dto.Ingrediente;
import com.example.recetascocina.dto.Usuario;
import com.example.recetascocina.helpers.IngredientesDBOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class UsuariosDAOSqLite implements UsuariosDAO{
    private IngredientesDBOpenHelper db;

    public UsuariosDAOSqLite(Context cx){
        this.db = new IngredientesDBOpenHelper(cx,"DBRecetasApp",null,1);
    }

    @Override
    public Usuario save(Usuario usuario) {
        SQLiteDatabase writer = this.db.getWritableDatabase();

        try {

            String sql = String.format("INSERT INTO usuarios(" +
                    "nickname) VALUES ('%s')", usuario.getNickname());
            writer.execSQL(sql);
        } catch (Exception e) {

        } finally {
            writer.close();

        }


        return null;
    }

    @Override
    public boolean getUser(String nickname) {
        SQLiteDatabase reader = this.db.getReadableDatabase();
        Usuario usuario = new Usuario();
        boolean existeUser = false;
        try {
            if (reader != null) {
                Cursor c = reader.rawQuery("SELECT * FROM usuarios WHERE nickname='"+nickname+"'",null);
                if (c.moveToFirst()) {
                    existeUser = true;
                }else{
                    existeUser = false;
                }

            }
        } catch (Exception ex) {
            usuario = null;
        }finally {
            reader.close();
        }

        return existeUser;
    }

    @Override
    public Usuario getUser() {
        SQLiteDatabase reader = this.db.getReadableDatabase();
        Usuario user = new Usuario();
        try {
            if (reader != null) {
                Cursor c = reader.rawQuery("SELECT nickname FROM usuarios", null);
                if (c.moveToFirst()) {


                        user.setNickname(c.getString(0));



                }else{
                    user = null;
                }

            }
        } catch (Exception ex) {
            user = null;
        }finally {
            reader.close();
        }
        return user;
    }
}
