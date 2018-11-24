package com.prep.prep2018;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class Fragment_Main extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner Main_Seccion;
    private String [] fill_idseccion,fill_seccion;
    private ArrayAdapter<CharSequence> valores_seccion;

    private Spinner Main_Casilla;
    private String [] fill_CasillaId,fill_CasillaTipo;
    private ArrayAdapter<CharSequence> valores_casilla;

    private Spinner Main_TipoEleccion;
    private String [] fill_TipoEleccionId,fill_TipoEleccion;
    private ArrayAdapter<CharSequence> valores_TipoEleccion;

    private String [] candidatos = {"pan", "pri", "prd",
            "verde", "pt", "mc", "na",
//            "ave",
//            "cardenista",
            "morena",
            "pes",
//            "alianzapri",
//            "alianzapan",
            "independiente",
//            "pup",
//            "psd",
//            "prm",
            "nulos"
    };
    private int [] ImgPartidos = {
            R.drawable.pan1,
            R.drawable.pri2,
            R.drawable.prd3,
            R.drawable.verde4,
            R.drawable.pt5,
            R.drawable.mc6,
            R.drawable.na7,
            R.drawable.morena9,
            R.drawable.pes19,
//            R.drawable.pup15,
//            R.drawable.psd16,
//            R.drawable.pmr17,
            R.drawable.independiente13,
            R.drawable.votonulo14
    };
    private String [] Images = {
            Data.Server_Address+Data.carpeta_imagen+"1pan.png",
            Data.Server_Address+Data.carpeta_imagen+"2pri.png",
            Data.Server_Address+Data.carpeta_imagen+"3prd.png",
            Data.Server_Address+Data.carpeta_imagen+"4verde.png",
            Data.Server_Address+Data.carpeta_imagen+"5pt.png",
            Data.Server_Address+Data.carpeta_imagen+"6mc.jpg",
            Data.Server_Address+Data.carpeta_imagen+"7na.png",
//            Data.Server_Address+Data.carpeta_imagen+"8ave.jpg",
//            Data.Server_Address+Data.carpeta_imagen+"8cardenista.png",
            Data.Server_Address+Data.carpeta_imagen+"9morena.jpg",
            Data.Server_Address+Data.carpeta_imagen+"10pes.png",
//            Data.Server_Address+Data.carpeta_imagen+"11alianzapri.jpg",
//            Data.Server_Address+Data.carpeta_imagen+"12alianzapan.jpg",
            Data.Server_Address+Data.carpeta_imagen+"13independiente.jpg",
//            Data.Server_Address+Data.carpeta_imagen+"15pup.jpg",
//            Data.Server_Address+Data.carpeta_imagen+"16psd.jpg",
//            Data.Server_Address+Data.carpeta_imagen+"17pmr.jpg",
            Data.Server_Address+Data.carpeta_imagen+"14votonulo.jpg"
    };

    private List<String> columna;
    private List<String> valor_columna;

    private ListView  Main_Lista;
    private List<Conteo> listaVotos;
    private List<Conteo> temp_listaVotos;
    private RecyclerView Main_ListaR;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ImageView Main_Evidencia;
//    TODO: Image Managment
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMARA_WRITE_PER=0;
    String mCurrentPhotoPath;
    File image;
    OutputStream outputStream = null;


    Button Main_Enviar;

    private OnFragmentInteractionListener mListener;
    public Fragment_Main() {
    }
    // TODO: Rename and change types and number of parameters
    public static Fragment_Main newInstance(String param1, String param2) {
        Fragment_Main fragment = new Fragment_Main();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Main_Seccion = (Spinner) view.findViewById(R.id.Main_Seccion);
        Main_Casilla = (Spinner) view.findViewById(R.id.Main_Casilla);
        Main_TipoEleccion = (Spinner) view.findViewById(R.id.Main_TipoEleccion);
        Main_Lista = (ListView) view.findViewById(R.id.Main_Lista);
        Main_ListaR = (RecyclerView) view.findViewById(R.id.Main_ListaR);
        Main_Evidencia = (ImageView) view.findViewById(R.id.Main_Evidencia);

        Main_Seccion.setClickable(false);
        Main_Casilla.setClickable(false);
        Main_TipoEleccion.setClickable(false);

        new Fill_Seccion(Main_Seccion, Data.preferences.getString(Data.iAppID,"")).execute();
        Main_Seccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String idseccion = fill_idseccion[position];
                new Fill_TipoCasilla(Main_Casilla,idseccion).execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Main_Casilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String idcasilla = fill_CasillaId[position];
                new Fill_TipoEleccion(Main_TipoEleccion,idcasilla).execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Main_TipoEleccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int seccion = Main_Seccion.getSelectedItemPosition();
                int casilla = Main_Casilla.getSelectedItemPosition();
                int eleccion = Main_TipoEleccion.getSelectedItemPosition();
                String secvalor = fill_idseccion[seccion];
                String casvalor = fill_CasillaId[casilla];
                String elevalor = fill_TipoEleccionId[eleccion];
                new List_Voto(secvalor,casvalor,elevalor).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Main_Enviar = (Button) view.findViewById(R.id.Main_Enviar);
        Main_Enviar.setClickable(false);
        Main_Evidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT).show();
                if (Main_Evidencia.getDrawable()==null || Main_Evidencia.getDrawable().toString().equals(Data.EvidenciaBitmapString)){
                    Toast.makeText(getContext(),"Obteniendo Camara",Toast.LENGTH_SHORT).show();
                    getCamara();
                }else{
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setMessage("Desea recapturar la imagen?")
                            .setTitle("Aviso")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(),"Obteniendo Camara",Toast.LENGTH_SHORT).show();
                                    getCamara();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null).show();
                    builder.create();
                }

            }
        });

    }


    private void getCamara(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },REQUEST_CAMARA_WRITE_PER);
        }else  {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(getContext(),"com.prep.android.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP){
                    takePictureIntent.setClipData(ClipData.newRawUri("",photoURI));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException{
      String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      String imageFileName = "Prep2018_"+ timestamp +"_";
      File storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);

      image = File.createTempFile(imageFileName,".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
      return image;
    }

//  TODO: Aqui se comprime la imagen
    private void setPic() {
        //TODO: Get the dimensions of the view;
        int targetWidth = 550;
//                Main_Evidencia.getWidth();
        int targetHeight = 550;
//                Main_Evidencia.getHeight();
        //TODO: Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoWidth = bmOptions.outWidth;
        int photoheight = bmOptions.outHeight;

        //TODO: Determine how much to scale down the image
        int scaleFactor = Math.min(photoWidth / targetWidth, photoheight / targetHeight);

        //TODO: Decode the image file into a Bitmap sized to fill the view
        bmOptions.inJustDecodeBounds =false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable=true;

        try {
            int m_compress = 50;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
            outputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG,m_compress,outputStream);
            Main_Evidencia.setImageBitmap(bitmap);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Main_Evidencia.setImageBitmap(imageBitmap);
            setPic();
        }
        if (requestCode==0&&resultCode==RESULT_OK){
            dispatchTakePictureIntent();
        }
    }


    private class Fill_Seccion extends AsyncTask<Void,Boolean,Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        private Spinner MainSecciona;
        private String iAppId;
        public Fill_Seccion(Spinner mainSecciona, String iAppId) {
            MainSecciona = mainSecciona;
            this.iAppId = iAppId;
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando Seccion...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("iAppid",iAppId));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectSeccion);
            HttpResponse httpResponse = null;
            String responsedecoded = "";

            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                JSONArray jsonArray = new JSONArray(responsedecoded);
                fill_idseccion = new String[jsonArray.length()];
                fill_seccion = new String[jsonArray.length()];
                if (responsedecoded.equals("[]")){
                    fill_idseccion[0]="";
                    fill_seccion[0]="";
                    httpEntity.consumeContent();
                    return false;
                }else{
                    for (int i = 0; i<jsonArray.length();i++){
                        fill_idseccion[i] = jsonArray.getJSONObject(i).getString("idseccion");
                        fill_seccion[i] = jsonArray.getJSONObject(i).getString("seccion")+" - "+jsonArray.getJSONObject(i).getString("municipio");
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
            progressDialog.dismiss();
            if (aBoolean){
                MainSecciona.setClickable(true);
                fillseccion();
                final int item = MainSecciona.getSelectedItemPosition();
                String idSeccion = fill_idseccion[item];
                new Fill_TipoCasilla(Main_Casilla,idSeccion).execute();

            }else{
                MainSecciona.setClickable(false);
            }
        }
    }
    private void fillseccion(){
        valores_seccion = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_text,fill_seccion);
        valores_seccion.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        Main_Seccion.setAdapter(valores_seccion);
    }

    private class Fill_TipoCasilla extends AsyncTask<Void,Boolean,Boolean>{

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        private Spinner MainCasilla;
        private String iSeccion;

        public Fill_TipoCasilla(Spinner mainCasilla, String iSeccion) {
            MainCasilla = mainCasilla;
            this.iSeccion = iSeccion;
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
            datatosend.add(new BasicNameValuePair("iSeccion",iSeccion));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectCasilla);
            HttpResponse httpResponse = null;
            String responsedecoded = "";

            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                JSONArray jsonArray = new JSONArray(responsedecoded);
                fill_CasillaId = new String[jsonArray.length()];
                fill_CasillaTipo = new String[jsonArray.length()];
                if (responsedecoded.equals("[]")){
                    fill_CasillaId[0]="";
                    fill_CasillaTipo[0]="";
                    httpEntity.consumeContent();
                    return false;
                }else{
                    for (int i = 0; i<jsonArray.length();i++){
                        fill_CasillaId[i] = jsonArray.getJSONObject(i).getString("idtipo");
                        fill_CasillaTipo[i] = jsonArray.getJSONObject(i).getString("tipo");
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
            progressDialog.dismiss();
            if (aBoolean){
                MainCasilla.setClickable(true);
                FillCasillas();
                int position = MainCasilla.getSelectedItemPosition();
                String idCasilla = fill_CasillaId[position];
                 new Fill_TipoEleccion(Main_TipoEleccion,idCasilla).execute();
            }else{
                MainCasilla.setClickable(false);
            }
        }
    }
    private void FillCasillas(){
        valores_casilla = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_text,fill_CasillaTipo);
        valores_casilla.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        Main_Casilla.setAdapter(valores_casilla);
    }

    private class Fill_TipoEleccion extends AsyncTask<Void,Boolean,Boolean>{

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        private Spinner MainTipoEleccion;
        private String iCasilla;

        public Fill_TipoEleccion(Spinner mainTipoEleccion, String iCasilla) {
            MainTipoEleccion = mainTipoEleccion;
            this.iCasilla = iCasilla;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando Candidatura...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("iCasilla",iCasilla));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectTipoEleccion);
            HttpResponse httpResponse = null;
            String responsedecoded = "";

            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                JSONArray jsonArray = new JSONArray(responsedecoded);
                fill_TipoEleccionId = new String[jsonArray.length()];
                fill_TipoEleccion = new String[jsonArray.length()];
                if (responsedecoded.equals("[]")){
                    fill_TipoEleccionId[0]="";
                    fill_TipoEleccion[0]="";
                    httpEntity.consumeContent();
                    return false;
                }else{
                    for (int i = 0; i<jsonArray.length();i++){
                        fill_TipoEleccionId[i] = jsonArray.getJSONObject(i).getString("idtipoeleccion");
                        fill_TipoEleccion[i] = jsonArray.getJSONObject(i).getString("eleccion");
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
            progressDialog.dismiss();
            if (aBoolean){
                MainTipoEleccion.setClickable(true);
                FillTipoEleccion();
                int seccion = Main_Seccion.getSelectedItemPosition();
                int casilla = Main_Casilla.getSelectedItemPosition();
                int eleccion = Main_TipoEleccion.getSelectedItemPosition();
                String secvalor = fill_idseccion[seccion];
                String casvalor = fill_CasillaId[casilla];
                String elevalor = fill_TipoEleccionId[eleccion];
                new List_Voto(secvalor,casvalor,elevalor).execute();
            }else{
                MainTipoEleccion.setClickable(false);
            }
        }
    }
    private void FillTipoEleccion(){
        valores_TipoEleccion = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_text,fill_TipoEleccion);
        valores_TipoEleccion.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        Main_TipoEleccion.setAdapter(valores_TipoEleccion);
    }

//  TODO: AQUI SE PONE LA IMAGEN
    private class List_Voto extends AsyncTask<Void,Boolean,Boolean>{

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        String Seccion,Casilla,eleccion;
        String URLImagen;

        public List_Voto(String seccion, String casilla, String eleccion) {
            Seccion = seccion;
            Casilla = casilla;
            this.eleccion = eleccion;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando Votos...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            listaVotos = new ArrayList<Conteo>();
            temp_listaVotos = new ArrayList<Conteo>();

            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("eleccion",eleccion));
            datatosend.add(new BasicNameValuePair("seccion",Seccion));
            datatosend.add(new BasicNameValuePair("tipo",Casilla));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectVotos);
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
                    URLImagen = jsonArray.getJSONObject(0).getString("sabana");
                    for (int i=0; i<candidatos.length;i++){
                        Conteo conteo = new Conteo();
                        conteo.setPartido(candidatos[i]);
                        conteo.setVoto(jsonArray.getJSONObject(0).getString(candidatos[i]));
                        listaVotos.add(conteo);
                        Conteo temp_conteo = new Conteo();
                        temp_conteo.setPartido(candidatos[i]);
                        temp_conteo.setVoto(jsonArray.getJSONObject(0).getString(candidatos[i]));
                        temp_listaVotos.add(temp_conteo);
                    }
                    httpEntity.consumeContent();
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if (aBoolean){
                Main_ListaR.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(getActivity());
                Main_ListaR.setLayoutManager(layoutManager);
                adapter = new RecycleViewAdapter(temp_listaVotos);
                Main_ListaR.setAdapter(adapter);

                if (URLImagen.equals("null") || URLImagen.equals("")){
                    Data.EvidenciaBitmapString = "null";
                    Main_Evidencia.setImageDrawable(null);
                }else{
                    Picasso.get()
                        .load(Data.Server_Address+URLImagen)
                        .into(Main_Evidencia, new Callback() {
                            @Override
                            public void onSuccess() {
                                Data.EvidenciaBitmapString =  Main_Evidencia.getDrawable().toString();
                            }
                            @Override
                            public void onError(Exception e) {
                                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                                builder.setMessage("No se pudo cargar la imagen: "+e.toString())
                                        .setTitle("Error")
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.ok, null).show();
                                builder.create();
                            }
                        });
                    Main_Evidencia.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (Main_Evidencia.getDrawable()!=null){
                                FullEvidencia.URL = Data.Server_Address+URLImagen;
                                startActivity(new Intent(getContext(),FullEvidencia.class));
                            }
                            return false;
                        }
                    });
                }
                Main_Enviar.setClickable(true);
                Main_Enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Data.preferences.getString(Data.iAppCaptura,"").equals("0")){
                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                            builder.setMessage("No cuentas con la autorización para capturar datos")
                                    .setTitle("Aviso")
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.ok, null).show();
                            builder.create();
                        }else{
                            progressDialog.setMessage("Analizando datos...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            String analisis=analizando();
                            if (!analisis.equals("ninguno")){
                                progressDialog.dismiss();
                                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                                if (Main_Evidencia.getDrawable()!=null){
                                    if (Data.EvidenciaBitmapString.equals(Main_Evidencia.getDrawable().toString())){
                                        builder.setMessage("Desea actualizar los datos con misma Evidencia?")
                                                .setTitle("Misma Evidencia")
                                                .setCancelable(false)
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        new actualizar_datos().execute();
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.cancel, null).show();
                                        builder.create();
                                    }else{
                                        builder.setMessage(analisis)
                                                .setTitle("Estos datos se actualizaran junto con la imagen: ")
                                                .setCancelable(false)
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        int seccion = Main_Seccion.getSelectedItemPosition();
                                                        int casilla = Main_Casilla.getSelectedItemPosition();
                                                        int eleccion = Main_TipoEleccion.getSelectedItemPosition();
                                                        String secvalor = fill_idseccion[seccion];
                                                        String casvalor = fill_CasillaId[casilla];
                                                        String elevalor = fill_TipoEleccionId[eleccion];
                                                        Bitmap imagesubiendo = ((BitmapDrawable) Main_Evidencia.getDrawable()).getBitmap();
                                                        new subir_imagen(elevalor,secvalor,casvalor,imagesubiendo).execute();
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.cancel,null).show();
                                        builder.create();
                                    }
                                }else{
                                    builder.setMessage("Desea actualizar los datos sin Evidencia?")
                                            .setTitle("Evidencia Vacia")
                                            .setCancelable(false)
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    new actualizar_datos().execute();
                                                }
                                            })
                                            .setNegativeButton(android.R.string.cancel, null).show();
                                    builder.create();
                                }

                            }else{
                                progressDialog.dismiss();
                                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                                builder.setMessage("No ha realizado ningun cambio!")
                                        .setTitle("Aviso")
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .setNegativeButton(android.R.string.cancel, null).show();
                                builder.create();
                            }
                        }
                    }
                });
            }
        }
    }




    private class subir_imagen extends AsyncTask<Void,Boolean,Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        String eleccion, seccion, tipo;
        Bitmap bitmap_ImagenASubir;
        String responsedecoded = "";
        public subir_imagen(String eleccion, String seccion, String tipo, Bitmap bitmap_ImagenASubir) {
            this.eleccion = eleccion;
            this.seccion = seccion;
            this.tipo = tipo;
            this.bitmap_ImagenASubir = bitmap_ImagenASubir;
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Actualizando Evidencia");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap_ImagenASubir.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            String image64 =  Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("imagen",image64));
            datatosend.add(new BasicNameValuePair("eleccion",eleccion));
            datatosend.add(new BasicNameValuePair("seccion",seccion));
            datatosend.add(new BasicNameValuePair("tipo",tipo));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SubirImagen);
            HttpResponse httpResponse = null;


            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                JSONArray jsonArray = new JSONArray(responsedecoded);
                String Exito = jsonArray.getJSONObject(0).getString("EXITO");
                if (Exito.equals("SI")){
                    httpEntity.consumeContent();
                    return true;
                }else{
                    httpEntity.consumeContent();
                    return false;
                }
            }catch (Exception e){
                Log.e("Mensaje-server",responsedecoded);
                e.printStackTrace();
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            if (aBoolean){
                new actualizar_datos().execute();
            }else{
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setMessage("Subiendo Imagen: "+responsedecoded)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int seccion = Main_Seccion.getSelectedItemPosition();
                                int casilla = Main_Casilla.getSelectedItemPosition();
                                int eleccion = Main_TipoEleccion.getSelectedItemPosition();
                                String secvalor = fill_idseccion[seccion];
                                String casvalor = fill_CasillaId[casilla];
                                String elevalor = fill_TipoEleccionId[eleccion];
                                new List_Voto(secvalor,casvalor,elevalor).execute();
                            }
                        }).show();
                builder.create();
            }
        }
    }

    private class actualizar_datos extends AsyncTask<Void,Boolean,Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        int seccion = Main_Seccion.getSelectedItemPosition();
        int casilla = Main_Casilla.getSelectedItemPosition();
        int eleccion = Main_TipoEleccion.getSelectedItemPosition();
        String secvalor = fill_idseccion[seccion];
        String casvalor = fill_CasillaId[casilla];
        String elevalor = fill_TipoEleccionId[eleccion];

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Actualizando Datos");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
//            TODO: ** Actualizar todos las columnas **
            for (int i=0;i<columna.size();i++){
                ArrayList<NameValuePair> datatosend = new ArrayList<>();
                datatosend.add(new BasicNameValuePair("eleccion",elevalor));
                datatosend.add(new BasicNameValuePair("seccion",secvalor));
                datatosend.add(new BasicNameValuePair("tipo",casvalor));
                datatosend.add(new BasicNameValuePair("campo",columna.get(i)));
                datatosend.add(new BasicNameValuePair("valor",valor_columna.get(i)));
                HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(Data.Server_Address+Data.UpdateVotos);
                HttpResponse httpResponse = null;
                String responsedecoded = "";

                try {
                    post.setEntity(new UrlEncodedFormEntity(datatosend));
                    httpResponse = client.execute(post);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                    httpEntity.consumeContent();
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if (aBoolean){
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setMessage("Cambios realizados")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int seccion = Main_Seccion.getSelectedItemPosition();
                                int casilla = Main_Casilla.getSelectedItemPosition();
                                int eleccion = Main_TipoEleccion.getSelectedItemPosition();
                                String secvalor = fill_idseccion[seccion];
                                String casvalor = fill_CasillaId[casilla];
                                String elevalor = fill_TipoEleccionId[eleccion];
                                new List_Voto(secvalor,casvalor,elevalor).execute();
                            }
                        }).show();
                builder.create();
            }else{
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setMessage("Cambios no fueron realizados")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, null).show();
                builder.create();
            }
        }
    }

    private String analizando(){
        columna = new ArrayList<String>();
        valor_columna = new ArrayList<String>();
        String text="ninguno";
        int contador=0;
        if (listaVotos.size()==temp_listaVotos.size()){
            text="";
            text+="Valores:\n";
            for (int i = 0;i<listaVotos.size();i++){
                if (!listaVotos.get(i).getVoto().equals(temp_listaVotos.get(i).getVoto())){
                    contador++;
                    text+=" "+listaVotos.get(i).getPartido()+": "+listaVotos.get(i).getVoto()+" - "+ temp_listaVotos.get(i).getVoto()+"\n";
                    columna.add(listaVotos.get(i).getPartido());
                    valor_columna.add(temp_listaVotos.get(i).getVoto());
                }
            }
        }
        if (contador==0){
            return "ninguno";
        }else{
            return text;
        }
    }



    private class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

        private List<Conteo> conteo;

        public RecycleViewAdapter(List<Conteo> conteo) {
            this.conteo = conteo;
        }

        @NonNull
        @Override
        public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.votos, parent, false);

            ViewHolder vh = new ViewHolder(itemView);

            return vh;
        }
        public  class ViewHolder extends RecyclerView.ViewHolder {
            TextView Votos_candidatos;
            EditText Votos_cantidad;
            ImageView Votos_imagen;
            public ViewHolder(View view) {
                super(view);
                Votos_imagen = (ImageView) view.findViewById(R.id.Voto_imagen);
                Votos_candidatos = (TextView) view.findViewById(R.id.Voto_candidato);
                Votos_cantidad = (EditText)  view.findViewById(R.id.Voto_cantidad);
                Votos_cantidad.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        temp_listaVotos.get(getAdapterPosition()).setVoto(Votos_cantidad.getText().toString());
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        }
        @Override
        public void onBindViewHolder(@NonNull final RecycleViewAdapter.ViewHolder holder, final int position) {
            holder.Votos_candidatos.setText(temp_listaVotos.get(position).getPartido());
            holder.Votos_cantidad.setText(temp_listaVotos.get(position).getVoto());
            Glide.with(getActivity()).load(ImgPartidos[position]).into(holder.Votos_imagen);
        }
        @Override
        public int getItemCount() {
            return temp_listaVotos.size();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
