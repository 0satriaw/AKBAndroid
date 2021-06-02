package com.igustinyomansatriawibawa.atmakoreanbbq.ui.pesanan.model;

public class Pesanan {

    public int id;
    public int id_menu;
    public int id_reservasi;
    public int id_transaksi;
    public int jumlah;
    public double total_harga;
    public String nama_menu;
    public String tipe_menu;
    public String gambar;
    public int status_pesanan;

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getTipe_menu() {
        return tipe_menu;
    }

    public void setTipe_menu(String tipe_menu) {
        this.tipe_menu = tipe_menu;
    }

    public Pesanan(int id, int id_menu, int id_reservasi, int id_transaksi, int jumlah, double total_harga, String nama_menu, String tipe_menu, String gambar, int status_pesanan) {
        this.id = id;
        this.id_menu = id_menu;
        this.id_reservasi = id_reservasi;
        this.id_transaksi = id_transaksi;
        this.jumlah = jumlah;
        this.total_harga = total_harga;
        this.nama_menu = nama_menu;
        this.tipe_menu = tipe_menu;
        this.gambar = gambar;
        this.status_pesanan = status_pesanan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public int getId_reservasi() {
        return id_reservasi;
    }

    public void setId_reservasi(int id_reservasi) {
        this.id_reservasi = id_reservasi;
    }

    public int getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(int id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(double total_harga) {
        this.total_harga = total_harga;
    }

    public int getStatus_pesanan() {
        return status_pesanan;
    }

    public void setStatus_pesanan(int status_pesanan) {
        this.status_pesanan = status_pesanan;
    }
}
