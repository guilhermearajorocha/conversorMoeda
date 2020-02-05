package com.example.coinconverter.ui.home

import android.R
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.google.gson.annotations.SerializedName

class Rates
{
    @SerializedName("CAD")
    var cad: Double? = null
    @SerializedName("EUR")
    var eur: Double? = null
    @SerializedName("BRL")
    var brl: Double? = null
    @SerializedName("USD")
    var usd: Double? = null
}

class Valores
{
    @SerializedName("rates")
    var rates: Rates? = null
}

class ValoresHistorico(var brl: Double, var eur: Double)

object DBContract{
    object TabelaHistorico: BaseColumns {
        const val TABELA = "historico"
        const val VALORBRL = "valorbrl"
        const val VALOREUR = "valoreur"
    }
}

class HistoricoDBHelper (context: Context) : SQLiteOpenHelper(context, "dbhistorico", null, 1) {
    val SQL_CREATE = "CREATE TABLE historico (valorbrl text primary key, valoreur text primary key)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    @Throws(SQLiteConstraintException::class)
    fun inserirValor(valores: ValoresHistorico): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.TabelaHistorico.VALORBRL, ".%2f".format(valores.brl))
        values.put(DBContract.TabelaHistorico.VALOREUR, ".%2f".format(valores.eur))
        val newRowId = db.insert(DBContract.TabelaHistorico.TABELA, null, values)

        return true
    }

    fun retornaHistorico(): ArrayList<ValoresHistorico>{
        var retorno = ArrayList<ValoresHistorico>()

        var db = writableDatabase

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                "select " + DBContract.TabelaHistorico.VALORBRL + ", " + DBContract.TabelaHistorico.VALOREUR +
                        " from " + DBContract.TabelaHistorico.TABELA, null
            )
        }catch(e: SQLiteException){
            db.execSQL(SQL_CREATE)
            return retorno
        }

        var brl: Double
        var usd: Double

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {

                brl = cursor.getString(cursor.getColumnIndex(DBContract.TabelaHistorico.VALORBRL)).toDouble()
                usd = cursor.getString(cursor.getColumnIndex(DBContract.TabelaHistorico.VALOREUR)).toDouble()

                retorno.add(ValoresHistorico(brl, usd))
                cursor.moveToNext()
            }
        }
        return retorno
    }
}