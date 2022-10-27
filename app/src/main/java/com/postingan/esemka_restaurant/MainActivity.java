package com.postingan.esemka_restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.postingan.esemka_restaurant.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    CodeTask codeTask;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        codeTask = new CodeTask();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("LOADING..");

        binding.btnLoginStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        binding.btnHomeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etTableCode.getText().length() == 0){
                    binding.etTableCode.setError("Field Still Empty");
                } else {
                    codeTask.execute();
                }
            }
        });
    }

    public class CodeTask extends AsyncTask<String, String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                String code = binding.etTableCode.getText().toString();
                URL url = new URL("http://10.0.2.2:5000/Api/Table/" + code);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("accept", "text/plain");

                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1){
                    result += (char) data;
                    data = inputStreamReader.read();
                }

                if (conn.getResponseCode() == 200){
                    startActivity(new Intent(MainActivity.this, ContainerActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Table Not Found", Toast.LENGTH_SHORT);
                }
            } catch (Exception ex){
                Log.e("table", ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

        }
    }
}