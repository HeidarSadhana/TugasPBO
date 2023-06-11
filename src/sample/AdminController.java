package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminController {

    @FXML
    private ToggleGroup editbahan;

    @FXML
    private RadioButton editbahan1;

    @FXML
    private RadioButton editbahan2;

    @FXML
    private RadioButton editbahan3;

    @FXML
    private ToggleGroup addbahan;

    @FXML
    private RadioButton addbahan1;

    @FXML
    private RadioButton addbahan2;

    @FXML
    private RadioButton addbahan3;

    @FXML
    private TableColumn<?, ?> akunR;

    @FXML
    private TableColumn<?, ?> bahanA;

    @FXML
    private TableColumn<?, ?> beratA;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnLogout;

    @FXML
    private AnchorPane editProduk;

    @FXML
    private TableColumn<?, ?> hargaA;

    @FXML
    private TableColumn<?, ?> hargaR;

    @FXML
    private Label labelproduk;

    @FXML
    private AnchorPane lihatproduct;

    @FXML
    private Button menuLihat;

    @FXML
    private Button menuTambah;

    @FXML
    private Button menuriwayat;

    @FXML
    private TableColumn<?, ?> namaA;

    @FXML
    private TableColumn<?, ?> orderR;

    @FXML
    private AnchorPane riwayatpesanan;

    @FXML
    private TableColumn<?, ?> stokA;

    @FXML
    private TableView<?> tableRiwayat;

    @FXML
    private TableView<amplang> tableamplang;

    @FXML
    private AnchorPane tambahproduct;

    @FXML
    private TableColumn<?, ?> tanggalR;

    @FXML
    private TableColumn<?, ?> totalR;

    @FXML
    private TextField txtCari;

    @FXML
    private TextField txtEditNama;

    @FXML
    private TextField txtHarga;

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtStok;

    @FXML
    private TextField txtStokEdit;

    @FXML
    private TextField txtxHargaEdit;

    @FXML
    private Label username;

    @FXML
    private ComboBox<String> cbberat;

    @FXML
    private ComboBox<String> cbberatedit;

    public void setCbberat(ObservableList<String> berat) {
        cbberat.setItems(berat);
        cbberatedit.setItems(berat);
    }

    public void setUsername(String user) {
        username.setText(user);
    }

    @FXML
    void addData(MouseEvent event) {
        try{
            String query = "SELECT * FROM tbproduk WHERE nama = ?";
            connect = Database.connectDB();
            prepare = connect.prepareStatement(query);
            prepare.setString(1,txtNama.getText());
            result = prepare.executeQuery();
            if (!result.next()){
                    int berat = Integer.parseInt(cbberat.getValue());
                    int stok = Integer.parseInt(txtStok.getText());
                    int harga = Integer.parseInt(txtHarga.getText());

                    RadioButton seleksi = (RadioButton) addbahan.getSelectedToggle();
                    String bahan = seleksi.getText();

                    amplang ampelang = new amplang(txtNama.getText(), bahan, berat, stok, harga);
                    ampelang.addtodb();
            } else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplikat Nama Produk");
                alert.setContentText("Add Data Produk Gagal");
                alert.showAndWait();
            }
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setContentText("Add Data Produk Gagal (Ada Error Coy)");
            alert.showAndWait();
        }
        clrForm();
    }

    @FXML
    void btnEditClicked(MouseEvent event) {
        lihatproduct.setVisible(false);
        editProduk.setVisible(true);

        amplang ampelang = tableamplang.getSelectionModel().getSelectedItem();
        txtEditNama.setPromptText(ampelang.getNama());

        if (ampelang.getBahan().equals("Ikan Tenggiri")){
            editbahan.selectToggle(editbahan1);
        }else if (ampelang.getBahan().equals("Ikan Gabus (Haruan)")){
            editbahan.selectToggle(editbahan2);
        }else if(ampelang.getBahan().equals("Ikan Pipih")){
            editbahan.selectToggle(editbahan3);
        }

        int berat = ampelang.getBerat();
        String beratString = Integer.toString(berat);
        cbberatedit.setValue(beratString);

        int stok = ampelang.getStok();
        String stokString = Integer.toString(stok);
        txtStokEdit.setPromptText(stokString);

        int harga = ampelang.getHarga();
        String hargaString = Integer.toString(harga);
        txtxHargaEdit.setPromptText(hargaString);
        clrForm();
    }

    @FXML
    void btnHpsClicked(MouseEvent event) throws SQLException {
        ButtonType yes = new ButtonType("Ya", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Tidak", ButtonBar.ButtonData.CANCEL_CLOSE);
        amplang ampelang = tableamplang.getSelectionModel().getSelectedItem();

        int id_produk = 0;
        String ambil_id = "SELECT id_produk FROM tbproduk WHERE nama = ?";
        prepare = connect.prepareStatement(ambil_id);
        prepare.setString(1, ampelang.getNama());
        result = prepare.executeQuery();
        if(result.next()){
            id_produk = result.getInt("id_produk");
        }

        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Yakin ingin menghapus "+ampelang.getNama(),
                yes,
                no);

        alert.setTitle("Menghapus data");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            ampelang.hapustodb(id_produk);
        }

        txtCari.setText("");
        settableamplang(txtCari.getText());
    }

    void clrForm(){
        txtNama.clear();
        txtHarga.clear();
        txtStok.clear();
        cbberat.setValue("berat");
        addbahan.selectToggle(null);

        txtEditNama.clear();
        txtxHargaEdit.clear();
        txtStokEdit.clear();
        editbahan.selectToggle(null);
        cbberatedit.setValue("berat");
    }

    @FXML
    void clrForm(MouseEvent event) {
        txtNama.clear();
        txtHarga.clear();
        txtStok.clear();
        cbberatedit.setValue("berat");
        addbahan.selectToggle(null);

        txtEditNama.clear();
        txtxHargaEdit.clear();
        txtStokEdit.clear();
        editbahan.selectToggle(null);
        cbberatedit.setValue("berat");
    }

    @FXML
    void editData(MouseEvent event) throws SQLException {
        int id_produk = 0;
        String nama = txtEditNama.getText();
        int berat = 0;
        int stok = 0;
        int harga = 0;

        amplang ampelang = tableamplang.getSelectionModel().getSelectedItem();
        if (nama.isEmpty()) {
            nama = ampelang.getNama();
        }
        if (txtStokEdit.getText().isEmpty()) {
            stok = ampelang.getStok();
        } else {
            stok = Integer.parseInt(txtStokEdit.getText());
        }
        if (txtxHargaEdit.getText().isEmpty()) {
            harga = ampelang.getHarga();
        } else {
            harga = Integer.parseInt(txtxHargaEdit.getText());
        }

        if(berat == 0){
            berat = ampelang.getBerat();
        }

        String ambil_id = "SELECT id_produk FROM tbproduk WHERE nama = ?";
        prepare = connect.prepareStatement(ambil_id);
        prepare.setString(1, ampelang.getNama());
        result = prepare.executeQuery();
        if (result.next()) {
            id_produk = result.getInt("id_produk");
        }

        System.out.println(id_produk + "id");

        RadioButton seleksi = (RadioButton) editbahan.getSelectedToggle();
        String bahan = seleksi.getText();

        ampelang.edittodb(nama, bahan, berat, stok, harga, id_produk);

        txtCari.setText("");
        settableamplang(txtCari.getText());
    }

    @FXML
    void exit(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void lihatProduk(MouseEvent event) {
        menuTambah.setStyle(null);
        tambahproduct.setVisible(false);
        editProduk.setVisible(false);
        lihatproduct.setVisible(true);
        clrForm();
        settableamplang("");
    }

    @FXML
    void logout(MouseEvent event) {
        try {
            btnLogout.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("loginpage.fxml"));
            Stage stage = new Stage();
            Main.drag(stage, root);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root, 250, 300));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void tambahProduk(MouseEvent event) {
        menuLihat.setStyle(null);
        lihatproduct.setVisible(false);
        editProduk.setVisible(false);
        tambahproduct.setVisible(true);
        clrForm();
    }

    @FXML
    void txtCariData(KeyEvent event) {
        settableamplang(txtCari.getText());
    }

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    ObservableList<amplang> amplangList;

    public void settableamplang(String s){
        try{
            connect = Database.connectDB();
            String SQL = "SELECT * FROM tbproduk WHERE nama LIKE '%" + s + "%'";
            prepare = connect.prepareStatement(SQL);
            result = prepare.executeQuery(SQL);

            this.amplangList = FXCollections.observableArrayList();
            while(result.next()){
                amplang amplangbaru = new amplang(
                        result.getString("nama"),
                        result.getString("bahan"),
                        result.getInt("berat"),
                        result.getInt("stok"),
                        result.getInt("harga")
                );
                amplangList.add(amplangbaru);
            }
            namaA.setCellValueFactory(new PropertyValueFactory<>("nama"));
            bahanA.setCellValueFactory(new PropertyValueFactory<> ("bahan"));
            beratA.setCellValueFactory(new PropertyValueFactory<> ("berat"));
            stokA.setCellValueFactory(new PropertyValueFactory<> ("stok"));
            hargaA.setCellValueFactory(new PropertyValueFactory<> ("harga"));

            tableamplang.setItems(amplangList);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
