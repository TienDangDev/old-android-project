package edu.hanu.a2_2001040203.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.hanu.a2_2001040203.MainActivity;
import edu.hanu.a2_2001040203.R;
import edu.hanu.a2_2001040203.db.DbHelper;
import edu.hanu.a2_2001040203.helper.ImageLoadTask;
import edu.hanu.a2_2001040203.model.Product;

public class prodAdapter extends RecyclerView.Adapter<prodAdapter.ProdHolder> {

    private List<Product> productList;
    public prodAdapter(List<Product> productList){
        this.productList = productList;
    }


    public class ProdHolder extends RecyclerView.ViewHolder{
        private TextView prodName;
        private ImageView prodImage;
        private TextView prodPrice;
        private ImageButton addCart;
        private Context context;

        public ProdHolder (@NonNull View prodView, Context context) {
            super(prodView);
            this.context = context;
            prodName = prodView.findViewById(R.id.prodName);
            prodPrice = prodView.findViewById(R.id.prodPrice);
            prodImage = prodView.findViewById(R.id.probImage);
            addCart = prodView.findViewById(R.id.addCart);
        }

        public void bind(Product prod){
            prodName.setText(prod.getName());
            ImageLoadTask imageLoadTask = new ImageLoadTask(prod.getThumbnail(), prodImage);
            String formattedPrice = String.format(Locale.getDefault(), "%,d d", prod.getPrice());
            System.out.println(formattedPrice);
            prodPrice.setText(formattedPrice);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Bitmap> futureBitmap = executorService.submit(imageLoadTask);

            try {
                Bitmap bitmap = futureBitmap.get();
                prodImage.setImageBitmap(bitmap);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }


    }


    @NonNull
    @Override
    public ProdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View prodView = inflater.inflate(R.layout.items, parent, false);
        return new ProdHolder(prodView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);

        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                DbHelper dbHelper = new DbHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String sql = "SELECT * FROM product WHERE id=?";
                Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(product.getId())});

                if (cursor.getCount() > 0) {
                    Toast.makeText(context, "You already added this product", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put("id", product.getId());
                    values.put("name",product.getName());
                    values.put("thumbnail", product.getThumbnail());
                    values.put("price", product.getPrice());
                    values.put("amount",1);

                    db.insert("product", null, values);
                    Toast.makeText(context, "Added to cart!", Toast.LENGTH_LONG).show();
                }
                cursor.close();
                db.close();

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }





}
