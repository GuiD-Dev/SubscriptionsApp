package com.pvsul.inscricaopvsul.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.pvsul.inscricaopvsul.Models.Usuario;

public class UsuarioDBDAO extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private Context contexto;
    private static final String DATABASE_NAME = "Usuarios";
    private static final int DATABASE_VERSION = 1;

    private static class TableUsuarios implements BaseColumns {
        public static final String TABLE_NAME = "DadosUsuario";
        public static final String COL_NOME = "NomeCompleto";
        public static final String COL_DATA_NASCIMENTO = "DataNascimento";
        public static final String COL_CPF = "CPF";
        public static final String COL_TELEFONE = "Telefone";
    }

    public UsuarioDBDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String comando = "create table " + TableUsuarios.TABLE_NAME +
                        "(" + TableUsuarios._ID + "integer primary key autoincrement," +
                        TableUsuarios.COL_NOME + " text not null," +
                        TableUsuarios.COL_DATA_NASCIMENTO + " text not null," +
                        TableUsuarios.COL_CPF + " text not null," +
                        TableUsuarios.COL_TELEFONE + " text not null)";
        db.execSQL(comando);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TableUsuarios.TABLE_NAME);
        onCreate(db);
    }

    public void read() {
        if (!db.isOpen())
            db = getWritableDatabase();

        String query = "select * from ? where ? = ?";
        String[] args = { TableUsuarios.TABLE_NAME, TableUsuarios.COL_NOME, "Teste" };

        Cursor cursor = db.rawQuery(query, args);

        cursor.moveToFirst();
    }

    public void insert(Usuario u) {
        if (!db.isOpen())
            db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TableUsuarios.COL_NOME, u.getNomeCompleto());
        values.put(TableUsuarios.COL_DATA_NASCIMENTO, u.getDataNascimento());
        values.put(TableUsuarios.COL_CPF, u.getCpf());
        values.put(TableUsuarios.COL_TELEFONE, u.getTelefone());
        db.insert(TableUsuarios.TABLE_NAME, null, values);
    }

    public void update(Usuario u) {
        if (!db.isOpen())
            db = getWritableDatabase();
    }

    public void delete(int id) {
        if (!db.isOpen())
            db = getWritableDatabase();

        db.delete(TableUsuarios.TABLE_NAME, TableUsuarios._ID + " = " + id, null);
    }

}
