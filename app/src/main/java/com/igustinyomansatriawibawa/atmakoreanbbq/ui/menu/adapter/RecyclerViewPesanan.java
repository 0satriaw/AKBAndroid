package com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

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
import com.igustinyomansatriawibawa.atmakoreanbbq.R;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.ItemAfterQrMenuBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.ItemMenuBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.ItemPesananBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.model.Menu;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.pesanan.model.Pesanan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.igustinyomansatriawibawa.atmakoreanbbq.API.Api.ROOT_URL;

public class RecyclerViewPesanan extends RecyclerView.Adapter<RecyclerViewPesanan.PesananViewHolder> {

    private Pesanan pesanan;
    private Context context;
    private List<Pesanan> pesananList;
    private List<Pesanan> pesananListFilter;
    SharedPreferences shared;
    SharedPreferences getShared;
    int id,id_reservasi;

    public RecyclerViewPesanan(Context context, List<Pesanan> menuList) {
        this.context = context;
        this.pesananList = menuList;
        this.pesananListFilter = menuList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewPesanan.PesananViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        //Get idUser from sharedpreferences
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        id = shared.getInt("id", -1);
        getShared = context.getSharedPreferences("getIdReservasi",Context.MODE_PRIVATE);
        id_reservasi = getShared.getInt("id_reservasi",-1);

        ItemPesananBinding item = ItemPesananBinding.inflate(layoutInflater, viewGroup, false);
        return new RecyclerViewPesanan.PesananViewHolder(item);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewPesanan.PesananViewHolder holder, final int position) {
        final Pesanan pesanan = pesananList.get(position);

        holder.txtNamaMenu.setText(pesanan.getNama_menu());
        holder.txtHarga.setText("Subtotal : Rp. " +String.valueOf(pesanan.getTotal_harga()));
        holder.txtTipe.setText(pesanan.getTipe_menu());
        holder.txtJumlah.setText("Jumlah : "+ String.valueOf(pesanan.getJumlah()));
        Glide.with(context).load(ROOT_URL+pesanan.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivImage);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
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

                        if(jumlah>0 ){
                            editPesanan(pesanan, view,jumlah,id,id_reservasi);

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

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext())
                        .setTitle("Apakah anda yakin ingin menghapus pesanan?");


                alert.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            hapusPesanan(pesanan);
                            Navigation.findNavController(view).navigate(R.id.navigation_pesanan);
                            notifyDataSetChanged();
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

    private void hapusPesanan(@NonNull final Pesanan pesanan){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, Api.URL_DELETEORDER+pesanan.getId(),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };
        queue.add(stringRequest);

    }

    private void editPesanan(@NonNull final Pesanan pesanan, View view ,final int jumlah, final int id_reservasi, final int id){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, Api.URL_UPDATECART+pesanan.getId(),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("message").equals("Update Pesanan Success")){
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.navigation_pesanan);
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
                int status_pesanan = 5;

                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_menu", String.valueOf(pesanan.getId_menu()));
                params.put("id_transaksi", String.valueOf(id_reservasi));
                params.put("id_reservasi", String.valueOf(id));
                params.put("jumlah", String.valueOf(jumlah));
                params.put("status_pesanan", String.valueOf(status_pesanan));
                params.put("total_harga", String.valueOf(pesanan.getTotal_harga()));

//                System.out.println("params set!");
//                System.out.println(menu.getId());
//                System.out.println(id);
//                System.out.println(id_reservasi);
//                System.out.println(jumlah);
//                System.out.println(totalharga);
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
        return pesananListFilter!=null? pesananListFilter.size() : 0;
    }

    public class PesananViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaMenu, txtHarga, txtJumlah, txtTipe;
        private int id;
        private MaterialButton btnEdit, btnHapus;
        private ImageView ivImage;

        public PesananViewHolder(ItemPesananBinding itemView) {
            super(itemView.getRoot());
            txtHarga = itemView.getRoot().findViewById(R.id.tv_subtotal);
            txtNamaMenu = itemView.getRoot().findViewById(R.id.tv_namamenu);
            txtJumlah = itemView.getRoot().findViewById(R.id.tv_jumlah);
            txtTipe = itemView.getRoot().findViewById(R.id.tv_tipe);
            ivImage = itemView.getRoot().findViewById(R.id.imagemenu);
            btnEdit = itemView.getRoot().findViewById(R.id.btnEdit);
            btnHapus = itemView.getRoot().findViewById(R.id.btnHapus);
        }
    }


}
