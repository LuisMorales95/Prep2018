package com.prep.prep2018;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Grafica.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Grafica#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Grafica extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//    private PieChart mChart;
    HorizontalBarChart chart;

    private Spinner Grafica_DISTRITOS;
    private Spinner Grafica_Eleccion;
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

            // array of graph different colors
    ArrayList<Integer> colors = new ArrayList<Integer>();
    private String [] idseccion, nombreseccion;
    private List<SECCIONES> lista_seccion;
    private List<Eleccion> lista_eleccion;
    private String [] idtipoeleccion,nomeleccion;

    private OnFragmentInteractionListener mListener;

    public Fragment_Grafica() {}

    // TODO: Rename and change types and number of parameters
    public static Fragment_Grafica newInstance(String param1, String param2) {
        Fragment_Grafica fragment = new Fragment_Grafica();
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
        return inflater.inflate(R.layout.fragment_grafica, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chart = (HorizontalBarChart) view.findViewById(R.id.bar_chart);
//        mChart = (PieChart) view.findViewById(R.id.piechart);
        Grafica_DISTRITOS= (Spinner) view.findViewById(R.id.Grafica_DISTRITOS);
        Grafica_Eleccion = (Spinner) view.findViewById(R.id.Grafica_Eleccion);
        Grafica_DISTRITOS.setClickable(false);
        Grafica_Eleccion.setClickable(false);
        new llenar_distrito(Grafica_DISTRITOS).execute();
        Grafica_DISTRITOS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new EleccionesCorrespondientes(idseccion[position]).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Grafica_Eleccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new graficar_votos(
                        idseccion[Grafica_DISTRITOS.getSelectedItemPosition()],
                        idtipoeleccion[Grafica_Eleccion.getSelectedItemPosition()]
                ).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private class llenar_distrito extends AsyncTask<Void,Boolean,Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Spinner spinner_distrito;


        public llenar_distrito(Spinner spinner_distrito) {
            this.spinner_distrito = spinner_distrito;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando Distritos...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectAllSeccions);
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
                    String seccion="";
                    lista_seccion = new ArrayList<SECCIONES>();
                    for (int i = 0; i < jsonArray.length(); i++){
                        if (!seccion.equals(jsonArray.getJSONObject(i).getString("seccion"))){
                            seccion=jsonArray.getJSONObject(i).getString("seccion");
                            SECCIONES secciones = new SECCIONES();
                            secciones.setIdsecciones(jsonArray.getJSONObject(i).getString("idseccion"));
                            secciones.setNombreseccion(jsonArray.getJSONObject(i).getString("seccion")+" "+jsonArray.getJSONObject(i).getString("municipio"));
                            lista_seccion.add(secciones);
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
                Grafica_DISTRITOS.setClickable(true);
                idseccion = new String[lista_seccion.size()];
                nombreseccion = new String[lista_seccion.size()];
                for (int i = 0; i < lista_seccion.size(); i++){
                    idseccion[i]=lista_seccion.get(i).getIdsecciones();
                    nombreseccion[i]=lista_seccion.get(i).getNombreseccion();
                }
                fillSecciones(nombreseccion,Grafica_DISTRITOS);
                new EleccionesCorrespondientes(idseccion[Grafica_DISTRITOS.getSelectedItemPosition()]).execute();

            }else{

            }
            super.onPostExecute(aBoolean);
        }
    }
    private void fillSecciones(String [] filled_list, Spinner Spinner){
        ArrayAdapter<CharSequence> adapters = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, filled_list);
        adapters.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        Spinner.setAdapter(adapters);
    }





    private class EleccionesCorrespondientes extends AsyncTask<Void,Boolean,Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        String idseccionn;
        public EleccionesCorrespondientes(String idseccion) {
            this.idseccionn = idseccion;
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando Elecciones");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("idseccion",idseccionn));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectGEleccion);
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
                    String nomeleccion="";
                    lista_eleccion = new ArrayList<Eleccion>();
                    for (int i = 0; i < jsonArray.length(); i++){
                        if (!nomeleccion.equals(jsonArray.getJSONObject(i).getString("eleccion"))){
                            nomeleccion =jsonArray.getJSONObject(i).getString("eleccion");
                            Eleccion eleccion = new Eleccion();

                            eleccion.setIdeleccion(jsonArray.getJSONObject(i).getString("idtipoeleccion"));
                            eleccion.setNomeleccion(jsonArray.getJSONObject(i).getString("eleccion"));
                            lista_eleccion.add(eleccion);
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
                Grafica_Eleccion.setClickable(true);
                idtipoeleccion = new String[lista_eleccion.size()];
                nomeleccion = new String[lista_eleccion.size()];
                for (int i = 0; i < lista_eleccion.size(); i++){
                    idtipoeleccion[i]=lista_eleccion.get(i).getIdeleccion();
                    nomeleccion[i]=lista_eleccion.get(i).getNomeleccion();
                }
                filleleccions(nomeleccion,Grafica_Eleccion);

                new graficar_votos(
                        idseccion[Grafica_DISTRITOS.getSelectedItemPosition()],
                        idtipoeleccion[Grafica_Eleccion.getSelectedItemPosition()]
                ).execute();
            }else{

            }
            super.onPostExecute(aBoolean);
        }
    }
    private void filleleccions(String [] filled_list, Spinner Spinner){
        ArrayAdapter<CharSequence> adapters = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, filled_list);
        adapters.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        Spinner.setAdapter(adapters);
    }





    private class graficar_votos extends AsyncTask<Void,Boolean,Boolean>{
        String position_spinner_seccion;
        String position_spinner_eleccion;
        List<votos> votosList;
        int AlianzaPAN=0,AlianzaMORENA=0,AlianzaPRI=0,Nueva_Alianza=0,independiente=0,Nulo=0;

        public graficar_votos(String position_spinner_seccion, String position_spinner_eleccion) {
            this.position_spinner_seccion = position_spinner_seccion;
            this.position_spinner_eleccion = position_spinner_eleccion;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("secid",position_spinner_seccion));
            datatosend.add(new BasicNameValuePair("idtipo",position_spinner_eleccion));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.ContarVotosSeccion);
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
                    votosList = new ArrayList<votos>();
                    for (int i=0;i<jsonArray.length();i++){
                        for (int l=0;l<candidatos.length;l++){
                            if (position_spinner_eleccion.equals("4")){
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
                colors.add(ContextCompat.getColor(getContext(), R.color.color18));
                colors.add(ContextCompat.getColor(getContext(),R.color.color12));
                if (position_spinner_eleccion.equals("4")){
                    colors.add(ContextCompat.getColor(getContext(), R.color.color9));
                }
                colors.add(ContextCompat.getColor(getContext(), R.color.color14));
                colors.add(ContextCompat.getColor(getContext(), R.color.color2));
                colors.add(ContextCompat.getColor(getContext(), R.color.color5));


                ArrayList<Float> array_drawGraph = new ArrayList<Float>();
                array_drawGraph.add((float) Nulo);
                array_drawGraph.add((float) independiente);
                if (position_spinner_eleccion.equals("4")){
                    array_drawGraph.add((float) Nueva_Alianza);
                }
                array_drawGraph.add((float) AlianzaPRI);
                array_drawGraph.add((float) AlianzaMORENA);
                array_drawGraph.add((float) AlianzaPAN);

                ArrayList<String> array_Nombre = new ArrayList<String>();
                array_Nombre.add(nombre_alianza[5]);
                array_Nombre.add(nombre_alianza[4]);
                if (position_spinner_eleccion.equals("4")){
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

                Legend l = chart.getLegend();


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
                chart.setDescription(description);
                chart.animateY(5000);
                chart.setData(data);
                chart.setFitBars(true); // make the x-axis fit exactly all bars
                chart.invalidate(); // refresh
                chart.notifyDataSetChanged();
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

    private class SECCIONES{
        String idsecciones,nombreseccion;

        public SECCIONES() {
        }

        public String getIdsecciones() {
            return idsecciones;
        }

        public void setIdsecciones(String idsecciones) {
            this.idsecciones = idsecciones;
        }

        public String getNombreseccion() {
            return nombreseccion;
        }

        public void setNombreseccion(String nombreseccion) {
            this.nombreseccion = nombreseccion;
        }
    }

    private class Eleccion{
        String ideleccion;
        String nomeleccion;
        public Eleccion() {
        }
        public String getIdeleccion() {
            return ideleccion;
        }
        public void setIdeleccion(String ideleccion) {
            this.ideleccion = ideleccion;
        }
        public String getNomeleccion() {
            return nomeleccion;
        }
        public void setNomeleccion(String nomeleccion) {
            this.nomeleccion = nomeleccion;
        }
    }

    private class votos{
        String partido,canvoto;

        public votos() {
        }

        public String getPartido() {
            return partido;
        }

        public void setPartido(String partido) {
            this.partido = partido;
        }

        public String getCanvoto() {
            return canvoto;
        }

        public void setCanvoto(String canvoto) {
            this.canvoto = canvoto;
        }
    }
}
