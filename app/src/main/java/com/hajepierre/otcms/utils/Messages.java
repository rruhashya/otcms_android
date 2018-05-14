package com.hajepierre.otcms.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by jeanpierre on 5/13/18.
 */

public class Messages
{
    public static void showMessage(String title, String message, Context context)
    {

        AlertDialog.Builder mBuilder= new AlertDialog.Builder(context);
        mBuilder.setTitle(title);
        mBuilder.setMessage(message);

        //Setting buttons
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

//        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.dismiss();
//            }
//        });
//
//        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.dismiss();
//            }
//        });

        AlertDialog dialog= mBuilder.create();
        dialog.show();
    }
}
