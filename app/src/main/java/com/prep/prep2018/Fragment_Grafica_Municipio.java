package com.prep.prep2018;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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
import java.util.Collections;
import java.util.List;

public class Fragment_Grafica_Municipio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner Grafica_Municipio_Municipio;
    private List<MUNICIPIOS> municipiosList;
    private String[] idMunicipios;
    private String[] nomMunicipios;
    private Spinner Grafica_Municipio_Eleccion;
    private List<ELECCIONES> eleccionesList;
    private String[] Eleccionesid,EleccionesNombre;

    private String [] candidatos = {
            "pan", "prd", "mc",
            "morena","pt","pes",
            "pri","verde", "na",
            "independiente",
            "nulos"
//            "ave",
//            "cardenista",
//            "alianzapri", "alianzapan",
//             "pup", "psd", "prm",

    };
    private String [] nombre_alianza = {
            "Alianza-PAN",
            "Alianza-Morena",
            "Alianza-PRI",
            "Nueva Alianza",
            "Independiente",
            "Votos Nulos"
    };
    private HorizontalBarChart Grafica_Municipio_Grafica;
    private int onload = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_Grafica_Municipio() {
    }
// TODO: Rename and change types and number of parameters
    public static Fragment_Grafica_Municipio newInstance(String param1, String param2) {
        Fragment_Grafica_Municipio fragment = new Fragment_Grafica_Municipio();
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
        return inflater.inflate(R.layout.fragment_grafica_municipio, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Grafica_Municipio_Municipio = (Spinner) view.findViewById(R.id.Grafica_Municipio_Municipio);
        Grafica_Municipio_Eleccion = (Spinner) view.findViewById(R.id.Grafica_Municipio_Eleccion);
        Grafica_Municipio_Grafica = (HorizontalBarChart) view.findViewById(R.id.Grafica_Municipio_Grafica);
        Grafica_Municipio_Municipio.setClickable(false);
        Grafica_Municipio_Eleccion.setClickable(false);

        Grafica_Municipio_Municipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (onload==1){
                    new llenar_eleccion(idMunicipios[position],Grafica_Municipio_Eleccion).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Grafica_Municipio_Eleccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (onload==1){
                    new graficar_votos(
                            idMunicipios[Grafica_Municipio_Municipio.getSelectedItemPosition()],
                            Eleccionesid[position]
                    ).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new llenar_municipio().execute();
        onload=1;

    }



    private class llenar_municipio extends AsyncTask<Void,Boolean,Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando Municipios...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectMunicipio);
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

                    municipiosList = new ArrayList<MUNICIPIOS>();
                    for (int i = 0; i < jsonArray.length(); i++){
                        MUNICIPIOS municipios = new MUNICIPIOS();
                        municipios.setIdMunicipios(jsonArray.getJSONObject(i).getString("idmunicipio"));
                        municipios.setNombreMunicipio(jsonArray.getJSONObject(i).getString("municipio"));
                        municipiosList.add(municipios);
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
                Grafica_Municipio_Municipio.setClickable(true);
                idMunicipios = new String[municipiosList.size()];
                nomMunicipios = new String[municipiosList.size()];
                for (int i=0; i<municipiosList.size(); i++){
                    idMunicipios[i] = municipiosList.get(i).getIdMunicipios();
                    nomMunicipios[i] = municipiosList.get(i).getNombreMunicipio();
                }
                fillMunicipios(nomMunicipios,Grafica_Municipio_Municipio);

                new llenar_eleccion(idMunicipios[Grafica_Municipio_Municipio.getSelectedItemPosition()],Grafica_Municipio_Eleccion).execute();

            }else{

            }
            super.onPostExecute(aBoolean);
        }
    }
    private void fillMunicipios(String [] filled_list, Spinner Spinner){
        ArrayAdapter<CharSequence> adapters = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, filled_list);
        adapters.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        Spinner.setAdapter(adapters);
    }









    private class llenar_eleccion extends AsyncTask<Void,Boolean,Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        String municipio_selected_id;
        Spinner elecspinner;

        public llenar_eleccion(String municipio_selected_id, Spinner elecspinner) {
            this.municipio_selected_id = municipio_selected_id;
            this.elecspinner = elecspinner;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando Eleccion...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("idMunicipio",municipio_selected_id));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectMEleccion);
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
                    String nombre="";
                    eleccionesList = new ArrayList<ELECCIONES>();
                    for (int i = 0; i < jsonArray.length(); i++){
                        if (!nombre.equals(jsonArray.getJSONObject(i).getString("eleccion"))){
                            nombre =jsonArray.getJSONObject(i).getString("eleccion");
                            ELECCIONES eleccion = new ELECCIONES();

                            eleccion.setIdELECCIONES(jsonArray.getJSONObject(i).getString("idtipoeleccion"));
                            eleccion.setNombreELECCIONES(jsonArray.getJSONObject(i).getString("eleccion"));
                            eleccionesList.add(eleccion);
                        }
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
                Grafica_Municipio_Eleccion.setClickable(true);
                Eleccionesid = new String[eleccionesList.size()];
                EleccionesNombre = new String[eleccionesList.size()];
                for (int i=0; i<eleccionesList.size(); i++){
                    Eleccionesid[i] = eleccionesList.get(i).getIdELECCIONES();
                    EleccionesNombre[i] = eleccionesList.get(i).getNombreELECCIONES();
                }
                fillELeccion(EleccionesNombre,elecspinner);
                new graficar_votos(
                        idMunicipios[Grafica_Municipio_Municipio.getSelectedItemPosition()],
                        Eleccionesid[Grafica_Municipio_Eleccion.getSelectedItemPosition()]
                ).execute();
            }else{

                Grafica_Municipio_Eleccion.setClickable(false);
                String [] vacio = {"Sin ELecciones Asignadas"};
                fillELeccion(vacio,elecspinner);

            }
            super.onPostExecute(aBoolean);
        }
    }
    private void fillELeccion(String [] filled_list, Spinner Spinner){
        ArrayAdapter<CharSequence> adapters = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, filled_list);
        adapters.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        Spinner.setAdapter(adapters);
    }














    private class graficar_votos extends AsyncTask<Void,Boolean,Boolean>{
        String positionMunicipio;
        String positionEleccion;
        int AlianzaPAN=0,AlianzaMORENA=0,AlianzaPRI=0,Nueva_Alianza=0,independiente=0,Nulo=0;

        public graficar_votos(String positionMunicipio, String positionEleccion) {
            this.positionMunicipio = positionMunicipio;
            this.positionEleccion = positionEleccion;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("idmunicipio",positionMunicipio));
            datatosend.add(new BasicNameValuePair("idtipoEle",positionEleccion));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.ContarVotosEleccion);
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

                    for (int i=0;i<jsonArray.length();i++){
                        for (int l=0;l<candidatos.length;l++){
                            if (positionEleccion.equals("4")){
                                if (l<=2){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        AlianzaPAN += 0;
                                    }else{
                                        AlianzaPAN += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }else if (l<=5){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        AlianzaMORENA += 0;
                                    }else{
                                        AlianzaMORENA += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }else if (l<=7){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        AlianzaPRI += 0;
                                    }else {
                                        AlianzaPRI += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }else if (l<=8){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        Nueva_Alianza += 0;
                                    }else {
                                        Nueva_Alianza += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }else if (l<=9){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        independiente += 0;
                                    }else{
                                        independiente += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }else if (l>9){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        Nulo += 0;
                                    }else{
                                        Nulo += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }
                            }else{
                                if (l<=2){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        AlianzaPAN += 0;
                                    }else{
                                        AlianzaPAN += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }else if (l<=5){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        AlianzaMORENA += 0;
                                    }else{
                                        AlianzaMORENA += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }else if (l<=8){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        AlianzaPRI += 0;
                                    }else {
                                        AlianzaPRI += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }else if (l<=9){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        independiente += 0;
                                    }else{
                                        independiente += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }else if (l>9){
                                    if (jsonArray.getJSONObject(i).getString(candidatos[l]).equals("null")){
                                        Nulo += 0;
                                    }else{
                                        Nulo += Integer.valueOf(jsonArray.getJSONObject(i).getString(candidatos[l]));
                                    }
                                }
                            }
                        }
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
            super.onPostExecute(aBoolean);
            if (aBoolean){
                ArrayList<Integer> colors = new ArrayList<Integer>();
                colors.add(ContextCompat.getColor(getContext(), R.color.color18));
                colors.add(ContextCompat.getColor(getContext(),R.color.color12));
                if (positionEleccion.equals("4")){
                    colors.add(ContextCompat.getColor(getContext(), R.color.color9));
                }
                colors.add(ContextCompat.getColor(getContext(), R.color.color14));
                colors.add(ContextCompat.getColor(getContext(), R.color.color2));
                colors.add(ContextCompat.getColor(getContext(), R.color.color5));


                ArrayList<Float> array_drawGraph = new ArrayList<Float>();
                array_drawGraph.add((float) Nulo);
                array_drawGraph.add((float) independiente);
                if (positionEleccion.equals("4")){
                    array_drawGraph.add((float) Nueva_Alianza);
                }
                array_drawGraph.add((float) AlianzaPRI);
                array_drawGraph.add((float) AlianzaMORENA);
                array_drawGraph.add((float) AlianzaPAN);

                ArrayList<String> array_Nombre = new ArrayList<String>();
                array_Nombre.add(nombre_alianza[5]);
                array_Nombre.add(nombre_alianza[4]);
                if (positionEleccion.equals("4")){
                    array_Nombre.add(nombre_alianza[3]);
                }
                array_Nombre.add(nombre_alianza[2]);
                array_Nombre.add(nombre_alianza[1]);
                array_Nombre.add(nombre_alianza[0]);

                ArrayList<BarEntry> entriess = new ArrayList<BarEntry>();
                // count is the number of values you need to display into graph
                for (int i=0; i<array_drawGraph.size(); i++) {
                    entriess.add(new BarEntry(Float.valueOf(String.valueOf(i)),array_drawGraph.get(i),array_Nombre.get(i)));
                }


                BarDataSet set = new BarDataSet(entriess,"");
                set.setColors(colors);

                BarData data = new BarData(set);
                data.setBarWidth(0.9f); // set custom bar width

                Legend l = Grafica_Municipio_Grafica.getLegend();


                l.setWordWrapEnabled(true);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);
                List<LegendEntry> legendEntryList = new ArrayList<LegendEntry>();
                for (int i = 0; i< array_Nombre.size();i++){
                    legendEntryList.add(new LegendEntry(array_Nombre.get(i), Legend.LegendForm.CIRCLE,14f,10f, null,colors.get(i)));
                }
                Collections.reverse(legendEntryList);
                l.setCustom(legendEntryList);
                l.setTextColor(Color.BLACK);
                l.setEnabled(true);
                Description description = new Description();
                description.setText("Prep-2018");
                Grafica_Municipio_Grafica.setDescription(description);
                Grafica_Municipio_Grafica.animateY(5000);
                Grafica_Municipio_Grafica.setData(data);
                Grafica_Municipio_Grafica.setFitBars(true); // make the x-axis fit exactly all bars
                Grafica_Municipio_Grafica.invalidate(); // refresh
                Grafica_Municipio_Grafica.notifyDataSetChanged();
            }else{

                Snackbar.make(getView(),"No ay Datos",Snackbar.LENGTH_LONG).show();

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
    private class MUNICIPIOS{
        String idMunicipios,NombreMunicipio;

        public MUNICIPIOS() {
        }

        public String getIdMunicipios() {
            return idMunicipios;
        }
        public void setIdMunicipios(String idMunicipios) {
            this.idMunicipios = idMunicipios;
        }
        public String getNombreMunicipio() {
            return NombreMunicipio;
        }
        public void setNombreMunicipio(String nombreMunicipio) {
            NombreMunicipio = nombreMunicipio;
        }
    }
    private class ELECCIONES{
        String idELECCIONES,NombreELECCIONES;

        public ELECCIONES() {
        }

        public String getIdELECCIONES() {
            return idELECCIONES;
        }

        public void setIdELECCIONES(String idELECCIONES) {
            this.idELECCIONES = idELECCIONES;
        }

        public String getNombreELECCIONES() {
            return NombreELECCIONES;
        }

        public void setNombreELECCIONES(String nombreELECCIONES) {
            NombreELECCIONES = nombreELECCIONES;
        }
    }
}
