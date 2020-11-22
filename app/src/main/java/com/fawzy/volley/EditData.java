package com.fawzy.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fawzy.volley.controler.SessionManager;
import com.fawzy.volley.controler.VolleySingleTone;
import com.fawzy.volley.servers.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditData extends AppCompatActivity {
    private EditText name , author ;
    private Button edit ;
    private int id ;
     private Bundle extras ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        name = findViewById(R.id.name);
        author = findViewById(R.id.author);
        edit = findViewById(R.id.edit);

        extras = getIntent().getExtras();
        if (extras != null){
            name.setText(extras.getString("name"));
            author.setText(extras.getString("author"));
            id = extras.getInt("id");
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditData(id);
                finish();
                startActivity(new Intent(EditData.this,MainActivity.class));
            }
        });


    }

    public void EditData(int id){
        final String Name = name.getText().toString().trim();
        final String Author = author.getText().toString().trim();
        final String token = SessionManager.getInstance(this).getToken().getToken();

        if (TextUtils.isEmpty(Name)) {
            name.setError("Enter your E-Mail");
            name.requestFocus();
            return;
        } else if (TextUtils.isEmpty(Author)) {
            author.setError("Enter your Password");
            author.requestFocus();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("name",Name);
            postparams.put("author",Author);
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, Urls.Data_url+"/"+id , postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")){
                                Toast.makeText(EditData.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }else {
                                Toast.makeText(EditData.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditData.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        )
        {
            public Map<String,String> getHeaders(){
                Map<String,String> map = new HashMap<>();
                map.put("Accept","application/json");
                map.put("Authorization","Bearer  "+ token);
                return map ;
            }
        }
                ;
        VolleySingleTone.getInstance(this).addRequestQueue(jsonObjectRequest);
    }







}