package com.niazi.teastyapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.niazi.teastyapp.Dialog.Progress_Dialog;
import com.niazi.teastyapp.Model.User;
import com.niazi.teastyapp.R;
import com.niazi.teastyapp.databinding.ActivitySignUpBinding;

public class Sign_Up_Activity extends AppCompatActivity {

    ActivitySignUpBinding binding;


    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Progress_Dialog progressDialog = new Progress_Dialog(this);


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        binding.signUpBtn.setOnClickListener(view ->
        {

            String ed_Stname = binding.edname.getText().toString();
            String ed_Stphone = binding.edmobileno.getText().toString();
            String ed_Stresturent = binding.edrestaurants.getText().toString();
            String ed_address = binding.address.getText().toString();
            String ed_email = binding.edemail.getText().toString();
            String ed_Stconfpass = binding.edconfirmpass.getText().toString();


            if (ed_Stname.isEmpty()) {

                binding.edname.setError("Please type a Name");

            } else if (ed_Stphone.isEmpty() || ed_Stphone.length() < 11) {
                showError(binding.edmobileno, "Mobile Number must contain 11 character");

            } else if (ed_email.isEmpty()) {


                binding.edemail.setError("Please type Email");
            }
            else if (!ed_email.contains("@")) {
                showError(binding.edemail, "Use @ in Your Email");
            }   else if (ed_Stresturent.isEmpty()) {
                showError(binding.edrestaurants, "Please type Restaurant name");

            } else if (ed_Stconfpass.isEmpty() || ed_Stconfpass.length() < 7) {
                showError(binding.edconfirmpass, "Password must contain 7 character");

            } else if (ed_address.isEmpty() ) {
                showError(binding.address, "Please type Your Address");

            } else  {
                AlertDialog.Builder dialog = new AlertDialog.Builder( Sign_Up_Activity.this);

                View view2 = LayoutInflater.from(Sign_Up_Activity.this).
                        inflate(R.layout.dialog_box,
                                (ConstraintLayout)findViewById(R.id.conback));

                dialog.setView(view2);

                ((TextView) view2.findViewById(R.id.Textmessage)).setText("Are You Sure To Create Your Account ");
                ((TextView) view2.findViewById(R.id.yes)).setText("Yes");
                ((TextView) view2.findViewById(R.id.no)).setText("No");
                final AlertDialog alertDialog = dialog.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                view2.findViewById(R.id.no).setOnClickListener(view1 -> {

                    Toast.makeText(Sign_Up_Activity.this, "Cancel", Toast.LENGTH_SHORT).show();

                    alertDialog.dismiss();
                });
                view2.findViewById(R.id.yes).setOnClickListener(view1 -> {

                    progressDialog.show();



                    auth.createUserWithEmailAndPassword(ed_email,ed_Stconfpass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    String uid = auth.getUid();


                                    User user = new User(uid,ed_Stname,ed_Stphone, ed_email, ed_Stresturent,ed_address, ed_Stconfpass);


                                    database.getReference()
                                            .child("Seller Data")
                                            .child(uid)
                                            .setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Sign_Up_Activity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(Sign_Up_Activity.this, Seller_Data_Activity.class);
                                                    startActivity(intent);
                                                    finish();


                                                    progressDialog.dismiss();



                                                }
                                            }).
                                            addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    progressDialog.dismiss();
                                                    AlertDialog.Builder dialog = new AlertDialog.Builder( Sign_Up_Activity.this);

                                                    View view2 = LayoutInflater.from(Sign_Up_Activity.this).
                                                            inflate(R.layout.warn_dialog_box,(ConstraintLayout)findViewById(R.id.warnbac));

                                                    dialog.setView(view2);

                                                    ((TextView) view2.findViewById(R.id.Textmessag)).setText(e.getLocalizedMessage().toString());
                                                    ((TextView) view2.findViewById(R.id.okay)).setText("Okay");
                                                    ((TextView) view2.findViewById(R.id.error)).setText("Error");
                                                    final AlertDialog alertDialog = dialog.create();
                                                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                                    view2.findViewById(R.id.okay).setOnClickListener(view3 -> {

                                                        alertDialog.dismiss();
                                                    });

                                                    alertDialog.setCancelable(false);

                                                    alertDialog.setContentView(view2);


                                                    alertDialog.show();


                                                }
                                            });
                                }

                            });
                });


                  alertDialog.show();
            }


                });


        binding.signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_Up_Activity.this,Sign_In_Activity.class);
                startActivity(intent);
            }
        });
    }



    private void showError(EditText edname, String s) {

        edname.setError(s);
        edname.requestFocus();


    }
}