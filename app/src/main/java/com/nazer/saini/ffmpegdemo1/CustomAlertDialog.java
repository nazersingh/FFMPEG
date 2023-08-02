package com.nazer.saini.ffmpegdemo1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CustomAlertDialog {

    public static void showEditTextAlert(Activity activity,AlertCallback<String> alertCallback)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("New File Name");
        alertDialog.setMessage("Enter new file name");

        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("YES",
                (dialog, which) -> {
                   String fileName = input.getText().toString();
                        if (fileName.isEmpty()) {
                            Toast.makeText(activity, "Please fill fileName", Toast.LENGTH_SHORT).show();
                        } else {
                            alertCallback.onDoneClick(fileName);
                        }
                });

        alertDialog.setNegativeButton("NO",
                (dialog, which) -> dialog.cancel());

        alertDialog.show();
    }

    public interface AlertCallback<T>
    {
        void onDoneClick(T t);
    }
}
