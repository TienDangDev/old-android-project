package edu.hanu.a2_2001040203;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.hanu.a2_2001040203.adapter.CartAdapter;
import edu.hanu.a2_2001040203.db.DbHelper;
import edu.hanu.a2_2001040203.model.Product;

public class CartActivity extends AppCompatActivity {

//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        RecyclerView cartList = findViewById(R.id.cartList);
        cartList.setLayoutManager(new LinearLayoutManager(this));

        List<Product> productList = getCartList();

        CartAdapter cartAdapter = new CartAdapter(productList, new CartAdapter.OnProductQuantityChangedListener() {
            @Override
            public void onProductQuantityChanged() {
                updateTotalPrice();
            }
        });

        cartList.setAdapter(cartAdapter);

        TextView totalCartPrice = findViewById(R.id.totalCartPrice);
        long totalPrice = cartAdapter.getTotalPrice();
        String formattedPrice = String.format(Locale.getDefault(), "%,d VND", totalPrice);
        totalCartPrice.setText(formattedPrice);
    }
    public ArrayList<Product> getCartList() {
        ArrayList<Product> productList = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] col = {"id", "thumbnail", "name", "price", "amount", "category"};
        Cursor cursor = db.query("product", col, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow("thumbnail"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                long price = cursor.getLong(cursor.getColumnIndexOrThrow("price"));
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                Product product = new Product(id, thumbnail, name, price, price, amount);
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return productList;
    }
    private void updateTotalPrice() {
        List<Product> productList = getCartList();

        long total = 0;
        for (Product product : productList) {
            total += product.getPrice() * product.getQuantity();
        }
        TextView totalPriceTextView = findViewById(R.id.totalCartPrice);
        totalPriceTextView.setText(String.format(Locale.getDefault(), "%,d VND", total));
    }


}