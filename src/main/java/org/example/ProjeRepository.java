package org.example;

import java.io.File;
import java.util.List;

public interface ProjeRepository {

    void projeEkle(Proje proje, File dosya);

    List<Proje> tumProjeleriGetir();
    Proje projeBulByAd(String ad);
    void dosyayiIndir(String dosyaAdi, String hedefYol);
}
