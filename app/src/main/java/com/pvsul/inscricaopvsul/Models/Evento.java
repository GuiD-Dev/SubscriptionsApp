package com.pvsul.inscricaopvsul.Models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.pvsul.inscricaopvsul.Util.Util;

@IgnoreExtraProperties
public class Evento {
    private String key;
    private String descricao;
    private String inicio;
    private String fim;
    private float valor;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public boolean isInscrito(String keyUser) {
        final boolean[] retorno = {false};

        Util.eventosFDBRef.child(key).child("Inscricoes").orderByValue().equalTo(keyUser)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retorno[0] = dataSnapshot.exists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        return retorno[0];
    }
}
