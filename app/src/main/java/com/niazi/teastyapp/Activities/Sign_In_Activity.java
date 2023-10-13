package com.niazi.teastyapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.niazi.teastyapp.Dialog.Progress_Dialog;
import com.niazi.teastyapp.R;
import com.niazi.teastyapp.databinding.SignInActivityBinding;

/** @noinspection ALL*/
public class Sign_In_Activity extends AppCompatActivity {

    SignInActivityBinding binding;

    FirebaseAuth firebaseAuth;

    Progress_Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignInActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth= FirebaseAuth.getInstance();


        progressDialog = new Progress_Dialog(Sign_In_Activity.this);

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialog = new AlertDialog.Builder( Sign_In_Activity.this);

                View view2 = LayoutInflater.from(Sign_In_Activity.this).
                        inflate(R.layout.dialog_box,(ConstraintLayout)findViewById(R.id.conback));

                dialog.setView(view2);

                ((TextView) view2.findViewById(R.id.Textmessage)).setText("Are You To Login In Your Account ");
                ((TextView) view2.findViewById(R.id.yes)).setText("Yes");
                ((TextView) view2.findViewById(R.id.no)).setText("No");
                final AlertDialog alertDialog = dialog.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));


                view2.findViewById(R.id.no).setOnClickListener(view1 -> {

                    Toast.makeText(Sign_In_Activity.this, "Cancel", Toast.LENGTH_SHORT).show();

                                    });
                view2.findViewById(R.id.yes).setOnClickListener(view1 -> {


                    progressDialog.setTitle("Login Please Wait...");
                progressDialog.show();
                String ed_StEmal = binding.edemail.getText().toString();
                String ed_Stpass = binding.edpassword.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(ed_StEmal, ed_Stpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (ed_StEmal.isEmpty()) {
                            showError(binding.edemail, "Email is no valid");
                        }else if(!ed_StEmal.contains("@")) {
                            showError(binding.edemail, "Email must contain @");

                        }
                        else if (ed_Stpass.isEmpty() || ed_Stpass.length() < 7) {
                            showError(binding.edpassword, "Password must contain 7 character");

                        }
                         else if (task.isSuccessful()) {


                            Toast.makeText(Sign_In_Activity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Sign_In_Activity.this, Seller_Data_Activity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();

                        } else {
                               AlertDialog.Builder dialog = new AlertDialog.Builder( Sign_In_Activity.this);

                                View view2 = LayoutInflater.from(Sign_In_Activity.this).
                                        inflate(R.layout.warn_dialog_box,(ConstraintLayout)findViewById(R.id.warnbac));

                                dialog.setView(view2);

                                ((TextView) view2.findViewById(R.id.Textmessag)).setText("Some Thing went wrong");
                                ((TextView) view2.findViewById(R.id.okay)).setText("Okay");
                                final AlertDialog alertDialog = dialog.create();
                                alertDialog.setCancelable(false);

                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                                view2.findViewById(R.id.okay).setOnClickListener(view1 -> {



                                    alertDialog.dismiss();

                                    progressDialog.dismiss();
                                });;

                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder( Sign_In_Activity.this);

                        View view2 = LayoutInflater.from(Sign_In_Activity.this).
                                inflate(R.layout.warn_dialog_box,(ConstraintLayout)findViewById(R.id.warnbac));

                        dialog.setView(view2);

                        ((TextView) view2.findViewById(R.id.Textmessag)).setText(e.getLocalizedMessage().toString());
                        ((TextView) view2.findViewById(R.id.okay)).setText("Okay");
                        final AlertDialog alertDialog = dialog.create();
                        alertDialog.setCancelable(false);

                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                        view2.findViewById(R.id.okay).setOnClickListener(view1 -> {



                                alertDialog.dismiss();

                        });
                    }
                });

            });
            alertDialog.show();
            }

        });


        binding.signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_In_Activity.this, Sign_Up_Activity.class);
                startActivity(intent);

            }
        });
    }

    private void showError(EditText edname, String s) {

        edname.setError(s);
        edname.requestFocus();


    }

}