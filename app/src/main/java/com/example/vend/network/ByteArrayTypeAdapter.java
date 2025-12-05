package com.example.vend.network;

import android.util.Base64;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * TypeAdapter personalizado para converter strings Base64 em byte[]
 * Resolve o problema: "Expected BEGIN_ARRAY but was STRING"
 */
public class ByteArrayTypeAdapter extends TypeAdapter<byte[]> {

    @Override
    public void write(JsonWriter out, byte[] value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            // Converte byte[] para Base64 string
            String base64 = Base64.encodeToString(value, Base64.NO_WRAP);
            out.value(base64);
        }
    }

    @Override
    public byte[] read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        // Lê como string
        String base64String = in.nextString();

        if (base64String == null || base64String.isEmpty()) {
            return new byte[0];
        }

        try {
            // Converte Base64 string para byte[]
            return Base64.decode(base64String, Base64.DEFAULT);
        } catch (IllegalArgumentException e) {
            // Se não for Base64 válido, retorna array vazio
            return new byte[0];
        }
    }
}