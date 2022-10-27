package com.postingan.esemka_restaurant.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.postingan.esemka_restaurant.Adapter.FoodAdapter;
import com.postingan.esemka_restaurant.ListTableActivity;
import com.postingan.esemka_restaurant.LoginActivity;
import com.postingan.esemka_restaurant.Model.Food;
import com.postingan.esemka_restaurant.R;
import com.postingan.esemka_restaurant.databinding.FragmentAyamBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AyamFragment extends Fragment {
    FragmentAyamBinding binding;
    ProgressDialog progressDialog;
    GetFoodTask getFoodTask;
    List<Food> foods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAyamBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(binding.getRoot().getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("LOADING...");
        getFoodTask = new GetFoodTask();
        getFoodTask.execute();

        return binding.getRoot();
    }

    public class GetFoodTask extends AsyncTask<String, String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL("http://10.0.2.2:5000/Api/Menu/Category/Ayam");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("accept", "text/plain");

                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1){
                    result += (char) data;
                    data = inputStreamReader.read();
                }

                if (conn.getResponseCode() == 200){
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Food food = new Food();
                        food.setId(jsonObject.getString("id"));
                        food.setName(jsonObject.getString("name"));
                        food.setPrice(jsonObject.getInt("price"));

                        foods.add(food);
                    }
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
            binding.rvAyam.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), RecyclerView.VERTICAL, false));
            binding.rvAyam.setAdapter(new FoodAdapter(foods));

        }
    }
}