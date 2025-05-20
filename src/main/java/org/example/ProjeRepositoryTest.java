package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.io.File;
import java.util.List;

public class ProjeRepositoryTest {
    public static void main(String[] args) {
        MongoClient mongoClient=MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db=mongoClient.getDatabase("teslim");
        ProjeRepository repo=new MongoProjeRepository(db);


        //Proje ekleme testi
        File bosFile = new File("dummy.txt"); // Geçici bir dosya
        Proje yeniProje= new Proje("Yazılım Testi","2025-02-21","ss","ahmet kaya","1235654");
        repo.projeEkle(yeniProje,bosFile);
        System.out.println("Proje eklendi.");

        //Proje lieteleme testi
        List<Proje> projeler=repo.tumProjeleriGetir();
        System.out.println("Projeler:");
        for (Proje p:projeler){
            System.out.println("- "+p.getAd()+" / "+p.getTeslimTarihi());
        }

        //Ada göre proje bulma testi
        Proje bul=repo.projeBulByAd("Yazılım Testi");
        if (bul!=null){
            System.out.println("Aranan Proje: "+bul.getAd());
        }
        else {
            System.out.println("Proje bulunamadı.");
        }
        repo.dosyayiIndir("ornek.txt", "C:\\Users\\Yusuf\\Desktop\\ornek2.txt");

    }
}
