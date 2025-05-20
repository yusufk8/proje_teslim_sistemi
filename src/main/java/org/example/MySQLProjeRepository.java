package org.example;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.*;

public class MySQLProjeRepository implements ProjeRepository {
    private final Connection conn;

    public MySQLProjeRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void projeEkle(Proje proje, File dosya) {
        try {
            String sql = "INSERT INTO projeTeslim (ad, teslim_tarihi, dosya_adi, ogrenci_adsoyad, ogrenci_no, dosya_veri) VALUES ()";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, proje.getAd());
            stmt.setString(2, proje.getTeslimTarihi());
            stmt.setString(3, proje.getDosyaAdi());
            stmt.setString(4, proje.getOgrenciAdSoyad());
            stmt.setString(5, proje.getOgrenciNo());
            stmt.setBinaryStream(6, new FileInputStream(dosya));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Proje> tumProjeleriGetir() {
        List<Proje> liste = new ArrayList<>();
        try {
            String sql = "SELECT ad, teslim_tarihi, dosya_adi, ogrenci_adsoyad, ogrenci_no FROM projeTeslim";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Proje p = new Proje(
                        rs.getString("ad"),
                        rs.getString("teslim_tarihi"),
                        rs.getString("dosya_adi"),
                        rs.getString("ogrenci_adsoyad"),
                        rs.getString("ogrenci_no")
                );
                liste.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public Proje projeBulByAd(String ad) {
        try {
            String sql = "SELECT * FROM projeTeslim WHERE ad = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ad);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Proje(
                        rs.getString("ad"),
                        rs.getString("teslim_tarihi"),
                        rs.getString("dosya_adi"),
                        rs.getString("ogrenci_adsoyad"),
                        rs.getString("ogrenci_no")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void dosyayiIndir(String dosyaAdi, String hedefYol) {
        try {
            String sql = "SELECT dosya_veri FROM projeTeslim WHERE dosya_adi = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dosyaAdi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                InputStream is = rs.getBinaryStream("dosya_veri");
                Files.copy(is, new File(hedefYol).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
