package steph.rs.tienditauwu.esquemaSqlite.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import steph.rs.tienditauwu.data.modelo.Producto;
import steph.rs.tienditauwu.esquemaSqlite.ConexionSqliteHelper;
import steph.rs.tienditauwu.esquemaSqlite.tablas.ProductoTabla;


public class Update {

    public static void actualizar(Context context, Object param, String tabla) {

    ConexionSqliteHelper con = new ConexionSqliteHelper(context);

    SQLiteDatabase db = con.getWritableDatabase();

    ContentValues values = new ContentValues();

    switch (tabla)
    {
        case ProductoTabla.TABLA:

            Producto prod = (Producto)param;

            values.put(ProductoTabla.PROD_NOMBRE,prod.getProd_nombre());
            values.put(ProductoTabla.PROD_PRECIO,prod.getProd_precio());
            values.put(ProductoTabla.PROD_RUTA_FOTO,prod.getProd_ruta_foto());

            db.update(ProductoTabla.TABLA,values, ProductoTabla.PROD_ID + " = ?", new String[] {String.valueOf(prod.getProd_id())});

            break;
    }
}
}
