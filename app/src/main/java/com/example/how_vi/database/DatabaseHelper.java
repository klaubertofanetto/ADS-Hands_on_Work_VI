package com.example.how_vi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.how_vi.R;
import com.example.how_vi.Usuario.Usuario;
import com.example.how_vi.bandas.Banda;
import com.example.how_vi.discos.Disco;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lp_collection";
    private static final int VERSAO_BANCO = 1;
    private static final String TABLE_DISCO = "disco";
    private static final String TABLE_BANDA = "banda";
    private static final String TABLE_COLECAO = "colecao";
    private static final String TABLE_USUARIO = "usuario";

    private static final String CREATE_TABLE_DISCO = createTableCommand(TABLE_DISCO);
    private static final String CREATE_TABLE_BANDA = createTableCommand(TABLE_BANDA);
    private static final String CREATE_TABLE_COLECAO = createTableCommand(TABLE_COLECAO);
    private static final String CREATE_TABLE_USUARIO = createTableCommand(TABLE_USUARIO);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DISCO);
        db.execSQL(CREATE_TABLE_BANDA);
        db.execSQL(CREATE_TABLE_COLECAO);
        db.execSQL(CREATE_TABLE_USUARIO);
        db.execSQL(beforeDeleteBandaTrigger());
        db.execSQL(beforeDeleteDiscoTrigger());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] tabelas = {TABLE_BANDA, TABLE_DISCO, TABLE_COLECAO, TABLE_USUARIO};
        for (String tabela : tabelas) {
            db.execSQL(dropTableCommand(tabela));
        }
        db.execSQL("DROP TRIGGER BANDA_BD");
        db.execSQL("DROP TRIGGER DISCO_BD");
        onCreate(db);
    }

    private static String createTableCommand(String table) {
        StringBuilder str = new StringBuilder();
        str.append("CREATE TABLE ");
        str.append(table);
        str.append(" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        switch (table) {
            case "disco":
                str.append(" nome VARCHAR(100), ");
                str.append(" ano_lancamento INTEGER, ");
                str.append(" id_banda INTEGER NOT NULL ");
                break;
            case "banda":
                str.append(" nomeBanda VARCHAR(100) ");
                break;
            case "colecao":
                str.append(" id_disco INTEGER NOT NULL, ");
                str.append(" id_usuario INTEGER NOT NULL ");
                break;
            case "usuario":
                str.append(" nome VARCHAR(100), ");
                str.append(" email VARCHAR(100), ");
                str.append(" senha VARCHAR(100) ");
                break;
        }
        str.append(");");
        return str.toString();
    }

    private static String dropTableCommand(String table) {
        StringBuilder str = new StringBuilder();
        str.append("DROP TABLE IF EXISTS ");
        str.append(table);
        return str.toString();
    }

    private static String beforeDeleteBandaTrigger() {
        StringBuilder str = new StringBuilder();
        str.append("CREATE TRIGGER IF NOT EXISTS BANDA_BD ");
        str.append("BEFORE DELETE ");
        str.append("ON[banda] for each row ");
        str.append("BEGIN ");
        str.append("DELETE FROM disco where id_banda = old._id; ");
        str.append("END ");

        return str.toString();
    }

    private static String beforeDeleteDiscoTrigger() {
        StringBuilder str = new StringBuilder();
        str.append("CREATE TRIGGER IF NOT EXISTS DISCO_BD ");
        str.append("BEFORE DELETE ");
        str.append("ON[disco] for each row ");
        str.append("BEGIN ");
        str.append("DELETE FROM colecao where id_disco = old._id; ");
        str.append("END ");

        return str.toString();
    }

    /* INICIO CRUD BANDA */
    public long createBanda(String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nomeBanda", nome);
        long id = db.insert(TABLE_BANDA, null, cv);
        db.close();
        return id;
    }

    public long updateBanda(Banda b) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nomeBanda", b.getNome());
        long affected = db.update(TABLE_BANDA, cv, "_id = ?", new String[]{String.valueOf(b.getId())});
        return affected;
    }

    public long deleteBanda(Banda b) {
        SQLiteDatabase db = this.getWritableDatabase();
        long affected = db.delete(TABLE_BANDA, "_id=?", new String[]{String.valueOf(b.getId())});
        db.close();
        return affected;
    }

    public void getAllBanda(Context context, ListView lv) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id", "nomeBanda"};
        Cursor data = db.query(TABLE_BANDA, columns, null, null, null, null, "nomeBanda");
        int[] to = {R.id.tvListarBandaId, R.id.tvListarBandaNome};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.banda_item_list_view, data, columns, to, 0);
        lv.setAdapter(simpleCursorAdapter);
        db.close();
    }

    public Banda getBandaById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id", "nomeBanda"};
        String[] args = {String.valueOf(id)};
        Cursor data = db.query(TABLE_BANDA, columns, "_id = ?", args, null, null, null);
        data.moveToFirst();
        Banda b = new Banda(data.getInt(0), data.getString(1));
        data.close();
        db.close();
        return b;
    }

    public boolean isBandaRepetida(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"nomeBanda"};
        String[] args = {nome};
        Cursor data = db.query(TABLE_BANDA, columns, "nomeBanda = ? COLLATE NOCASE", args, null, null, null);
        try{
            return data.moveToFirst();
        } finally {
            db.close();
            data.close();
        }
    }

    public void getAllNameBanda(ArrayList<Integer> listBandaId, ArrayList<String> listBandaNome) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id", "nomeBanda"};
        Cursor data = db.query(TABLE_BANDA, columns, null, null, null, null, "nomeBanda");
        while (data.moveToNext()) {
            int idColumnIndex = data.getColumnIndex("_id");
            listBandaId.add(Integer.parseInt(data.getString(idColumnIndex)));
            int nomeColumnIndex = data.getColumnIndex("nomeBanda");
            listBandaNome.add(data.getString(nomeColumnIndex));
        }
    }

    /* FIM CRUD BANDA */

    /* INICIO CRUD DISCO */
    public long createDisco(Disco disco) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", disco.getNome());
        cv.put("ano_lancamento", disco.getAnoLancamento());
        cv.put("id_banda", disco.getId_banda());
        long id = db.insert(TABLE_DISCO, null, cv);
        db.close();
        return id;
    }

    public long updateDisco(Disco disco) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", disco.getNome());
        cv.put("ano_lancamento", disco.getAnoLancamento());
        cv.put("id_banda", disco.getId_banda());
        long affected = db.update(TABLE_DISCO, cv, "_id = ?", new String[]{String.valueOf(disco.getId())});
        return affected;
    }

    public long deleteDisco(Disco disco) {
        SQLiteDatabase db = this.getWritableDatabase();
        long affected = db.delete(TABLE_DISCO, "_id=?", new String[]{String.valueOf(disco.getId())});
        db.close();
        return affected;
    }

    public void getAllDiscos(Context context, ListView lv) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id", "nome", "ano_lancamento", "id_banda", "nomeBanda"};
        String query = "SELECT disco._id, disco.nome, disco.ano_lancamento, disco.id_banda, banda.nomeBanda " + //
                "FROM disco INNER JOIN banda on banda._id = disco.id_banda order by banda.nomeBanda, disco.ano_lancamento";
        Cursor data = db.rawQuery(query, null);
        int[] to = {R.id.tvListarDiscoId, R.id.tvListarDiscoNome, R.id.tvListarDiscoAno, R.id.tvListarDiscoBandaId, R.id.tvListarDiscoBandaNome};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.disco_item_list_view, data, columns, to, 0);
        lv.setAdapter(simpleCursorAdapter);
        db.close();
    }

    public Disco getDiscoById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id", "nome", "ano_lancamento", "id_banda"};
        String[] args = {String.valueOf(id)};
        Cursor data = db.query(TABLE_DISCO, columns, "_id = ?", args, null, null, null);
        data.moveToFirst();
        Disco disco = new Disco();
        disco.setId(data.getInt(0));
        disco.setNome(data.getString(1));
        disco.setAnoLancamento(data.getInt(2));
        disco.setId_banda(data.getInt(3));
        disco.setBanda(getBandaById(disco.getId_banda()));
        data.close();
        db.close();
        return disco;
    }

    public void getDiscosForSpinner(ArrayList<Integer> listDiscoId, ArrayList<String> listDisco) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT disco._id, disco.nome, banda.nomeBanda FROM disco INNER JOIN banda on banda._id = disco.id_banda order by banda.nomeBanda";
        Cursor data = db.rawQuery(query, null);
        while (data.moveToNext()) {
            int idColumnIndex = data.getColumnIndex("_id");
            listDiscoId.add(Integer.parseInt(data.getString(idColumnIndex)));
            int discoColumnIndex = data.getColumnIndex("nome");
            int bandaColumnIndex = data.getColumnIndex("nomeBanda");
            String discoInfo = data.getString(bandaColumnIndex) + " | " + data.getString(discoColumnIndex);
            listDisco.add(discoInfo);
        }
    }

    public boolean isDiscoRepetido(int id_banda, String nome_disco){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id_banda, nome"};
        String[] args = {String.valueOf(id_banda), nome_disco};
        Cursor data = db.query(TABLE_DISCO, columns, "id_banda = ? AND nome=? COLLATE NOCASE", args, null, null, null);
        try{
            return data.moveToFirst();
        } finally {
            db.close();
            data.close();
        }

    }

    /* FIM CRUD DISCO */

    /* INICIO CRUD USU??RIO */
    public long createUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", usuario.getNome());
        cv.put("email", usuario.getEmail());
        cv.put("senha", usuario.getSenha());
        long id = db.insert(TABLE_USUARIO, null, cv);
        db.close();
        return id;
    }

    public long updateUsuario (Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", usuario.getNome());
        cv.put("email", usuario.getEmail());
        long affected = db.update(TABLE_USUARIO, cv, "_id = ?", new String[]{String.valueOf(usuario.getId())});
        db.close();
        return affected;
    }

    public boolean isSenhaValida(int id, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id", "senha"};
        String[] args = {String.valueOf(id), senha};
        Cursor data = db.query(TABLE_USUARIO, columns, "_id = ? AND senha = ?", args, null, null, null);
        try{
            return data.moveToFirst();
        } finally {
            db.close();
            data.close();
        }

    }

    public boolean getUsuarioByNome(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id"};
        String[] args = {String.valueOf(nome)};
        Cursor data = db.query(TABLE_USUARIO, columns, "nome = ? COLLATE NOCASE", args, null, null, null);
        try{
            return data.moveToFirst();
        } finally {
            data.close();
            db.close();
        }
    }

    public boolean getUsuarioByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id"};
        String[] args = {String.valueOf(email)};
        Cursor data = db.query(TABLE_USUARIO, columns, "email = ? COLLATE NOCASE", args, null, null, null);
        try {
            return data.moveToFirst();
        } finally {
            data.close();
            db.close();
        }
    }

    public Usuario buscaLogin(String login, String senha){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id", "nome", "email", "senha"};
        String[] args = {String.valueOf(login), String.valueOf(login), String.valueOf(senha)};
        Cursor data = db.query(TABLE_USUARIO, columns, "( nome = ? COLLATE NOCASE OR email = ? COLLATE NOCASE) AND senha = ?", args, null, null, null);
        Usuario usuario = new Usuario();
        if (data.moveToFirst()){
            usuario.setId(data.getInt(0));
            usuario.setNome(data.getString(1));
            usuario.setEmail(data.getString(2));
            usuario.setSenha(data.getString(3));
        } else {
            usuario.setId(0);
        }
        data.close();
        db.close();
        return usuario;
    }
    /* FIM CRUD USU??RIO */

    /* IN??CIO CRUD COLE????O */

    public void getAllDiscosFromColecao(Context context, ListView lv, int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"_id", "nome", "ano_lancamento", "id_banda", "nomeBanda"};
        String query = "SELECT colecao._id, disco.nome, disco.ano_lancamento, disco.id_banda, banda.nomeBanda FROM disco " + //
                        "INNER JOIN banda on banda._id = disco.id_banda " + //
                        "INNER JOIN colecao on colecao.id_disco = disco._id WHERE colecao.id_usuario = " + Integer.toString(idUsuario) +" order by banda.nomeBanda";
        Cursor data = db.rawQuery(query, null);
        int[] to = {R.id.tvListarColecaoId, R.id.tvListarColecaoDiscoNome, R.id.tvListarColecaoDiscoAno, R.id.tvListarColecaoBandaId, R.id.tvListarColecaoBandaNome};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.colecao_item_list_view, data, columns, to, 0);
        lv.setAdapter(simpleCursorAdapter);
        db.close();
    }

    public long insertInColecao(int id_usuario, int id_disco){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_usuario", id_usuario);
        cv.put("id_disco", id_disco);
        long id = db.insert(TABLE_COLECAO, null, cv);
        db.close();
        return id;
    }

    public long deleteFromColecao(String id_colecao){
        SQLiteDatabase db = this.getWritableDatabase();
        long affected = db.delete(TABLE_COLECAO, "_id=?", new String[]{id_colecao});
        db.close();
        return affected;
    }

    public boolean isDiscoNaColecao(int id_disco, int id_usuario){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id_disco, id_usuario"};
        String[] args = {String.valueOf(id_disco), String.valueOf(id_usuario)};
        Cursor data = db.query(TABLE_COLECAO, columns, "id_disco = ? AND id_usuario=?", args, null, null, null);
        try{
            return data.moveToFirst();
        } finally {
            db.close();
            data.close();
        }
    }

    /* FIM CRUD COLE????O */
}

