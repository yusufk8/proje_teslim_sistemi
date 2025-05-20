package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import com.mongodb.client.*;
import org.bson.Document;
import org.example.MongoProjeRepository;
import org.example.Proje;
import org.example.ProjeRepository;

public class ProjeTeslimForm extends JFrame {
    private JTextField adField;
    private JTextField tarihField;
    private JTextArea sonucArea;
    private JButton ekleButton, listeleButton;
    private JButton dosyaSecButton;
    private File secilenDosya = null;
    private java.util.List<Proje> liste = new java.util.ArrayList<>();
    private JList<String> projeList;
    private JTextField ogrenciAdField;
    private JTextField ogrenciNoField;

    private ProjeRepository repo;

    public ProjeTeslimForm() {

        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("teslim");
        repo = new MongoProjeRepository(db);


        setTitle("📋 Proje Teslim Sistemi");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);


        JLabel adLabel = new JLabel("Proje Adı:");
        adLabel.setBounds(20, 90, 80, 25);
        add(adLabel);

        adField = new JTextField();
        adField.setBounds(100, 90, 250, 25);
        add(adField);

        JLabel tarihLabel = new JLabel("Teslim Tarihi:");
        tarihLabel.setBounds(20, 120, 80, 25);
        add(tarihLabel);

        tarihField = new JTextField();
        tarihField.setBounds(100, 120, 250, 25);
        tarihField.setEditable(false);
        tarihField.setText(java.time.LocalDate.now().toString());

        add(tarihField);

        JLabel adSoyadLabel = new JLabel("Ad Soyad:");
        adSoyadLabel.setBounds(20, 20, 80, 25);
        add(adSoyadLabel);

        ogrenciAdField = new JTextField();
        ogrenciAdField.setBounds(100, 20, 250, 25);
        add(ogrenciAdField);

        JLabel noLabel = new JLabel("Öğrenci No:");
        noLabel.setBounds(20, 60, 80, 25);
        add(noLabel);

        ogrenciNoField = new JTextField();
        ogrenciNoField.setBounds(100, 60, 250, 25);
        add(ogrenciNoField);

        JLabel dosyaLabel = new JLabel("Dosya Seç: ");
        dosyaLabel.setBounds(20, 160, 80, 25);
        add(dosyaLabel);


        DefaultListModel<String> listModel = new DefaultListModel<>();
        projeList = new JList<>(listModel);
        projeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScroll = new JScrollPane(projeList);
        listScroll.setBounds(360, 20, 450, 220); // sağ tarafa yerleştiriyoruz
        add(listScroll);

        dosyaSecButton = new JButton("Dosya Seç");
        dosyaSecButton.setBounds(100, 160, 100, 30);
        add(dosyaSecButton);

        JButton btnDosyaIndir = new JButton("Dosyayı İndir");
        btnDosyaIndir.setBounds(190, 200, 130, 30);
        add(btnDosyaIndir);

        dosyaSecButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                secilenDosya = chooser.getSelectedFile();
                sonucArea.setText("📄 Seçilen dosya: " + secilenDosya.getName());
            }
        });

        ekleButton = new JButton("Ekle");
        ekleButton.setBounds(20, 200, 80, 30);
        add(ekleButton);

        listeleButton = new JButton("Listele");
        listeleButton.setBounds(105, 200, 80, 30);
        add(listeleButton);

        sonucArea = new JTextArea();
        sonucArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(sonucArea);
        scroll.setBounds(20, 240, 300, 70);
        add(scroll);


        btnDosyaIndir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int secilenIndex = projeList.getSelectedIndex(); // seçili projeyi al
                if (secilenIndex >= 0) {
                    Proje secilenProje = liste.get(secilenIndex);
                    String dosyaAdi = secilenProje.getDosyaAdi();
                    String userHome = System.getProperty("user.home");
                    String hedefYol = userHome + File.separator + "Desktop" + File.separator + "Projeler" + File.separator + dosyaAdi;

                    try {
                        repo.dosyayiIndir(dosyaAdi, hedefYol);
                        JOptionPane.showMessageDialog(null, "Dosya başarıyla indirildi:\n" + hedefYol);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen bir proje seçin!");
                }
            }
        });



        ekleButton.addActionListener(e -> {
            String ogrenciAdSoyad=ogrenciAdField.getText();
            String ogrenciNo=ogrenciNoField.getText();
            String ad = adField.getText();
            String tarih = tarihField.getText();
            String dosya=secilenDosya.getName();
            if (!ad.isEmpty() && !tarih.isEmpty() && secilenDosya != null && !ogrenciAdSoyad.isEmpty() && !ogrenciNo.isEmpty()) {
                Proje p = new Proje(ad, tarih, dosya, ogrenciAdSoyad, ogrenciNo);
                repo.projeEkle(p, secilenDosya);
                sonucArea.setText("✅ Proje ve dosya yüklendi.");
                adField.setText(""); tarihField.setText("");
                ogrenciAdField.setText(""); ogrenciNoField.setText("");
                tarihField.setText(java.time.LocalDate.now().toString());
                secilenDosya = null;
            } else {
                sonucArea.setText("⚠️ Lütfen tüm alanları doldurun ve dosya seçin.");
            }
        });

        listeleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder builder = new StringBuilder();
                liste = repo.tumProjeleriGetir();

                DefaultListModel<String> model = new DefaultListModel<>();

                for (Proje p : liste) {
                    String satir = "- " + p.getAd() + " | " + p.getTeslimTarihi()
                            + " | " + p.getDosyaAdi()
                            + " | " + p.getOgrenciAdSoyad()
                            + " | " + p.getOgrenciNo();
                    builder.append(satir).append("\n");
                    model.addElement(satir);
                }

                 // TextArea gösterim
                projeList.setModel(model);
            }
        });

        setVisible(true);
    }
}
