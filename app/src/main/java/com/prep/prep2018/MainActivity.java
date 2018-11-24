package com.prep.prep2018;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        Fragment_Main.OnFragmentInteractionListener,
        Fragment_AgregarUsuario.OnFragmentInteractionListener,
        Fragment_AsignarSeccion.OnFragmentInteractionListener,
        Fragment_Grafica.OnFragmentInteractionListener,
        Fragment_Grafica_Eleccion.OnFragmentInteractionListener,
        Fragment_ActualizarUsuario.OnFragmentInteractionListener,
        Fragment_Grafica_Municipio.OnFragmentInteractionListener{

    FragmentManager fragmentManager = getSupportFragmentManager();
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState==null){
            Fragment fragment = null;
            Class fragmenteclass = null;
            fragmenteclass = Fragment_Main.class;
            try{
                fragment = (Fragment) fragmenteclass.newInstance();
            }catch (Exception e){
                e.printStackTrace();
            }
            fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();
            getSupportActionBar().setTitle("Subir Voto");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView Navheader_nombre = header.findViewById(R.id.NavHeader_nombre);
        TextView Navheader_Usuario = header.findViewById(R.id.NavHeader_Usuario);
        SharedPreferences preferences = Data.preferences;
        Navheader_nombre.setText(preferences.getString(Data.iAppNombre,""));
//        Navheader_Usuario.setText(preferences.getString(Data.iAppLogin,""));
        Navheader_Usuario.setText("Prep 2018");
        navigationView.setNavigationItemSelectedListener(this);
        ocultar_campos();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(!(getSupportFragmentManager().findFragmentById(R.id.flContent) instanceof Fragment_Main))
            {
                Fragment_Main fragment_main = new Fragment_Main();
                getSupportActionBar().setTitle("Subir Voto");
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment_main, fragment_main.getClass().getName()).commit();

            }else {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setMessage("Seguro que desea salir de la aplicación?")
                        .setTitle("Aviso")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        }).setNegativeButton(android.R.string.cancel,null).show();
                builder.create();
//                super.onBackPressed();
            }
//            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = null;
                Class fragmenteclass = null;

                if (id == R.id.Subir_voto) {

                    fragmenteclass = Fragment_Main.class;
                    getSupportActionBar().setTitle(item.getTitle());

                }else if(id == R.id.Graficar) {

                    fragmenteclass = Fragment_Grafica.class;
                    getSupportActionBar().setTitle(item.getTitle());

                }else if (id == R.id.Graficar_eleccion) {

                    fragmenteclass = Fragment_Grafica_Eleccion.class;
                    getSupportActionBar().setTitle(item.getTitle());

                }else if (id == R.id.Graficar_Municipio) {

                    fragmenteclass = Fragment_Grafica_Municipio.class;
                    getSupportActionBar().setTitle(item.getTitle());

                } else if (id == R.id.Agregar_usuario) {
                    fragmenteclass = Fragment_AgregarUsuario.class;
                    getSupportActionBar().setTitle(item.getTitle());
                } else if (id == R.id.Asignar_seccion) {
                    fragmenteclass = Fragment_AsignarSeccion.class;
                    getSupportActionBar().setTitle(item.getTitle());
                } else if (id == R.id.salir) {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Esta seguro de querer Cerrar Sesión")
                            .setTitle("Advertencia")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = Data.preferences.edit();
                                    editor.putString(Data.iAppID,"");
                                    editor.putString(Data.iAppLogin,"");
                                    editor.putString(Data.iAppNombre,"");
                                    editor.putString(Data.iAppPwd,"");
                                    editor.putString(Data.iAppActivo,"");
                                    editor.putString(Data.iAppAdmin,"");
                                    editor.putString(Data.iAppMobil,"");
                                    editor.putString(Data.iAppCaptura,"");
                                    editor.putString(Data.iAppGrafica,"");
                                    editor.putString(Data.iAppUserMobil,"");
                                    editor.apply();
                                    startActivity(new Intent(MainActivity.this,Login.class));
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel,null).show();
                    builder.create();
                }
                if (fragmenteclass!=null){
                    try{
                        fragment = (Fragment) fragmenteclass.newInstance();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();

                }
            }
        },450);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void ocultar_campos(){
        if (Data.preferences.getString(Data.iAppGrafica,"").equals("0")){
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(false);
        }else{
            navigationView.getMenu().getItem(1).setVisible(true);
            navigationView.getMenu().getItem(2).setVisible(true);
            navigationView.getMenu().getItem(3).setVisible(true);

        }
        if (Data.preferences.getString(Data.iAppAdmin,"").equals("0")){
            navigationView.getMenu().getItem(4).setVisible(false);
        }else{
            navigationView.getMenu().getItem(4).setVisible(true);

        }
    }
}
