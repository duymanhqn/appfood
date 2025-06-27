// app/src/main/java/com/example/foodapp/CartAdapter.java
package com.example.foodapp;

import android.content.Context;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.foodapp.apiImages.CartModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Holder> {
    private final Context ctx;
    private final List<CartModel> list;
    private final Runnable onTotalChanged;

    public CartAdapter(Context ctx, List<CartModel> list, Runnable onTotalChanged) {
        this.ctx = ctx;
        this.list = list;
        this.onTotalChanged = onTotalChanged;
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_cart, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        CartModel m = list.get(pos);

        h.txtTen.setText(m.getTenMon());
        h.txtGia.setText(m.getDongia() + m.getDVT());
        h.tvQty.setText(String.valueOf(m.getSoluong()));
        Glide.with(ctx)
                .load("http://10.0.2.2:8888/images/" + m.getHinhanh())
                .into(h.img);

        h.btnDecrease.setOnClickListener(v -> {
            int newQty = m.getSoluong() - 1;
            if (newQty < 1) return;
            updateQty(m.getIdGH(), newQty, success -> {
                if (success) {
                    m.setSoluong(newQty);
                    notifyItemChanged(pos);
                    onTotalChanged.run();
                }
            });
        });

        h.btnIncrease.setOnClickListener(v -> {
            int newQty = m.getSoluong() + 1;
            updateQty(m.getIdGH(), newQty, success -> {
                if (success) {
                    m.setSoluong(newQty);
                    notifyItemChanged(pos);
                    onTotalChanged.run();
                }
            });
        });

        h.btnDelete.setOnClickListener(v -> {
            deleteItem(m.getIdGH(), success -> {
                if (success) {
                    list.remove(pos);
                    notifyItemRemoved(pos);
                    onTotalChanged.run();
                }
            });
        });
    }

    @Override public int getItemCount() { return list.size(); }

    class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtTen, txtGia, tvQty;
        Button btnDecrease, btnIncrease, btnDelete;

        Holder(@NonNull View v) {
            super(v);
            img         = v.findViewById(R.id.imgSanPhamCart);
            txtTen      = v.findViewById(R.id.txtTenCart);
            txtGia      = v.findViewById(R.id.txtGiaCart);
            btnDecrease = v.findViewById(R.id.btn_decrease);
            tvQty       = v.findViewById(R.id.tv_quantity);
            btnIncrease = v.findViewById(R.id.btn_increase);
            btnDelete   = v.findViewById(R.id.btn_delete);
        }
    }

    // Gọi PUT /api/cart/:idGH
    private void updateQty(int idGH, int qty, Callback cb) {
        String url = "http://10.0.2.2:8888/api/cart/" + idGH;
        RequestQueue q = Volley.newRequestQueue(ctx);
        JSONObject body = new JSONObject();
        try { body.put("sluong", qty); }
        catch (JSONException e) { e.printStackTrace(); }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, body,
                resp -> cb.onResult(true),
                err -> { Toast.makeText(ctx, "Cập nhật thất bại", Toast.LENGTH_SHORT).show(); cb.onResult(false); }
        );
        q.add(req);
    }

    // Gọi DELETE /api/cart/:idGH
    private void deleteItem(int idGH, Callback cb) {
        String url = "http://10.0.2.2:8888/api/cart/" + idGH;
        RequestQueue q = Volley.newRequestQueue(ctx);
        StringRequest req = new StringRequest(Request.Method.DELETE, url,
                resp -> cb.onResult(true),
                err -> { Toast.makeText(ctx, "Xóa thất bại", Toast.LENGTH_SHORT).show(); cb.onResult(false); }
        );
        q.add(req);
    }

    interface Callback { void onResult(boolean success); }
}
