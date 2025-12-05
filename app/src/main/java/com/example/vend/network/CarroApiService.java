package com.example.vend.network;

import com.example.vend.model.Carro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Interface de serviço da API REST VEND
 * Base URL: https://vend-api-fff0gyacc8bybxhy.brazilsouth-01.azurewebsites.net/
 */
public interface CarroApiService {

    /**
     * Lista todos os carros disponíveis
     * GET /carros
     */
    @GET("carros")
    Call<List<Carro>> listarCarros();

    /**
     * Busca um carro específico por ID
     * GET /carros/{id}
     */
    @GET("carros/{id}")
    Call<Carro> buscarCarroPorId(@Path("id") Long id);

    /**
     * Busca carros por marca
     * GET /carros/marca/{marca}
     */
    @GET("carros/marca/{marca}")
    Call<List<Carro>> buscarCarrosPorMarca(@Path("marca") String marca);

    /**
     * Busca carros por marca e modelo
     * GET /carros/marca/{marca}/modelo/{modelo}
     */
    @GET("carros/marca/{marca}/modelo/{modelo}")
    Call<List<Carro>> buscarCarrosPorMarcaModelo(
            @Path("marca") String marca,
            @Path("modelo") String modelo
    );

    /**
     * Cadastra um novo carro
     * POST /carros
     */
    @POST("carros")
    Call<Void> cadastrarCarro(@Body Carro carro);

    /**
     * Deleta um carro
     * DELETE /carros/{id}
     */
    @DELETE("carros/{id}")
    Call<String> deletarCarro(@Path("id") Long id);
}