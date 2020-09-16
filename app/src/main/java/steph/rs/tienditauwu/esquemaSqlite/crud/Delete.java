package steph.rs.tienditauwu.esquemaSqlite.crud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;

import steph.rs.tienditauwu.esquemaSqlite.ConexionSqliteHelper;
import steph.rs.tienditauwu.esquemaSqlite.tablas.ProductoTabla;

public class Delete {

    public static void eliminar(Context context, int codigo, String tabla){

        ConexionSqliteHelper con = new ConexionSqliteHelper(context);

        SQLiteDatabase db = con.getWritableDatabase();

        switch (tabla){
            case ProductoTabla.TABLA:
                db.delete(ProductoTabla.TABLA, ProductoTabla.PROD_ID + " = ?", new String[]{String.valueOf(codigo)});
                break;
        }
    }

}
