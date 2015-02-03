package com.paypal.developer.brasilino;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ricardo on 01/02/2015.
 */
public class MyAlertDialogFragment extends DialogFragment {

    public static MyAlertDialogFragment newInstance() {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.view_dialog, null);
        final EditText edtIp = (EditText) view.findViewById(R.id.edtIp);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor spEditor = sp.edit();
        edtIp.setText(sp.getString("ip", ""));

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_logo_paypal)
                .setTitle("Configuracao IP Carro")
                .setView(view)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String ipTemp = edtIp.getText().toString();
                                spEditor.putString("ip", ipTemp);
                                spEditor.commit();

                                Toast.makeText(getActivity(), "Configuração salva!", Toast.LENGTH_LONG).show();
                            }
                        }
                )
                .setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create();
    }
}
