package com.niazi.teastyapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.niazi.teastyapp.Model.User;
import com.niazi.teastyapp.R;
import com.niazi.teastyapp.databinding.ActivitySignUpBinding;

public class Sign_Up_Activity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    FirebaseAuth auth;
    ProgressDialog progressDialog;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(Sign_Up_Activity.this);


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        binding.signUpBtn.setOnClickListener(view ->
        {

            String ed_Stname = binding.edname.getText().toString();
            String ed_Stphone = binding.edmobileno.getText().toString();
            String ed_Stemail = binding.edemail.getText().toString();
            String ed_Stpassw = binding.edpassword.getText().toString();
            String ed_Stconfpass = binding.edconfirmpass.getText().toString();


            if (ed_Stname.isEmpty()) {

                binding.edname.setError("Please type a Name");

            } else if (ed_Stphone.isEmpty() || ed_Stphone.length() < 11) {
                showError(binding.edmobileno, "Mobile Number must contain 11 character");

            } else if (ed_Stemail.isEmpty()) {


                binding.edname.setError("Please type Email");
            }  else if (ed_Stpassw.isEmpty() || ed_Stphone.length() < 7) {
                showError(binding.edpassword, "Password must contain 7 character");

            } else if (ed_Stconfpass.isEmpty() || ed_Stconfpass.length() < 7) {
                showError(binding.edconfirmpass, "Password must contain 7 character");

            } else if (!ed_Stemail.contains("@")) {
                showError(binding.edemail, "Use @ in Your Email");
            } else if(binding.edpassword == binding.edconfirmpass)  {

                Dialog dialog = new Dialog( Sign_Up_Activity.this);

                dialog.setContentView(R.layout.dialog_box);
                TextView warningtext = dialog.findViewById(R.id.Textmessage);
                TextView yes = dialog.findViewById(R.id.yes);
                TextView no = dialog.findViewById(R.id.no);

                dialog.setCancelable(false);
                warningtext.setText("Are You Sure To Create Your Account");
                yes.setOnClickListener(view1 -> {
                    progressDialog.setMessage("Create Your Profile...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    auth.createUserWithEmailAndPassword(ed_Stemail,ed_Stconfpass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    String uid = auth.getUid();


                                    User user = new User(uid,ed_Stname,ed_Stphone, ed_Stemail, ed_Stconfpass);


                                    database.getReference()
                                            .child(uid)
                                            .child("record")
                                            .setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Sign_Up_Activity.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(Sign_Up_Activity.this, Sign_In_Activity.class);
                                                    startActivity(intent);
                                                    finish();


                                                    progressDialog.dismiss();


                                                }
                                            }).
                                            addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Dialog dialog = new Dialog( Sign_Up_Activity.this);

                                                    dialog.setContentView(R.layout.warn_dialog_box);
                                                    TextView warningtext = dialog.findViewById(R.id.Textmessage);
                                                    TextView yes = dialog.findViewById(R.id.yes);

                                                    dialog.setCancelable(false);
                                                    warningtext.setText(e.getLocalizedMessage().toString());

                                                    yes.setOnClickListener(view2 -> {
                                                        dialog.dismiss();
                                                    });
                                                    progressDialog.dismiss();

                                                    dialog.show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {


                                    Dialog dialog = new Dialog( Sign_Up_Activity.this);

                                    dialog.setContentView(R.layout.warn_dialog_box);
                                    TextView warningtext = dialog.findViewById(R.id.Textmessage);
                                    TextView yes = dialog.findViewById(R.id.yes);

                                    dialog.setCancelable(false);
                                    warningtext.setText(e.getLocalizedMessage().toString());

                                    yes.setOnClickListener(view2 -> {
                                        dialog.dismiss();
                                    });
                                    progressDialog.dismiss();

                                    dialog.show();
                                }
                            });
                });
                no.setOnClickListener(view1 -> {

                    Toast.makeText(Sign_Up_Activity.this, "Cancel", Toast.LENGTH_SHORT).show();

                });

                  dialog.show();
            }


                });
            }



    private void showError(EditText edname, String s) {

        edname.setError(s);
        edname.requestFocus();


    }
}