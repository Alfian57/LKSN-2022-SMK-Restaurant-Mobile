package com.postingan.esemka_restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.postingan.esemka_restaurant.Adapter.TableAdapter;
import com.postingan.esemka_restaurant.Helper.Auth;
import com.postingan.esemka_restaurant.Model.Table;
import com.postingan.esemka_restaurant.databinding.ActivityListTableBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListTableActivity extends AppCompatActivity {
    ActivityListTableBinding binding;
    Auth auth;
    List<Table> tables;
    ProgressDialog progressDialog;
    GetTableTask getTableTask;
    AddTableTask addTableTask;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListTableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = new Auth(ListTableActivity.this);
        tables = new ArrayList<>();
        progressDialog = new ProgressDialog(ListTableActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("LOADING..");
        getTableTask = new GetTableTask();
        addTableTask = new AddTableTask();
        getTableTask.execute();
        editText = new EditText(ListTableActivity.this);

        binding.btnAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListTableActivity.this);
                builder.setTitle("Input Table Number");
                builder.setView(editText);
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Open Table", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int number = Integer.parseInt(editText.getText().toString());
                            if (number >= 0 && number <= 50){
                                addTableTask.execute();
                            } else {
                                Toast.makeText(ListTableActivity.this, "Insert Between 0 and 50", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex){
                            Toast.makeText(ListTableActivity.this, "Input A Number", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.show();
            }
        });

    }

    public class AddTableTask extends AsyncTask<String, String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {
                URL url = new URL("http://10.0.2.2:5000/Api/Table?number=" + editText.getText().toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("accept", "text/plain");
                conn.setRequestProperty("Authorization", auth.getToken());

                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1){
                    result += (char) data;
                    data = inputStreamReader.read();
                }

                int code = conn.getResponseCode();
                Log.e("code", String.valueOf(code));
                if (code == 200){
                    return result;
                }

            } catch (Exception ex){
                Log.e("addTable", ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            if (s != null){
                Toast.makeText(ListTableActivity.this, s, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ListTableActivity.this, "Table Number Already Used", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GetTableTask extends AsyncTask<String, String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {
                URL url = new URL("http://10.0.2.2:5000/Api/Table");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("Authorization", auth.getToken());

                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1){
                    result += (char) data;
                    data = inputStreamReader.read();
                }

                if (conn.getResponseCode() == 200){
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Table table = new Table();
                        table.setId(jsonObject.getString("id"));
                        table.setNumber(jsonObject.getInt("number"));
                        table.setCode(jsonObject.getString("code"));
                        table.setTotal(jsonObject.getInt("total"));
                        tables.add(table);
                    }
                } else {
                    startActivity(new Intent(ListTableActivity.this, LoginActivity.class));
                    Toast.makeText(ListTableActivity.this, "Login First", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex){
                Log.e("getTable", ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            binding.rvTable.setLayoutManager(new LinearLayoutManager(ListTableActivity.this, LinearLayoutManager.VERTICAL, false));
            binding.rvTable.setAdapter(new TableAdapter(tables, new TableAdapter.OnItemClickListener() {
                @Override
                public void onDetailClick(String id) {
                    Intent i = new Intent(ListTableActivity.this, AdminOrdersActivity.class);
                    i.putExtra("id", id);
                    startActivity(i);
                }
            }));
        }
    }
}