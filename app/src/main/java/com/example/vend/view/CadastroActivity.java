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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vend.R;
import com.example.vend.dto.UsuarioClienteCadastrarDTO;
import com.example.vend.network.ApiClient;
import com.example.vend.network.UsuarioClienteApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "CadastroActivity";
    private static final String BASE_URL = "https://vend-api-fff0gyacc8bybxhy.brazilsouth-01.azurewebsites.net/";

    // Views
    private EditText etEmail;
    private EditText etSenha;
    private EditText etConfirmarSenha;
    private Button btnCadastrar;
    private Button btnVoltarLogin;
    private ProgressBar progressBar;

    // API Service
    private UsuarioClienteApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        initViews();
        setupApi();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmailCadastro);
        etSenha = findViewById(R.id.etSenhaCadastro);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);
        progressBar = findViewById(R.id.progressBarCadastro);
    }

    private void setupApi() {
        apiService = ApiClient.getClient(BASE_URL).create(UsuarioClienteApiService.class);
    }

    private void setupListeners() {
        btnCadastrar.setOnClickListener(v -> cadastrar());

        btnVoltarLogin.setOnClickListener(v -> {
            finish(); // Volta para a tela de login
        });
    }

    private void cadastrar() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        // Validações
        if (!validarCampos(email, senha, confirmarSenha)) {
            return;
        }

        // Desabilitar botão durante requisição
        setLoading(true);

        // Criar DTO
        UsuarioClienteCadastrarDTO dto = new UsuarioClienteCadastrarDTO(email, senha);

        // Fazer requisição
        Call<ResponseBody> call = apiService.cadastrarUsuario(dto);

        Log.d(TAG, "Iniciando cadastro para: " + email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                setLoading(false);

                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful()) {
                    try {
                        // Ler a resposta como String
                        String mensagem = "";
                        if (response.body() != null) {
                            mensagem = response.body().string();
                            Log.d(TAG, "Mensagem do servidor: " + mensagem);
                        }

                        // Cadastro realizado com sucesso
                        Toast.makeText(CadastroActivity.this,
                                "Cadastro realizado com sucesso!",
                                Toast.LENGTH_SHORT).show();

                        // Voltar para login com o email preenchido
                        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                        intent.putExtra("email_cadastrado", email);
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao ler resposta: " + e.getMessage(), e);
                        Toast.makeText(CadastroActivity.this,
                                "Cadastro realizado, mas houve erro ao processar resposta",
                                Toast.LENGTH_SHORT).show();

                        // Mesmo com erro ao ler resposta, volta para login
                        finish();
                    }

                } else {
                    // Erro no cadastro
                    String erro = "Erro ao cadastrar: ";

                    try {
                        if (response.errorBody() != null) {
                            erro += response.errorBody().string();
                        } else {
                            erro += "Código " + response.code();
                        }
                    } catch (Exception e) {
                        erro += "Código " + response.code();
                    }

                    Log.e(TAG, erro);

                    // Verificar se é email duplicado
                    if (response.code() == 400 && erro.contains("já foi cadastrado")) {
                        Toast.makeText(CadastroActivity.this,
                                "Este email já está cadastrado!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CadastroActivity.this, erro, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                setLoading(false);

                String erro = "Falha na conexão: " + t.getMessage();
                Log.e(TAG, erro, t);

                Toast.makeText(CadastroActivity.this,
                        "Erro ao conectar com o servidor. Verifique sua conexão.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validarCampos(String email, String senha, String confirmarSenha) {
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

        // Validar tamanho mínimo da senha
        if (senha.length() < 6) {
            etSenha.setError("Senha deve ter no mínimo 6 caracteres");
            etSenha.requestFocus();
            return false;
        }

        // Validar confirmação de senha
        if (TextUtils.isEmpty(confirmarSenha)) {
            etConfirmarSenha.setError("Confirme sua senha");
            etConfirmarSenha.requestFocus();
            return false;
        }

        // Validar se as senhas coincidem
        if (!senha.equals(confirmarSenha)) {
            etConfirmarSenha.setError("As senhas não coincidem");
            etConfirmarSenha.requestFocus();
            return false;
        }

        return true;
    }

    private void setLoading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            btnCadastrar.setEnabled(false);
            btnVoltarLogin.setEnabled(false);
            etEmail.setEnabled(false);
            etSenha.setEnabled(false);
            etConfirmarSenha.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnCadastrar.setEnabled(true);
            btnVoltarLogin.setEnabled(true);
            etEmail.setEnabled(true);
            etSenha.setEnabled(true);
            etConfirmarSenha.setEnabled(true);
        }
    }
}