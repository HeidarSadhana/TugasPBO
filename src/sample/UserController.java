package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    public int id_akun;

    @FXML
    private TableColumn<amplang, String> bahanA;

    @FXML
    private TableColumn<amplang, String> beratA;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnLogout;

    @FXML
    private TableColumn<amplang, String> hargaA;

    @FXML
    private TableColumn<amplang, String> merkA;

    @FXML
    private TableColumn<amplang, String> stokA;

    @FXML
    private TableView<amplang> tableamplang;

    @FXML
    private TextField txtCari;

    @FXML
    private Label username;

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
            merkA.setCellValueFactory(new PropertyValueFactory<>("nama"));
            bahanA.setCellValueFactory(new PropertyValueFactory<> ("bahan"));
            beratA.setCellValueFactory(new PropertyValueFactory<> ("berat"));
            stokA.setCellValueFactory(new PropertyValueFactory<> ("stok"));
            hargaA.setCellValueFactory(new PropertyValueFactory<> ("harga"));

            tableamplang.setItems(amplangList);
        } catch(SQLException e) {
            System.out.println("Gagal Load Data Produk");
        }
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
    void txtCariData(KeyEvent event) {
        settableamplang(txtCari.getText());
    }

    public void setUsername(String text) {
        username.setText(text);
    }
}
