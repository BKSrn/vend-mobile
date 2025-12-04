// java
package com.example.vend.network;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;
import com.example.vend.model.Carro;

public interface CarroApiService {
    @GET("carros")
    Call<List<Carro>> listarCarros();
}
