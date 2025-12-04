package com.example.vend.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "usuarios_cliente")
public class UsuarioCliente {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String email;
    private String senha;

    //private List<Carro> carrinho;

    public UsuarioCliente(String email, String senha) {
        this.email = email;
        this.senha = senha;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
