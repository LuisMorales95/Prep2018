package com.prep.prep2018;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import java.util.List;

public class Dar_seccion extends AppCompatActivity {

    private ListView Dar_Seccion_Secciones;
    private ListView Dar_Seccion_Asignadas;
    static String idUSUARIO ="";
    static String NombreUsuario = "";
    private TextView Dar_Seccion_Nombre;

    private List<seccion_item> seccion_itemList;
    private List<seccion_Asignada_item> seccion_asignada_itemList;
    private ListAdapter adapter;
    private ListAdapterAsignadas adapterAsignadas;
    int dar=0,eliminar=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dar_seccion);
        getSupportActionBar().setTitle("Asignar Seccion");

        Dar_Seccion_Secciones = (ListView) findViewById(R.id.Dar_Seccion_Secciones);
        Dar_Seccion_Asignadas = (ListView) findViewById(R.id.Dar_Seccion_Asignadas);
        Dar_Seccion_Nombre = (TextView) findViewById(R.id.Dar_Seccion_Nombre);
        Dar_Seccion_Nombre.setText(NombreUsuario);
        new secciones(idUSUARIO).execute();

        Dar_Seccion_Secciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            final boolean[] nondoubleclick = {true};
            final long[] firstclickTime = {0L};
            final int DOUBLE_CLICK_TIMEOUT = 200;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                synchronized (this){
                    if (firstclickTime[0] ==0){
                        firstclickTime[0] = SystemClock.elapsedRealtime();
                        nondoubleclick[0]=true;
                    }else{
                        long deltatime = SystemClock.elapsedRealtime()-firstclickTime[0];
                        firstclickTime[0]=0;
                        if (deltatime<DOUBLE_CLICK_TIMEOUT){
                            nondoubleclick[0]=false;
                            this.onItemDoubleClick(parent,view,position,id);
                            return;
                        }
                    }
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (nondoubleclick[0]){
                                String seccionid = seccion_itemList.get(position).getSeccion_id();
                                seccion_itemList.remove(position);
                                new asignar_seccion_a_usuario(idUSUARIO, seccionid, position).execute();
                            }
                        }
                    },DOUBLE_CLICK_TIMEOUT);
                }
            }
            public void onItemDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(getCurrentFocus(),"Solo de un Click",Snackbar.LENGTH_LONG).show();
            }
        });
        Dar_Seccion_Asignadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            final boolean[] nondoubleclick = {true};
            final long[] firstclickTime = {0L};
            final int DOUBLE_CLICK_TIMEOUT = 200;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                synchronized (this){
                    if (firstclickTime[0] ==0){
                        firstclickTime[0] = SystemClock.elapsedRealtime();
                        nondoubleclick[0]=true;
                    }else{
                        long deltatime = SystemClock.elapsedRealtime()-firstclickTime[0];
                        firstclickTime[0]=0;
                        if (deltatime<DOUBLE_CLICK_TIMEOUT){
                            nondoubleclick[0]=false;
                            this.onItemDoubleClick(parent,view,position,id);
                            return;
                        }
                    }
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (nondoubleclick[0]){
                                String idseccionusr = seccion_asignada_itemList.get(position).getIdseccionusr();
                                new delete_seccion_a_usuario(idseccionusr,position).execute();
                            }
                        }
                    },DOUBLE_CLICK_TIMEOUT);
                }
            }
            public void onItemDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(getCurrentFocus(),"Solo de un Click",Snackbar.LENGTH_LONG).show();
            }
        });

    }


    private class secciones extends AsyncTask<Void,Boolean,Boolean> {
        String id_usuario;

        public secciones(String id_usuario) {
            this.id_usuario = id_usuario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("id_usuario",id_usuario));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectSeccionesLibres);
            HttpResponse httpResponse = null;
            String responsedecoded = "";

            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                JSONArray jsonArray = new JSONArray(responsedecoded);
                if (responsedecoded.equals("[]")){
                    httpEntity.consumeContent();
                    return false;
                }else{
                    httpEntity.consumeContent();
                    seccion_itemList = new ArrayList<seccion_item>();
                    for (int i = 0; i<jsonArray.length();i++){
                        seccion_item item = new seccion_item();
                        item.setSeccion_id(jsonArray.getJSONObject(i).getString("idseccion"));
                        item.setSeccion_seccion(jsonArray.getJSONObject(i).getString("seccion"));
                        item.setSeccion_municipio(jsonArray.getJSONObject(i).getString("municipio"));
                        item.setSeccion_ubicacion(jsonArray.getJSONObject(i).getString("ubicacion"));
                        seccion_itemList.add(item);
                    }
                }
                httpEntity.consumeContent();
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                adapter = new ListAdapter(seccion_itemList);
                Dar_Seccion_Secciones.setAdapter(adapter);
                new secciones_asignadas(idUSUARIO).execute();
            }else{
                new secciones_asignadas(idUSUARIO).execute();
            }
        }
    }
    private class ListAdapter extends BaseAdapter {

        private List<seccion_item> valores;
        private LayoutInflater inflater;

        public ListAdapter(List<seccion_item> valores) {
            this.valores = valores;
        }

        @Override
        public int getCount() {
            return valores.size();
        }

        @Override
        public Object getItem(int position) {
            return valores.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (inflater == null) inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) convertView = inflater.inflate(R.layout.dar_seccion_item, null);

            TextView dar_seccion_item_seccion = (TextView) convertView.findViewById(R.id.dar_seccion_item_seccion);
            TextView dar_seccion_item_municipio = (TextView) convertView.findViewById(R.id.dar_seccion_item_municipio);
            TextView dar_seccion_item_direccion = (TextView) convertView.findViewById(R.id.dar_seccion_item_direccion);

            dar_seccion_item_seccion.setText(valores.get(position).getSeccion_seccion());
            dar_seccion_item_municipio.setText(valores.get(position).getSeccion_municipio());
            dar_seccion_item_direccion.setText(valores.get(position).getSeccion_ubicacion());

            return convertView;
        }
    }


    private class secciones_asignadas extends AsyncTask<Void,Boolean,Boolean> {
        String id_usuario;

        public secciones_asignadas(String id_usuario) {
            this.id_usuario = id_usuario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("id_usuario",id_usuario));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectSeccionesAsignadas);
            HttpResponse httpResponse = null;
            String responsedecoded = "";

            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                JSONArray jsonArray = new JSONArray(responsedecoded);
                if (responsedecoded.equals("[]")){
                    httpEntity.consumeContent();
                    return false;
                }else{
                    httpEntity.consumeContent();
                    seccion_asignada_itemList = new ArrayList<seccion_Asignada_item>();
                    for (int i = 0; i<jsonArray.length();i++){
                        seccion_Asignada_item item = new seccion_Asignada_item();
                        item.setIdseccionusr(jsonArray.getJSONObject(i).getString("idseccionusr"));
                        item.setSeccion_id(jsonArray.getJSONObject(i).getString("idseccion"));
                        item.setSeccion_seccion(jsonArray.getJSONObject(i).getString("seccion"));
                        item.setSeccion_municipio(jsonArray.getJSONObject(i).getString("municipio"));
                        item.setSeccion_ubicacion(jsonArray.getJSONObject(i).getString("ubicacion"));
                        seccion_asignada_itemList.add(item);
                    }
                }
                httpEntity.consumeContent();
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                if (seccion_asignada_itemList.isEmpty()){
                    adapter.notifyDataSetChanged();
                }else{
                    adapterAsignadas = new ListAdapterAsignadas(seccion_asignada_itemList);
                    Dar_Seccion_Asignadas.setAdapter(adapterAsignadas);
                }
            }
        }
    }
    private class ListAdapterAsignadas extends BaseAdapter {

        private List<seccion_Asignada_item> valores;
        private LayoutInflater inflater;

        public ListAdapterAsignadas(List<seccion_Asignada_item> valores) {
            this.valores = valores;
        }

        @Override
        public int getCount() {
            return valores.size();
        }

        @Override
        public Object getItem(int position) {
            return valores.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (inflater == null) inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) convertView = inflater.inflate(R.layout.dar_seccion_item, null);

            TextView dar_seccion_item_seccion = (TextView) convertView.findViewById(R.id.dar_seccion_item_seccion);
            TextView dar_seccion_item_municipio = (TextView) convertView.findViewById(R.id.dar_seccion_item_municipio);
            TextView dar_seccion_item_direccion = (TextView) convertView.findViewById(R.id.dar_seccion_item_direccion);

            dar_seccion_item_seccion.setText(valores.get(position).getSeccion_seccion());
            dar_seccion_item_municipio.setText(valores.get(position).getSeccion_municipio());
            dar_seccion_item_direccion.setText(valores.get(position).getSeccion_ubicacion());

            return convertView;
        }
    }


    private class seccion_item {
        String seccion_id,seccion_seccion,seccion_ubicacion,seccion_municipio;

        public seccion_item() {
        }

        public seccion_item(String seccion_id, String seccion_seccion, String seccion_ubicacion, String seccion_municipio) {
            this.seccion_id = seccion_id;
            this.seccion_seccion = seccion_seccion;
            this.seccion_ubicacion = seccion_ubicacion;
            this.seccion_municipio = seccion_municipio;
        }

        public String getSeccion_id() {
            return seccion_id;
        }

        public void setSeccion_id(String seccion_id) {
            this.seccion_id = seccion_id;
        }

        public String getSeccion_seccion() {
            return seccion_seccion;
        }

        public void setSeccion_seccion(String seccion_seccion) {
            this.seccion_seccion = seccion_seccion;
        }

        public String getSeccion_ubicacion() {
            return seccion_ubicacion;
        }

        public void setSeccion_ubicacion(String seccion_ubicacion) {
            this.seccion_ubicacion = seccion_ubicacion;
        }

        public String getSeccion_municipio() {
            return seccion_municipio;
        }

        public void setSeccion_municipio(String seccion_municipio) {
            this.seccion_municipio = seccion_municipio;
        }
    }
    private class seccion_Asignada_item{
        String idseccionusr,seccion_id,seccion_seccion,seccion_ubicacion,seccion_municipio;

        public seccion_Asignada_item() {
        }

        public seccion_Asignada_item(String idseccionusr, String seccion_id, String seccion_seccion, String seccion_ubicacion, String seccion_municipio) {
            this.idseccionusr = idseccionusr;
            this.seccion_id = seccion_id;
            this.seccion_seccion = seccion_seccion;
            this.seccion_ubicacion = seccion_ubicacion;
            this.seccion_municipio = seccion_municipio;
        }

        public String getIdseccionusr() {
            return idseccionusr;
        }

        public void setIdseccionusr(String idseccionusr) {
            this.idseccionusr = idseccionusr;
        }

        public String getSeccion_id() {
            return seccion_id;
        }

        public void setSeccion_id(String seccion_id) {
            this.seccion_id = seccion_id;
        }

        public String getSeccion_seccion() {
            return seccion_seccion;
        }

        public void setSeccion_seccion(String seccion_seccion) {
            this.seccion_seccion = seccion_seccion;
        }

        public String getSeccion_ubicacion() {
            return seccion_ubicacion;
        }

        public void setSeccion_ubicacion(String seccion_ubicacion) {
            this.seccion_ubicacion = seccion_ubicacion;
        }

        public String getSeccion_municipio() {
            return seccion_municipio;
        }

        public void setSeccion_municipio(String seccion_municipio) {
            this.seccion_municipio = seccion_municipio;
        }
    }

    private class asignar_seccion_a_usuario extends AsyncTask<Void,Boolean,Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(Dar_seccion.this);
        private String idUsuario,idSeccion;
        Integer position;

        public asignar_seccion_a_usuario(String idUsuario, String idSeccion, Integer position) {
            this.idUsuario = idUsuario;
            this.idSeccion = idSeccion;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando Casillas...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("idusuario",idUsuario));
            datatosend.add(new BasicNameValuePair("idseccion",idSeccion));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.InsertSeccionUsuarios);
            HttpResponse httpResponse = null;
            String responsedecoded = "";

            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                JSONArray jsonArray = new JSONArray(responsedecoded);
                if (responsedecoded.equals("[]")){
                    httpEntity.consumeContent();
                    return false;
                }else{
                    httpEntity.consumeContent();
                    if (jsonArray.getJSONObject(0).getString("EXITO").equals("SI")){
                        return true;
                    }
                }
                httpEntity.consumeContent();
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            if (aBoolean){
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                /*new secciones(idUSUARIO).execute();*/
            }else{
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Dar_seccion.this);
                builder.setMessage("Su solicitud no se ha realizado con exito!")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, null).show();
                builder.create();
            }
        }
    }
    private class delete_seccion_a_usuario extends AsyncTask<Void,Boolean,Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(Dar_seccion.this);
        private String idseccionusr;
        private Integer position;

        public delete_seccion_a_usuario(String idseccionusr, Integer position) {
            this.idseccionusr = idseccionusr;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando Casillas...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("idseccionusr",idseccionusr));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.DeleteSeccionesAsignadas);
            HttpResponse httpResponse = null;
            String responsedecoded = "";

            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                JSONArray jsonArray = new JSONArray(responsedecoded);
                if (responsedecoded.equals("[]")){
                    httpEntity.consumeContent();
                    return false;
                }else{
                    httpEntity.consumeContent();
                    if (jsonArray.getJSONObject(0).getString("EXITO").equals("SI")){
                        return true;
                    }
                }
                httpEntity.consumeContent();
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            if (aBoolean){
//                finish();
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                /*new secciones(idUSUARIO).execute();*/
            }else{
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Dar_seccion.this);
                builder.setMessage("Su solicitud no se ha realizado con exito!")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, null).show();
                builder.create();
            }
        }
    }


}
