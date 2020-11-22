package com.fawzy.volley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fawzy.volley.controler.Adapter;
import com.fawzy.volley.controler.SessionManager;
import com.fawzy.volley.controler.VolleySingleTone;
import com.fawzy.volley.model.Book;
import com.fawzy.volley.servers.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
private RecyclerView recyclerView ;
    private RequestQueue requestQueue ;
    private String token ;
    private List<Book> bookList ;
    private static Adapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        if (SessionManager.getInstance(this).chechlogin()){
            if (SessionManager.getInstance(this).getToken() != null){
                 token = SessionManager.getInstance(this).getToken().getToken();
            }else{
                finish();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        }

        recyclerView = findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        bookList = new ArrayList<>();
        bookList = getData() ;
        adapter = new Adapter(this,bookList);
        recyclerView.setAdapter(adapter);


    }

    public List<Book> getData(){
        bookList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.Data_url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Book book = new Book();
                        book.setId(jsonObject.getInt("id"));
                        book.setName(jsonObject.getString("name"));
                        book.setAuthor(jsonObject.getString("author"));
                        book.setCreated_at(jsonObject.getString("created_at"));

                        bookList.add(book);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
          return bookList ;
    }

    public static void notifyadapter(){
        adapter.notifyDataSetChanged();
    }








}