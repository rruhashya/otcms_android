package com.hajepierre.otcms.utils;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hajepierre.otcms.db.Border;
import com.hajepierre.otcms.db.Complaint;
import com.hajepierre.otcms.db.DBAdapter;
import com.hajepierre.otcms.db.Issue;
import com.hajepierre.otcms.db.IssueCategory;
import com.hajepierre.otcms.db.Profile;
import com.hajepierre.otcms.db.RemoteData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by jeanpierre on 5/10/18.
 */

public class Synch
{
    private RequestQueue requestQueue;

    private DBAdapter dbAdapter;

    private final String BASE_URL = "http://197.243.19.149/AndroPub2/Service2.asmx/";

    private final String BORDERS_URL = BASE_URL + "getBordersList";

    private final String ISSUES_URL = BASE_URL + "getIssueList";

    private final String CATEGORIES_URL = BASE_URL + "getIssuCatList";

    private final String CHECK_TRADER_URL = BASE_URL + "IsTraderRegistered";

    private final String SAVE_TRADER_URL = BASE_URL + "AddNewTrader";

    private final String SAVE_COMPLAINT_URL = BASE_URL + "AddNewComplaint";

    public Synch()
    {

    }

    public RequestQueue getRequestQueue()
    {

        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue)
    {

        this.requestQueue = requestQueue;
    }

    public DBAdapter getDbAdapter()
    {

        return dbAdapter;
    }

    public void setDbAdapter(DBAdapter dbAdapter)
    {

        this.dbAdapter = dbAdapter;
    }

    private void getBorders()
    {

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, UrlConfigs.BORDERS_URL,
                null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        JSONObject json = response.getJSONObject(i);
                        Border border = new Border();
                        border.setId(json.getInt("BorderID"));
                        border.setName(json.getString("BorderName"));

                        dbAdapter.insertBorder(border);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                System.out.println(error.getCause());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void getIssue()
    {

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, UrlConfigs.ISSUES_URL,
                null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        JSONObject json = response.getJSONObject(i);
                        Issue issue = new Issue();
                        issue.setId(json.getInt("NatID"));
                        issue.setName(json.getString("NatName"));
                        issue.setCategoryId(json.getInt("CatNbr"));
                        dbAdapter.insertIssue(issue);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                System.out.println(error.getCause());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void getIssueCategory()
    {

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                UrlConfigs.CATEGORIES_URL, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        JSONObject json = response.getJSONObject(i);
                        IssueCategory category = new IssueCategory();
                        category.setId(json.getInt("catNbr"));
                        category.setName(json.getString("CatName"));
                        System.out.println(json.toString());
                        dbAdapter.insertIssueCategory(category);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                System.out.println(error.getCause());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void saveTrader(JSONObject object)
    {
        try
        {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    SAVE_TRADER_URL, object, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {

                    if (response.has("d"))
                    {
                        Log.e("Response on save", response.toString());
                        try
                        {
                            int resValue = response.getInt("d");
                            if (resValue == 1)
                            {
                                dbAdapter.editProfile(1, 1);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {

                    System.out.println(error.getCause());
                }
            });

            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception ex)
        {
        }
    }

    public void saveComplaint(JSONObject object, final int id)
    {
        try
        {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    SAVE_COMPLAINT_URL, object, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    Log.e("Response on save", response.toString());
                    if (response.has("d"))
                    {
                        try
                        {
                            int resValue = response.getInt("d");
                            if (resValue == 1)
                            {
                                dbAdapter.deleteComplaintById(id);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    System.out.println(error.getCause());
                }
            });

            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception ex)
        {
        }

    }

    private void syncComplaints()
    {

        List<Complaint> complaintList = dbAdapter.findComplains();
        if (complaintList != null)
        {
            if (!complaintList.isEmpty())
            {
                for (Complaint complaint : complaintList)
                {
                    JSONObject json = complaint.toJson();
                    if (json != null)
                    {
                        saveComplaint(json, complaint.getId());
                    }
                }
            }
        }

    }

    private void syncProfile()
    {

        Profile profile = dbAdapter.findProfile();

        if (profile != null)
        {
            if (profile.getStatus() == 0)
            {
               this.saveTrader(profile.toJson());
            }
        }
    }

    public void doSynch()
    {

        List<RemoteData> borders = dbAdapter.findBorder();
        List<RemoteData> categories = dbAdapter.findIssueCategories();
        List<Issue> issues = dbAdapter.findIssues();

        boolean downloadBorders;
        boolean downloadIssues;
        boolean downloadCategories;

        if (borders == null)
        {
            downloadBorders = true;
        }
        else
        {
            downloadBorders = borders.isEmpty();
        }

        if (categories == null)
        {
            downloadCategories = true;
        }
        else
        {
            downloadCategories = categories.isEmpty();
        }

        if (issues == null)
        {
            downloadIssues = true;
        }
        else
        {
            downloadIssues = issues.isEmpty();
        }

        if (downloadBorders)
        {
            System.out.println("Downloading borders");
            getBorders();
        }

        if (downloadCategories)
        {
            System.out.println("Downloading Categories");
            getIssueCategory();
        }

        if (downloadIssues)
        {
            System.out.println("Downloading issues");
            getIssue();
        }

        //Attempt to synch profile and complaints
        syncComplaints();
        syncProfile();

    }

    public void updateData()
    {

        getBorders();
        getIssueCategory();
        getIssue();
    }
}
