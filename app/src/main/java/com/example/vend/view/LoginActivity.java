package com.example.vend.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vend.R;
import com.example.vend.dto.UsuarioClienteLoginDTO;
import com.example.vend.dto.UsuarioClienteResponseDTO;
import com.example.vend.network.ApiClient;
import com.example.vend.network.UsuarioClienteApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String BASE_URL = "https://vend-api-fff0gyacc8bybxhy.brazilsouth-01.azurewebsites.net/";

    // Views
    private EditText etEmail;
    private EditText etSenha;
    private Button btnLogin;
    private TextView tvCadastrar;
    private ProgressBar progressBar;

    // API Service
    private UsuarioClienteApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupApi();
        setupListeners();
        verificarEmailCadastrado();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnLogin = findViewById(R.id.btnLogin);
        tvCadastrar = findViewById(R.id.tvCadastrar);
        progressBar = findViewById(R.id.progressBarLogin);
    }

    private void setupApi() {
        apiService = ApiClient.getClient(BASE_URL).create(UsuarioClienteApiService.class);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> login());

        tvCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }

    private void verificarEmailCadastrado() {
        // Se veio da tela de cadastro, preenche o email
        String emailCadastrado = getIntent().getStringExtra("email_cadastrado");
        if (emailCadastrado != null && !emailCadastrado.isEmpty()) {
            etEmail.setText(emailCadastrado);
            etSenha.requestFocus();
            Toast.makeText(this, "Conta criada! Faça login", Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        // Validações
        if (!validarCampos(email, senha)) {
            return;
        }

        // Desabilitar botão durante requisição
        setLoading(true);

        // Criar DTO
        UsuarioClienteLoginDTO dto = new UsuarioClienteLoginDTO(email, senha);

        // Fazer requisição
        Call<UsuarioClienteResponseDTO> call = apiService.login(dto);

        call.enqueue(new Callback<UsuarioClienteResponseDTO>() {
            @Override
            public void onResponse(@NonNull Call<UsuarioClienteResponseDTO> call,
                                   @NonNull Response<UsuarioClienteResponseDTO> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    // Login bem-sucedido
                    UsuarioClienteResponseDTO usuario = response.body();
                    Log.d(TAG, "Login bem-sucedido: " + usuario.getEmail());

                    Toast.makeText(LoginActivity.this,
                            "Bem-vindo, " + usuario.getEmail(),
                            Toast.LENGTH_SHORT).show();

                    // Ir para o catálogo
                    Intent intent = new Intent(LoginActivity.this, CatalogoActivity.class);
                    intent.putExtra("user_email", usuario.getEmail());
                    startActivity(intent);
                    finish();

                } else {
                    // Credenciais inválidas
                    String erro = "Email ou senha incorretos";

                    if (response.code() == 400) {
                        erro = "Dados inválidos. Verifique email e senha.";
                    } else if (response.code() == 404) {
                        erro = "Usuário não encontrado.";
                    }

                    Log.e(TAG, "Erro no login: " + response.code());
                    Toast.makeText(LoginActivity.this, erro, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UsuarioClienteResponseDTO> call, @NonNull Throwable t) {
                setLoading(false);

                String erro = "Falha na conexão: " + t.getMessage();
                Log.e(TAG, erro, t);
                Toast.makeText(LoginActivity.this,
                        "Erro ao conectar com o servidor. Verifique sua conexão.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validarCampos(String email, String senha) {
        // Validar email vazio
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email é obrigatório");
            etEmail.requestFocus();
            return false;
        }

        // Validar formato do email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            etEmail.requestFocus();
            return false;
        }

        // Validar senha vazia
        if (TextUtils.isEmpty(senha)) {
            etSenha.setError("Senha é obrigatória");
            etSenha.requestFocus();
            return false;
        }

        return true;
    }

    private void setLoading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            tvCadastrar.setEnabled(false);
            etEmail.setEnabled(false);
            etSenha.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            tvCadastrar.setEnabled(true);
            etEmail.setEnabled(true);
            etSenha.setEnabled(true);
        }
    }
}