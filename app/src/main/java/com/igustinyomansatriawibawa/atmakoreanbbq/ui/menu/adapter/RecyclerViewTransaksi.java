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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.igustinyomansatriawibawa.atmakoreanbbq.R;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.ItemPesananBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.ItemTransaksiBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.pesanan.model.Pesanan;

import java.util.List;

import static com.igustinyomansatriawibawa.atmakoreanbbq.API.Api.ROOT_URL;

public class RecyclerViewTransaksi extends RecyclerView.Adapter<RecyclerViewTransaksi.TransaksiViewHolder>{

    private Pesanan pesanan;
    private Context context;
    private List<Pesanan> pesananList;
    private List<Pesanan> pesananListFilter;
    SharedPreferences shared;
    SharedPreferences getShared;
    int id,id_reservasi;

    public RecyclerViewTransaksi(Context context, List<Pesanan> menuList) {
        this.context = context;
        this.pesananList = menuList;
        this.pesananListFilter = menuList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewTransaksi.TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        //Get idUser from sharedpreferences
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        id = shared.getInt("id", -1);
        getShared = context.getSharedPreferences("getIdReservasi",Context.MODE_PRIVATE);
        id_reservasi = getShared.getInt("id_reservasi",-1);

        ItemTransaksiBinding item = ItemTransaksiBinding.inflate(layoutInflater, viewGroup, false);
        return new RecyclerViewTransaksi.TransaksiViewHolder(item);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewTransaksi.TransaksiViewHolder holder, final int position) {
        final Pesanan pesanan = pesananList.get(position);

        holder.txtNamaMenu.setText(pesanan.getNama_menu());
        holder.txtHarga.setText("Subtotal : Rp. " +String.valueOf(pesanan.getTotal_harga()));
        holder.txtTipe.setText(pesanan.getTipe_menu());
        holder.txtJumlah.setText("Jumlah : "+ String.valueOf(pesanan.getJumlah()));
        Glide.with(context).load(ROOT_URL+pesanan.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return pesananListFilter!=null? pesananListFilter.size() : 0;
    }

    public class TransaksiViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaMenu, txtHarga, txtJumlah, txtTipe;
        private int id;
        private MaterialButton btnEdit, btnHapus;
        private ImageView ivImage;

        public TransaksiViewHolder(ItemTransaksiBinding itemView) {
            super(itemView.getRoot());
            txtHarga = itemView.getRoot().findViewById(R.id.tv_subtotal);
            txtNamaMenu = itemView.getRoot().findViewById(R.id.tv_namamenu);
            txtJumlah = itemView.getRoot().findViewById(R.id.tv_jumlah);
            txtTipe = itemView.getRoot().findViewById(R.id.tv_tipemenu);
            ivImage = itemView.getRoot().findViewById(R.id.imagemenu);
        }
    }

}
