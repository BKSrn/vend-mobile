package com.example.vend.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vend.R;
import com.example.vend.model.Carro;
import com.example.vend.network.ApiClient;
import com.example.vend.network.CarroApiService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogoActivity extends AppCompatActivity {

    private static final String TAG = "CatalogoActivity";
    private static final String BASE_URL = "https://vend-api-fff0gyacc8bybxhy.brazilsouth-01.azurewebsites.net/";

    private RecyclerView rvCatalogo;
    private ProgressBar progressBar;
    private TextView tvMensagemVazia;
    private CarroAdapter adapter;
    private List<Carro> listaCarros = new ArrayList<>();

    private CarroApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        initViews();
        setupRecyclerView();
        setupApi();
        carregarCarros();
    }

    private void initViews() {
        rvCatalogo = findViewById(R.id.rv_catalogo);
        progressBar = findViewById(R.id.progressBar);
        tvMensagemVazia = findViewById(R.id.tvMensagemVazia);
    }

    private void setupRecyclerView() {
        adapter = new CarroAdapter(listaCarros);
        rvCatalogo.setLayoutManager(new GridLayoutManager(this, 2));
        rvCatalogo.setAdapter(adapter);
    }

    private void setupApi() {
        apiService = ApiClient.getClient(BASE_URL).create(CarroApiService.class);
    }

    private void carregarCarros() {
        mostrarLoading(true);

        Call<List<Carro>> call = apiService.listarCarros();

        call.enqueue(new Callback<List<Carro>>() {
            @Override
            public void onResponse(@NonNull Call<List<Carro>> call, @NonNull Response<List<Carro>> response) {
                mostrarLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    listaCarros.clear();
                    listaCarros.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (listaCarros.isEmpty()) {
                        mostrarMensagemVazia(true);
                    } else {
                        mostrarMensagemVazia(false);
                        Log.d(TAG, "Carros carregados: " + listaCarros.size());
                    }
                } else {
                    String erro = "Erro ao carregar carros. Código: " + response.code();
                    Log.e(TAG, erro);
                    Toast.makeText(CatalogoActivity.this, erro, Toast.LENGTH_SHORT).show();
                    mostrarMensagemVazia(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Carro>> call, @NonNull Throwable t) {
                mostrarLoading(false);
                mostrarMensagemVazia(true);

                String erro = "Falha na conexão: " + t.getMessage();
                Log.e(TAG, erro, t);
                Toast.makeText(CatalogoActivity.this,
                        "Erro ao conectar com o servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarLoading(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        rvCatalogo.setVisibility(mostrar ? View.GONE : View.VISIBLE);
    }

    private void mostrarMensagemVazia(boolean mostrar) {
        tvMensagemVazia.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        rvCatalogo.setVisibility(mostrar ? View.GONE : View.VISIBLE);
    }

    // ==================== ADAPTER ====================

    private class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.CarroViewHolder> {

        private final List<Carro> carros;
        private final NumberFormat formatoMoeda;

        public CarroAdapter(List<Carro> carros) {
            this.carros = carros;
            this.formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        }

        @NonNull
        @Override
        public CarroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_carro, parent, false);
            return new CarroViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CarroViewHolder holder, int position) {
            Carro carro = carros.get(position);
            holder.bind(carro);
        }

        @Override
        public int getItemCount() {
            return carros.size();
        }

        class CarroViewHolder extends RecyclerView.ViewHolder {

            private final ImageView ivCarro;
            private final TextView tvModelo;
            private final TextView tvMarca;
            private final TextView tvAno;
            private final TextView tvPreco;
            private final TextView tvCarroceria;

            public CarroViewHolder(@NonNull View itemView) {
                super(itemView);
                ivCarro = itemView.findViewById(R.id.ivCarro);
                tvModelo = itemView.findViewById(R.id.tvModelo);
                tvMarca = itemView.findViewById(R.id.tvMarca);
                tvAno = itemView.findViewById(R.id.tvAno);
                tvPreco = itemView.findViewById(R.id.tvPreco);
                tvCarroceria = itemView.findViewById(R.id.tvCarroceria);
            }

            public void bind(Carro carro) {
                // Modelo
                tvModelo.setText(carro.getModelo() != null ?
                        capitalize(carro.getModelo()) : "N/A");

                // Marca
                tvMarca.setText(carro.getMarca() != null ?
                        capitalize(carro.getMarca()) : "N/A");

                // Ano
                tvAno.setText(carro.getAno() != null ?
                        String.valueOf(carro.getAno()) : "N/A");

                // Preço
                if (carro.getPreco() != null) {
                    tvPreco.setText(formatoMoeda.format(carro.getPreco()));
                } else {
                    tvPreco.setText("Preço não disponível");
                }

                // Carroceria
                tvCarroceria.setText(carro.getCarroceria() != null ?
                        carro.getCarroceria() : "N/A");

                // Imagem
                if (carro.getImagem() != null && carro.getImagem().length > 0) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(
                                carro.getImagem(), 0, carro.getImagem().length);
                        ivCarro.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao decodificar imagem", e);
                        ivCarro.setImageResource(R.drawable.ic_home); // placeholder
                    }
                } else {
                    ivCarro.setImageResource(R.drawable.ic_home); // placeholder
                }

                // Click listener
                itemView.setOnClickListener(v -> {
                    Toast.makeText(itemView.getContext(),
                            carro.getModelo() + " - " + formatoMoeda.format(carro.getPreco()),
                            Toast.LENGTH_SHORT).show();
                });
            }

            private String capitalize(String texto) {
                if (texto == null || texto.isEmpty()) {
                    return texto;
                }
                return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
            }
        }
    }
}