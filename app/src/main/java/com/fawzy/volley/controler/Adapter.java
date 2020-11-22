package com.fawzy.volley.controler;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fawzy.volley.AddActivity;
import com.fawzy.volley.EditData;
import com.fawzy.volley.MainActivity;
import com.fawzy.volley.R;
import com.fawzy.volley.model.Book;
import com.fawzy.volley.servers.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

private Context context ;
private List<Book> bookList ;

    public Adapter( Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw,parent,false);
        return  new ViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Book book = bookList.get(position);
        holder.name.setText(book.getName());
        holder.author.setText(book.getAuthor());
        holder.time.setText(formatDate(book.getCreated_at()));
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteData(position,book.getId());
            }
        });
        holder.draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditData.class);
                intent.putExtra("id",book.getId());
                intent.putExtra("name",book.getName());
                intent.putExtra("author",book.getAuthor());
                context.startActivity(intent);
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i = new Intent(context, AddActivity.class);
              context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author, time , name ;
        ImageView draw , cancel , add ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.txt_author);
            time = itemView.findViewById(R.id.txt_time);
            name = itemView.findViewById(R.id.txt_name);
            draw = itemView.findViewById(R.id.img1);
            cancel =  itemView.findViewById(R.id.img2);
            add = itemView.findViewById(R.id.img3);
        }
    }

    private String formatDate(String data){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date date  = simpleDateFormat.parse(data);
            SimpleDateFormat fmtout = new SimpleDateFormat("MM/dd HH:mm");
            return fmtout.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
         return  "" ;
    }

    public void DeleteData(int position, int id){
        final String token = SessionManager.getInstance(context).getToken().getToken();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.Delete_url+id ,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")){
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }else {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        VolleySingleTone.getInstance(context).addRequestQueue(jsonObjectRequest);

        bookList.remove(position);
        MainActivity.notifyadapter();
    }


}
