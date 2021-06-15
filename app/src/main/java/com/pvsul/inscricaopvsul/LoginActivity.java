package com.pvsul.inscricaopvsul;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pvsul.inscricaopvsul.Models.Usuario;
import com.pvsul.inscricaopvsul.Util.Util;

public class LoginActivity extends Activity {

    private EditText edtLogin;
    private EditText edtSenha;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Util.prefs = getSharedPreferences(Util.PREFS_FILE_NAME, MODE_PRIVATE);

        if (Util.getPrefLogin() != null && Util.getPrefSenha() != null) {
            finish();
            Intent i = new Intent(getBaseContext(), ListaEventosActivity.class);
            startActivity(i);
        }

        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        Button btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = Util.usuariosFDBRef.orderByChild(Util.USER_LOGIN_KEY).equalTo(edtLogin.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot value : dataSnapshot.getChildren()) {
                                Usuario u = value.getValue(Usuario.class);
                                u.setKey(value.getKey());

                                if (u.getSenha().equals(edtSenha.getText().toString())) {
                                    Util.atualizarDadosUsuario(u);
                                    finish();
                                    Intent i = new Intent(getBaseContext(), ListaEventosActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getBaseContext(), "Senha incorreta.", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Usuário não existe.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        });

        TextView txvCadastro = (TextView) findViewById(R.id.txvCadastrar);
        txvCadastro.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(getBaseContext(), CadastroActivity.class);
                startActivityForResult(i, Util.REQUEST_CADASTRO);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Util.REQUEST_CADASTRO && resultCode == RESULT_OK) {
            finish();
            Intent i = new Intent(getBaseContext(), ListaEventosActivity.class);
            startActivity(i);
        }
    }
}
