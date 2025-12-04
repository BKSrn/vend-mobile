package com.example.vend.view;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vend.R;
import com.example.vend.controller.CarroController;
import com.example.vend.model.Carro;
import com.example.vend.network.ApiClient;
import com.example.vend.network.CarroApiService;

import java.util.ArrayList;
import java.util.List;

public class CatalogoActivity extends AppCompatActivity {

    private RecyclerView rv;
    private CarroController carroController;
    private final List<Carro> carros = new ArrayList<>();
    private CarroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        carroController = new CarroController(this);

        // Pegar dados do Intent do login
        String userName = getIntent().getStringExtra("user_name");
        if (userName != null) {
            Toast.makeText(this, "Bem-vindo, " + userName, Toast.LENGTH_SHORT).show();
        }

        initViews();
        loadCarros();
    }

    private void initViews() {
        rv = findViewById(R.id.rv_catalogo);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarroAdapter(carros);
        rv.setAdapter(adapter);
    }

    private void loadCarros() {
        String baseUrl = "https://vend-api-fff0gyacc8bybxhy.brazilsouth-01.azurewebsites.net/";
        CarroApiService api = ApiClient.getClient(baseUrl).create(CarroApiService.class);
        api.listarCarros().enqueue(new retrofit2.Callback<java.util.List<com.example.vend.model.Carro>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.List<com.example.vend.model.Carro>> call,
                                   retrofit2.Response<java.util.List<com.example.vend.model.Carro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carros.clear();
                    carros.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    String err = "Erro código: " + response.code();
                    try {
                        if (response.errorBody() != null) err += " body: " + response.errorBody().string();
                    } catch (Exception e) {
                        err += " (erro ao ler errorBody)";
                    }
                    android.util.Log.e("CatalogoActivity", err);
                    Toast.makeText(CatalogoActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.List<com.example.vend.model.Carro>> call, Throwable t) {
                android.util.Log.e("CatalogoActivity", "onFailure", t);
                Toast.makeText(CatalogoActivity.this, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.VH> {
        private final List<Carro> items;

        CarroAdapter(List<Carro> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            android.view.View v = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            Carro c = items.get(position);
            // Exibir uma representação do carro; ajuste conforme campos reais (ex: getNome()/getModelo())
            holder.text.setText(c != null ? c.toString() : "N/A");
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            final TextView text;
            VH(@NonNull android.view.View itemView) {
                super(itemView);
                text = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
