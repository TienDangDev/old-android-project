package edu.hanu.a2_2001040203.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.hanu.a2_2001040203.CartActivity;
import edu.hanu.a2_2001040203.R;
import edu.hanu.a2_2001040203.db.DbHelper;
import edu.hanu.a2_2001040203.helper.ImageLoadTask;
import edu.hanu.a2_2001040203.model.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> productList;
    private OnProductQuantityChangedListener listener;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    public CartAdapter(List<Product> productList, OnProductQuantityChangedListener onProductQuantityChangedListener) {
        this.productList = productList;
        this.listener = onProductQuantityChangedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImage;
        TextView cartName;
        TextView cartPrice;
        TextView cartAmount;
        TextView prodTotalPrice;
        ImageButton upBtn;
        ImageButton downBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cartImage);
            cartName = itemView.findViewById(R.id.cartName);
            cartPrice = itemView.findViewById(R.id.cartPrice);
            cartAmount = itemView.findViewById(R.id.num);
            prodTotalPrice = itemView.findViewById(R.id.totalProdPrice);
            upBtn = itemView.findViewById(R.id.plusOne);
            downBtn = itemView.findViewById(R.id.minusOne);

        }

        public void bind(Product product){
            long price = product.getPrice();
            int amount = product.getQuantity();
            String thumbnail = product.getThumbnail();
            long totalCartPrice = price * amount;
            String formattedPrice = String.format(Locale.getDefault(), "%,d VND", price);
            String formattedTotalCartPrice = String.format(Locale.getDefault(), "%,d d", totalCartPrice);

            //bind
            ImageLoadTask imageLoadTask = new ImageLoadTask(thumbnail,cartImage);
            cartPrice.setText(formattedPrice);
            cartName.setText(product.getName());
            prodTotalPrice.setText(formattedTotalCartPrice);
            cartAmount.setText(String.valueOf(amount));

            //total price for each items
            cartAmount.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    int quantity = Integer.parseInt(v.getText().toString());
                    product.setQuantity(quantity);
                    prodTotalPrice.setText(String.format(Locale.getDefault(), "%,d d", product.getPrice() * product.getQuantity()));
                    listener.onProductQuantityChanged();
                    return true;
                }
                return false;
            });

            //image load + bind
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Bitmap> futureBitmap = executorService.submit(imageLoadTask);
            try {
                Bitmap bitmap = futureBitmap.get();
                cartImage.setImageBitmap(bitmap);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            //product amount button
            upBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentAmount = product.getQuantity();
                    product.setQuantity(currentAmount + 1);
                    long newTotalPrice = getTotalPrice();
                    cartAmount.setText(String.valueOf(currentAmount));
                    prodTotalPrice.setText(String.format(Locale.getDefault(), "%,d VND", newTotalPrice));
                    listener.onProductQuantityChanged();
                    // notify the adapter that the data has changed
                    notifyDataSetChanged();
                }
            });

            downBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentAmount = product.getQuantity();
                    if (currentAmount > 1) {
                        product.setQuantity(currentAmount - 1);
                        long newTotalPrice = getTotalPrice();
                        cartAmount.setText(String.valueOf(currentAmount));
                        // notify the adapter that the data has changed
                        notifyDataSetChanged();
                    }
                    else {
                        int id = product.getId();
                        Context context = view.getContext();
                        DbHelper dbHelper = new DbHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db = dbHelper.getWritableDatabase();
                        int result = db.delete("product", "id = ?", new String[]{ id+"" });
                        productList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        listener.onProductQuantityChanged();
                    }
                }
            });
        }
    }
    public long getTotalPrice() {
        long totalPrice = 0;
        for (Product product : productList) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
        return totalPrice;
    }


    public interface OnProductQuantityChangedListener {
        void onProductQuantityChanged();
    }



}


