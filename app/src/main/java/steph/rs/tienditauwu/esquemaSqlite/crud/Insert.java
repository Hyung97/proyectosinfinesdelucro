package steph.rs.tienditauwu.esquemaSqlite.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import java.util.List;

import steph.rs.tienditauwu.data.modelo.Producto;
import steph.rs.tienditauwu.data.preferencia.SessionPreferences;
import steph.rs.tienditauwu.esquemaSqlite.ConexionSqliteHelper;
import steph.rs.tienditauwu.esquemaSqlite.tablas.ProductoTabla;


public class Insert {

    public static void registrar(Context context, Object param, String tabla) {

        ConexionSqliteHelper con = new ConexionSqliteHelper(context);

        SQLiteDatabase db = con.getWritableDatabase();

        ContentValues values = new ContentValues();

        switch (tabla)
        {
            case ProductoTabla.TABLA:
                Producto producto = (Producto)param;
                values.put(ProductoTabla.PROD_ID, producto.getProd_id());
                values.put(ProductoTabla.PROD_NOMBRE, producto.getProd_nombre());
                values.put(ProductoTabla.PROD_PRECIO, producto.getProd_precio());
                values.put(ProductoTabla.PROD_RUTA_FOTO, producto.getProd_ruta_foto());
                db.insert(ProductoTabla.TABLA, ProductoTabla.PROD_ID,  values);
                SessionPreferences.get(context).setProducto(producto.getProd_id());
                break;
        }
    }
}