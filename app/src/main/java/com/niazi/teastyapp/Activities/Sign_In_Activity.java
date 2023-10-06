package com.niazi.teastyapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.niazi.teastyapp.R;
import com.niazi.teastyapp.databinding.SignInActivityBinding;

/** @noinspection ALL*/
public class Sign_In_Activity extends AppCompatActivity {

    SignInActivityBinding binding;

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignInActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth= FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(Sign_In_Activity.this);

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                progressDialog.setTitle("Login Please Wait...");
                progressDialog.show();
                String ed_StEmal = binding.edemail.getText().toString();
                String ed_Stpass = binding.edpasswor.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(ed_StEmal, ed_Stpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (ed_StEmal.isEmpty()) {
                            showError(binding.edemail, "Email is no valid");
                        }else if(!ed_StEmal.contains("@")) {
                            showError(binding.edemail, "Email must contain @");

                        }
                        else if (ed_Stpass.isEmpty() || ed_Stpass.length() < 7) {
                            showError(binding.edpasswor, "Password must contain 7 character");

                        }
                         else if (task.isSuccessful()) {


                            Toast.makeText(Sign_In_Activity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Sign_In_Activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();

                        } else {

                            Toast.makeText(Sign_In_Activity.this, "Wrong Email Or Password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Dialog dialog = new Dialog( Sign_In_Activity.this);

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