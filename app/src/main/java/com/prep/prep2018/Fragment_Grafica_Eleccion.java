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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Grafica_Eleccion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Grafica_Eleccion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Grafica_Eleccion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner Grafica_Eleccion_Spinner;
    private List<Eleccion> EleccionList;
    private HorizontalBarChart Grafica_Eleccion_Barchart;
    private String [] candidatos = {
            "pan","prd","mc",
            "morena","pt","pes",
            "pri", "verde",  "na",
            "independiente",
            "nulos"
    };
    private String [] nombre_alianza = {
            "Alianza-PAN",
            "Alianza-Morena",
            "Alianza-PRI",
            "Nueva Alianza",
            "Independiente",
            "Votos Nulos"
    };
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;
    public Fragment_Grafica_Eleccion() {}
    // TODO: Rename and change types and number of parameters
    public static Fragment_Grafica_Eleccion newInstance(String param1, String param2) {
        Fragment_Grafica_Eleccion fragment = new Fragment_Grafica_Eleccion();
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
        return inflater.inflate(R.layout.fragment_grafica_eleccion, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Grafica_Eleccion_Spinner = (Spinner) view.findViewById(R.id.Grafica_Eleccion_Spinner);
        Grafica_Eleccion_Barchart = (HorizontalBarChart) view.findViewById(R.id.Grafica_Eleccion_Barchart);
        Grafica_Eleccion_Spinner.setClickable(false);
        new Fill_Eleccion(Grafica_Eleccion_Spinner).execute();
        Grafica_Eleccion_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new Contar_Graficar_Alianza(EleccionList.get(position).getIdtipoEleccion(),Grafica_Eleccion_Barchart).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class Fill_Eleccion extends AsyncTask<Void,Boolean,Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        private Spinner Eleccion_Spinner;
        public Fill_Eleccion(Spinner eleccion_Spinner) {
            Eleccion_Spinner = eleccion_Spinner;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Obteniendo Informacion");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.SelectEleccion);
            HttpResponse httpResponse = null;
            String responsedecoded = "";
            try {
                post.setEntity(new UrlEncodedFormEntity(datatosend));
                httpResponse = client.execute(post);
                HttpEntity httpEntity = httpResponse.getEntity();
                responsedecoded = EntityUtils.toString(httpEntity,"UTF-8");
                EleccionList = new ArrayList<Eleccion>();
                JSONArray jsonArray = new JSONArray(responsedecoded);
                if (responsedecoded.equals("[]")){
                    httpEntity.consumeContent();
                    return false;
                }else{
                    for (int i=0;i<jsonArray.length();i++){
                        Eleccion eleccion = new Eleccion();
                        eleccion.setIdtipoEleccion(jsonArray.getJSONObject(i).getString("idtipoeleccion"));
                        eleccion.setNomEleccion(jsonArray.getJSONObject(i).getString("eleccion"));
                        EleccionList.add(eleccion);
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
            progressDialog.dismiss();
            if (aBoolean){
                Eleccion_Spinner.setClickable(true);
                String [] NomEleccion = new String[EleccionList.size()];
                for (int i=0;i<EleccionList.size();i++){
                    NomEleccion[i]=EleccionList.get(i).getNomEleccion();
                }
                fillSecciones(NomEleccion,Eleccion_Spinner);
                new Contar_Graficar_Alianza(EleccionList.get(Eleccion_Spinner.getSelectedItemPosition()).getIdtipoEleccion(),Grafica_Eleccion_Barchart).execute();
            }
        }
    }
    private void fillSecciones(String [] filled_list, Spinner Spinner){
        ArrayAdapter<CharSequence> adapters = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, filled_list);
        adapters.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        Spinner.setAdapter(adapters);
    }

    private class Contar_Graficar_Alianza extends AsyncTask<Void,Boolean,Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        String idtipoe;
        HorizontalBarChart Eleccion_Barchart;

        int AlianzaPAN=0,AlianzaMORENA=0,AlianzaPRI=0,Nueva_Alianza=0,independiente=0,Nulo=0;

        public Contar_Graficar_Alianza(String idtipoe, HorizontalBarChart eleccion_Barchart) {
            this.idtipoe = idtipoe;
            Eleccion_Barchart = eleccion_Barchart;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Calculando datos...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<NameValuePair> datatosend = new ArrayList<>();
            datatosend.add(new BasicNameValuePair("tipoe",idtipoe));
            HttpParams httpRequestParams = HTTPPARAMS.GETHTTPREQUESTPARAMS();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Data.Server_Address+Data.ContarAlianza);
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
                            if (idtipoe.equals("4")){
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
            progressDialog.dismiss();
            super.onPostExecute(aBoolean);
            if (aBoolean){
                /*Toast.makeText(getContext(),String.valueOf(AlianzaPAN)+"-"+String.valueOf(AlianzaPRI)+"-"+String.valueOf(AlianzaMORENA)+"-"+String.valueOf(Nulo),Toast.LENGTH_LONG).show();*/

                ArrayList<Integer> colors = new ArrayList<Integer>();
                colors.add(ContextCompat.getColor(getContext(), R.color.color18));
                colors.add(ContextCompat.getColor(getContext(),R.color.color12));
                if (idtipoe.equals("4")){
                    colors.add(ContextCompat.getColor(getContext(), R.color.color9));
                }
                colors.add(ContextCompat.getColor(getContext(), R.color.color14));
                colors.add(ContextCompat.getColor(getContext(), R.color.color2));
                colors.add(ContextCompat.getColor(getContext(), R.color.color5));

                ArrayList<Float> array_drawGraph = new ArrayList<Float>();
                array_drawGraph.add((float) Nulo);
                array_drawGraph.add((float) independiente);
                if (idtipoe.equals("4")){
                    array_drawGraph.add((float) Nueva_Alianza);
                }
                array_drawGraph.add((float) AlianzaPRI);
                array_drawGraph.add((float) AlianzaMORENA);
                array_drawGraph.add((float) AlianzaPAN);

                ArrayList<String> array_Nombre = new ArrayList<String>();
                array_Nombre.add(nombre_alianza[5]);
                array_Nombre.add(nombre_alianza[4]);
                if (idtipoe.equals("4")){
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

                Legend l = Eleccion_Barchart.getLegend();


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
                Eleccion_Barchart.setDescription(description);
                Eleccion_Barchart.animateY(5000);
                Eleccion_Barchart.setData(data);
                Eleccion_Barchart.setFitBars(true); // make the x-axis fit exactly all bars
                Eleccion_Barchart.invalidate(); // refresh
                Eleccion_Barchart.notifyDataSetChanged();
            }else{
                Snackbar.make(getView(),"No ay Datos",Snackbar.LENGTH_LONG).show();
            }
        }
    }


























    private class Eleccion{
        String idtipoEleccion;
        String NomEleccion;

        public Eleccion() {
        }

        public String getIdtipoEleccion() {
            return idtipoEleccion;
        }
        public void setIdtipoEleccion(String idtipoEleccion) {
            this.idtipoEleccion = idtipoEleccion;
        }
        public String getNomEleccion() {
            return NomEleccion;
        }
        public void setNomEleccion(String nomEleccion) {
            NomEleccion = nomEleccion;
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
