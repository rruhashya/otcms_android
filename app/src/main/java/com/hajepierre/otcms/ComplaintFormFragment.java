package com.hajepierre.otcms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hajepierre.otcms.db.Border;
import com.hajepierre.otcms.db.Complaint;
import com.hajepierre.otcms.db.DBAdapter;
import com.hajepierre.otcms.db.Issue;
import com.hajepierre.otcms.db.IssueCategory;
import com.hajepierre.otcms.db.Profile;
import com.hajepierre.otcms.db.RemoteData;
import com.hajepierre.otcms.utils.Messages;
import com.hajepierre.otcms.utils.RemoteService;
import com.hajepierre.otcms.utils.Synch;
import com.hajepierre.otcms.utils.UrlConfigs;
import com.hajepierre.otcms.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintFormFragment extends Fragment
{

    private Spinner issuesCategorySpinner;

    private Spinner issuesSpinner;

    private Spinner bordersSpinner;

    private String complaintCategory;

    private EditText txtComment;

    private String issue;

    private String border;
    private int borderId;

    private Map<Integer, RemoteData> issueCategories;

    private DBAdapter dbAdapter;

    private Complaint complaint;

    private Synch synch;

    private RequestQueue requestQueue;

    private Map<Integer, Issue> mapIssues;
    private Map<Integer, RemoteData> mapBorders;


    private int issueId = -1;
    private String issueName;

    public ComplaintFormFragment()
    {
        // Required empty public constructor
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

        issueCategories = new HashMap<>();
        mapBorders= new HashMap<>();
        View rootView = inflater.inflate(R.layout.fragment_complaint_form, container, false);


        if (dbAdapter == null)
        {
            dbAdapter = new DBAdapter(getActivity());
            dbAdapter.open();
        }
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        mapIssues = new HashMap<>();

        txtComment = rootView.findViewById(R.id.txtComments);

        //WORKING WITH ISSUES
        issuesSpinner = rootView.findViewById(R.id.listIssue);

        issuesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {

                issueId = pos;
                issueName=parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //Working with ISSUE CATEGORIES
        issuesCategorySpinner = rootView.findViewById(R.id.listIssueCategory);
        List<RemoteData> issuesCategories = dbAdapter.findIssueCategories();

        if (issuesCategories != null)
        {
            if (!issuesCategories.isEmpty())
            {
                List<String> categories = new ArrayList<>();
                int count = 0;
                for (RemoteData cat : issuesCategories)
                {
                    categories.add(cat.getName());
                    issueCategories.put(count, cat);
                    count++;
                }

                ArrayAdapter<String> issueCategories = new ArrayAdapter<>(container.getContext(),
                        android.R.layout.simple_list_item_1, categories);
                issueCategories.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                issuesCategorySpinner.setAdapter(issueCategories);
            }
        }

        issuesCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {

                complaintCategory = parent.getItemAtPosition(pos).toString();
                RemoteData complaint = dbAdapter.findCategoryByName(complaintCategory);
                System.out.println(complaint.getName() + " " + complaint.getId());

                if (complaint != null)
                {
                    List<Issue> dbData = dbAdapter.findIssuesByCategoryId(complaint.getId());

                    if (dbData != null)
                    {
                        if (!dbData.isEmpty())
                        {
                            List<String> issuesData = new ArrayList<>();
                            int count = 0;
                            for (Issue issue : dbData)
                            {
                                issuesData.add(issue.getName());
                                mapIssues.put(count, issue);
                                count++;
                            }
                            ArrayAdapter<String> issues = new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_list_item_1, issuesData);
                            issues.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item);
                            issuesSpinner.setAdapter(issues);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //WORKING WITH BORDER
        bordersSpinner = rootView.findViewById(R.id.listBorders);
        List<RemoteData> borders = dbAdapter.findBorder();


        if (borders != null)
        {
            if (!borders.isEmpty())
            {
                List<String> bordeData = new ArrayList<>();
                int count=0;
                for (RemoteData border : borders)
                {
                    bordeData.add(border.getName());
                    mapBorders.put(count,border);
                    count++;
                }

                ArrayAdapter<String> bordersData = new ArrayAdapter<>(container.getContext(),
                        android.R.layout.simple_list_item_1, bordeData);
                bordersData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                bordersSpinner.setAdapter(bordersData);
            }
        }

        bordersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {

                border = parent.getItemAtPosition(pos).toString();
                borderId= mapBorders.get(pos).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        Button btnSubmit = rootView.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (!mapIssues.isEmpty())
                {
                    synch = new Synch();
                    synch.setRequestQueue(requestQueue);
                    synch.setDbAdapter(dbAdapter);

                    complaint = new Complaint();
                    complaint.setComments(txtComment.getText().toString());
                    complaint.setBorder(border);
                    complaint.setIssue(issueId);
                    complaint.setIssueCategory(complaintCategory);
                    complaint.setIssueName(issueName);
                    complaint.setBorderId(borderId);

                    Profile profile = dbAdapter.findProfile();

                    if (profile != null)
                    {
                        complaint.setNationalId(profile.getNationalId());

                        System.out.println(complaint);
                        System.out.println("JSON-->"+complaint.toJson().toString());

                        if (Utils.isNetworkAvailable(getActivity()))
                        {
                            saveComplaint(complaint);
                        }
                        else
                        {
                            dbAdapter.insertComplaint(complaint);
                            Messages.showMessage(getString(R.string.title_info),
                                    getString(R.string.complaint_saved_offline), getActivity());

//                            Toast.makeText(getActivity(),
//                                    "Your complaint was save offline, it will be sent the next time you get connected on internet",
//                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Messages.showMessage(getString(R.string.title_warning),
                                getString(R.string.internet_required_to_record_complaint), getActivity());
//                        Toast.makeText(getActivity(),
//                                "Profile is required before you can record your complaint",
//                                Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Messages.showMessage(getString(R.string.title_warning),
                            getString(R.string.issue_required), getActivity());

//                    Toast.makeText(getActivity(), "Please select issue before you proceed",
//                            Toast.LENGTH_LONG).show();
                }
            }
        });


        return rootView;
    }


    private void saveComplaint(final Complaint complaint)
    {

        System.out.println(complaint.toJson().toString());
        try
        {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    UrlConfigs.SAVE_COMPLAINT_URL, complaint.toJson(),
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {

                            Log.e("Response on save", response.toString());
                            try
                            {
                                //int resValue = response.getInt("d");

                                JSONObject json=response.getJSONObject("d");
                                int resValue = json.getInt("d");
                                String description=json.getString("description");
                                //System.out.println(resValue);
                                if (resValue == 1)
                                {

                                    Messages.showMessage(getString(R.string.title_succes),
                                            getString(R.string.comaplaint_success), getActivity());
                                    txtComment.setText("");
                                }
                                else
                                {
                                    Messages.showMessage(getString(R.string.title_error), description.toUpperCase(),
                                            getActivity());

                                    dbAdapter.insertComplaint(complaint);
                                }
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
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception ex)
        {
        }

    }

}
