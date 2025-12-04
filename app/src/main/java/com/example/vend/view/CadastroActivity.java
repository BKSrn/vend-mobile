// views/CadastroActivity.java
package com.example.vend.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vend.R;
import com.example.vend.controller.UsuarioClienteController;
import com.example.vend.model.UsuarioCliente;

public class CadastroActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSalvar;
    private UsuarioClienteController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        userController = new UsuarioClienteController(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmailCadastro);
        etPassword = findViewById(R.id.etSenhaCadastro);
        btnSalvar = findViewById(R.id.btnCadastrar);
    }

    private void setupListeners() {
        btnSalvar.setOnClickListener(v -> cadastrar());
    }

    private void cadastrar() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar objeto UsuarioCliente
        UsuarioCliente user = new UsuarioCliente(email, password);

        // Salvar no banco via controller
        userController.cadastraUsuario(user, new UsuarioClienteController.OnUserCreatedListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(CadastroActivity.this,
                        "Cadastro realizado com sucesso!",
                        Toast.LENGTH_SHORT).show();
                finish(); // Volta para a tela anterior
            }

            @Override
            public void onError(String message) {
                Toast.makeText(CadastroActivity.this,
                        "Erro: " + message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}