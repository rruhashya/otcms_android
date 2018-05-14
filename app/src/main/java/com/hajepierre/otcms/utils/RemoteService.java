package com.hajepierre.otcms.utils;

import java.util.Map;

/**
 * Created by jeanpierre on 5/5/18.
 */

public class RemoteService
{
    private final static String BASE_URL = "http://197.243.19.149/AndroPub2/Service2.asmx/";

    public static String getData(String url, Map<String, String> params, String method)
    {

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod(method);
        if (params != null)
        {
            System.out.println(requestPackage.getEncodedParams());
            requestPackage.setParams(params);
        }
        requestPackage.setUri(BASE_URL+url);
        return HttpManager.getData(requestPackage);

    }
}
