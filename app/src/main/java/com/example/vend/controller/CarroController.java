package com.example.vend.controller;

import android.content.Context;

import com.example.vend.database.AppDatabase;
import com.example.vend.database.dao.CarroDao;
import com.example.vend.model.Carro;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CarroController {

    private final CarroDao carroDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    public CarroController(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.carroDao = db.carroDao;
    }

    public void insert(Carro carro, Callback<Carro> callback) {
        executor.execute(() -> {
            try {
                long id = carroDao.insert(carro); // assume insert returns long
                carro.setId(id);
                callback.onSuccess(carro);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void update(Carro carro, Callback<Void> callback) {
        executor.execute(() -> {
            try {
                carroDao.update(carro);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void delete(Carro carro, Callback<Void> callback) {
        executor.execute(() -> {
            try {
                carroDao.delete(carro);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getById(long id, Callback<Carro> callback) {
        executor.execute(() -> {
            try {
                Carro carro = carroDao.getById(id);
                callback.onSuccess(carro);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getAll(Callback<List<Carro>> callback) {
        executor.execute(() -> {
            try {
                List<Carro> list = carroDao.getAll();
                callback.onSuccess(list);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }
}
