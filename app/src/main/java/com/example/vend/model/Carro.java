package com.example.vend.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "carros",
        foreignKeys = @ForeignKey(
                entity = UsuarioCliente.class,
                parentColumns = "id",
                childColumns = "usuario_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Carro {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private Long id;

    @SerializedName("modelo")
    private String modelo;

    @SerializedName("preco")
    private Double preco;

    @SerializedName("marca")
    private String marca;

    @SerializedName("ano")
    private Integer ano;

    @SerializedName("carroceria")
    private String carroceria;

    @SerializedName("imagem")
    private byte[] imagem;

    @ColumnInfo(name = "usuario_id")
    private int usuarioId;

    public Carro() {
    }

    public Carro(String carroceria, byte[] imagem, String modelo, Integer ano, Double preco, String marca) {
        this.carroceria = carroceria;
        this.imagem = imagem;
        this.modelo = modelo;
        this.ano = ano;
        this.preco = preco;
        this.marca = marca;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getCarroceria() {
        return carroceria;
    }

    public void setCarroceria(String carroceria) {
        this.carroceria = carroceria;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%d) - R$ %.2f",
                marca != null ? marca.toUpperCase() : "N/A",
                modelo != null ? modelo.toUpperCase() : "N/A",
                ano != null ? ano : 0,
                preco != null ? preco : 0.0
        );
    }
}