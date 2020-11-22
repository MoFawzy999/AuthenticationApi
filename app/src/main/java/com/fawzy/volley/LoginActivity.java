package com.fawzy.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fawzy.volley.controler.SessionManager;
import com.fawzy.volley.controler.VolleySingleTone;
import com.fawzy.volley.model.User;
import com.fawzy.volley.servers.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
   private EditText email , passsword ;
   private Button login,register ;
   private ProgressBar progressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        passsword = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progress);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity( new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginuser();
            }
        });

    }



    public void Loginuser(){
        final String Email = email.getText().toString().trim();
        final String Password = passsword.getText().toString().trim();

        if (TextUtils.isEmpty(Email)) {
            email.setError("Enter your E-Mail");
            email.requestFocus();
            return;
        } else if (TextUtils.isEmpty(Password)) {
            passsword.setError("Enter your Password");
            passsword.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Urls.Login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.VISIBLE);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getJSONObject("success") != null) {  // 3shan hna success mash boolean lakn gia fal json anha object
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),
                                obj.getString("welcome"), Toast.LENGTH_SHORT).show();

                        JSONObject userJson = obj.getJSONObject("success");

                        User user = new User(userJson.getString("token"));

                        SessionManager.getInstance(getApplicationContext()).userlogin(user);

                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "login failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("email", Email);
                params.put("password", Password);
                return  params;
            }
        }   ;

        VolleySingleTone.getInstance(this).addRequestQueue(stringRequest);
    }














}