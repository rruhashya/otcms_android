package com.hajepierre.otcms.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Created by jeanpierre on 5/2/18.
 */

public class Utils
{
    public static boolean isNetworkAvailable(Context context)
    {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean validate(EditText input)
    {

        if (StringUtils.isBlank(input.getText().toString()))
        {
            input.setError("This field is required");
            return false;
        }
        return true;
    }

    public static boolean validateEmail(EditText input)
    {

        if (StringUtils.isNumeric(input.getText().toString()))
        {
            input.setError("This field should be all numeric");
            return false;
        }
        return true;
    }

    public static void setNewLocale(Context c, String language)
    {

        updateResources(c, language);
    }

    private static void updateResources(Context context, String language)
    {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
