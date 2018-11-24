package com.prep.prep2018;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_AsignarSeccion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_AsignarSeccion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AsignarSeccion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText AsignarSeccion_Buscar;
    private RecyclerView AsignarSeccion_RecyclerList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<usuarios> lista_de_usuario;

    private OnFragmentInteractionListener mListener;

    public Fragment_AsignarSeccion() {}


    // TODO: Rename and change types and number of parameters
    public static Fragment_AsignarSeccion newInstance(String param1, String param2) {
        Fragment_AsignarSeccion fragment = new Fragment_AsignarSeccion();
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
        return inflater.inflate(R.layout.fragment_asignar_seccion, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AsignarSeccion_Buscar = (EditText) view.findViewById(R.id.AsignarSeccion_Buscar);
        AsignarSeccion_Buscar.setFilters(new InputFilter[]{
                new InputFilter.AllCaps(),
        });
        AsignarSeccion_RecyclerList = (RecyclerView) view.findViewById(R.id.AsignarSeccion_RecyclerList);
        new llenar_lista_usuarios().execute();

    }





    public class llenar_lista_usuarios extends AsyncTask<Void,Boolean,Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lista_de_usuario = new ArrayList<usuarios>();
            progressDialog.setMessage("Cargando Usuarios");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectUsuarios);
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
                    for (int i = 0; i<jsonArray.length();i++){
                        usuarios usuarios = new usuarios();
                        usuarios.setIdusuarios(jsonArray.getJSONObject(i).getString("iAppID"));
                        usuarios.setNombre(jsonArray.getJSONObject(i).getString("iAppNombre"));
                        usuarios.setUsuario(jsonArray.getJSONObject(i).getString("iAppLogin"));
                        usuarios.setContrasenia(jsonArray.getJSONObject(i).getString("iAppPwd"));
                        usuarios.setActivo(jsonArray.getJSONObject(i).getString("iAppActivo"));
                        usuarios.setAdmin(jsonArray.getJSONObject(i).getString("iAppAdmin"));
                        usuarios.setCapturar(jsonArray.getJSONObject(i).getString("iAppCaptura"));
                        usuarios.setGrafica(jsonArray.getJSONObject(i).getString("iAppGrafica"));
                        lista_de_usuario.add(usuarios);
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
                AsignarSeccion_RecyclerList.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getActivity());
                AsignarSeccion_RecyclerList.setLayoutManager(layoutManager);
                adapter = new RecycleViewAdapter(lista_de_usuario);
                AsignarSeccion_RecyclerList.setAdapter(adapter);
                AsignarSeccion_Buscar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int textlength = s.length();
                        List<usuarios> listatemp = new ArrayList<usuarios>();
                        for (usuarios usuarioss:lista_de_usuario ){
                            if (textlength<=usuarioss.getNombre().length()){
                                if (usuarioss.getNombre().toUpperCase().contains(s.toString().toUpperCase())){
                                    listatemp.add(usuarioss);
                                }
                            }
                        }
                        adapter = new RecycleViewAdapter(listatemp);
                        AsignarSeccion_RecyclerList.setAdapter(adapter);
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }else{

            }
        }
    }

    private class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

        List<usuarios> usuariosList;

        public RecycleViewAdapter(List<usuarios> usuariosList) {
            this.usuariosList = usuariosList;
        }

        @NonNull
        @Override
        public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.asignarseccion_item, parent, false);

            ViewHolder vh = new ViewHolder(itemView);
            return vh;
        }
        public  class ViewHolder extends RecyclerView.ViewHolder {
            TextView AsignarSeccionItem_Nombre;
            TextView AsignarSeccionItem_Usuario;
            TextView AsignarSeccionItem_Contrasena;
            TextView AsignarSeccionItem_Activo;
            TextView AsignarSeccionItem_Admin;
            TextView AsignarSeccionItem_Grafica;
            TextView AsignarSeccionItem_Capturar;
            public ViewHolder(View view) {
                super(view);
                AsignarSeccionItem_Nombre = (TextView) view.findViewById(R.id.AsignarSeccionItem_Nombre);
                AsignarSeccionItem_Usuario = (TextView) view.findViewById(R.id.AsignarSeccionItem_Usuario);
                AsignarSeccionItem_Contrasena = (TextView) view.findViewById(R.id.AsignarSeccionItem_Contrasena);
                AsignarSeccionItem_Activo = (TextView) view.findViewById(R.id.AsignarSeccionItem_Activo);
                AsignarSeccionItem_Admin = (TextView) view.findViewById(R.id.AsignarSeccionItem_Admin);
                AsignarSeccionItem_Grafica = (TextView) view.findViewById(R.id.AsignarSeccionItem_Grafica);
                AsignarSeccionItem_Capturar = (TextView) view.findViewById(R.id.AsignarSeccionItem_Capturar);
            }
        }
        @Override
        public void onBindViewHolder(@NonNull final RecycleViewAdapter.ViewHolder holder, final int position) {
            holder.AsignarSeccionItem_Nombre.setText(usuariosList.get(position).getNombre());
            holder.AsignarSeccionItem_Usuario.setText("Usuario: "+usuariosList.get(position).getUsuario());
            holder.AsignarSeccionItem_Contrasena.setText("Contraseña: "+usuariosList.get(position).getContrasenia());
            holder.AsignarSeccionItem_Activo.setText("Activada: "+usuariosList.get(position).getActivo());
            holder.AsignarSeccionItem_Admin.setText("Administrador: "+usuariosList.get(position).getAdmin());
            holder.AsignarSeccionItem_Grafica.setText("Grafica: "+usuariosList.get(position).getGrafica());
            holder.AsignarSeccionItem_Capturar.setText("Captura: "+usuariosList.get(position).getCapturar());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),Dar_seccion.class));
                    Dar_seccion.idUSUARIO = usuariosList.get(position).getIdusuarios();
                    Dar_seccion.NombreUsuario = usuariosList.get(position).getNombre();
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Fragment_ActualizarUsuario.id_usuario = usuariosList.get(position).getIdusuarios();
                    Fragment_ActualizarUsuario.Nombre = usuariosList.get(position).getNombre();
                    Fragment_ActualizarUsuario.Usuario = usuariosList.get(position).getUsuario();
                    Fragment_ActualizarUsuario.Contrasenia = usuariosList.get(position).getContrasenia();
                    Fragment_ActualizarUsuario.Activar = usuariosList.get(position).getActivo();
                    Fragment_ActualizarUsuario.Admin = usuariosList.get(position).getAdmin();
                    Fragment_ActualizarUsuario.Grafica = usuariosList.get(position).getGrafica();
                    Fragment_ActualizarUsuario.Capturar = usuariosList.get(position).getCapturar();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.flContent,new Fragment_ActualizarUsuario()).commit();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return usuariosList.size();
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

    private class usuarios {
        String idusuarios,nombre,usuario,contrasenia,activo,admin,grafica,capturar;

        public usuarios() {
        }

        public usuarios(String idusuarios, String nombre, String usuario, String contrasenia, String activo, String admin, String grafica, String capturar) {
            this.idusuarios = idusuarios;
            this.nombre = nombre;
            this.usuario = usuario;
            this.contrasenia = contrasenia;
            this.activo = activo;
            this.admin = admin;
            this.grafica = grafica;
            this.capturar = capturar;
        }

        public String getIdusuarios() {
            return idusuarios;
        }

        public void setIdusuarios(String idusuarios) {
            this.idusuarios = idusuarios;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getContrasenia() {
            return contrasenia;
        }

        public void setContrasenia(String contrasenia) {
            this.contrasenia = contrasenia;
        }

        public String getActivo() {
            return activo;
        }

        public void setActivo(String activo) {
            this.activo = activo;
        }

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
        }

        public String getGrafica() {
            return grafica;
        }

        public void setGrafica(String grafica) {
            this.grafica = grafica;
        }

        public String getCapturar() {
            return capturar;
        }

        public void setCapturar(String capturar) {
            this.capturar = capturar;
        }
    }
}
