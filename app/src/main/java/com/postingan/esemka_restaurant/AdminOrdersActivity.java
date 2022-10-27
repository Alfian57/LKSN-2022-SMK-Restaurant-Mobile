package com.postingan.esemka_restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.postingan.esemka_restaurant.Adapter.OrderAdapter;
import com.postingan.esemka_restaurant.Helper.Auth;
import com.postingan.esemka_restaurant.Model.Menu;
import com.postingan.esemka_restaurant.Model.Order;
import com.postingan.esemka_restaurant.Model.OrderDetail;
import com.postingan.esemka_restaurant.databinding.ActivityAdminOrdersBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdminOrdersActivity extends AppCompatActivity {
    ActivityAdminOrdersBinding binding;
    GetOrderTask getOrderTask;
    ClosedTableTask closedTableTask;
    ProgressDialog progressDialog;
    Auth auth;
    String id;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            Toast.makeText(this, "Failed to Get Id", Toast.LENGTH_SHORT).show();
        } else {
            id = bundle.getString("id");
        }

        total = 0;
        progressDialog = new ProgressDialog(AdminOrdersActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        getOrderTask = new GetOrderTask();
        closedTableTask = new ClosedTableTask();
        getOrderTask.execute();
        auth = new Auth(AdminOrdersActivity.this);

        binding.btnCloseTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closedTableTask.execute();
            }
        });
    }

    public class ClosedTableTask extends AsyncTask<String, String, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL("http://10.0.2.2:5000/Api/Table/" + id + "/Close");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("accept", "text/plain");
                connection.setRequestProperty("Authorization", auth.getToken());

                if (connection.getResponseCode() == 200){
                    return true;
                }

            } catch (Exception ex){
                Log.e("closeTable", ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean){
                GetOrderTask getOrderTask = new GetOrderTask();
                getOrderTask.execute();
            }
        }
    }

    public class GetOrderTask extends AsyncTask<String, String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL("http://10.0.2.2:5000/Api/Table/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("accept", "text/plain");
                conn.setRequestProperty("Authorization", auth.getToken());

                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1){
                    result += (char) data;
                    data = inputStreamReader.read();
                }

                if (conn.getResponseCode() == 200){
                    return result;
                } else {
                    return null;
                }
            } catch (Exception ex){
                Log.e("getOrder", ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            if (s == null){
                Toast.makeText(AdminOrdersActivity.this, "Failed To Get Detail Order", Toast.LENGTH_SHORT).show();
            } else {
                List<Menu> menus = new ArrayList<>();
                List<OrderDetail> orderDetails = new ArrayList<>();
                List<Order> orders = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int number = jsonObject.getInt("number");
                    String code = jsonObject.getString("code");
                    binding.tvTableNameOrder.setText("Table " + number);
                    binding.tvTableCodeOrder.setText(code);
                    JSONArray jsonArray = jsonObject.getJSONArray("orders");

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("orderDetails");
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                            JSONArray jsonArray3 = jsonObject2.getJSONArray("menu");

                            for (int k = 0; k < jsonArray3.length(); k++) {
                                JSONObject jsonObject3 = jsonArray3.getJSONObject(k);

                                Menu menu = new Menu();
                                menu.setName(jsonObject3.getString("name"));
                                menu.setPrice(jsonObject3.getInt("price"));
                                menus.add(menu);
                            }

                            OrderDetail orderDetail = new OrderDetail();
                            orderDetail.setSubTotal(jsonObject2.getInt("subTotal"));
                            total += jsonObject2.getInt("subTotal");
                            orderDetail.setQuantity(jsonObject2.getInt("quantity"));
                            orderDetail.setMenus(menus);
                            orderDetails.add(orderDetail);
                        }


                        Order order = new Order();
                        order.setStatus(jsonObject1.getString("status"));
                        order.setCreatedAt(jsonObject1.getString("createdAt"));
                        order.setOrderDetails(orderDetails);
                        orders.add(order);
                    }
                } catch (JSONException e) {
                    //Log.e("getOrder", e.getMessage());
                }
                binding.rvOrder.setLayoutManager(new LinearLayoutManager(AdminOrdersActivity.this, LinearLayoutManager.VERTICAL, false));
                binding.rvOrder.setAdapter(new OrderAdapter(orders));
                binding.tvTotal.setText("Rp " + total);
            }

        }
    }
}