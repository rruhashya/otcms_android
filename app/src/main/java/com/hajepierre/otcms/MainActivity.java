package com.hajepierre.otcms;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.hajepierre.otcms.db.Border;
import com.hajepierre.otcms.db.DBAdapter;
import com.hajepierre.otcms.db.Issue;
import com.hajepierre.otcms.db.IssueCategory;
import com.hajepierre.otcms.db.Profile;
import com.hajepierre.otcms.db.RemoteData;
import com.hajepierre.otcms.utils.RemoteService;
import com.hajepierre.otcms.utils.Synch;
import com.hajepierre.otcms.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    private DBAdapter dbAdapter;

    private Synch synch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        synch = new Synch();
        synch.setDbAdapter(dbAdapter);
        synch.setRequestQueue(requestQueue);

        RemoteData language = dbAdapter.findLanguage();
        if (language != null)
        {
            Utils.setNewLocale(this, language.getName());
        }

        if (Utils.isNetworkAvailable(this))
        {
            synch.doSynch();
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        goHome();

    }

    private void launchFragment(Object fragment)
    {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, (Fragment) fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (item.getItemId() == R.id.action_settings)
        {
            LanguagesFragment languagesFragment = new LanguagesFragment();
            launchFragment(languagesFragment);
        }

        if (item.getItemId() == R.id.action_back_home)
        {
            goHome();
        }

        if (item.getItemId() == R.id.action_download)
        {
            if (Utils.isNetworkAvailable(this))
            {
                synch.updateData();
                Toast.makeText(this,
                        "Downloading data, you may proceed with other operation (in background process)",
                        Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,
                        "Internet Connection is required for you to be able update complaint related data",
                        Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void goHome()
    {

        Profile profile = dbAdapter.findProfile();
        System.out.println(profile);

        if (profile != null)
        {
            ComplaintFormFragment complaintFormFragment = new ComplaintFormFragment();
            complaintFormFragment.setDbAdapter(dbAdapter);
            launchFragment(complaintFormFragment);
        }
        else
        {
            CheckFragment checkFragment = new CheckFragment();
            checkFragment.setDbAdapter(dbAdapter);
            launchFragment(checkFragment);
        }
    }

}
