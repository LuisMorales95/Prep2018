package com.prep.prep2018;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

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
 * {@link Fragment_ActualizarUsuario.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_ActualizarUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_ActualizarUsuario extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static String id_usuario,Nombre,Usuario,Contrasenia,Activar,Admin,Grafica,Capturar;
    private EditText
            ActualizarUsuario_Nombre,
            ActualizarUsuario_Usuario,
            ActualizarUsuario_Contrasenia;
    private Switch
            ActualizarUsuario_Activar,
            ActualizarUsuario_Admin,
            ActualizarUsuario_Grafica,
            ActualizarUsuario_Capturar;
    private Button ActualizarUsuario_Actualizar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_ActualizarUsuario() {
    }


    // TODO: Rename and change types and number of parameters
    public static Fragment_ActualizarUsuario newInstance(String param1, String param2) {
        Fragment_ActualizarUsuario fragment = new Fragment_ActualizarUsuario();
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
        return inflater.inflate(R.layout.fragment_actualizar_usuario, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActualizarUsuario_Nombre = (EditText) view.findViewById(R.id.ActualizarUsuario_Nombre);
        ActualizarUsuario_Usuario = (EditText) view.findViewById(R.id.ActualizarUsuario_Usuario);
        ActualizarUsuario_Contrasenia = (EditText) view.findViewById(R.id.ActualizarUsuario_Contrasenia);
        ActualizarUsuario_Activar = (Switch) view.findViewById(R.id.ActualizarUsuario_Activar);
        ActualizarUsuario_Admin = (Switch) view.findViewById(R.id.ActualizarUsuario_Admin);
        ActualizarUsuario_Grafica = (Switch) view.findViewById(R.id.ActualizarUsuario_Grafica);
        ActualizarUsuario_Capturar = (Switch) view.findViewById(R.id.ActualizarUsuario_Capturar);
        ActualizarUsuario_Actualizar = (Button) view.findViewById(R.id.ActualizarUsuario_Actualizar);

        ActualizarUsuario_Nombre.setText(Nombre);
        ActualizarUsuario_Usuario.setText(Usuario);
        ActualizarUsuario_Contrasenia.setText(Contrasenia);
        if (Activar.equals("0")){ ActualizarUsuario_Activar.setChecked(false); }else{ ActualizarUsuario_Activar.setChecked(true); }
        if (Admin.equals("0")){ ActualizarUsuario_Admin.setChecked(false); }else{ ActualizarUsuario_Admin.setChecked(true); }
        if (Grafica.equals("0")){ ActualizarUsuario_Grafica.setChecked(false); }else{ ActualizarUsuario_Grafica.setChecked(true); }
        if (Capturar.equals("0")){ ActualizarUsuario_Capturar.setChecked(false); }else{ ActualizarUsuario_Capturar.setChecked(true); }
        ActualizarUsuario_Actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActualizarUsuario_Nombre.getText().toString().isEmpty()||ActualizarUsuario_Usuario.getText().toString().isEmpty()||ActualizarUsuario_Contrasenia.getText().toString().isEmpty()){
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setMessage("Llene los camppos de Nombre, Usuario, Contraseña")
                            .setTitle("Aviso")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, null).show();
                    builder.create();
                }else{
                    Activar = (ActualizarUsuario_Activar.isChecked()) ? "1" : "0";
                    Admin = (ActualizarUsuario_Admin.isChecked()) ? "1" : "0";
                    Grafica = (ActualizarUsuario_Grafica.isChecked()) ? "1" : "0";
                    Capturar = (ActualizarUsuario_Capturar.isChecked()) ? "1" : "0";

                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setMessage(
                            "Nombre: "+ActualizarUsuario_Nombre.getText().toString()+"\n"+
                            "Usuario: "+ActualizarUsuario_Usuario.getText().toString()+"\n"+
                            "Contraseña: "+ActualizarUsuario_Contrasenia.getText().toString()+"\n"+
                            "Cuenta: "+Activar+"\n"+
                            "Administrador: "+Admin+"\n"+
                            "Grafica: "+Grafica+"\n"+
                            "Capturar: "+Capturar+"\n")
                            .setTitle("Aviso")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new UpdateUsuario(id_usuario,ActualizarUsuario_Nombre.getText().toString(),ActualizarUsuario_Usuario.getText().toString(),ActualizarUsuario_Contrasenia.getText().toString(),Activar,Admin,Grafica,Capturar).execute();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null).show();
                    builder.create();

                }
            }
        });
    }

    private class UpdateUsuario extends AsyncTask<Void,Boolean,Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        String  update_idusuario, update_nombre, update_usuario, update_contra,
                update_Activo, update_Admin, update_Grafica, update_Capturar;
        public UpdateUsuario(String update_idusuario, String update_nombre, String update_usuario, String update_contra, String update_Activo, String update_Admin, String update_Grafica, String update_Capturar) {
            this.update_idusuario = update_idusuario;
            this.update_nombre = update_nombre;
            this.update_usuario = update_usuario;
            this.update_contra = update_contra;
            this.update_Activo = update_Activo;
            this.update_Admin = update_Admin;
            this.update_Grafica = update_Grafica;
            this.update_Capturar = update_Capturar;
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Actualizando Usuario...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {

            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("idusuario",update_idusuario));
            datatosend.add(new BasicNameValuePair("nombre",update_nombre));
            datatosend.add(new BasicNameValuePair("usuario",update_usuario));
            datatosend.add(new BasicNameValuePair("passwd",update_contra));
            datatosend.add(new BasicNameValuePair("activo",update_Activo));
            datatosend.add(new BasicNameValuePair("admin",update_Admin));
            datatosend.add(new BasicNameValuePair("grafica",update_Grafica));
            datatosend.add(new BasicNameValuePair("captura",update_Capturar));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.UpdateUsuarioInfo);
            HttpResponse httpResponse = null;
            String responsedecoded = "";
            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                JSONArray jsonArray = new JSONArray(responsedecoded);
                if (jsonArray.getJSONObject(0).getString("EXITO").equals("SI")){
                    httpEntity.consumeContent();
                    return true;
                }else{
                    httpEntity.consumeContent();
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("Error: ",responsedecoded);
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setMessage("Usuario Actualizado Exitosamente!")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.flContent,new Fragment_AsignarSeccion()).commit();
                            }
                        }).show();
                builder.create();
            }else{
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setMessage("Usuario no pudo ser actualizado!")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, null).show();
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
