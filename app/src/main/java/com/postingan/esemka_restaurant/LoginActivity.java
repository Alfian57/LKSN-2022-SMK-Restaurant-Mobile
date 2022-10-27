package com.postingan.esemka_restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.postingan.esemka_restaurant.Helper.Auth;
import com.postingan.esemka_restaurant.databinding.ActivityLoginBinding;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    LoginTask loginTask;
    Auth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginTask = new LoginTask();
        auth = new Auth(LoginActivity.this);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("LOADING..");

        binding.btnLoginCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        binding.btnLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccess = true;

                if (binding.etEmail.getText().length() == 0){
                    binding.etEmail.setError("Field Still Empty");
                    isSuccess = false;
                }
                if (binding.etPassword.getText().length() == 0){
                    binding.etPassword.setError("Field Still Empty");
                    isSuccess = false;
                }

                if (isSuccess){
                    loginTask.execute();
                }
            }
        });
    }

    public class LoginTask extends AsyncTask<String, String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL("http://10.0.2.2:5000/Api/Auth");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("accept", "text/plain");
                conn.setRequestProperty("Content-Type", "application/json");

                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();

                String request = "{" +
                        "\"email\": \"" + email + "\"," +
                        "\"password\": \"" + password + "\"" +
                        "}";

                OutputStream outputStream = conn.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                outputStreamWriter.write(request);
                outputStreamWriter.flush();

                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1){
                    result += (char) data;
                    data = inputStreamReader.read();
                }

                if (conn.getResponseCode() == 200){
                    JSONObject jsonObject = new JSONObject(result);
                    String token = jsonObject.getString("token");
                    Log.e("token", token);
                    auth.setToken(token);
                    startActivity(new Intent(LoginActivity.this, ListTableActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT);
                }
            } catch (Exception ex){
                Log.e("login", ex.getMessage());
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