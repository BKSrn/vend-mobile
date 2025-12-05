package com.example.vend.network;

import com.example.vend.dto.UsuarioClienteCadastrarDTO;
import com.example.vend.dto.UsuarioClienteLoginDTO;
import com.example.vend.dto.UsuarioClienteResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Interface de serviço da API REST VEND para Usuários Cliente
 * Base URL: https://vend-api-fff0gyacc8bybxhy.brazilsouth-01.azurewebsites.net/
 */
public interface UsuarioClienteApiService {

    /**
     * Cadastra um novo usuário cliente
     * POST /usuariosCliente
     *
     * @param dto Dados de cadastro (email e senha)
     * @return Mensagem de sucesso
     */
    @POST("usuariosCliente/cadastro")
    Call<String> cadastrarUsuario(@Body UsuarioClienteCadastrarDTO dto);

    /**
     * Realiza login do usuário cliente
     * POST /usuariosCliente (usando POST ao invés de GET)
     *
     * Nota: O backend usa @GetMapping, mas Retrofit não permite @Body em GET.
     * A solução é usar POST aqui e ajustar o backend futuramente.
     *
     * @param dto Dados de login (email e senha)
     * @return Dados do usuário autenticado
     */
    @POST("usuariosCliente/login")
    Call<UsuarioClienteResponseDTO> login(@Body UsuarioClienteLoginDTO dto);
}