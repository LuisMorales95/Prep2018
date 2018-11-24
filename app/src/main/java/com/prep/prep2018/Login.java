package com.prep.prep2018;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.ArrayList;

public class Login extends AppCompatActivity {



    ImageButton Login_Log;
    EditText Login_usuario,Login_contra;
    /*ConstraintLayout PantallaLogin;*/
    TextView Login_version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
        /*PantallaLogin = (ConstraintLayout) findViewById(R.id.PantallaLogin);
        PantallaLogin.setBackgroundResource(R.drawable.backgrounds);*/
        Login_version = (TextView) findViewById(R.id.Login_version);
        Login_version.setText("Prep2018_Beta.1.0");
        Data.preferences = getSharedPreferences("InfoUsu",MODE_PRIVATE);
        String Usuario = Data.preferences.getString(Data.iAppLogin,"");
        String Contrasenia = Data.preferences.getString(Data.iAppPwd,"");
        Login_usuario = (EditText) findViewById(R.id.Login_usuario);
        Login_contra = (EditText) findViewById(R.id.Login_contra);
        Login_Log = (ImageButton) findViewById(R.id.Login_Log);
        if (Usuario.isEmpty()&&Contrasenia.isEmpty()){

            Login_Log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Login_usuario.getText().toString().isEmpty()&&Login_contra.getText().toString().isEmpty()){
                        Snackbar.make(getCurrentFocus(),"Rellene los Campos",Snackbar.LENGTH_LONG).show();
                    }else{
                        new login(Login_usuario.getText().toString(),Login_contra.getText().toString(),Data.preferences).execute();
                    }

                }
            });
        }else{
            new login(Usuario,Contrasenia,Data.preferences).execute();
        }


    }

    private class login extends AsyncTask<Void,Integer,Integer>{
        ProgressDialog progressDialog = new ProgressDialog(Login.this);

        String Usuario, Contra;
        SharedPreferences preferences;
        String iAppID,iAppLogin,iAppNombre,iAppPwd,iAppActivo,iAppAdmin,iAppMobil,iAppCaptura,iAppGrafica,iAppUsrMobil;
        public login(String usuario, String contra, SharedPreferences preferences) {
            Usuario = usuario;
            Contra = contra;
            this.preferences = preferences;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando informacion personal...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("usuario",Usuario));
            datatosend.add(new BasicNameValuePair("contra",Contra));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.Loginphp);
            // ++ RESPONSE FROM SERVER ++
            HttpResponse httpResponse = null;
            String responsedecoded = "";

            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");

                if (responsedecoded.equals("[]")){
                    return 1;
                }else{
                    JSONArray jsonArray = new JSONArray(responsedecoded);
                    iAppID = jsonArray.getJSONObject(0).getString("iAppID");
                    iAppLogin = jsonArray.getJSONObject(0).getString("iAppLogin");
                    iAppNombre = jsonArray.getJSONObject(0).getString("iAppNombre");
                    iAppPwd = jsonArray.getJSONObject(0).getString("iAppPwd");
                    iAppActivo = jsonArray.getJSONObject(0).getString("iAppActivo");
                    iAppAdmin = jsonArray.getJSONObject(0).getString("iAppAdmin");
                    iAppMobil = jsonArray.getJSONObject(0).getString("iAppMobil");
                    iAppCaptura = jsonArray.getJSONObject(0).getString("iAppCaptura");
                    iAppGrafica = jsonArray.getJSONObject(0).getString("iAppGrafica");
                    iAppUsrMobil = jsonArray.getJSONObject(0).getString("iAppUsrMobil");
                    if (iAppActivo.equals("1")){
                        return 2;
                    }else{
                        return 3;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return 0;
        }


        @Override
        protected void onPostExecute(Integer aBoolean) {
            progressDialog.dismiss();
            if (aBoolean.equals(0)){
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                builder.setMessage("Error en la solicitud")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Login_Log.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Login_usuario.getText().toString().isEmpty()&&Login_contra.getText().toString().isEmpty()){
                                            Snackbar.make(getCurrentFocus(),"Rellene los Campos",Snackbar.LENGTH_LONG).show();
                                        }else{
                                            new login(Login_usuario.getText().toString(),Login_contra.getText().toString(),Data.preferences).execute();
                                        }

                                    }
                                });
                            }
                        }).show();
                builder.create();
            }else if (aBoolean.equals(1)){
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                builder.setMessage("Usuario Invalido")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Login_Log.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Login_usuario.getText().toString().isEmpty()&&Login_contra.getText().toString().isEmpty()){
                                            Snackbar.make(getCurrentFocus(),"Rellene los Campos",Snackbar.LENGTH_LONG).show();
                                        }else{
                                            new login(Login_usuario.getText().toString(),Login_contra.getText().toString(),Data.preferences).execute();
                                        }

                                    }
                                });
                            }
                        }).show();
                builder.create();
            }else if (aBoolean.equals(2)){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Data.iAppID,iAppID);
                editor.putString(Data.iAppLogin,iAppLogin);
                editor.putString(Data.iAppNombre,iAppNombre);
                editor.putString(Data.iAppPwd,iAppPwd);
                editor.putString(Data.iAppActivo,iAppActivo);
                editor.putString(Data.iAppAdmin,iAppAdmin);
                editor.putString(Data.iAppMobil,iAppMobil);
                editor.putString(Data.iAppCaptura,iAppCaptura);
                editor.putString(Data.iAppGrafica,iAppGrafica);
                editor.putString(Data.iAppUserMobil,iAppUsrMobil);
                editor.apply();
                finish();
                startActivity(new Intent(Login.this,MainActivity.class));
            }else if (aBoolean.equals(3)){
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                builder.setMessage("Su cuenta no esta activada")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Login_Log.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Login_usuario.getText().toString().isEmpty()&&Login_contra.getText().toString().isEmpty()){
                                            Snackbar.make(getCurrentFocus(),"Rellene los Campos",Snackbar.LENGTH_LONG).show();
                                        }else{
                                            new login(Login_usuario.getText().toString(),Login_contra.getText().toString(),Data.preferences).execute();
                                        }

                                    }
                                });
                            }
                        }).show();
                builder.create();
            }
        }
    }

}
