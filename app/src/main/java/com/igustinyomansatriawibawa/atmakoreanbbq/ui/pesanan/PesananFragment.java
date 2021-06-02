package com.igustinyomansatriawibawa.atmakoreanbbq.ui.pesanan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.igustinyomansatriawibawa.atmakoreanbbq.API.Api;
import com.igustinyomansatriawibawa.atmakoreanbbq.AfterQrActivity;
import com.igustinyomansatriawibawa.atmakoreanbbq.MainActivity;
import com.igustinyomansatriawibawa.atmakoreanbbq.R;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.FragmentMenuBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.FragmentPesananBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.FragmentPesananBindingImpl;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.AfterQrMenu;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.MenuViewModel;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.adapter.RecyclerViewMenu;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.adapter.RecyclerViewPesanan;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.model.Menu;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.pesanan.model.Pesanan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class PesananFragment extends Fragment {

    private HomeViewModel homeViewModel;
    SharedPreferences shared;
    SharedPreferences getShared;
    private ArrayList<Pesanan> listPesanan;
    private RecyclerView recyclerView;
    private FragmentPesananBinding pesananBinding;
    private RecyclerViewPesanan adapter;
    private TextView txtTotalHarga;
    private int id_reservasi,id;
    private View view;
    private  double totalHarga = 0;
    private MaterialButton btnCheckout;

//    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pesananBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_pesanan,container, false);
        view = pesananBinding.getRoot();
        txtTotalHarga = view.findViewById(R.id.text_total);

        AfterQrActivity main = (AfterQrActivity) getActivity();
        shared = view.getContext().getSharedPreferences("getId", Context.MODE_PRIVATE);
        id = shared.getInt("id", -1);
        getShared = view.getContext().getSharedPreferences("getIdReservasi",Context.MODE_PRIVATE);
        id_reservasi = getShared.getInt("id_reservasi",-1);

        btnCheckout = view.findViewById(R.id.btnPesan);
        listPesanan = new ArrayList<Pesanan>();
        getAllOrder();

         btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext())
                            .setTitle("Apakah sudah yakin dengan pesanan anda??");


                    alert.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkout(id_reservasi,id);

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

        recyclerView = pesananBinding.rvPesanan;

        adapter = new RecyclerViewPesanan(getContext(),listPesanan);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

//
//        searchView();
        return view;
    }

//    private  void getTotalHarga(ArrayList<Pesanan> listPesanan){
//        for (int i = 0; i < listPesanan.size() ; i++) {
//            listPesanan.getClass().getTota
//        }        return;
//    }

    private void checkout(final int id_reservasi, final int id){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, Api.URL_UPDATEPESANAN+id,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Navigation.findNavController(view).navigate(R.id.navigation_pesanan);
                            Toast.makeText(view.getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),"Unable to pesan: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                int status_pesanan = 5;

                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_transaksi", String.valueOf(id));
                params.put("id_reservasi", String.valueOf(id_reservasi));

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

    private void getAllOrder(){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading...");
        progressDialog.setTitle("Menampilkan data pesanan");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, Api.URL_SHOWORDER+id
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data menu
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!listPesanan.isEmpty())
                        listPesanan.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        Integer id       = jsonObject.optInt("id");
                        Integer id_menu   = jsonObject.optInt("id_menu");
                        Integer id_reservasi   = jsonObject.optInt("id_reservasi");
                        Integer id_transaksi  = jsonObject.optInt("id_transaksi");
                        Integer jumlah      = jsonObject.optInt("jumlah");
                        Double total_harga = jsonObject.optDouble("total_harga");
                        String nama_menu      = jsonObject.optString("nama_menu");
                        String tipe_menu = jsonObject.optString("tipe");
                        String gambar = jsonObject.optString("gambar");
                        Integer status_pesanan = jsonObject.optInt("status_pesanan");

                        totalHarga=totalHarga+total_harga;

                        System.out.println(totalHarga);

                        //Membuat objek Pesanan
                        Pesanan pesanan = new Pesanan(id, id_menu, id_reservasi, id_transaksi, jumlah, total_harga, nama_menu, tipe_menu, gambar, status_pesanan);

                        listPesanan.add(pesanan);

                    }
                    adapter.notifyDataSetChanged();
                    txtTotalHarga.setText("Total Harga : Rp. " + String.valueOf(totalHarga));
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke request queue yang sudah dideklarasi
        queue.add(jsonObjectRequest);
    }
}