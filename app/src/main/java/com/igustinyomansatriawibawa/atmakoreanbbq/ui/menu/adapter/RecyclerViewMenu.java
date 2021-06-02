package com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.igustinyomansatriawibawa.atmakoreanbbq.API.Api;
import com.igustinyomansatriawibawa.atmakoreanbbq.AfterQrActivity;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.FragmentAfterQrMenuBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.FragmentMenuBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.ItemMenuBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.ItemAfterQrMenuBinding;

import com.igustinyomansatriawibawa.atmakoreanbbq.R;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.AfterQrMenu;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.model.Menu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.igustinyomansatriawibawa.atmakoreanbbq.API.Api.ROOT_URL;

public class RecyclerViewMenu extends RecyclerView.Adapter<RecyclerViewMenu.MenuViewHolder> {

    private Menu menu;
    private Context context;
    private List<Menu> menuList;
    private List<Menu> menuListFilter;
    SharedPreferences shared;
    SharedPreferences getShared;
    int id,id_reservasi;


    public RecyclerViewMenu(Context context, List<Menu> menuList) {
        this.context = context;
        this.menuList = menuList;
        this.menuListFilter = menuList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewMenu.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        //Get idUser from sharedpreferences
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        id = shared.getInt("id", -1);
        getShared = context.getSharedPreferences("getIdReservasi",Context.MODE_PRIVATE);
        id_reservasi = getShared.getInt("id_reservasi",-1);

        if (id == -1) {
            ItemMenuBinding binding = ItemMenuBinding.inflate(layoutInflater, viewGroup, false);
            return new RecyclerViewMenu.MenuViewHolder(binding);
        }
        else {
            ItemAfterQrMenuBinding item = ItemAfterQrMenuBinding.inflate(layoutInflater, viewGroup, false);
            return new RecyclerViewMenu.MenuViewHolder(item);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position) {
        final Menu menu = menuListFilter.get(position);

        holder.txtNamaMenu.setText(menu.getNama_menu());
        holder.txtHarga.setText("Rp." +String.valueOf(menu.getHarga()));
        holder.txtDesk.setText(menu.getDeskripsi());
        holder.txtTipe.setText(menu.getTipe());
        holder.txtUnit.setText("/"+menu.getUnit());
        Glide.with(context).load(ROOT_URL+menu.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivImage);
        if(id!=-1){
            holder.txtStok.setText("Stok : "+String.valueOf(menu.getStok()));
            holder.btnTambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext())
                            .setTitle("Masukan jumlah ");

                    final EditText input = new EditText(view.getContext());
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setRawInputType(Configuration.KEYBOARD_12KEY);
                    input.setGravity(Gravity.CENTER);

                    input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
                    alert.setView(input);

                    alert.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(input.getText().toString().equals("")){
                                return;
                            }

                            Integer jumlah = Integer.parseInt(String.valueOf(input.getText()));

                            if(jumlah>0 && jumlah<menu.getStok()){
                                tambahKeranjang(menu, jumlah,id,id_reservasi);
                                Navigation.findNavController(view).navigate(R.id.navigation_menu);
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(context, "Jumlah beli harus kurang dari stok", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.show();
                }
            });
        }

    }

    private void tambahKeranjang(@NonNull final Menu menu, final int jumlah, final int id_reservasi, final int id){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_STORE_UPDATE,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("message").equals("Add Pesanan Success")){
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Unable to pesan: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                double totalharga = menu.getHarga()*jumlah;
                int status_pesanan = 5;

                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_menu", String.valueOf(menu.getId()));
                params.put("id_transaksi", String.valueOf(id_reservasi));
                params.put("id_reservasi", String.valueOf(id));
                params.put("jumlah", String.valueOf(jumlah));
                params.put("status_pesanan", String.valueOf(status_pesanan));
                params.put("total_harga", String.valueOf(totalharga));

                System.out.println("params set!");
                System.out.println(menu.getId());
                System.out.println(id);
                System.out.println(id_reservasi);
                System.out.println(jumlah);
                System.out.println(totalharga);
                System.out.println(status_pesanan);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };
        queue.add(stringRequest);

    }

    @Override
    public int getItemCount() {
        return menuListFilter!=null? menuListFilter.size() : 0;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaMenu, txtHarga, txtTipe, txtUnit, txtDesk, txtStok;
        private int id;
        private ImageView ivImage;
        MaterialButton btnTambah;

        public MenuViewHolder(ItemMenuBinding itemView) {
            super(itemView.getRoot());
            txtDesk = itemView.getRoot().findViewById(R.id.tv_deskripsi);
            txtHarga = itemView.getRoot().findViewById(R.id.tv_harga);
            txtNamaMenu = itemView.getRoot().findViewById(R.id.tv_namamenu);
            txtTipe = itemView.getRoot().findViewById(R.id.tv_tipemenu);
            txtUnit = itemView.getRoot().findViewById(R.id.tv_unit);
            ivImage = itemView.getRoot().findViewById(R.id.imagemenu);
        }

        public MenuViewHolder(ItemAfterQrMenuBinding itemView) {
            super(itemView.getRoot());
            txtStok = itemView.getRoot().findViewById(R.id.tv_stok);
            txtDesk = itemView.getRoot().findViewById(R.id.tv_deskripsi);
            txtHarga = itemView.getRoot().findViewById(R.id.tv_harga);
            txtNamaMenu = itemView.getRoot().findViewById(R.id.tv_namamenu);
            txtTipe = itemView.getRoot().findViewById(R.id.tv_tipemenu);
            txtUnit = itemView.getRoot().findViewById(R.id.tv_unit);
            ivImage = itemView.getRoot().findViewById(R.id.imagemenu);
            btnTambah = itemView.getRoot().findViewById(R.id.btnTambah);
        }
    }

    private Filter search = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String input = charSequence.toString().toLowerCase();
            if(input.isEmpty()){
                menuListFilter = menuList;
            }else{
                List<Menu> filteredList = new ArrayList<>();
                for(Menu menu : menuList){
                    if(menu.getNama_menu().toLowerCase().contains(input)||
                    menu.getTipe().toLowerCase().contains(input)){
                        filteredList.add(menu);
                    }
                }
                menuListFilter = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = menuListFilter;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            menuListFilter = (ArrayList<Menu>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    public Filter getFilter(){
        return search;
    }
}