package com.hajepierre.otcms;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.hajepierre.otcms.db.DBAdapter;
import com.hajepierre.otcms.db.Profile;
import com.hajepierre.otcms.utils.FragmentService;
import com.hajepierre.otcms.utils.RemoteService;
import com.hajepierre.otcms.utils.Synch;
import com.hajepierre.otcms.utils.UrlConfigs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckFragment extends Fragment
{
    private EditText input;

    private DBAdapter dbAdapter;

    private RequestQueue requestQueue;
    private String nationalId;

    private Synch synch;

    public CheckFragment()
    {

    }

    public DBAdapter getDbAdapter()
    {

        return dbAdapter;
    }

    public void setDbAdapter(DBAdapter dbAdapter)
    {

        this.dbAdapter = dbAdapter;
    }

    public RequestQueue getRequestQueue()
    {

        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue)
    {

        this.requestQueue = requestQueue;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_check, container, false);

        input = rootView.findViewById(R.id.nationId);

        if (dbAdapter == null)
        {
            dbAdapter = new DBAdapter(getActivity());
            dbAdapter.open();
        }
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        synch = new Synch();
        synch.setDbAdapter(dbAdapter);
        synch.setRequestQueue(requestQueue);

        Button btnCheck = rootView.findViewById(R.id.btnCheck);

        btnCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (!(input.getText().toString().isEmpty()))
                {
                    nationalId=input.getText().toString();
                    checkTrader(input.getText().toString());
                }
                else
                {
                    input.setError("This field is required");
                }
            }
        });

        return rootView;
    }

    public void checkTrader(final String nationalId)
    {

        try
        {

            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                    UrlConfigs.CHECK_TRADER_URL + "?psspport=" + nationalId, null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {

                            try
                            {
                                JSONObject result = response.getJSONObject(0);
                                Profile profile = new Profile();
                                profile.setId(1);
                                profile.setStatus(1);
                                profile.setNationalId(result.getString("passprt"));
                                String name = result.getString("names");
                                String names[] = name.split(" ");

                                if (names.length > 1)
                                {
                                    profile.setLastName(names[0]);
                                    profile.setFirstName(names[1]);
                                }
                                else
                                {
                                    profile.setFirstName(name);
                                }

                                profile.setNationality(result.getString("nationality"));

                                dbAdapter.insertProfile(profile);

                                ComplaintFormFragment mapFragment = new ComplaintFormFragment();
                                mapFragment.setDbAdapter(dbAdapter);
                                mapFragment.setRequestQueue(requestQueue);
                                FragmentService.launchFragment(mapFragment, getActivity());
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {

                    ProfileFragment mapFragment = new ProfileFragment();
                    mapFragment.setNationalIdValue(nationalId);
                    mapFragment.setDbAdapter(dbAdapter);
                    mapFragment.setRequestQueue(requestQueue);
                    FragmentService.launchFragment(mapFragment, getActivity());
                }
            });

            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception ex)
        {

        }
    }
}
