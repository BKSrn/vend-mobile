package com.example.vend.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vend.database.dao.CarroDao;
import com.example.vend.database.dao.UsuarioDao;
import com.example.vend.model.Carro;
import com.example.vend.model.UsuarioCliente;

// database/AppDatabase.java
@Database(entities = {UsuarioCliente.class, Carro.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public UsuarioDao usuarioDao;
    public CarroDao carroDao;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "vend_db"
            ).build();
        }
        return instance;
    }
}