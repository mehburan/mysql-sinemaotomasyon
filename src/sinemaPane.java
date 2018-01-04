
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;


public class sinemaPane extends javax.swing.JFrame {

    ArrayList<satis> fyt = new ArrayList<>();
    ArrayList<satis> sts = new ArrayList<>();
    ArrayList<kapasite> fek = new ArrayList<>();
    ArrayList<satis> para = new ArrayList<>();
    DB db = new DB();
    
    public void filmucbilgi() {
    String url ="http://www.beyazperde.com/filmler/film-257791/";
        try {
            Document doc = Jsoup.connect(url).timeout(30000).ignoreContentType(true).get();
            Elements bilgi = doc.getElementsByAttributeValue("class", "synopsis-txt");
            for (Element element : bilgi) {
                jTextFilmBilgi.setText(bilgi.text());
            }
        } catch (Exception e) {
            System.err.println("Hata : "+e);
        }
        
    }
    
    public void filmikibilgi() {
    String url ="http://www.beyazperde.com/filmler/film-256075/";
        try {
            Document doc = Jsoup.connect(url).timeout(30000).ignoreContentType(true).get();
            Elements konu = doc.getElementsByAttributeValue("class", "synopsis-txt");
            for (Element element : konu) {
                jTextFilmBilgi.setText(konu.text());
            }
        } catch (Exception e) {
            System.err.println("Hata : "+e);
        }
    }
    
    public void filmbilgi () {
        String url = "http://www.beyazperde.com/filmler/film-249882/";
        try {
            Document doc = Jsoup.connect(url).timeout(30000).ignoreContentType(true).get();
            Elements basliks = doc.getElementsByAttributeValue("class", "synopsis-txt");
            
            for (Element baslik : basliks) {
                jTextFilmBilgi.setText(baslik.text());
            }
        } catch (Exception e) {
            System.err.println("Bağlantı hatası : " + e);
        }
    }
    
    public String sifregetir () {
        
        String tas=null;
        try {
            String query ="select kullaniciSifre from kullanicigiris";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
            tas=rs.getString(""+kullaniciEnum.kullaniciSifre); 
            }
        } catch (Exception e) { System.err.println("HATA : "+e);
        } finally {
        db.kapat();
        }
        return tas;
    }

    public void hata() {
        try {

            String query = "select *from kullanicigiris";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                kullaniciGiris kg = new kullaniciGiris();
                kg.setKullaniciAdi(rs.getString("" + kullaniciEnum.kullaniciAdi));
                kg.setSifre(rs.getString("" + kullaniciEnum.kullaniciSifre));
            }

        } catch (Exception e) {
            System.err.println("Hatasız kul olmaz : " + e);
        } finally {
            db.kapat();
        }
    }

    public sinemaPane() {

        initComponents();
        satisbitirBtn.setVisible(false);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("media/kestik.png"));

        System.out.println(txtEskiSifre.getText());
        satiscombogetir();
        satiscomboseansgetir();
        satiscombosalongetir();
        satisfiyattopla();
        tarih();
        hata();
        raporgetir();
        seanskap();
        satistable();
        yeni();
        sifregetir();
        
        
                

    }

    public void satiscombogetir() {
        DefaultComboBoxModel<String> dcm = new DefaultComboBoxModel<>();
        sts.clear();
        try {
            String query = "select filmAd from seanslar";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                dcm.addElement(rs.getString("filmAd"));
            }
            jComboBoxFilm.setModel(dcm);

        } catch (Exception e) {
            System.err.println("Satış ComboBox Film Getir Hatası: " + e);
        }
    }

    public void seanskap() {

        kapasite kp = new kapasite();
        DefaultTableModel dtm = new DefaultTableModel();

        dtm.addColumn("Film");
        dtm.addColumn("Seans 1");
        dtm.addColumn("Seans 2");
        dtm.addColumn("Seans 3");
        dtm.addColumn("Kapasite 1");
        dtm.addColumn("Kapasite 2");
        dtm.addColumn("Kapasite 3");
        try {
            String query = "SELECT filmAd,seans1,seans2,seans3,kapasite1,kapasite2,kapasite3 from seanslar";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                kp.setFilm(rs.getString("" + kapasiteEnum.filmAd));
                kp.setSeaone(rs.getString("" + kapasiteEnum.seans1));
                kp.setSeatwo(rs.getString("" + kapasiteEnum.seans2));
                kp.setSeathree(rs.getString("" + kapasiteEnum.seans3));
                kp.setKapone(rs.getString("" + kapasiteEnum.kapasite1));
                kp.setKaptwo(rs.getString("" + kapasiteEnum.kapasite2));
                kp.setKapthree(rs.getString("" + kapasiteEnum.kapasite3));

                dtm.addRow(new String[]{kp.getFilm(), kp.getSeaone(), kp.getSeatwo(), kp.getSeathree(), kp.getKapone(), kp.getKaptwo(), kp.getKapthree()});
            }
        } catch (Exception e) {
            System.err.println("Seans-Kapasite Getir Hatası : " + e);
        }
        seakapTable.setModel(dtm);

    }

    public void satiscomboseansgetir() {
        DefaultComboBoxModel<String> dcm2 = new DefaultComboBoxModel<>();
        sts.clear();
        try {
            String film = "" + jComboBoxFilm.getSelectedItem();
            String query = "select seans1,seans2,seans3 from seanslar WHERE filmAd ='" + film + "'";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                dcm2.addElement(rs.getString("seans1"));
                dcm2.addElement(rs.getString("seans2"));
                dcm2.addElement(rs.getString("seans3"));
            }
        } catch (Exception e) {
            System.err.println("Satış ComboBox Film Getir Hatası : " + e);
        }
        jComboBoxSeans.setModel(dcm2);
    }

    public void satiscombosalongetir() {
        DefaultComboBoxModel<String> dcm3 = new DefaultComboBoxModel<>();
        sts.clear();
        try {
            String salon = "" + jComboBoxFilm.getSelectedItem();
            String query = "select salon from seanslar where filmAd ='" + salon + "'";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                dcm3.addElement(rs.getString("salon"));
            }
        } catch (Exception e) {
            System.err.println("Satış ComboBox Salon getir hatası : " + e);
        }
        jComboBoxSalon.setModel(dcm3);
    }

    public void satiscomboturgetir() {
        sts.clear();
        DefaultComboBoxModel<String> dcm4 = new DefaultComboBoxModel<>();
        try {
            String query = "select tur from musteritur";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                dcm4.addElement(rs.getString("tur"));
            }
        } catch (Exception e) {
            System.err.println("Satış ComboBox Tür Getir Hatası : " + e);
        }
        jComboBox1.setModel(dcm4);
    }

    public void tarih() {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy ");
        String saat = sdf.format(dt);
        jTextField2.setText(saat);

    }

    public void deneme() {
        kapasite kp = new kapasite();
        if (jComboBoxFilm.getSelectedIndex() == 0 && jComboBoxSeans.getSelectedIndex() == 0) {
            try {
                String q = "select kapasite1 from seanslar WHERE id ='1'";
                ResultSet rs = db.baglan().executeQuery(q);
                while (rs.next()) {
                    kp.setKapone(rs.getString("" + kapasiteEnum.kapasite1));
                }
                int kap = Integer.valueOf(kp.getKapone());
                db.kapat();

                int a = kap - 1;

                String query = "UPDATE seanslar SET kapasite1= '" + a + "' WHERE id =1";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    System.out.println("Güncelleme başarılı");

                }
            } catch (Exception e) {
                System.err.println("Güncelleme hatası : " + e);
            }
        }

        if (jComboBoxFilm.getSelectedIndex() == 0 && jComboBoxSeans.getSelectedIndex() == 1) {
            try {
                String q = "select kapasite2 from seanslar WHERE id ='1'";
                ResultSet rs = db.baglan().executeQuery(q);
                while (rs.next()) {
                    kp.setKaptwo(rs.getString("" + kapasiteEnum.kapasite2));
                }
                int kap = Integer.valueOf(kp.getKaptwo());
                db.kapat();

                int a = kap - 1;

                String query = "UPDATE seanslar SET kapasite2= '" + a + "' WHERE id =1";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    System.out.println("Güncelleme başarılı");

                }
            } catch (Exception e) {
                System.err.println("Güncelleme hatası : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 0 && jComboBoxSeans.getSelectedIndex() == 2) {
            try {
                String q = "select kapasite3 from seanslar WHERE id ='1'";
                ResultSet rs = db.baglan().executeQuery(q);
                while (rs.next()) {
                    kp.setKapthree(rs.getString("" + kapasiteEnum.kapasite3));
                }
                int kap = Integer.valueOf(kp.getKapthree());
                db.kapat();

                int a = kap - 1;

                String query = "UPDATE seanslar SET kapasite3= '" + a + "' WHERE id =1";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    System.out.println("Güncelleme başarılı");

                }
            } catch (Exception e) {
                System.err.println("Güncelleme hatası : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 1 && jComboBoxSeans.getSelectedIndex() == 0) {
            try {
                String q = "select kapasite1 from seanslar WHERE id ='2'";
                ResultSet rs = db.baglan().executeQuery(q);
                while (rs.next()) {
                    kp.setKapone(rs.getString("" + kapasiteEnum.kapasite1));
                }
                int kap = Integer.valueOf(kp.getKapone());
                db.kapat();

                int a = kap - 1;

                String query = "UPDATE seanslar SET kapasite1= '" + a + "' WHERE id =2";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    System.out.println("Güncelleme başarılı");

                }
            } catch (Exception e) {
                System.err.println("Güncelleme hatası : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 1 && jComboBoxSeans.getSelectedIndex() == 1) {
            try {
                String q = "select kapasite2 from seanslar WHERE id ='2'";
                ResultSet rs = db.baglan().executeQuery(q);
                while (rs.next()) {
                    kp.setKaptwo(rs.getString("" + kapasiteEnum.kapasite2));
                }
                int kap = Integer.valueOf(kp.getKaptwo());
                db.kapat();

                int a = kap - 1;

                String query = "UPDATE seanslar SET kapasite2= '" + a + "' WHERE id =2";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    System.out.println("Güncelleme başarılı");

                }
            } catch (Exception e) {
                System.err.println("Güncelleme hatası : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 1 && jComboBoxSeans.getSelectedIndex() == 2) {
            try {
                String q = "select kapasite3 from seanslar WHERE id ='2'";
                ResultSet rs = db.baglan().executeQuery(q);
                while (rs.next()) {
                    kp.setKapthree(rs.getString("" + kapasiteEnum.kapasite3));
                }
                int kap = Integer.valueOf(kp.getKapthree());
                db.kapat();

                int a = kap - 1;

                String query = "UPDATE seanslar SET kapasite3= '" + a + "' WHERE id =2";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    System.out.println("Güncelleme başarılı");

                }
            } catch (Exception e) {
                System.err.println("Güncelleme hatası : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 2 && jComboBoxSeans.getSelectedIndex() == 0) {
            try {
                String q = "select kapasite1 from seanslar WHERE id ='3'";
                ResultSet rs = db.baglan().executeQuery(q);
                while (rs.next()) {
                    kp.setKapone(rs.getString("" + kapasiteEnum.kapasite1));
                }
                int kap = Integer.valueOf(kp.getKapone());
                db.kapat();

                int a = kap - 1;

                String query = "UPDATE seanslar SET kapasite1= '" + a + "' WHERE id =3";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    System.out.println("Güncelleme başarılı");

                }
            } catch (Exception e) {
                System.err.println("Güncelleme hatası : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 2 && jComboBoxSeans.getSelectedIndex() == 1) {
            try {
                String q = "select kapasite2 from seanslar WHERE id ='3'";
                ResultSet rs = db.baglan().executeQuery(q);
                while (rs.next()) {
                    kp.setKaptwo(rs.getString("" + kapasiteEnum.kapasite2));
                }
                int kap = Integer.valueOf(kp.getKaptwo());
                db.kapat();

                int a = kap - 1;

                String query = "UPDATE seanslar SET kapasite2= '" + a + "' WHERE id =3";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    System.out.println("Güncelleme başarılı");

                }
            } catch (Exception e) {
                System.err.println("Güncelleme hatası : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 2 && jComboBoxSeans.getSelectedIndex() == 2) {
            try {
                String q = "select kapasite3 from seanslar WHERE id ='3'";
                ResultSet rs = db.baglan().executeQuery(q);
                while (rs.next()) {
                    kp.setKapthree(rs.getString("" + kapasiteEnum.kapasite3));
                }
                int kap = Integer.valueOf(kp.getKapthree());
                db.kapat();

                int a = kap - 1;

                String query = "UPDATE seanslar SET kapasite3= '" + a + "' WHERE id =3";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    System.out.println("Güncelleme başarılı");

                }
            } catch (Exception e) {
                System.err.println("Güncelleme hatası : " + e);
            }
        }
        seanskap();

    }

    public void seansyaz() {

        if (jComboBoxFilm.getSelectedIndex() == 0 && jComboBoxSeans.getSelectedIndex() == 0) {
            try {
                String query = "select kapasite1 from seanslar WHERE id ='1'";
                ResultSet rs = db.baglan().executeQuery(query);
                while (rs.next()) {
                    String a = (rs.getString("" + kapasiteEnum.kapasite1));
                    txtkapasite.setText(a);
                }

            } catch (Exception e) {
                System.err.println("Kapasite yaz hata :" + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 0 && jComboBoxSeans.getSelectedIndex() == 1) {
            try {
                String query = "select kapasite2 from seanslar where id = '1'";
                ResultSet rs = db.baglan().executeQuery(query);
                while (rs.next()) {
                    String a = rs.getString("" + kapasiteEnum.kapasite2);
                    txtkapasite.setText(a);
                }
            } catch (Exception e) {
                System.err.println("Kapasite yaz hata : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 0 && jComboBoxSeans.getSelectedIndex() == 2) {
            try {
                String query = "select kapasite3 from seanslar where id = '1'";
                ResultSet rs = db.baglan().executeQuery(query);
                while (rs.next()) {
                    String a = rs.getString("" + kapasiteEnum.kapasite3);
                    txtkapasite.setText(a);
                }
            } catch (Exception e) {
                System.err.println("Kapasite yaz hata : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 1 && jComboBoxSeans.getSelectedIndex() == 0) {
            try {
                String query = "select kapasite1 from seanslar where id = '2'";
                ResultSet rs = db.baglan().executeQuery(query);
                while (rs.next()) {
                    String a = rs.getString("" + kapasiteEnum.kapasite1);
                    txtkapasite.setText(a);
                }
            } catch (Exception e) {
                System.err.println("Kapasite yaz hata : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 1 && jComboBoxSeans.getSelectedIndex() == 1) {
            try {
                String query = "select kapasite2 from seanslar where id = '2'";
                ResultSet rs = db.baglan().executeQuery(query);
                while (rs.next()) {
                    String a = rs.getString("" + kapasiteEnum.kapasite2);
                    txtkapasite.setText(a);
                }
            } catch (Exception e) {
                System.err.println("Kapasite yaz hata : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 1 && jComboBoxSeans.getSelectedIndex() == 2) {
            try {
                String query = "select kapasite3 from seanslar where id = '2'";
                ResultSet rs = db.baglan().executeQuery(query);
                while (rs.next()) {
                    String a = rs.getString("" + kapasiteEnum.kapasite3);
                    txtkapasite.setText(a);
                }
            } catch (Exception e) {
                System.err.println("Kapasite yaz hata : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 2 && jComboBoxSeans.getSelectedIndex() == 0) {
            try {
                String query = "select kapasite1 from seanslar where id = '3'";
                ResultSet rs = db.baglan().executeQuery(query);
                while (rs.next()) {
                    String a = rs.getString("" + kapasiteEnum.kapasite1);
                    txtkapasite.setText(a);
                }
            } catch (Exception e) {
                System.err.println("Kapasite yaz hata : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 2 && jComboBoxSeans.getSelectedIndex() == 1) {
            try {
                String query = "select kapasite2 from seanslar where id = '3'";
                ResultSet rs = db.baglan().executeQuery(query);
                while (rs.next()) {
                    String a = rs.getString("" + kapasiteEnum.kapasite2);
                    txtkapasite.setText(a);
                }
            } catch (Exception e) {
                System.err.println("Kapasite yaz hata : " + e);
            }
        }
        if (jComboBoxFilm.getSelectedIndex() == 2 && jComboBoxSeans.getSelectedIndex() == 2) {
            try {
                String query = "select kapasite3 from seanslar where id = '3'";
                ResultSet rs = db.baglan().executeQuery(query);
                while (rs.next()) {
                    String a = rs.getString("" + kapasiteEnum.kapasite3);
                    txtkapasite.setText(a);
                }
            } catch (Exception e) {
                System.err.println("Kapasite yaz hata : " + e);
            }
        }
    }

    public void satistable() {
        DefaultTableModel dtm = new DefaultTableModel();
        satiss ss = new satiss();
        dtm.addColumn("Tarih");
        dtm.addColumn("Satış");
        try {
            String query = "select raportarih, sum(raptopsatis) as satis from raporlar GROUP BY raportarih";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {

                ss.setTar(rs.getString("" + satEnum.raportarih));
                ss.setPar(rs.getString("satis"));
                dtm.addRow(new String[]{ss.getTar(), ss.getPar()});
            }
        } catch (Exception e) {
            System.err.println("satis table hatası : " + e);
        }
        tableGunluk.setModel(dtm);
        System.out.println(ss.getTar());
        System.out.println(ss.getPar());

    }

    public void yeni() {
        kapasite kp = new kapasite();
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("Id");
        dtm.addColumn("Film Adı");
        dtm.addColumn("Seans 1");
        dtm.addColumn("Seans 2");
        dtm.addColumn("Seans 3");
        dtm.addColumn("Kapasite 1");
        dtm.addColumn("Kapasite 2");
        dtm.addColumn("Kapasite 3");
        try {
            String query = "select id,filmAd, seans1,seans2, seans3, kapasite1, kapasite2,kapasite3 from seanslar";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                kp.setId(rs.getInt(""+kapasiteEnum.id));
                kp.setFilm(rs.getString("" + kapasiteEnum.filmAd));
                kp.setSeaone(rs.getString("" + kapasiteEnum.seans1));
                kp.setSeatwo(rs.getString("" + kapasiteEnum.seans2));
                kp.setSeathree(rs.getString("" + kapasiteEnum.seans3));
                kp.setKapone(rs.getString("" + kapasiteEnum.kapasite1));
                kp.setKaptwo(rs.getString("" + kapasiteEnum.kapasite2));
                kp.setKapthree(rs.getString("" + kapasiteEnum.kapasite3));

                dtm.addRow(new String[]{""+kp.getId(),kp.getFilm(), kp.getSeaone(), kp.getSeatwo(), kp.getSeathree(), kp.getKapone(), kp.getKaptwo(), kp.getKapthree()});
            }
        } catch (Exception e) {
            System.err.println("Yeni getir hatası : " + e);
        } finally {
            db.kapat();
        }
        jTable1.setModel(dtm);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        filmSatisTable = new javax.swing.JTable();
        satisbitirBtn = new javax.swing.JButton();
        jComboBoxFilm = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jComboBoxSeans = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxSalon = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        listeleBtn = new javax.swing.JButton();
        txtSatisFiyat = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtkapasite = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        raporTable = new javax.swing.JTable();
        txttoplamsatis = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableGunluk = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        txtEskiSifre = new javax.swing.JTextField();
        txtYeniSifre = new javax.swing.JTextField();
        txtYeniSifreTekrar = new javax.swing.JTextField();
        guncelleBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTabbedPane7 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        seakapTable = new javax.swing.JTable();
        jTabbedPane8 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtad = new javax.swing.JTextField();
        txtS1 = new javax.swing.JTextField();
        txtS2 = new javax.swing.JTextField();
        txtS3 = new javax.swing.JTextField();
        txtK1 = new javax.swing.JTextField();
        txtK2 = new javax.swing.JTextField();
        txtK3 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jSil = new javax.swing.JButton();
        jDuz = new javax.swing.JButton();
        jEkl = new javax.swing.JButton();
        jDuzEkl = new javax.swing.JButton();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        filmbirBtn = new javax.swing.JButton();
        filmikiBtn = new javax.swing.JButton();
        filmucBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextFilmBilgi = new javax.swing.JTextPane();
        jTextField2 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        filmSatisTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Filmler", "Seans", "Salon", "Müşteri Tür", "Tarih"
            }
        ));
        jScrollPane4.setViewportView(filmSatisTable);

        satisbitirBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/ok.png"))); // NOI18N
        satisbitirBtn.setText("Satışı Bitir");
        satisbitirBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                satisbitirBtnActionPerformed(evt);
            }
        });

        jComboBoxFilm.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxFilmİtemStateChanged(evt);
            }
        });
        jComboBoxFilm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFilmActionPerformed(evt);
            }
        });

        jLabel10.setText("Filmler");

        jComboBoxSeans.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxSeansİtemStateChanged(evt);
            }
        });
        jComboBoxSeans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSeansActionPerformed(evt);
            }
        });

        jLabel11.setText("Seanslar");

        jComboBoxSalon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxSalonİtemStateChanged(evt);
            }
        });

        jLabel12.setText("Salonlar");

        listeleBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/arsiv.png"))); // NOI18N
        listeleBtn.setText("Listele");
        listeleBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listeleBtnMouseClicked(evt);
            }
        });
        listeleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listeleBtnActionPerformed(evt);
            }
        });

        jLabel13.setText("Fiyat");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel14.setText("Müşteri Tür");

        jLabel15.setText("TL");

        jLabel19.setText("Salon Kapasitesi");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBoxFilm, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
                            .addComponent(jComboBoxSeans, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxSalon, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(68, 68, 68)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel19)
                                        .addGap(27, 27, 27)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtkapasite, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtSatisFiyat, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel15)
                                        .addGap(88, 88, 88)
                                        .addComponent(listeleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addComponent(satisbitirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(7, 7, 7)
                .addComponent(jComboBoxFilm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxSeans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxSalon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtkapasite, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(listeleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(satisbitirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtSatisFiyat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(0, 192, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("FİLMLER", jPanel1);

        jTabbedPane1.addTab("FİLM SATIŞ", jTabbedPane2);

        raporTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Rapor Tarih", "Toplam Satış"
            }
        ));
        jScrollPane3.setViewportView(raporTable);

        jLabel16.setText("TL");

        jLabel17.setText("Toplam Satış: ");

        jLabel20.setText("Genel Tablo");

        tableGunluk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Tarih", "Satış"
            }
        ));
        jScrollPane5.setViewportView(tableGunluk);
        if (tableGunluk.getColumnModel().getColumnCount() > 0) {
            tableGunluk.getColumnModel().getColumn(0).setResizable(false);
        }

        jLabel21.setText("Günlük Tablo");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txttoplamsatis, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(120, 120, 120))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txttoplamsatis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(0, 207, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("RAPORLAR", jPanel3);

        jTabbedPane1.addTab("RAPORLAMA", jTabbedPane4);

        guncelleBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/refresh.png"))); // NOI18N
        guncelleBtn.setText("Güncelle");
        guncelleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guncelleBtnActionPerformed(evt);
            }
        });

        jLabel7.setText("Eski Şifre");

        jLabel8.setText("Yeni Şifre");

        jLabel9.setText("Yeni Şifre Tekrar");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(53, 53, 53)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEskiSifre)
                            .addComponent(txtYeniSifre)
                            .addComponent(txtYeniSifreTekrar, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(270, 270, 270)
                        .addComponent(guncelleBtn)))
                .addContainerGap(422, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEskiSifre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtYeniSifre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtYeniSifreTekrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addComponent(guncelleBtn)
                .addContainerGap(418, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("AYARLAR", jPanel5);

        jTabbedPane1.addTab("KULLANICI AYARLARI", jTabbedPane5);

        seakapTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(seakapTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab("SEANS-KAPASİTE", jPanel4);

        jTabbedPane1.addTab("SEANS-KAPASİTE", jTabbedPane7);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable1);

        jLabel22.setText("Film Adı");

        jLabel23.setText("Seans 1");

        jLabel24.setText("Seans 2");

        jLabel25.setText("Seans 3");

        jLabel26.setText("Kapasite 1");

        jLabel27.setText("Kapasite 2");

        jLabel28.setText("Kapasite 3");

        jSil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/delete.png"))); // NOI18N
        jSil.setText("Sil");
        jSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSilActionPerformed(evt);
            }
        });

        jDuz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/edit.png"))); // NOI18N
        jDuz.setText("Düzenle");
        jDuz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDuzActionPerformed(evt);
            }
        });

        jEkl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/add.png"))); // NOI18N
        jEkl.setText("Ekle");
        jEkl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEklActionPerformed(evt);
            }
        });

        jDuzEkl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/kamera.png"))); // NOI18N
        jDuzEkl.setText("Düzenle");
        jDuzEkl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDuzEklActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel22)
                                .addComponent(jLabel23))
                            .addComponent(jLabel25)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtad)
                            .addComponent(txtS1)
                            .addComponent(txtS2)
                            .addComponent(txtS3)
                            .addComponent(txtK1)
                            .addComponent(txtK2)
                            .addComponent(txtK3, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(226, 226, 226)
                                .addComponent(jSil))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addComponent(jEkl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jDuzEkl)))
                        .addGap(40, 40, 40)
                        .addComponent(jDuz)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSil)
                            .addComponent(jDuz))))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtS1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtS2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtS3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtK1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtK2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtK3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jEkl)
                    .addComponent(jDuzEkl))
                .addGap(0, 232, Short.MAX_VALUE))
        );

        jTabbedPane8.addTab("Film Ekle-Düzenle", jPanel6);

        jTabbedPane1.addTab("FİLM EKLE- DÜZENLE", jTabbedPane8);

        filmbirBtn.setIcon(new javax.swing.ImageIcon("C:\\Users\\MEBURİN\\Documents\\NetBeansProjects\\sinema\\image\\yolAyrımı.PNG")); // NOI18N
        filmbirBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filmbirBtnMouseClicked(evt);
            }
        });

        filmikiBtn.setIcon(new javax.swing.ImageIcon("C:\\Users\\MEBURİN\\Documents\\NetBeansProjects\\sinema\\image\\ketenpere.PNG")); // NOI18N
        filmikiBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filmikiBtnMouseClicked(evt);
            }
        });

        filmucBtn.setIcon(new javax.swing.ImageIcon("C:\\Users\\MEBURİN\\Documents\\NetBeansProjects\\sinema\\image\\ayla.PNG")); // NOI18N
        filmucBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filmucBtnMouseClicked(evt);
            }
        });

        jScrollPane2.setViewportView(jTextFilmBilgi);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(filmbirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(filmikiBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(filmucBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(filmikiBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(filmbirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filmucBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Film Bilgileri", jPanel2);

        jTabbedPane1.addTab("FİLM BİLGİLERİ", jTabbedPane3);

        jLabel18.setText("Tarih :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void guncelleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guncelleBtnActionPerformed
        String eski = txtEskiSifre.getText();
        String yeni = txtYeniSifre.getText();
        String yenitekrar = txtYeniSifreTekrar.getText();
        kullaniciGiris kg = new kullaniciGiris();
        
        if (sifregetir().equals(eski)) {

        if (yeni.equals(yenitekrar)) {
            try {
                String query = "UPDATE kullanicigiris set kullaniciSifre = '" + yeni + "'";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    
                    kg.setSifre(txtYeniSifre.getText());
                    if (kg.getSifre().equals(eski)) {
                        JOptionPane.showMessageDialog(this, "Eski Şifre Uyumsuz !");
                    }
                    JOptionPane.showMessageDialog(this, "Şifre Güncellendi !");
                }
            } catch (Exception e) {
                System.err.println("Şifre yenileme hatası : " + e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Yeni Şifreler Uymuyor !");
        } } else {
        JOptionPane.showMessageDialog(this, "Eski Şifre Yanlış !");
        }

    }//GEN-LAST:event_guncelleBtnActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void listeleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listeleBtnActionPerformed

        satisbitirBtn.setVisible(true);
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        DefaultTableModel dtm = new DefaultTableModel();
        String film = "" + jComboBoxFilm.getSelectedItem();
        String seans = "" + jComboBoxSeans.getSelectedItem();
        String salon = "" + jComboBoxSalon.getSelectedItem();
        String tur = "" + jComboBox1.getSelectedItem();
        String tarih = sdf.format(dt);

        dtm.addColumn("Filmler");
        dtm.addColumn("Seanslar");
        dtm.addColumn("Salonlar");
        dtm.addColumn("Müşteri Tür");
        dtm.addColumn("Tarih");

        dtm.addRow(new String[]{film, seans, salon, tur, tarih});
        filmSatisTable.setModel(dtm);

        int normalfiyat = 10;
        int ogrencifiyat = 8;
        int cocukfiyat = 6;
        if (jComboBox1.getSelectedItem().equals("normal")) {
            txtSatisFiyat.setText(String.valueOf(normalfiyat));
        } else if (jComboBox1.getSelectedItem().equals("ogrenci")) {
            txtSatisFiyat.setText(String.valueOf(ogrencifiyat));
        } else if (jComboBox1.getSelectedItem().equals("cocuk")) {
            txtSatisFiyat.setText(String.valueOf(cocukfiyat));
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen Müşteri Türü Seçiniz !");
        }
        seanskap();
        seansyaz();
        System.out.println(txtkapasite.getText());

    }//GEN-LAST:event_listeleBtnActionPerformed

    private void jComboBoxSalonİtemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxSalonİtemStateChanged

    }//GEN-LAST:event_jComboBoxSalonİtemStateChanged

    private void jComboBoxFilmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFilmActionPerformed

    }//GEN-LAST:event_jComboBoxFilmActionPerformed

    private void jComboBoxFilmİtemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxFilmİtemStateChanged
        satiscomboseansgetir();
        satiscombosalongetir();
        satiscomboturgetir();

    }//GEN-LAST:event_jComboBoxFilmİtemStateChanged

    private void satisbitirBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_satisbitirBtnActionPerformed
        JOptionPane.showMessageDialog(this, "Satış Tamamlandı !");
        fyt.clear();

        String fiyat = txtSatisFiyat.getText();

        try {
            String query = "insert into raporlar  (raportarih,raptopsatis) VALUES (NOW(),'" + fiyat + "')";
            int rs = db.baglan().executeUpdate(query);

            satis satis = new satis();
            satis.setRaptar("" + satisEnum.raportarih);
            satis.setRappara("" + satisEnum.raptopsatis);

        } catch (Exception e) {
            System.err.println("Rapora Gönderme Hatası : " + e);
        } finally {
            db.kapat();
        }
        raporgetir();
        satisfiyattopla();
        deneme();
        satisbitirBtn.setVisible(false);
    }//GEN-LAST:event_satisbitirBtnActionPerformed

    private void jComboBoxSeansİtemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxSeansİtemStateChanged

    }//GEN-LAST:event_jComboBoxSeansİtemStateChanged

    private void jComboBoxSeansActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSeansActionPerformed

    }//GEN-LAST:event_jComboBoxSeansActionPerformed

    private void listeleBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listeleBtnMouseClicked
        if (txtkapasite.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "Seans kapasitesi dolmuştur satış yapılamaz !");
            satisbitirBtn.setVisible(false);
        }
    }//GEN-LAST:event_listeleBtnMouseClicked

    private void jSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSilActionPerformed

        if (secilenseansid.equals("")) {
            JOptionPane.showMessageDialog(this, "Lütfen Tablodan Bir Veri Seçiniz !");
        } else {
            try {

                System.out.println("id degeri " + secilenseansid);
                String query = "delete from seanslar where id  ='" + secilenseansid + "'";
                int deger = db.baglan().executeUpdate(query);
                if (deger > 0) {
                    yeni();
                }
            } catch (Exception e) {
                System.err.println("Silme Hatası : " + e);
            } finally {
                db.kapat();
            }
        }
    }//GEN-LAST:event_jSilActionPerformed

    private void jDuzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDuzActionPerformed
kapasite kp = new kapasite();
        fek.clear();
        if (secilenseansid.equals("")) {
            JOptionPane.showMessageDialog(this, "Lütfen Tablodan Düzenlenecek Bir Değer Seçiniz ! ");
        } else {
            try {
                
                String q = "select filmAd, seans1,seans2, seans3, kapasite1, kapasite2,kapasite3 from seanslar where id = "+secilenseansid;
                System.out.println("sorgu"+q);
                ResultSet rs = db.baglan().executeQuery(q);
                while (rs.next()) {
                    kp.setFilm(rs.getString("" + kapasiteEnum.filmAd));
                    kp.setSeaone(rs.getString("" + kapasiteEnum.seans1));
                    kp.setSeatwo(rs.getString("" + kapasiteEnum.seans2));
                    kp.setSeathree(rs.getString("" + kapasiteEnum.seans3));
                    kp.setKapone(rs.getString("" + kapasiteEnum.kapasite1));
                    kp.setKaptwo(rs.getString("" + kapasiteEnum.kapasite2));
                    kp.setKapthree(rs.getString("" + kapasiteEnum.kapasite3));

                    fek.add(kp);
                }
            } catch (Exception e) {
                System.err.println("Bıktım Hata Yazmaktan : " + e);
            } finally {
                db.kapat();
            }
            for (kapasite kap : fek) {
                String dizi[] = new String[]{kap.getFilm(), kap.getSeaone(), kap.getSeatwo(), kap.getSeathree(), kap.getKapone(), kap.getKaptwo(), kap.getKapthree()};
                txtad.setText(dizi[0]);
                txtS1.setText(dizi[1]);
                txtS2.setText(dizi[2]);
                txtS3.setText(dizi[3]);
                txtK1.setText(dizi[4]);
                txtK2.setText(dizi[5]);
                txtK3.setText(dizi[6]);
            }
        }
    }//GEN-LAST:event_jDuzActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        secilenseansid = ""+jTable1.getValueAt(jTable1.getSelectedRow(), 0);
        System.out.println(""+secilenseansid);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jEklActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEklActionPerformed
    kapasite kp = new kapasite();
    kp.setFilm(txtad.getText().toUpperCase());
    kp.setSeaone(txtS1.getText());
    kp.setSeatwo(txtS2.getText());
    kp.setSeathree(txtS3.getText());
    kp.setKapone(txtK1.getText());
    kp.setKaptwo(txtK2.getText());
    kp.setKapthree(txtK3.getText());
        if (!kp.getFilm().equals("")&&!kp.getSeaone().equals("")&&!kp.getSeatwo().equals("")&&!kp.getSeathree().equals("")&&!kp.getKapone().equals("")&&!kp.getKaptwo().equals("")&&!kp.getKapthree().equals("")) {
        try {
            String query ="insert into seanslar (id,filmAd,seans1,seans2,seans3,kapasite1,kapasite2,kapasite3) VALUES (null,'"+kp.getFilm()+"','"+kp.getSeaone()+"','"+kp.getSeatwo()+"','"+kp.getSeathree()+"','"+kp.getKapone()+"','"+kp.getKaptwo()+"','"+kp.getKapthree()+"')";
           
            int deger = db.baglan().executeUpdate(query);
            if (deger>0) {
            JOptionPane.showMessageDialog(this,"Ekleme Başarılı !");
                yeni();
                txtad.setText("");
                txtS1.setText("");
                txtS2.setText("");
                txtS3.setText("");
                txtK1.setText("");
                txtK2.setText("");
                txtK3.setText("");
            }
        } catch (Exception e) {
            System.err.println("Hataaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa : "+e);
        } finally{
        db.kapat();
        }
        } else {
        JOptionPane.showMessageDialog(this, "Lütfen Tüm Alanları Doldurunuz !");
        }
    }//GEN-LAST:event_jEklActionPerformed

    private void jDuzEklActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDuzEklActionPerformed
       String fAdi = txtad.getText().toUpperCase();
        String sOne = txtS1.getText();
        String sTwo = txtS2.getText();
        String sThree = txtS3.getText();
        String kOne = txtK1.getText();
        String kTwo = txtK2.getText();
        String kThree = txtK3.getText();

        if (!txtad.getText().equals("") && !txtS1.getText().equals("") && !txtS2.getText().equals("") && !txtS3.getText().equals("") && !txtK1.getText().equals("") && !txtK2.getText().equals("") && !txtK3.getText().equals("")) {
            try {
                String query = "update seanslar set filmAd ='" + fAdi + "',seans1='" + sOne + "',seans2='" + sTwo + "',seans3='" + sThree + "',kapasite1='" + kOne + "',kapasite2='" + kTwo + "',kapasite3='"+kThree+"' where id =" + secilenseansid + ";";
                int deger = db.baglan().executeUpdate(query);
                if (deger>0) {
                    yeni();
                    JOptionPane.showMessageDialog(this, "Düzenleme İşlemi Tamamlandı !");
                txtad.setText("");
                txtS1.setText("");
                txtS2.setText("");
                txtS3.setText("");
                txtK1.setText("");
                txtK2.setText("");
                txtK3.setText("");
                }
            } catch (Exception e) {
                System.err.println("Hatasız Kul Olmaz : "+e);
            } finally{
            db.kapat();
            }
        } else {
        JOptionPane.showMessageDialog(this, "Boşluk Bırakmadan Düzenleme Yapınız !");
        }
        yeni();
        secilenseansid = "";
    }//GEN-LAST:event_jDuzEklActionPerformed

    private void filmbirBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filmbirBtnMouseClicked
     filmikibilgi();
    }//GEN-LAST:event_filmbirBtnMouseClicked

    private void filmucBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filmucBtnMouseClicked
        filmbilgi();
    }//GEN-LAST:event_filmucBtnMouseClicked

    private void filmikiBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filmikiBtnMouseClicked
       filmucbilgi();
    }//GEN-LAST:event_filmikiBtnMouseClicked

    

    public void raporgetir() {

        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("Tarih");
        dtm.addColumn("Fiyat");

        try {
            String query = "select raportarih,raptopsatis from raporlar ";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                satis satis = new satis();
                satis.setRaptar(rs.getString("" + satisEnum.raportarih));
                satis.setRappara(rs.getString("" + satisEnum.raptopsatis));
                dtm.addRow(new String[]{satis.getRaptar(), satis.getRappara()});
            }
        } catch (Exception e) {
            System.err.println("Rapor Getir Hatası: " + e);
        }

        raporTable.setModel(dtm);
    }

    public void satisfiyattopla() {
        try {
            String query = "select sum(raptopsatis) from raporlar ";
            ResultSet rs = db.baglan().executeQuery(query);
            while (rs.next()) {
                String deger = rs.getString("sum(raptopsatis)");
                txttoplamsatis.setText(deger);
            }
        } catch (Exception e) {
            System.err.println("Toplama hatası : " + e);
        }
        satistable();
    }

    String secilenseansid = "";

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            //here you can put the selected theme class name in JTattoo
            UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");

        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(sinemaPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(sinemaPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(sinemaPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(sinemaPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new sinemaPane().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTable filmSatisTable;
    private javax.swing.JButton filmbirBtn;
    private javax.swing.JButton filmikiBtn;
    private javax.swing.JButton filmucBtn;
    private javax.swing.JButton guncelleBtn;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBoxFilm;
    private javax.swing.JComboBox<String> jComboBoxSalon;
    private javax.swing.JComboBox<String> jComboBoxSeans;
    private javax.swing.JButton jDuz;
    private javax.swing.JButton jDuzEkl;
    private javax.swing.JButton jEkl;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JButton jSil;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane7;
    private javax.swing.JTabbedPane jTabbedPane8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextPane jTextFilmBilgi;
    private javax.swing.JButton listeleBtn;
    private javax.swing.JTable raporTable;
    private javax.swing.JButton satisbitirBtn;
    private javax.swing.JTable seakapTable;
    private javax.swing.JTable tableGunluk;
    private javax.swing.JTextField txtEskiSifre;
    private javax.swing.JTextField txtK1;
    private javax.swing.JTextField txtK2;
    private javax.swing.JTextField txtK3;
    private javax.swing.JTextField txtS1;
    private javax.swing.JTextField txtS2;
    private javax.swing.JTextField txtS3;
    private javax.swing.JTextField txtSatisFiyat;
    private javax.swing.JTextField txtYeniSifre;
    private javax.swing.JTextField txtYeniSifreTekrar;
    private javax.swing.JTextField txtad;
    private javax.swing.JTextField txtkapasite;
    private javax.swing.JTextField txttoplamsatis;
    // End of variables declaration//GEN-END:variables

}
