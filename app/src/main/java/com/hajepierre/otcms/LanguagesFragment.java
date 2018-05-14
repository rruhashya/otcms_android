package com.hajepierre.otcms;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hajepierre.otcms.db.DBAdapter;
import com.hajepierre.otcms.utils.FragmentService;
import com.hajepierre.otcms.utils.Messages;
import com.hajepierre.otcms.utils.Utils;

import java.util.Locale;

/**
 * Created by jeanpierre on 5/6/18.
 */

public class LanguagesFragment extends Fragment
{

    int[] IMAGES = {R.drawable.english, R.drawable.french, R.drawable.rwanda, R.drawable.swahili};

    String[] LANGAUGES = {"English", "Francais", "Kinyarwanda","Swahili"};
    String[] LOCALES = {"en", "fr", "rw","sw"};

    public LanguagesFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        final DBAdapter dbAdapter= new DBAdapter(getActivity());
        dbAdapter.open();

        View rootView = inflater.inflate(R.layout.languages_fragment, container, false);

        ListView listView = rootView.findViewById(R.id.listLanguages);

        CustomerAdapter customerAdapter= new CustomerAdapter(getActivity());
        listView.setAdapter(customerAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id)
            {
                Utils.setNewLocale(getActivity(), LOCALES[i]);
                dbAdapter.insertLanguage(LOCALES[i]);

                Messages.showMessage(getString(R.string.title_succes),"You set your language to "+LANGAUGES[i]
                        , getActivity());

                //Toast.makeText(getActivity(),"You set your language to "+LANGAUGES[i],Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }



    class CustomerAdapter extends BaseAdapter
    {

        private Context context;

        public CustomerAdapter(Context context)
        {

            this.context = context;
        }

        @Override
        public int getCount()
        {

            return IMAGES.length;
        }

        @Override
        public Object getItem(int position)
        {

            return null;
        }

        @Override
        public long getItemId(int position)
        {

            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.customerlayout, null);
            ImageView image = view.findViewById(R.id.langImage);
            TextView text = view.findViewById(R.id.languageName);
            image.setImageResource(IMAGES[i]);
            text.setText(LANGAUGES[i]);
            return view;
        }
    }
}
