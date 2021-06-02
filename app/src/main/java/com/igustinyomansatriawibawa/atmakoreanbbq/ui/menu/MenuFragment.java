package com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu;

import android.app.ProgressDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.igustinyomansatriawibawa.atmakoreanbbq.API.Api;
import com.igustinyomansatriawibawa.atmakoreanbbq.MainActivity;
import com.igustinyomansatriawibawa.atmakoreanbbq.R;
import com.igustinyomansatriawibawa.atmakoreanbbq.databinding.FragmentMenuBinding;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.adapter.RecyclerViewMenu;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.model.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class MenuFragment extends Fragment {

    private ArrayList<Menu> listMenu;
    private RecyclerView recyclerView;
    private MenuViewModel menuViewModel;
    private FragmentMenuBinding menuBinding;
    private RecyclerViewMenu adapter;
    private View view;
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        menuBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_menu,container, false);
        view = menuBinding.getRoot();

        MainActivity main = (MainActivity)getActivity();

        listMenu = new ArrayList<Menu>();
        getAllMenu();

        recyclerView = menuBinding.rvMenu;

        adapter = new RecyclerViewMenu(getContext(),listMenu);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        searchView();
        return view;
    }

    private void searchView(){
        searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void getAllMenu(){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading...");
        progressDialog.setTitle("Menampilkan data menu");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, Api.URL_MENU
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data menu
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!listMenu.isEmpty())
                        listMenu.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String id       = jsonObject.optString("id");
                        String id_bahan   = jsonObject.optString("id_bahan");
                        String nama_menu   = jsonObject.optString("nama_menu");
                        String deskripsi  = jsonObject.optString("deskripsi");
                        String unit      = jsonObject.optString("unit");
                        String tipe = jsonObject.optString("tipe");
                        Integer stok      = jsonObject.optInt("stok");
                        Double harga = jsonObject.optDouble("harga");
                        String gambar       = jsonObject.optString("gambar");
                        Integer serving_size = jsonObject.optInt("serving_size");
                        Integer status_hapus = jsonObject.optInt("status_hapus");


                        System.out.println("Menu retrieved");

                        //Membuat objek menu
                        Menu menu =
                                new Menu(Integer.parseInt(id),Integer.parseInt(id_bahan),nama_menu,deskripsi,
                                        unit, tipe, stok, harga, serving_size, gambar, status_hapus);

                        listMenu.add(menu);

                    }
                    adapter.notifyDataSetChanged();
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