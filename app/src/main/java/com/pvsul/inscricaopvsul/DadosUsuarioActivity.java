package com.pvsul.inscricaopvsul;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pvsul.inscricaopvsul.Models.Usuario;
import com.pvsul.inscricaopvsul.Util.Util;
import com.pvsul.inscricaopvsul.Util.ValueListenerConsulta;

import java.util.ArrayList;
import java.util.Calendar;

public class DadosUsuarioActivity extends Activity implements View.OnClickListener {

    EditText edtNomeCompleto, edtLogin, edtSenha, edtDataNascimento, edtCPF, edtTelefone;
    int dia, mes, ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        TextView txvTitulo = (TextView) findViewById(R.id.txvCadastro);
        txvTitulo.setText("MEU CADASTRO");

        Calendar cal = Calendar.getInstance();
        dia = cal.get(Calendar.DAY_OF_MONTH);
        mes = cal.get(Calendar.MONTH);
        ano = cal.get(Calendar.YEAR);

        edtNomeCompleto = (EditText) findViewById(R.id.edtNomeCompleto);
        edtNomeCompleto.setText(Util.getPrefNomeCompleto());

        edtLogin = (EditText) findViewById(R.id.edtLoginCadastro);
        edtLogin.setText(Util.getPrefLogin());

        edtSenha = (EditText) findViewById(R.id.edtSenhaCadastro);
        edtSenha.setText(Util.getPrefSenha());

        final boolean[] focado = new boolean[1];
        edtDataNascimento = (EditText) findViewById(R.id.edtDataNascimento);

        final DatePickerDialog dialog = new DatePickerDialog(
                DadosUsuarioActivity.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtDataNascimento.setText((dayOfMonth <= 9 ? "0" + dayOfMonth : dayOfMonth) +
                                "/" + (month <= 8 ? "0" + (month + 1) : month + 1) +
                                "/" + year);
                        dia = dayOfMonth;
                        mes = month;
                        ano = year;
                    }
                },
                ano, mes, dia
        );

        edtDataNascimento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focado[0] = hasFocus;
                if (!hasFocus) {
                    return;
                }

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        edtDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (focado[0]) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });
        edtDataNascimento.addTextChangedListener(new MaskTextWatcher(edtDataNascimento, new SimpleMaskFormatter("NN/NN/NNNN")));
        edtDataNascimento.setText(Util.getPrefDataNascimento());

        edtCPF = (EditText) findViewById(R.id.edtCPF);
        edtCPF.addTextChangedListener(new MaskTextWatcher(edtCPF, new SimpleMaskFormatter("NNN.NNN.NNN-NN")));
        edtCPF.setText(Util.getPrefCpf());

        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtTelefone.addTextChangedListener(new MaskTextWatcher(edtTelefone, new SimpleMaskFormatter("(NN) NNNNN-NNNN")));
        edtTelefone.setText(Util.getPrefTelefone());

        Button btnAtualizar = (Button) findViewById(R.id.btnCadastrar);
        btnAtualizar.setOnClickListener(this);
        btnAtualizar.setText("Atualizar");
    }

    @Override
    public void onClick(View v) {

        Util.usuariosFDBRef.orderByChild(Util.USER_LOGIN_KEY).equalTo(edtLogin.getText().toString())
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean existe = dataSnapshot.exists();
                    Usuario usuario = new Usuario();
                    if (existe)
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            usuario = value.getValue(Usuario.class);
                            usuario.setKey(value.getKey());
                        }

                    if (!existe || usuario.getKey() == null || usuario.getKey().equals(Util.getPrefUserKey())) {
                        usuario.setKey(Util.getPrefUserKey());
                        usuario.setNomeCompleto(edtNomeCompleto.getText().toString());
                        usuario.setLogin(edtLogin.getText().toString());
                        usuario.setSenha(edtSenha.getText().toString());
                        usuario.setDataNascimento(edtDataNascimento.getText().toString());
                        usuario.setCpf(edtCPF.getText().toString());
                        usuario.setTelefone(edtTelefone.getText().toString());

                        Util.usuariosFDBRef.child(Util.getPrefUserKey()).setValue(usuario);
                        Util.atualizarDadosUsuario(usuario);

                        Toast.makeText(getBaseContext(), "Dados atualizados com sucesso.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Já existe um usuário cadastrado com este login.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
    }
}
