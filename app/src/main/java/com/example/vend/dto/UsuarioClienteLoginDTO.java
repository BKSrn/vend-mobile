package com.example.vend.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO para login de usuário cliente
 */
public class UsuarioClienteLoginDTO {

    @SerializedName("email")
    private String email;

    @SerializedName("senha")
    private String senha;

    public UsuarioClienteLoginDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
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