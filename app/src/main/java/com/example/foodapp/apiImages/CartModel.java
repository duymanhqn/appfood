
package com.example.foodapp.apiImages;

public class CartModel {
    private int idGH;
    private String TenMon;
    private int dongia;
    private String DVT;
    private String hinhanh;
    private int soluong;

    public CartModel(int idGH, String tenMon, int dongia, String DVT, String hinhanh, int soluong) {
        this.idGH = idGH;
        this.TenMon = tenMon;
        this.dongia = dongia;
        this.DVT = DVT;
        this.hinhanh = hinhanh;
        this.soluong = soluong;
    }

    public int getIdGH() { return idGH; }
    public String getTenMon() { return TenMon; }
    public int getDongia() { return dongia; }
    public String getDVT() { return DVT; }
    public String getHinhanh() { return hinhanh; }
    public int getSoluong() { return soluong; }

    public void setSoluong(int soluong) { this.soluong = soluong; }
}
