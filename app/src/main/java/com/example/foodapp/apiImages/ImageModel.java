package com.example.foodapp.apiImages;

public class ImageModel {
    public static class MonAn {
        private String tenmAn;
        private String Gia;
        private String DVT;
        private String hinhanh;
        private String mota;

        public MonAn(String tenmAn, String Gia, String DVT, String hinhanh, String mota) {
            this.tenmAn = tenmAn;
            this.Gia = Gia;
            this.DVT = DVT;
            this.hinhanh = hinhanh;
            this.mota = mota;
        }

        // Getters
        public String getTenmAn() {
            return tenmAn;
        }

        public String getGia() {
            return Gia;
        }

        public String getDVT() {
            return DVT;
        }

        public String getHinhanh() {
            return hinhanh;
        }

        public String getMota() {
            return mota;
        }

        // Setters
        public void setTenmAn(String tenmAn) {
            this.tenmAn = tenmAn;
        }

        public void setGia(String Gia) {
            this.Gia = Gia;
        }

        public void setDVT(String DVT) {
            this.DVT = DVT;
        }

        public void setHinhanh(String hinhanh) {
            this.hinhanh = hinhanh;
        }

        public void setMota(String mota) {
            this.mota = mota;
        }
    }
}
