// app/src/main/java/com/example/vend/model/UsuarioDao.java
package com.example.vend.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.vend.model.Carro;
import com.example.vend.model.UsuarioCliente;

import java.util.List;

@Dao
public interface UsuarioDao {
    @Query("SELECT * FROM usuarios_cliente WHERE id = :userId")
    UsuarioCliente getById(int userId);

    @Query("SELECT * FROM usuarios_cliente WHERE email = :email")
    UsuarioCliente getByEmail(String email);

    @Query("SELECT * FROM usuarios_cliente WHERE email = :email AND senha = :senha")
    UsuarioCliente getByEmailAndPassword(String email, String senha);

    @Insert
    long insertUsuario(UsuarioCliente usuario);

    @Insert
    long insertCarro(Carro carro);

    @Update
    void update(UsuarioCliente usuario);

    @Delete
    void delete(UsuarioCliente usuario);

    @Query("SELECT * FROM usuarios_cliente")
    List<UsuarioCliente> getAllUsuarios();
}
