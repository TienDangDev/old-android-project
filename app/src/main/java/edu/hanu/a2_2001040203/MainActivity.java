package edu.hanu.a2_2001040203;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.SyncStateContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.hanu.a2_2001040203.adapter.prodAdapter;
import edu.hanu.a2_2001040203.model.Product;

public class MainActivity extends AppCompatActivity {
    private List<Product> productList;
    private RecyclerView prodList;
    private static final String url = "https://hanu-congnv.github.io/mpr-cart-api/products.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        productList = new ArrayList<>();
        RecyclerView prodList = findViewById(R.id.prodList);
        prodList.setLayoutManager(new GridLayoutManager(this,2));
        prodAdapter prodAdapter = new prodAdapter(productList);
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        Constants.executor.execute(new Runnable() {
            @Override
            public void run() {
                String json = loadJSON(url);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (json == null) {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            JSONArray root = new JSONArray(json);
                            for (int i = 0; i < root.length(); i++) {
                                JSONObject jsonObject = root.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String thumbnail = jsonObject.getString("thumbnail");
                                long price = jsonObject.getLong("unitPrice");
                                String category = jsonObject.getString("category");
                                productList.add(new Product(id,thumbnail,name,price,price, 1));
                                prodAdapter.notifyDataSetChanged();
                            }


//                            tvDescription.setText(description);
//                            JSONObject main = root.getJSONObject("main");
//                            int tempMin = main.getInt("temp_min");
//                            int tempMax = main.getInt("temp_max");
//                            tvTemp.setText(tempMin + "-" + tempMax);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//
                    }
                });
            }
        });

        prodList.setAdapter(prodAdapter);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cartBtn) {
            // Open cart activity
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Calculate the total price of all products in the cart
//    private double calculateCartTotalPrice() {
//        CartDatabaseHelper dbHelper = new CartDatabaseHelper(context);
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        String[] projection = {CartContract.CartEntry.COLUMN_NAME_PRODUCT_PRICE};
//        Cursor cursor = db.query(CartContract.CartEntry.TABLE_NAME, projection, null, null, null, null, null);
//
//        double totalPrice = 0;
//
//        while (cursor.moveToNext()) {
//            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(CartContract.CartEntry.COLUMN_NAME_PRODUCT_PRICE));
//            totalPrice += price;
//        }
//
//        cursor.close();
//        return totalPrice;
//    }
    public String loadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while(sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public boolean onCreateOptionMenu(Menu menu){
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            switch (item.getItemId()) {
//                // action with ID action_settings was selected
//                case R.id.action_settings:
//                    // this is where you put your own code to do what you want.
//                    break;
//                default:
//                    break;
//            }
//
//            return true;
//    }

}
