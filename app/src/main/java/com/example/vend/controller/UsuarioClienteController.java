// controllers/UserController.java
package com.example.vend.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.example.vend.database.AppDatabase;
import com.example.vend.database.dao.UsuarioDao;
import com.example.vend.model.UsuarioCliente;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsuarioClienteController {

    private final UsuarioDao usuarioDao;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public UsuarioClienteController(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.usuarioDao = db.usuarioDao;
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    // CADASTRO
    public void cadastraUsuario(UsuarioCliente usuarioCliente, OnUserCreatedListener listener) {
        executorService.execute(() -> {
            try {
                // Verificar se email já existe
                UsuarioCliente existeUsuario = usuarioDao.getByEmail(usuarioCliente.getEmail());
                if (existeUsuario != null) {
                    mainHandler.post(() -> listener.onError("Email já cadastrado"));
                    return;
                }

                usuarioDao.insertUsuario(usuarioCliente);
                mainHandler.post(listener::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> listener.onError(e.getMessage()));
            }
        });
    }

    // LOGIN
    public void login(String email, String password, OnLoginListener listener) {
        executorService.execute(() -> {
            try {
                UsuarioCliente usuarioCliente = usuarioDao.getByEmailAndPassword(email, password);

                if (usuarioCliente != null) {
                    mainHandler.post(() -> listener.onSuccess(usuarioCliente));
                } else {
                    mainHandler.post(() -> listener.onError("Email ou senha incorretos"));
                }
            } catch (Exception e) {
                mainHandler.post(() -> listener.onError(e.getMessage()));
            }
        });
    }

    // ATUALIZAR
    public void atualizaUsuario(UsuarioCliente usuarioCliente, OnUserUpdatedListener listener) {
        executorService.execute(() -> {
            try {
                usuarioDao.update(usuarioCliente);
                mainHandler.post(listener::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> listener.onError(e.getMessage()));
            }
        });
    }

    // DELETE
    public void deletaUsuario(UsuarioCliente usuarioCliente, OnUserDeletedListener listener) {
        executorService.execute(() -> {
            try {
                usuarioDao.delete(usuarioCliente);
                mainHandler.post(listener::onSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> listener.onError(e.getMessage()));
            }
        });
    }

    // Interfaces de callback
    public interface OnUserCreatedListener {
        void onSuccess();
        void onError(String message);
    }

    public interface OnLoginListener {
        void onSuccess(UsuarioCliente usuarioCliente);
        void onError(String message);
    }

    public interface OnUserUpdatedListener {
        void onSuccess();
        void onError(String message);
    }

    public interface OnUserDeletedListener {
        void onSuccess();
        void onError(String message);
    }
}