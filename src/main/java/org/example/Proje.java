package org.example;

public class Proje {
    private String id;
    private String ogrenciAdSoyad;
    private String ogrenciNo;
    private String ad;
    private String teslimTarihi;
    private String dosyaAdi;


    public Proje() {}

    public String getOgrenciAdSoyad() {
        return ogrenciAdSoyad;
    }

    public void setOgrenciAdSoyad(String ogrenciAdSoyad) {
        this.ogrenciAdSoyad = ogrenciAdSoyad;
    }

    public String getOgrenciNo() {
        return ogrenciNo;
    }

    public void setOgrenciNo(String ogrenciNo) {
        this.ogrenciNo = ogrenciNo;
    }

    public Proje(String ad, String teslimTarihi, String dosyaAdi, String ogrenciAdSoyad, String ogrenciNo){
        this.ad=ad;
        this.teslimTarihi=teslimTarihi;
        this.dosyaAdi=dosyaAdi;
        this.ogrenciAdSoyad = ogrenciAdSoyad;
        this.ogrenciNo = ogrenciNo;
    }

    public String getId(){return id;}

    public void setId(String id){this.id=id;}


    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }


    public String getTeslimTarihi() {
        return teslimTarihi;
    }

    public void setTeslimTarihi(String teslimTarihi) {
        this.teslimTarihi = teslimTarihi;
    }
    public String getDosyaAdi() {
        return dosyaAdi;
    }

    public void setDosyaAdi(String dosyaAdi) {
        this.dosyaAdi = dosyaAdi;
    }


}
