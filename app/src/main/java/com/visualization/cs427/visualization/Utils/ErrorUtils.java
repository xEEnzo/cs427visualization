package com.visualization.cs427.visualization.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


/**
 * Created by linhtnvo on 6/6/2016.
 */
public class ErrorUtils {
    public static void showDialog(Context mContext, String message) {
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        if (message.contains(":")) {
            message = message.split(":")[1];
        }
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if (isActivityNotFinishing(mContext)) {
            dialog.show();
        }
    }


    private static boolean isActivityNotFinishing(Context mContext) {
        return !((Activity) mContext).isFinishing();
    }
}
