package com.igustinyomansatriawibawa.atmakoreanbbq.ui.menu.model;

import java.io.Serializable;

public class Menu implements Serializable {

    public int id;
    public int id_bahan;
    public String nama_menu;
    public String deskripsi;
    public String unit;
    public String tipe;
    public int stok;
    public double harga;
    public int serving_size;
    public String gambar;
    public int status_hapus;

    public Menu(int id, int id_bahan, String nama_menu, String deskripsi,
                String unit, String tipe, int stok, double harga, int serving_size, String gambar,
                int status_hapus) {
        this.id = id;
        this.id_bahan = id_bahan;
        this.nama_menu = nama_menu;
        this.deskripsi = deskripsi;
        this.unit = unit;
        this.tipe = tipe;
        this.stok = stok;
        this.harga = harga;
        this.serving_size = serving_size;
        this.gambar = gambar;
        this.status_hapus = status_hapus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_bahan() {
        return id_bahan;
    }

    public void setId_bahan(int id_bahan) {
        this.id_bahan = id_bahan;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getServing_size() {
        return serving_size;
    }

    public void setServing_size(int serving_size) {
        this.serving_size = serving_size;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getStatus_hapus() {
        return status_hapus;
    }

    public void setStatus_hapus(int status_hapus) {
        this.status_hapus = status_hapus;
    }
}
