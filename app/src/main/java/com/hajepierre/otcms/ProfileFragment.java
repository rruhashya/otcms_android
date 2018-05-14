package com.hajepierre.otcms;


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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hajepierre.otcms.db.Complaint;
import com.hajepierre.otcms.db.DBAdapter;
import com.hajepierre.otcms.db.IssueCategory;
import com.hajepierre.otcms.db.Profile;
import com.hajepierre.otcms.db.RemoteData;
import com.hajepierre.otcms.utils.FragmentService;
import com.hajepierre.otcms.utils.Messages;
import com.hajepierre.otcms.utils.RemoteService;
import com.hajepierre.otcms.utils.Synch;
import com.hajepierre.otcms.utils.UrlConfigs;
import com.hajepierre.otcms.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment
{


    private Spinner nationalitySpinner;

    private Spinner yearOfBirthSpinner;

    private String nationality;

    private String yearOfBirth;

    private EditText txtFirstName;

    private EditText txtLastName;

    private EditText txtPhoneNumber;

    private EditText txtEmail;

    private EditText nationalId;

    private Profile profile;

    private DBAdapter dbAdapter;

    private Synch synch;

    private RequestQueue requestQueue;

    private String nationalIdValue;


    public ProfileFragment()
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

    public String getNationalIdValue()
    {

        return nationalIdValue;
    }

    public void setNationalIdValue(String nationalIdValue)
    {

        this.nationalIdValue = nationalIdValue;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {

        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        if (dbAdapter == null)
        {
            dbAdapter = new DBAdapter(getActivity());
            dbAdapter.open();
        }

        synch = new Synch();
        synch.setDbAdapter(dbAdapter);
        synch.setRequestQueue(requestQueue);

        final RadioButton rbMale = rootView.findViewById(R.id.rdMale);
        final RadioButton rbFemale = rootView.findViewById(R.id.rdFemale);
        txtFirstName = rootView.findViewById(R.id.txtFirstName);
        txtLastName = rootView.findViewById(R.id.txtLastName);
        txtPhoneNumber = rootView.findViewById(R.id.txtPhoneNumber);
        txtEmail = rootView.findViewById(R.id.txtEmail);
        nationalId = rootView.findViewById(R.id.txtNationalId);
        nationalId.setText(nationalIdValue);

        nationalitySpinner = rootView.findViewById(R.id.listNationality);
        ArrayAdapter<String> nationalities = new ArrayAdapter<>(container.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.nationalities));
        nationalities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationalitySpinner.setAdapter(nationalities);
        nationalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                nationality = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        yearOfBirthSpinner = rootView.findViewById(R.id.listYearOfBirth);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Integer> years = new ArrayList<>();
        for (int i = currentYear - 17; i >= 1950; i--)
        {
            years.add(i);
        }

        final ArrayAdapter<Integer> yearsOfBirth = new ArrayAdapter<>(container.getContext(),
                android.R.layout.simple_list_item_1, years);
        yearsOfBirth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearOfBirthSpinner.setAdapter(yearsOfBirth);
        yearOfBirthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                yearOfBirth = parent.getItemAtPosition(position).toString();
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

                if (isValid())
                {
                    profile = new Profile();

                    if (rbMale.isChecked())
                    {
                        profile.setGender("Male");
                    }
                    if (rbFemale.isChecked())
                    {
                        profile.setGender("Female");
                    }
                    profile.setId(1);
                    profile.setYearOfBirth(yearOfBirth);
                    profile.setNationality(nationality);
                    profile.setEmail(txtEmail.getText().toString());
                    profile.setNationalId(nationalId.getText().toString());
                    profile.setLastName(txtLastName.getText().toString());
                    profile.setFirstName(txtFirstName.getText().toString());
                    profile.setPhoneNumber(txtPhoneNumber.getText().toString());
                    profile.setStatus(0);

                    if (Utils.isNetworkAvailable(getActivity()))
                    {
                        profile.setStatus(1);
                        saveTrader(profile.toJson());
                    }
                    else
                    {
                        dbAdapter.insertProfile(profile);

                        Messages.showMessage(getString(R.string.title_warning),
                                getString(R.string.profile_offline), getActivity());

//                        Toast.makeText(getActivity(),
//                                "Your profile was saved offline, please get internet connection to create your online identity",
//                                Toast.LENGTH_SHORT).show();
                    }

                    dbAdapter.insertProfile(profile);
                    proceed();

                }
            }
        });

        return rootView;
    }

//    public void saveProfile(Profile profile)
//    {
//        //if condition is true
//        dbAdapter.insertProfile(profile);
//        proceed();
//    }

    private boolean isValid()
    {

        return Utils.validate(txtFirstName) && Utils.validate(txtLastName) && Utils.validate(
                txtPhoneNumber) && Utils.validate(txtEmail) && Utils.validate(nationalId);
    }

    private boolean hasData()
    {

        List<RemoteData> data = dbAdapter.findIssueCategories();
        if (data != null)
        {
            return !data.isEmpty();
        }
        return false;
    }

    private void proceed()
    {

        boolean canProceed = hasData();
        if (hasData())
        {
            canProceed = true;
        }
        else
        {
            if (Utils.isNetworkAvailable(getActivity()))
            {
                synch.doSynch();
                canProceed = true;
            }
            else
            {
                canProceed = false;
                Messages.showMessage(getString(R.string.title_warning),
                        getString(R.string.internet_required_for_complaint_record), getActivity());

//                Toast.makeText(getActivity(),
//                        "Internet connection is required before you can proceed to record your complaint",
//                        Toast.LENGTH_LONG).show();
            }
        }
        if (canProceed)
        {

            ComplaintFormFragment mapFragment = new ComplaintFormFragment();
            mapFragment.setDbAdapter(dbAdapter);
            mapFragment.setRequestQueue(requestQueue);
            FragmentService.launchFragment(mapFragment, getActivity());
        }
    }

    public void saveTrader(JSONObject object)
    {

        try
        {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    UrlConfigs.SAVE_TRADER_URL, object, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {

                    Log.e("TRADER RECORD RESPONSE", response.toString());

                    try
                    {
                        JSONObject json=response.getJSONObject("d");
                        int resValue = json.getInt("d");
                        String description=json.getString("description");
                        if (resValue == 1)
                        {
                            Messages.showMessage(getString(R.string.title_succes),
                                    getString(R.string.profile_success), getActivity());
                        }
                        else
                        {
                            Messages.showMessage(getString(R.string.title_error), description.toUpperCase(),
                                    getActivity());
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
                    //System.out.println(error.getMessage());
                }
            });

            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception ex)
        {
        }
    }

}
