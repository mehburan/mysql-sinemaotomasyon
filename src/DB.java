
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DB {

    private final String url ="jdbc:mysql://localhost/";
    private final String driver = "com.mysql.jdbc.Driver";
    final private String encode = "?useUnicode=true&characterEncoding=utf-8";
    
    private String dbName ="sinema";
    private String dbUser ="root";
    private String dbPass = "";
    
    
    private Connection conn =null;
    private Statement st = null;
    private PreparedStatement preSt = null;
    
    public DB () {
    }
    public DB (String dbName, String dbUser, String dbPass) {
    this.dbName = dbName;
    this.dbUser = dbUser;
    this.dbPass = dbPass;
    }
    public Statement baglan() {
        if (conn!=null) {
        kapat();
        }
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url+dbName+encode,dbUser,dbPass);
            st = conn.createStatement();
            System.out.println("Bağlantı başarılı !"); 
       } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Bağlantı hatası !"+e);
        }
        return st;
    }
    public PreparedStatement preBaglan(String query) {
        try {
            if (conn != null) {
                kapat();
            }
            // kütüphane hazırlanıyor
            Class.forName(driver);
            conn = DriverManager.getConnection(url+dbName+encode, dbUser, dbPass);
            preSt = conn.prepareStatement(query);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("PreparedStatement hatası : " + e);
        }
        return preSt;
    }
    
    public void kapat () {
        try {
            if (preSt != null) {
                preSt.close();
                System.out.println("PraperedStatement Kapandı");
                preSt = null;
            }
            if(st!= null) {
            st.close();
                System.out.println("st kapatıldı !");
                st = null;
            }
            if(conn!= null) {
                conn.close();
                    System.out.println("conn kapatıldı !");
                    conn= null;
                }
            
        } catch (SQLException e) {
            System.err.println("St ve Conn kapatma hatası !"+e);
        }
    
    }
    public static void main(String[] args) {
        DB db = new DB();
        db.baglan();
    }
//    public boolean girisyap (String alinanAd, String alinanSifre) throws SQLException {
//    PreparedStatement st;
//    ResultSet rs;
//    
//    DB db = new DB();
//        try {
//            db.baglan();
//        } catch (Exception e) {
//            System.err.println("Veritabanı bağlantı hatası"+e);
//        }
//    String query = "SELECT kullaniciSifre from kullanicigiris =?";
//    st = db.conn.prepareStatement(query);
//    st.setString(1,alinanAd);
//    rs = st.executeQuery();
//    return false;
//    }
    
//    public boolean GirisKontrol(kullaniciGiris kul){
////        String query="SELECT * FROM kullanicigiris where " +kullaniciEnum.kulAdi+ "='"+kul.getKullaniciAdi()+"' and  " +kullaniciEnum.sifre+ "='"+kul.getSifre()+"'";
////                
////        try {
////            ResultSet rs=baglan().executeQuery(query);
////            System.out.println(query);
////            System.out.println(rs.getString("kullanicigiris"));
////            if(Integer.valueOf(rs.getString("kullanicigiris"))>0){
////                return true;
////            }else{
////                return false;
////            }
////        } catch (Exception e) {
////        }finally{
////                kapat();
////            }
////        return false;
////    }
    
}
