package com.hajepierre.otcms.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.hajepierre.otcms.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jeanpierre on 4/30/18.
 */

public class FragmentService
{
    private static Map<String, Object> fragments = new HashMap<String, Object>();

    private static Set<String> stackEntries = new HashSet<String>();

    private static String lastEntry = "";

    //public static void launchFragment(Object fragment, String title, Activity activity)
    public static void launchFragment(Object fragment, Activity activity)
    {

        try
        {
            FragmentManager fragmentManager = activity.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//            if (!stackEntries.contains(fragment.getClass().getName()))
//            {
//                lastEntry = fragment.getClass().getName();
//                stackEntries.add(lastEntry);
//                fragmentTransaction.add((Fragment) fragment, fragment.getClass().getName()).addToBackStack(lastEntry);
//            }

            addFratment(fragment);

            fragmentTransaction.replace(R.id.frame_container, (Fragment) fragment).commit();
            //activity.setTitle(title);
        } catch (Exception ex)
        {
        }
    }

    public static void resumeLastEntry(Activity activity)
    {

        FragmentManager fragmentManager = activity.getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0)
        {
            String entries = "";
            for (String ent : fragments.keySet())
            {
                entries += ent;
            }
            Log.d("FRAGMENTS", entries);

            fragmentManager.popBackStackImmediate();
            stackEntries.remove(lastEntry);
        } else
        {
            Toast.makeText(activity, "Back stack is empty", Toast.LENGTH_LONG).show();
        }
    }

    public static Object resumeFragment(String name)
    {

        if (fragments.containsKey(name))
        {
            return fragments.get(name);
        }
        return null;
    }

    private static void addFratment(Object fragment)
    {

        if (!fragments.containsKey(fragment.getClass().getSimpleName()))
        {
            fragments.put(fragment.getClass().getSimpleName(), fragment);
        }
    }
}
