package com.example.vend.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vend.model.Carro;

import java.util.List;

@Dao
public interface CarroDao {

    @Query("SELECT * FROM carros WHERE id = :carroId")
    Carro getById(Long carroId);

    @Insert
    long insert(Carro carro);

    @Update
    void update(Carro carro);

    @Delete
    void delete(Carro carro);

    @Query("SELECT * FROM carros")
    List<Carro> getAll();
}
