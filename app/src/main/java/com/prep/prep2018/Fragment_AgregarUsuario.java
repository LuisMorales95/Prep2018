package com.prep.prep2018;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_AgregarUsuario.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_AgregarUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AgregarUsuario extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String Activar,Administrador,Grafica,Capturar;
    static int updateopcion=0;
    // TODO:CAMPOS
    private TextView Registar_titulo;
    private EditText Registar_Nombre,Registar_Usuario,Registar_Contrasenia;
    private Switch Registar_SActivar,Registar_SAdministrador,Registar_SGraficas,Registar_SCapturar;
    private Button Registar_CrearUsuario;


    private OnFragmentInteractionListener mListener;
    public Fragment_AgregarUsuario() {}
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_AgregarUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_AgregarUsuario newInstance(String param1, String param2) {
        Fragment_AgregarUsuario fragment = new Fragment_AgregarUsuario();
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
        return inflater.inflate(R.layout.fragment_agregar_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (updateopcion==2){
            Registar_titulo.setText("Actualizar Usuario");
        }
        Registar_titulo = (TextView) view.findViewById(R.id.Registar_titulo);

        Registar_Nombre = (EditText) view.findViewById(R.id.Registar_Nombre);
        Registar_Nombre.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        Registar_Usuario = (EditText) view.findViewById(R.id.Registar_Usuario);

        Registar_Contrasenia = (EditText) view.findViewById(R.id.Registar_Contrasenia);
        Registar_SActivar = (Switch) view.findViewById(R.id.Registar_SActivar);
        Registar_SAdministrador = (Switch) view.findViewById(R.id.Registar_SAdministrador);
        Registar_SGraficas = (Switch) view.findViewById(R.id.Registar_SGraficas);
        Registar_SCapturar = (Switch) view.findViewById(R.id.Registar_SCapturar);
        Registar_CrearUsuario = (Button) view.findViewById(R.id.Registar_CrearUsuario);

        Registar_CrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Registar_Nombre.getText().toString().isEmpty() ||
                    Registar_Usuario.getText().toString().isEmpty() ||
                    Registar_Contrasenia.getText().toString().isEmpty()
                ){
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setMessage("Llene los camppos de Nombre, Usuario, Contraseña")
                            .setTitle("Aviso")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, null)
                            .setNegativeButton(android.R.string.cancel, null).show();
                    builder.create();
                }else{
                    Activar = (Registar_SActivar.isChecked()) ? "1" : "0";
                    Administrador = (Registar_SAdministrador.isChecked()) ? "1" : "0";
                    Grafica = (Registar_SGraficas.isChecked()) ? "1" : "0";
                    Capturar = (Registar_SCapturar.isChecked()) ? "1" : "0";

                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setMessage(
                            "Nombre: "+Registar_Nombre.getText().toString()+"\n"+
                            "Usuario: "+Registar_Usuario.getText().toString()+"\n"+
                            "Contraseña: "+Registar_Contrasenia.getText().toString()+"\n"+
                            "Cuenta: "+Activar+"\n"+
                            "Administrador: "+Administrador+"\n"+
                            "Grafica: "+Grafica+"\n"+
                            "Capturar: "+Capturar+"\n")
                            .setTitle("Aviso")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new insertar_usuario(
                                            Registar_Nombre.getText().toString(),
                                            Registar_Usuario.getText().toString(),
                                            Registar_Contrasenia.getText().toString(),
                                            Activar,Administrador,Grafica,Capturar).execute();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null).show();
                    builder.create();
                }
            }
        });

    }
    public class insertar_usuario extends AsyncTask<Void,Boolean,Boolean>{
        String
        insertar_nombre,
        insertar_usuario,
        insertar_contra,
        insertar_ACuenta,
        insertar_PerAdmin,
        insertar_VGrafica,
        insertar_CapturarInfo;

        public insertar_usuario(String insertar_nombre, String insertar_usuario, String insertar_contra, String insertar_ACuenta, String insertar_PerAdmin, String insertar_VGrafica, String insertar_CapturarInfo) {
            this.insertar_nombre = insertar_nombre;
            this.insertar_usuario = insertar_usuario;
            this.insertar_contra = insertar_contra;
            this.insertar_ACuenta = insertar_ACuenta;
            this.insertar_PerAdmin = insertar_PerAdmin;
            this.insertar_VGrafica = insertar_VGrafica;
            this.insertar_CapturarInfo = insertar_CapturarInfo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("nombre",insertar_nombre));
            datatosend.add(new BasicNameValuePair("usuario",insertar_usuario));
            datatosend.add(new BasicNameValuePair("contra",insertar_contra));

            datatosend.add(new BasicNameValuePair("ACuenta",insertar_ACuenta));
            datatosend.add(new BasicNameValuePair("PerAdmin",insertar_PerAdmin));
            datatosend.add(new BasicNameValuePair("VGrafica",insertar_VGrafica));
            datatosend.add(new BasicNameValuePair("CapturarInfo",insertar_CapturarInfo));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.InsertUsuario);
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
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setMessage("Usuario Ingresado Exitosamente!")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Fragment_AsignarSeccion fragment_asignarSeccion = new Fragment_AsignarSeccion();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent,fragment_asignarSeccion).addToBackStack(null)                   .commit();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null).show();
                builder.create();
            }else{
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setMessage("Usuario No pudo ser ingresado!")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, null)
                        .setNegativeButton(android.R.string.cancel, null).show();
                builder.create();
            }
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
