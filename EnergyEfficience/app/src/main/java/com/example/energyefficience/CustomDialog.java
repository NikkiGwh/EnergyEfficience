package com.example.energyefficience;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomDialog extends DialogFragment {
    private String msg = "";
    public CustomDialog (int choice){
       switch (choice){
           case 1:
               msg = "Es wurden noch keine Zahlen zum sortieren geladen. Drücken sie zunächst auf GNERATE NEW NUMBERS";
               break;
           case 2:
               msg = "Bei der aller Ersten Nutzung der App muss zuerst auf GENERATE NUMBERS gedrückt werden um Zahlen zu Zufallszaheln zu erzeugen und zu speichern" +
                       "Beim ersten Drücken von MERGE SORT kommt es zu einer kurzen Wartezeit, da das Laden der Zahlen aus der Datenbank Zeit braucht";
               break;
           case 3:
               msg = "Bitte achten Sie darauf, dass Sie einen gültigen Zahlenwert ins Textfeld eingeben";
               break;
           case 4:
               msg = "Drücken Sie GENERATE NUMBERS bevor sie sortieren";
               break;
           case 5:
               msg = "Die minimale String size beträgt 10000 KB. Werte die kleiner sind, werden automatisch auf 10000 aufgerundet";
               break;
       }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }
}
