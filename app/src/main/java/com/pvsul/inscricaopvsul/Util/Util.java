package com.pvsul.inscricaopvsul.Util;

import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pvsul.inscricaopvsul.Models.Usuario;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private static final String USER_KEY = "key";
    private static final String USER_NOME_COMPLETO_KEY = "nomeCompleto";
    public static final String USER_LOGIN_KEY = "login";
    private static final String USER_PASS_KEY = "senha";
    private static final String USER_DATA_NASCIMENTO_KEY = "dataNascimento";
    private static final String USER_CPF_KEY = "cpf";
    private static final String USER_TELEFONE_KEY = "telefone";

    public static String getPrefUserKey() {
        return Util.prefs.getString(Util.USER_KEY, null);
    }

    public static String getPrefNomeCompleto() {
        return Util.prefs.getString(Util.USER_NOME_COMPLETO_KEY, null);
    }

    public static String getPrefLogin() {
        return Util.prefs.getString(Util.USER_LOGIN_KEY, null);
    }

    public static String getPrefSenha() {
        return Util.prefs.getString(Util.USER_PASS_KEY, null);
    }

    public static String getPrefDataNascimento() {
        return Util.prefs.getString(Util.USER_DATA_NASCIMENTO_KEY, null);
    }

    public static String getPrefCpf() {
        return Util.prefs.getString(Util.USER_CPF_KEY, null);
    }

    public static String getPrefTelefone() {
        return Util.prefs.getString(Util.USER_TELEFONE_KEY, null);
    }

    public static final String PREFS_FILE_NAME = "PrefsFile";
    public static SharedPreferences prefs;

    public static final int REQUEST_CADASTRO = 1000;

    private static DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    public static DatabaseReference usuariosFDBRef = firebaseRef.child("Usuarios");
    public static DatabaseReference eventosFDBRef = firebaseRef.child("Eventos");

    public static void atualizarDadosUsuario(Usuario u) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Util.USER_KEY, Util.prefs.getString(Util.USER_KEY, u.getKey()));
        editor.putString(Util.USER_NOME_COMPLETO_KEY, u.getNomeCompleto());
        editor.putString(Util.USER_LOGIN_KEY, u.getLogin());
        editor.putString(Util.USER_PASS_KEY, u.getSenha());
        editor.putString(Util.USER_DATA_NASCIMENTO_KEY, u.getDataNascimento());
        editor.putString(Util.USER_CPF_KEY, u.getCpf());
        editor.putString(Util.USER_TELEFONE_KEY, u.getTelefone());
        editor.commit();
    }

    public static void logout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Util.USER_KEY);
        editor.remove(Util.USER_NOME_COMPLETO_KEY);
        editor.remove(Util.USER_LOGIN_KEY);
        editor.remove(Util.USER_PASS_KEY);
        editor.remove(Util.USER_DATA_NASCIMENTO_KEY);
        editor.remove(Util.USER_CPF_KEY);
        editor.remove(Util.USER_TELEFONE_KEY);
        editor.commit();
    }

}