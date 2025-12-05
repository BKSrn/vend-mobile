package com.example.vend.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String TAG = "ApiClient";
    private static Retrofit retrofit;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            // Logging Interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message ->
                    Log.d(TAG, "HTTP: " + message)
            );
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Custom Interceptor para adicionar headers
            okhttp3.Interceptor headerInterceptor = chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                Log.d(TAG, "Request URL: " + request.url());
                Log.d(TAG, "Request Method: " + request.method());

                return chain.proceed(request);
            };

            // OkHttpClient com configurações de timeout
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

            // Configurar Gson com TypeAdapter customizado para byte[]
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(byte[].class, new ByteArrayTypeAdapter())
                    .setLenient() // Permite JSON menos rigoroso
                    .create();

            // Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Log.d(TAG, "Retrofit inicializado com Base URL: " + baseUrl);
            Log.d(TAG, "ByteArrayTypeAdapter registrado para conversão Base64");
        }
        return retrofit;
    }

    /**
     * Reseta a instância do Retrofit (útil para trocar de servidor)
     */
    public static void reset() {
        retrofit = null;
        Log.d(TAG, "Retrofit resetado");
    }
}