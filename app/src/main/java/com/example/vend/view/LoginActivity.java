package com.example.vend.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vend.R;
import com.example.vend.controller.UsuarioClienteController;
import com.example.vend.model.UsuarioCliente;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnCadastro;
    private UsuarioClienteController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar o controller
        userController = new UsuarioClienteController(this);

        // Vincular os componentes do XML
        initViews();

        // Configurar listeners
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnCadastro = findViewById(R.id.btnCadastrar);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> login());

        btnCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, com.example.vend.view.CadastroActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validações
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chamar o controller
        userController.login(email, password, new UsuarioClienteController.OnLoginListener() {
            @Override
            public void onSuccess(UsuarioCliente user) {
                Toast.makeText(LoginActivity.this,
                        "Bem-vindo, " + user.getEmail(),
                        Toast.LENGTH_SHORT).show();

                // Ir para a MainActivity
                Intent intent = new Intent(LoginActivity.this, CatalogoActivity.class);
                intent.putExtra("user_id", user.getId());
                intent.putExtra("user_email", user.getEmail());
                startActivity(intent);
                finish(); // Fecha a LoginActivity
            }

            @Override
            public void onError(String message) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}