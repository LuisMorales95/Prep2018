package com.prep.prep2018;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HTTPPARAMS {

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++                        HTTPREQUEST TO SERVER TIMED

    public static HttpParams GETHTTPREQUESTPARAMS(){
        HttpParams httprequestparams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httprequestparams,1000*30);
        HttpConnectionParams.setSoTimeout(httprequestparams,1000*30);
        return httprequestparams;
    }
}
