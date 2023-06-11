package sample;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class amplang {
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    protected String nama;
    protected String bahan;
    protected int berat;
    protected int stok;
    protected int harga;

    public amplang(String nama, String bahan, int berat, int stok, int harga) {
        this.nama = nama;
        this.bahan = bahan;
        this.berat = berat;
        this.stok = stok;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public int getBerat() {
        return berat;
    }

    public void setBerat(int berat) {
        this.berat = berat;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public void addtodb() throws SQLException {
        connect = Database.connectDB();
        String query = "INSERT INTO tbproduk(nama, bahan, berat, stok, harga) VALUES (?,?,?,?,?)";

        prepare = connect.prepareStatement(query);

        prepare.setString(1, this.getNama());
        prepare.setString(2, this.getBahan());
        prepare.setInt(3, this.getBerat());
        prepare.setInt(4, this.getStok());
        prepare.setInt(5, this.getHarga());

        prepare.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tambah Berhasil");
        alert.setContentText("Produk Berhasil di Tambah");
        alert.showAndWait();
    }

    public void hapustodb(int id_produk) throws SQLException {
        connect = Database.connectDB();
        String query = "delete from tbproduk where id_produk=?";

        prepare = connect.prepareStatement(query);
        prepare.setInt(1, id_produk);
        prepare.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hapus Berhasil");
        alert.setContentText("Produk Berhasil di Hapus");
        alert.showAndWait();
    }

    public void edittodb(String nama, String bahan, int berat, int stok, int harga, int id) throws SQLException{
        connect = Database.connectDB();
        this.setNama(nama);
        this.setBahan(bahan);
        this.setBerat(berat);
        this.setHarga(harga);

        String query = "update tbproduk set nama=?,bahan=?,berat=?,stok=?,harga=? where id_produk=?";

        prepare = connect.prepareStatement(query);

        prepare.setString(1, nama);
        prepare.setString(2, bahan);
        prepare.setInt(3, berat);
        prepare.setInt(4, stok);
        prepare.setInt(5, harga);
        prepare.setInt(6, id);

        prepare.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Berhasil");
        alert.setContentText("Produk Berhasil di Edit");
        alert.showAndWait();
    }
}
