package com.pvsul.inscricaopvsul;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.DragEvent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pvsul.inscricaopvsul.Models.Usuario;
import com.pvsul.inscricaopvsul.Util.Util;

import java.util.Calendar;

public class CadastroActivity extends Activity {

    int dia, mes, ano;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Calendar cal = Calendar.getInstance();
        dia = cal.get(Calendar.DAY_OF_MONTH);
        mes = cal.get(Calendar.MONTH);
        ano = cal.get(Calendar.YEAR);

        final EditText edtNomeCompleto = (EditText) findViewById(R.id.edtNomeCompleto);
        final EditText edtLogin = (EditText) findViewById(R.id.edtLoginCadastro);
        final EditText edtSenha = (EditText) findViewById(R.id.edtSenhaCadastro);
        final EditText edtDataNascimento = (EditText) findViewById(R.id.edtDataNascimento);
        edtDataNascimento.addTextChangedListener(new MaskTextWatcher(edtDataNascimento, new SimpleMaskFormatter("NN/NN/NNNN")));
        final EditText edtCPF = (EditText) findViewById(R.id.edtCPF);
        edtCPF.addTextChangedListener(new MaskTextWatcher(edtCPF, new SimpleMaskFormatter("NNN.NNN.NNN-NN")));
        final EditText edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtTelefone.addTextChangedListener(new MaskTextWatcher(edtTelefone, new SimpleMaskFormatter("(NN) NNNNN-NNNN")));

        final DatePickerDialog dialog = new DatePickerDialog(
                CadastroActivity.this,
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

        final boolean[] focado = new boolean[1];
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

        Button btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtLogin.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Login não pode ser vazio.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (edtSenha.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Senha não pode ser vazia.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edtDataNascimento.getText().toString().length() != 10) {
                    Toast.makeText(getBaseContext(), "Data de nascimento inválida.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtCPF.getText().toString().length() != 14) {
                    Toast.makeText(getBaseContext(), "CPF inválido.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtTelefone.getText().toString().length() != 15) {
                    Toast.makeText(getBaseContext(), "Telefone inválido.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Util.usuariosFDBRef.orderByChild(Util.USER_LOGIN_KEY).equalTo(edtLogin.getText().toString())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Usuario usuario = new Usuario();
                            usuario.setNomeCompleto(edtNomeCompleto.getText().toString());
                            usuario.setLogin(edtLogin.getText().toString());
                            usuario.setSenha(edtSenha.getText().toString());
                            usuario.setDataNascimento(edtDataNascimento.getText().toString());
                            usuario.setCpf(edtCPF.getText().toString());
                            usuario.setTelefone(edtTelefone.getText().toString());

                            String key = Util.usuariosFDBRef.push().getKey();
                            Util.usuariosFDBRef.child(key).setValue(usuario);

                            usuario.setKey(key);

                            Util.atualizarDadosUsuario(usuario);

                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Este login já encontra-se em uso.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
