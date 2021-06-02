package com.igustinyomansatriawibawa.atmakoreanbbq.ui.scanqr;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.material.button.MaterialButton;
import com.igustinyomansatriawibawa.atmakoreanbbq.API.Api;
import com.igustinyomansatriawibawa.atmakoreanbbq.AfterQrActivity;
import com.igustinyomansatriawibawa.atmakoreanbbq.MainActivity;
import com.igustinyomansatriawibawa.atmakoreanbbq.R;
import com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.AfterQrMenu;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class ScanQR extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private MaterialButton btnQR;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private View view;
    private SharedPreferences shared, getShared;
    public static final int mode = Activity.MODE_PRIVATE;
    int id, id_transaksi;

    public ScanQR(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        view = inflater.inflate(R.layout.fragment_scanqr, container, false);

        btnQR = view.findViewById(R.id.btnQR);

        shared = this.getActivity().getSharedPreferences("getId",mode);
        getShared = this.getActivity().getSharedPreferences("getIdReservasi",mode);
        id = shared.getInt("id",-1);

        MainActivity mainActivity = (MainActivity)getActivity();

        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getContext()).withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent i = new Intent(mainActivity, QrCodeActivity.class);
                                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                permissionDeniedResponse.getRequestedPermission();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                            }
                        }).check();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=Activity.RESULT_OK){
            if(data==null){
                return;
            }
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if(result!=null){
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("Scan QR gagal");
                alertDialog.setButton(alertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;
        }
        if(requestCode==REQUEST_CODE_QR_SCAN){
            if(data==null){
                return;
            }
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            scanQR(result);
        }
    }

    public void scanQR(final String result){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data qr");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, Api.ROOT_SCANQR + result
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    if(response.getString("message").equals("Scan QR Berhasil")) {

                        JSONObject jsonObject = response.getJSONObject("data");
                        String id_transaksi = jsonObject.optString("id");
                        String id_reservasi = jsonObject.optString("id_reservasi");

                        SharedPreferences.Editor editor = shared.edit();
                        editor.putInt("id", Integer.parseInt(id_transaksi));
                        editor.apply();

                        SharedPreferences.Editor editor2 = getShared.edit();
                        editor2.putInt("id_reservasi", Integer.parseInt(id_reservasi));
                        editor2.apply();

                        Intent intent= new Intent(getActivity(), AfterQrActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
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
                Toast.makeText(view.getContext(),"Unable to place order " + error.toString(), Toast.LENGTH_SHORT).show();
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
        queue.add(stringRequest);
    }
}