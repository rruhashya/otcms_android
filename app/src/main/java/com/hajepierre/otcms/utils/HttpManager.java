package com.hajepierre.otcms.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jeanpierre on 4/30/18.
 */

public class HttpManager
{

    public static String getData(RequestPackage p)
    {

        BufferedReader reader = null;
        String uri = p.getUri();

        if (p.getMethod().equals("GET"))
        {
            if (!p.getEncodedParams().isEmpty())
            {
                uri += "?" + p.getEncodedParams();
            }
        }

        try
        {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod());

            if (con.getRequestMethod().equals("POST"))
            {
                JSONObject json = new JSONObject(p.getParams());
                con.setDoOutput(true);
                OutputStreamWriter write = new OutputStreamWriter(con.getOutputStream());
                //write.write(p.getEncodedParams());
                write.write(json.toString());
                write.flush();
            }

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            System.out.println(sb);

            return sb.toString();

        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }
}
