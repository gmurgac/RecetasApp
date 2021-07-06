package com.example.recetascocina.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.example.recetascocina.dto.Ingrediente;
import com.example.recetascocina.helpers.IngredientesDBOpenHelper;

import java.util.ArrayList;
import java.util.List;
//Clase DAO coneccion con Sqlite Base de datos local de android
public class IngredientesDAOSqLite implements IngredientesDAO {

    private IngredientesDBOpenHelper db;
    //Constructor
    public IngredientesDAOSqLite(Context cx) {
        this.db = new IngredientesDBOpenHelper(cx,
                "DBRecetasApp",
                null,
                1);
    }
    //Guardar ingrediente en BBDD
    @Override
    public Ingrediente save(Ingrediente ingrediente) {
        SQLiteDatabase writer = this.db.getWritableDatabase();




        try {

            String sql = String.format("INSERT INTO ingredientes(" +
                    "nombre,cantidad,unidadMedida) VALUES ('%s','%f','%s')", ingrediente.getNombre(), ingrediente.getCantidad(), ingrediente.getUnidadMedida());
            writer.execSQL(sql);
        } catch (Exception e) {

        } finally {
            writer.close();

        }


        return null;
    }
    //Metodo obtener todos los ingredientes de BBDD
    @Override
    public List<Ingrediente> getAll() {
        SQLiteDatabase reader = this.db.getReadableDatabase();
        List<Ingrediente> ingredientes = new ArrayList<>();
        try {
            if (reader != null) {
                Cursor c = reader.rawQuery("SELECT nombre,cantidad,unidadMedida FROM ingredientes ORDER BY nombre", null);
                if (c.moveToFirst()) {
                    do {
                        Ingrediente i = new Ingrediente();
                        i.setNombre(c.getString(0));
                        i.setCantidad(c.getFloat(1));
                        i.setUnidadMedida(c.getString(2));
                        ingredientes.add(i);
                    } while (c.moveToNext());
                }
                reader.close();
            }
        } catch (Exception ex) {
            ingredientes = null;
        }
        return ingredientes;
    }

    //Metodo eliminar ingrediente de BBDD

    @Override
    public Ingrediente erase(Ingrediente ingrediente) {
        SQLiteDatabase writer = this.db.getWritableDatabase();
        String sql = String.format("DELETE FROM ingredientes " +
                "WHERE nombre='" + ingrediente.getNombre() + "'");
        writer.execSQL(sql);
        writer.close();
        return null;
    }
    //Metodo para modificar Ingrediente en BBDD
    @Override
    public Ingrediente modify(Ingrediente ingrediente) {
        SQLiteDatabase writer = this.db.getWritableDatabase();
        SQLiteDatabase reader = this.db.getReadableDatabase();
        List<Ingrediente> ingredientes = new ArrayList<>();
        try {

            Cursor c = reader.rawQuery("SELECT * FROM ingredientes WHERE nombre='" + ingrediente.getNombre() + "'", null);
            if (c.moveToFirst()) {
                ContentValues cv = new ContentValues();
                cv.put("cantidad", ingrediente.getCantidad());
                String[] nombreIgr = new String[]{"" + ingrediente.getNombre()};
                writer.update("ingredientes", cv, "nombre=?", nombreIgr);

            }
        } catch (Exception e) {

        } finally {
            writer.close();
            reader.close();
        }
        return null;

    }

    //Metodo para encontrar ingrediente a traves del nombre
    @Override
    public boolean findIngrediente(String nombre) {

        SQLiteDatabase reader = this.db.getReadableDatabase();

        try {

            Cursor c = reader.rawQuery("SELECT * FROM ingredientes WHERE nombre='" + nombre + "'", null);
            if (c.moveToFirst()) {
                return true;
            } else {
                return false;
            }

        }catch (Exception e){
            return false;
        }finally {
            reader.close();
        }
    }
}