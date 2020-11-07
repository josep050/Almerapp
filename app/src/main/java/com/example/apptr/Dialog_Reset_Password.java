package com.example.apptr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;


public class Dialog_Reset_Password extends AppCompatDialogFragment {
    private TextView txtview1;
    private TextView txtview_email;

    private FirebaseUser user;

    private ExampleDialogListener listener;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        txtview1 = view.findViewById(R.id.text1);
        txtview_email = view.findViewById(R.id.email_dialog);

        user = FirebaseAuth.getInstance().getCurrentUser();
        txtview_email.setText(user.getEmail());

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());


        builder.setView(view)
                .setTitle("Canvi de contrasenya")
                .setNegativeButton("Cancel·lar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.setMessage("Generant enllaç...");
                        progressDialog.show();

                        mAuth.sendPasswordResetEmail(user.getEmail())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                        progressDialog.dismiss();
                        Toasty.success(getActivity(), "S'ha enviat l'enllaç", Toast.LENGTH_LONG).show();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
    }
}