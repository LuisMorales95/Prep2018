package com.prep.prep2018;


import android.content.Context;
import android.content.SharedPreferences;

public class Data {
    /*static String Server_Address = "http://192.168.137.1/Prep2018/";*/
    static String Server_Address = "http://prepmobil2018.siscorpcn.com/Prep2018/";
    static String carpeta_imagen = "partidos/";

    static String EvidenciaBitmapString = "null";


    static String Loginphp = "login.php";
    static String SelectSeccion = "SelectSeccion.php";
    static String SelectCasilla = "SelectCasilla.php";
    static String SelectTipoEleccion = "SelectTipoEleccion.php";
    static String SelectVotos = "SelectVotos.php";
    static String UpdateVotos = "UpdateVotos.php";
    static String SelectUsuarios = "SelectUsuarios.php";
    static String InsertUsuario = "InsertUsuario.php";
    static String SelectSeccionesLibres = "SelectSeccionesLibres.php";
    static String SelectSeccionesAsignadas = "SelectSeccionesAsignadas.php";
    static String InsertSeccionUsuarios = "InsertSeccionUsuarios.php";
    static String DeleteSeccionesAsignadas = "DeleteSeccionesAsignadas.php";
    static String SelectDistritos = "SelectDistritos.php";
    static String SelectAllSeccions = "SelectAllSeccions.php";
    static String ContarVotosSeccion = "ContarVotosSeccion.php";
    static String SelectEleccion = "SelectEleccion.php";
    static String ContarAlianza = "ContarAlianza.php";
    static String UpdateUsuarioInfo = "UpdateUsuarioInfo.php";
    static String SubirImagen = "SubirImagen.php";
    static String SelectGEleccion = "SelectGEleccion.php";
    static String SelectMunicipio = "SelectMunicipio.php";
    static String SelectMEleccion = "SelectMEleccion.php";
    static String ContarVotosEleccion = "ContarVotosEleccion.php";

    static SharedPreferences preferences;
    static SharedPreferences.Editor preferenceeditor;
    static String
            iAppID="iAppID",
            iAppLogin="iAppLogin",
            iAppNombre="iAppNombre",
            iAppPwd="iAppPwd",
            iAppActivo="iAppActivo",
            iAppAdmin="iAppAdmin",
            iAppMobil="iAppMobil",
            iAppCaptura="iAppCaptura",
            iAppGrafica="iAppGrafica",
            iAppUserMobil="iAppUserMobil";
    }
