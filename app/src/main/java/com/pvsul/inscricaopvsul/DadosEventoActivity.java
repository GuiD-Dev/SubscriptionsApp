package com.pvsul.inscricaopvsul;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pvsul.inscricaopvsul.Models.Evento;
import com.pvsul.inscricaopvsul.Util.Util;

public class DadosEventoActivity extends Activity {

    TextView txvNomeEvento;
    TextView txvDescricao;
    TextView txvDataInicio;
    TextView txvDataFim;
    TextView txvValor;
    String keyEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_evento);

        txvNomeEvento = (TextView) findViewById(R.id.txvNomeEvento);
        txvDescricao = (TextView) findViewById(R.id.txvDescricao);
        txvDataInicio = (TextView) findViewById(R.id.txvDataInicio);
        txvDataFim = (TextView) findViewById(R.id.txvDataFim);
        txvValor = (TextView) findViewById(R.id.txvValor);

        keyEvento = getIntent().getStringExtra("Evento");

        Util.eventosFDBRef.child(keyEvento).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Evento evento = dataSnapshot.getValue(Evento.class);
                    txvNomeEvento.setText(keyEvento);
                    txvDescricao.setText(evento.getDescricao());
                    txvDataInicio.setText("Início: " + evento.getInicio());
                    txvDataFim.setText("Término: " + evento.getFim());
                    txvValor.setText("Valor: R$" + String.format("%.2f", evento.getValor()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        Button btnInscricao = (Button) findViewById(R.id.btnInscricao);
        btnInscricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String keyUser = Util.getPrefUserKey();
                Util.eventosFDBRef.child(keyEvento).child("Inscricoes").orderByValue().equalTo(keyUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getBaseContext(), "Sua inscrição já foi efetuada para este evento.", Toast.LENGTH_LONG).show();
                        } else {
                            Util.eventosFDBRef.child(keyEvento).child("Inscricoes").push().setValue(keyUser);
                            Toast.makeText(getBaseContext(), "Inscrição efetuada com sucesso.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        });

    }
}
