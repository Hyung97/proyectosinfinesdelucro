package steph.rs.tienditauwu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import steph.rs.tienditauwu.data.modelo.Producto;
import steph.rs.tienditauwu.data.preferencia.SessionPreferences;
import steph.rs.tienditauwu.data.util.Mensaje;
import steph.rs.tienditauwu.esquemaSqlite.crud.Delete;
import steph.rs.tienditauwu.esquemaSqlite.crud.Insert;
import steph.rs.tienditauwu.esquemaSqlite.crud.Update;
import steph.rs.tienditauwu.esquemaSqlite.tablas.ProductoTabla;

public class ProductoDetalleActivity extends AppCompatActivity {

    @BindView(R.id.apdTietNombreProducto)
    TextInputEditText nombre;

    @BindView(R.id.apdTietPrecioProducto)
    TextInputEditText precio;

    @BindView(R.id.apdIvImagenProducto)
    ImageView imagen;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.apdLLAgregar)
    LinearLayout agregar;

    @BindView(R.id.apdLLModificar)
    LinearLayout modificarEliminar;

    boolean bNuevo = true;
    Producto producto;

    String pathUri = "";
    final int SELECT_PICTURE = 200;

    boolean bModificado = false;

    Mensaje mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mensaje = new Mensaje(getApplicationContext());

        if (getIntent().hasExtra("bNuevo")) {
            bNuevo = getIntent().getBooleanExtra("bNuevo", true);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        agregar.setVisibility(bNuevo ? View.VISIBLE : View.INVISIBLE);
        modificarEliminar.setVisibility(bNuevo ? View.INVISIBLE : View.VISIBLE);

        if (!bNuevo){
            producto = (Producto)getIntent().getSerializableExtra("itemProducto");
            cargarVista(producto);
        }
        nombre.requestFocus();
    }

    @OnClick(R.id.apdBBuscarImagen)
    public void buscarImagen(){
        Intent selectInten = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectInten.setType("image/*");
        startActivityForResult(Intent.createChooser(selectInten, "selecciona app de imagen"), SELECT_PICTURE);
    }

    @OnClick(R.id.apdBQuitarImagen)
    public void quitarImagen(){
        Picasso.get().load(R.drawable.caja_producto)
                .resize(180,160)
                .centerCrop()
                .into(imagen);
        pathUri = "";
    }

    @OnClick(R.id.apdBAgregar)
    public void agregarProducto(){
        if (nombre.getText().length() > 0){
            if (precio.getText().length() > 0) {

                int codigo = SessionPreferences.get(getApplicationContext()).getProducto();

                Insert.registrar(getApplicationContext(), new Producto(codigo,
                        nombre.getText().toString(),
                        Double.parseDouble(precio.getText().toString()),
                        pathUri, false
                ), ProductoTabla.TABLA);

                mensaje.mensajeToasGuardar();
                bModificado = true;

                nombre.setText("");
                precio.setText("");
                pathUri = "";
                Picasso.get().load(R.drawable.caja_producto)
                        .resize(180, 160)
                        .centerCrop()
                        .into(imagen);

                nombre.requestFocus();
            }else {
                mensaje.mensajeToas("Debe ingresar un precio");
                precio.requestFocus();
            }
        }else {
            mensaje.mensajeToas("Debe ingresar un nombre");
            nombre.requestFocus();
        }
    }
    @OnClick(R.id.apdBModificar)
    public void modificarProducto(){

        if (nombre.getText().length() > 0){
            if (precio.getText().length() > 0){
                Update.actualizar(getApplicationContext(),
                        new Producto(producto.getProd_id(),
                                nombre.getText().toString(),
                                Double.parseDouble(precio.getText().toString()),
                                pathUri,
                                false
                        ), ProductoTabla.TABLA);

                mensaje.mensajeToasGuardar();
                bModificado = true;
                salirActivity();
            }else {
                mensaje.mensajeToas("Debe ingresar un precio");
                precio.requestFocus();
            }
        }else {
            mensaje.mensajeToas("Debe ingresar un nombre");
            nombre.requestFocus();
        }
    }

    @OnClick(R.id.apdBEliminar)
    public void eliminarProducto(){

        new AlertDialog.Builder(this)
                .setTitle("Productos")
                .setMessage("¿Deseas eliminar el producto?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Delete.eliminar(getApplicationContext(),producto.getProd_id(), ProductoTabla.TABLA);

                        mensaje.mensajeToasEliminar();
                        bModificado = true;
                        salirActivity();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void cargarVista(Producto producto) {

        nombre.setText(producto.getProd_nombre());
        precio.setText(String.valueOf(producto.getProd_precio()));
        if (producto.getProd_ruta_foto().length() <= 1 || producto.getProd_ruta_foto().isEmpty()){
            Picasso.get().load(R.drawable.caja_producto).into(imagen);;
        }else{
            Picasso.get().load(producto.getProd_ruta_foto())
                    .resize(180,160)
                    .error(R.drawable.producto_error)
                    .centerCrop()
                    .into(imagen);
        }
        pathUri = producto.getProd_ruta_foto();


    }

    void salirActivity(){

        if (bModificado){
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        salirActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                salirActivity();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
