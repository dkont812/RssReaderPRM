package com.example.rssreaderprm.Common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPDataHanlder {

    static String stream="";


    public HTTPDataHanlder(){

    }

    public String GetHTTPDataHanlder(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();

                String line = "";
                while((line=br.readLine())!=null){
                    sb.append(line);
                    stream = sb.toString();
                    urlConnection.disconnect();
                }
            }
        }catch (Exception e){
            return null;
        }

        return stream;
    }
}
