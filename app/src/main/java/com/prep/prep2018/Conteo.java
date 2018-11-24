package com.prep.prep2018;

public class Conteo {

    String Imagen;
    String Partido;
    String Voto;

    public Conteo(String imagen, String partido, String voto) {
        Imagen = imagen;
        Partido = partido;
        Voto = voto;
    }
    public Conteo(){

    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getPartido() {
        return Partido;
    }

    public void setPartido(String partido) {
        Partido = partido;
    }

    public String getVoto() {
        return Voto;
    }

    public void setVoto(String voto) {
        Voto = voto;
    }
}
