package entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import bean.Foco;
import bean.Prevencao;
import entity.db.EliminaDengueDb;
import utils.DateUtils;

/**
 * Created by Alexandre on 04/03/2015.
 */
public class PrevencaoEntity extends EliminaDengueDb {

    private static final String ID_FOCO = "id_foco";
    private static final String DATA_CRIACAO = "dt_criacao";
    private static final String DATA_PRAZO = "dt_prazo";
    private static final String DATA_EFETUADA = "dt_efetuada";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    public PrevencaoEntity(Context context) {
        super(context);
    }


    public String createPrevencaoTable() {
        String CREATE_TABLE_PREVENCAO = "CREATE TABLE " + TABELA_PREVENCAO + "("
                + ID_FOCO + " INTEGER PRIMARY KEY, "
                + LATITUDE + " FLOAT, "
                + LONGITUDE + " FLOAT, "
                + DATA_CRIACAO + " DATE, " + DATA_EFETUADA + " DATE, " + DATA_PRAZO + " DATE);";

        return CREATE_TABLE_PREVENCAO;
    }


    public void addPrevencao(Prevencao prevencao) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_FOCO, prevencao.getFoco().getCodigo());
        values.put(LATITUDE, prevencao.getLatitude());
        values.put(LONGITUDE, prevencao.getLongitude());
        values.put(DATA_CRIACAO, new DateUtils().DateToString(prevencao.getDataCriacao()));
        values.put(DATA_PRAZO, new DateUtils().DateToString(prevencao.getDataPrazo()));
        values.put(DATA_EFETUADA, new DateUtils().DateToString(prevencao.getDataEfetuada()));

        db.insert(TABELA_PREVENCAO, null, values);
        db.close();

    }


    public void delPrevencao(int idFoco){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABELA_PREVENCAO, ID_FOCO + "=" + idFoco, null);
    }

    public Prevencao getProximaPrevencao(){
        Prevencao prev = new Prevencao();

        Foco f = new Foco();
        f.setCodigo(-1);
        prev.setFoco(f);

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABELA_PREVENCAO +" WHERE " + DATA_PRAZO + " > date('now')" + " ORDER BY " + DATA_PRAZO + " ASC";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0) {
            c.moveToFirst();

            f = new FocoEntity(super.context).getFoco((c.getInt(c.getColumnIndex(ID_FOCO))));
            prev.setFoco(f);
            prev.setLatitude(c.getDouble(c.getColumnIndex(LATITUDE)));
            prev.setLongitude(c.getDouble(c.getColumnIndex(LONGITUDE)));
            prev.setDataCriacao(new DateUtils().StringToDate(c.getString(c.getColumnIndex(DATA_CRIACAO))));
            prev.setDataEfetuada(new DateUtils().StringToDate(c.getString(c.getColumnIndex(DATA_EFETUADA))));
            prev.setDataPrazo(new DateUtils().StringToDate(c.getString(c.getColumnIndex(DATA_PRAZO))));

        }

        return prev;
    }

    public ArrayList<Prevencao> getAllPrevencoes() {
        ArrayList<Prevencao> arrPrev = new ArrayList<Prevencao>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABELA_PREVENCAO + " ORDER BY " + DATA_PRAZO + " ASC";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0) {
            c.moveToFirst();


            do {
                Prevencao prev = new Prevencao();
                prev.setFoco(new FocoEntity(super.context).getFoco((c.getInt(c.getColumnIndex(ID_FOCO)))));
                prev.setDataCriacao(new DateUtils().StringToDate(c.getString(c.getColumnIndex(DATA_CRIACAO))));
                prev.setDataEfetuada(new DateUtils().StringToDate(c.getString(c.getColumnIndex(DATA_EFETUADA))));
                prev.setDataPrazo(new DateUtils().StringToDate(c.getString(c.getColumnIndex(DATA_PRAZO))));
                arrPrev.add(prev);
            } while (c.moveToNext());

        }

        return arrPrev;
    }





    public Prevencao getPrevencao(int idFoco) {
        Prevencao prev = new Prevencao();

        Foco f = new Foco();
        f.setCodigo(-1);
        prev.setFoco(f);


            SQLiteDatabase db = this.getReadableDatabase();

            String selectQuery = "SELECT * FROM " + TABELA_PREVENCAO + " WHERE " + ID_FOCO + " = " + idFoco;

            Cursor c = db.rawQuery(selectQuery, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                prev.getFoco().setCodigo((c.getInt(c.getColumnIndex(ID_FOCO))));
                prev.setDataCriacao(new DateUtils().StringToDate(c.getString(c.getColumnIndex(DATA_CRIACAO))));
                prev.setDataEfetuada(new DateUtils().StringToDate(c.getString(c.getColumnIndex(DATA_EFETUADA))));
                prev.setDataPrazo(new DateUtils().StringToDate(c.getString(c.getColumnIndex(DATA_PRAZO))));
            }

        return prev;
    }


    public int updatePrevencao(Prevencao prev) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_FOCO, prev.getFoco().getCodigo());
        values.put(LATITUDE, prev.getLatitude());
        values.put(LONGITUDE, prev.getLongitude());
        values.put(DATA_EFETUADA, new DateUtils().DateToString(prev.getDataEfetuada()));
        values.put(DATA_PRAZO, new DateUtils().DateToString(prev.getDataPrazo()));
        values.put(DATA_CRIACAO, new DateUtils().DateToString(prev.getDataCriacao()));

        return db.update(TABELA_PREVENCAO, values, ID_FOCO + " = ?",
                new String[]{String.valueOf(prev.getFoco().getCodigo())});
    }

}