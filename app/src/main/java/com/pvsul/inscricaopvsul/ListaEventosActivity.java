package com.pvsul.inscricaopvsul;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.pvsul.inscricaopvsul.Models.Evento;
import com.pvsul.inscricaopvsul.Util.Util;

public class ListaEventosActivity extends Activity {

    private FirebaseListAdapter<Evento> fbAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        ImageView imvBack = (ImageView) findViewById(R.id.imvBack);
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.logout();
                finish();
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        ImageView imvDadosUsuario = (ImageView) findViewById(R.id.imvDadosUsuario);
        imvDadosUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), DadosUsuarioActivity.class);
                startActivity(i);
            }
        });

        ListView listaEventos = (ListView) findViewById(R.id.lsvEventos);
        listaEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), DadosEventoActivity.class);
                i.putExtra("Evento", fbAdapter.getRef(position).getKey());
                startActivity(i);
            }
        });

        fbAdapter = new FirebaseListAdapter<Evento>(this, Evento.class, R.layout.evento_list_item, Util.eventosFDBRef) {
            @Override
            protected void populateView(View v, Evento model, int position) {
                TextView txvNome = (TextView) v.findViewById(R.id.txvNome);
                TextView txvValor = (TextView) v.findViewById(R.id.txvValorEvento);
                txvNome.setText(fbAdapter.getRef(position).getKey());
                txvValor.setText("R$ " + String.format("%.2f", model.getValor()));
                fbAdapter.notifyDataSetChanged();
            }
        };

        listaEventos.setAdapter(fbAdapter);

    }
}
