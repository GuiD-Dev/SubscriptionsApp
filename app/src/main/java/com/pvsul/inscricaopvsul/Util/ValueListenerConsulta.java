package com.pvsul.inscricaopvsul.Util;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pvsul.inscricaopvsul.Models.Usuario;

import java.util.ArrayList;

public class ValueListenerConsulta<T> implements ValueEventListener {

    private Class<T> classe;
    private ArrayList<T> resultado = new ArrayList();
    public ArrayList<T> getResultado() {
        return resultado;
    }

    public ValueListenerConsulta(Class<T> classe) {
        this.classe = classe;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            for (DataSnapshot value : dataSnapshot.getChildren()) {
                if (classe == Usuario.class) {
                    Usuario usuario = value.getValue(Usuario.class);
                    resultado.add((T) usuario);
                }
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {}
}
