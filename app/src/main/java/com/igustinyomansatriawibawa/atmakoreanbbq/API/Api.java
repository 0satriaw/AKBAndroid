package com.igustinyomansatriawibawa.atmakoreanbbq.API;

public class Api {
    public static final String ROOT_URL = "https://atmakb.xyz/public";
    public static final String ROOT_API = ROOT_URL+"/api/";
    public static final String ROOT_SCANQR = ROOT_API+"scanqr/";

    //MENU
    public static final String URL_MENU = ROOT_API+"menu"; // GET

    //PESANAN
    public static final String URL_STORE_UPDATE = ROOT_API+"pesanan";
    public static final String URL_UPDATECART = ROOT_API+"updatecart/";
    public static final String URL_SHOWORDER = ROOT_API+"showorder/";
    public static final String URL_DELETEORDER = ROOT_API+"pesanan/";
    public static final String URL_SHOWPESANAN = ROOT_API+"showpesanan/";
    public static final String URL_UPDATEPESANAN = ROOT_API+"updatepesanan/";

    //RESERVASI
    public static final String URL_ENDRESERVASI = ROOT_API+"endreservasi/";

}
