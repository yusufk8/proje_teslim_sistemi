package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MongoProjeRepository implements ProjeRepository {

    private final MongoCollection<Document> collection;
    private final MongoDatabase database;


    public MongoProjeRepository(MongoDatabase db){


        this.database=db;
        this.collection=db.getCollection("projeTeslim");

    }



    @Override
    public void projeEkle(Proje proje, File dosya) {
        Document doc=new Document("ad",proje.getAd())
                .append("teslimTarihi",proje.getTeslimTarihi())
                .append("ogrenciAdSoyad", proje.getOgrenciAdSoyad())
                .append("ogrenciNo", proje.getOgrenciNo());
        if (dosya != null) {
            doc.append("dosyaAdi", dosya.getName());

        }

        collection.insertOne(doc);
        System.out.println("Doküman eklendi: " + doc.toJson());

        if (dosya!=null){
        try {
            GridFSBucket bucket = GridFSBuckets.create(database);
            FileInputStream streamToUpload = new FileInputStream(dosya);

            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(1024 * 1024)
                    .metadata(new Document("projeAd", proje.getAd()));

            bucket.uploadFromStream(dosya.getName(), streamToUpload, options);
            streamToUpload.close();
        } catch (IOException e) {
            e.printStackTrace();
        }}

    }

    @Override
    public List<Proje> tumProjeleriGetir() {
        List<Proje> liste=new ArrayList<>();
        for (Document doc:collection.find()){
            String ogrenciAdSoyad=doc.getString("ogrenciAdSoyad");
            String ogrenciNo=doc.getString("ogrenciNo");
            String ad=doc.getString("ad");
            String teslimTarihi=doc.getString("teslimTarihi");
            String dosyaAdi=doc.getString("dosyaAdi");
            liste.add(new Proje(ad,teslimTarihi,dosyaAdi,ogrenciAdSoyad,ogrenciNo));
        }
        return liste;
    }

    @Override
    public Proje projeBulByAd(String ad) {
        Document filtre= new Document("ad",ad);
        Document doc=collection.find(filtre).first();
        if (doc!=null){
            return new Proje(doc.getString("ogrenciAdSoyad"),doc.getString("ogrenciNo"),doc.getString("ad")
                    ,doc.getString("teslimTarihi"),doc.getString("dosyaAdi") );
        }
        return null;
    }
    public void dosyayiIndir(String dosyaAdi, String hedefYol) {
        try {
            GridFSBucket bucket = GridFSBuckets.create(database);
            FileOutputStream streamToDownloadTo = new FileOutputStream(hedefYol);
            bucket.downloadToStream(dosyaAdi, streamToDownloadTo);
            streamToDownloadTo.close();
            System.out.println("Dosya başarıyla indirildi: " + hedefYol);
        } catch (IOException e) {
            System.out.println("Dosya indirme sırasında hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
