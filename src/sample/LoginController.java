package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Database;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private Button btnlog;

    @FXML
    private Button btnreg;

    @FXML
    private Label labelbelum;

    @FXML
    private Label labellogin;

    @FXML
    private Label labelregis;

    @FXML
    private Label labelsudah;

    @FXML
    private AnchorPane loginpage;

    @FXML
    private Label pindahlogin;

    @FXML
    private Label pindahregis;

    @FXML
    private AnchorPane registerpage;

    @FXML
    private PasswordField txtkpassreg;

    @FXML
    private PasswordField txtpasslog;

    @FXML
    private PasswordField txtpassreg;

    @FXML
    private TextField txtuserlog;

    @FXML
    private TextField txtuserreg;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    final ObservableList<String> berat = FXCollections.observableArrayList("200", "500", "700", "1000");

    @FXML
    void login(MouseEvent event) {
        String query = "SELECT * FROM tbakun WHERE username = ? AND password = ?";
        connect = Database.connectDB();
        try{
            Alert alert;
            prepare = connect.prepareStatement(query);
            prepare.setString(1, txtuserlog.getText());
            prepare.setString(2, txtpasslog.getText());
            result = prepare.executeQuery();
            if(result.next()){
                Parent root;
                if("Admin".equals(result.getString("username"))){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("adminpage.fxml"));
                    root = loader.load();
                    AdminController controller = loader.getController();
                    controller.setUsername(txtuserlog.getText());
                    controller.setCbberat(berat);
                }else{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("userpage.fxml"));
                    root = loader.load();
                    UserController controller = loader.getController();
                    controller.settableamplang("");
                    controller.setUsername(txtuserlog.getText());
                    controller.id_akun = result.getInt("id_akun");
                }
                btnlog.getScene().getWindow().hide();
                Stage stage = new Stage();
                Main.drag(stage, root);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(new Scene(root,725,512));
                stage.show();
                bersih();
            } else {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login Gagal");
                alert.setHeaderText(null);
                alert.setContentText("Username/Password Salah");
                alert.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void pindahlogin(MouseEvent event) {
        loginpage.setVisible(true);
        registerpage.setVisible(false);
    }

    @FXML
    void pindahregis(MouseEvent event) {
        loginpage.setVisible(false);
        registerpage.setVisible(true);
    }

    void bersih(){
        txtpassreg.clear();
        txtuserreg.clear();
        txtkpassreg.clear();
        txtuserlog.clear();
        txtpasslog.clear();
    }

    @FXML
    void register(MouseEvent event) throws SQLException{
        String selectQuery = "select * from tbakun where username=?";
        connect = Database.connectDB();

        prepare = connect.prepareStatement(selectQuery);
        prepare.setString(1, txtuserreg.getText());
        result = prepare.executeQuery();

        if(result.next()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Pesan Informasi");
            alert.setContentText("Username Telah Ada");
            alert.showAndWait();
            return;
        }

        if (txtpassreg.getText().equals(txtkpassreg.getText())){
            String insertQuery = "Insert into tbakun(username, password) values (?,?)";
            connect = Database.connectDB();

            prepare = connect.prepareStatement(insertQuery);
            prepare.setString(1, txtuserreg.getText());
            prepare.setString(2, txtpassreg.getText());
            prepare.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pesan Informasi");
            alert.setContentText("Registrasi Akun Berhasil");
            alert.showAndWait();
            registerpage.setVisible(false);
            loginpage.setVisible(true);
            bersih();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Pesan Informasi");
            alert.setContentText("Password dan Konfirmasi Password Beda");
            alert.showAndWait();
        }
    }
}






























