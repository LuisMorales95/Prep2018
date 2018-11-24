package com.prep.prep2018;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FullEvidencia extends AppCompatActivity {
    static String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_evidencia);
        ImageView imageView = (ImageView) findViewById(R.id.FullEvidencia_imagen);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Imagen...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Picasso.get().load(URL).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getBaseContext());
                builder.setMessage("No se pudo cargar la imagen!")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FullEvidencia.this.finish();
                            }
                        }).show();
                builder.create();
            }
        });
    }
}
