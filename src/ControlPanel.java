import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by andre on 5/11/2016.
 */
public class ControlPanel implements Initializable{
    @FXML
    private Button StartBtn;
    @FXML
    private Button QuitBtn;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert StartBtn != null : "fx:id=\"StartBtn\" was not injected: check your FXML file.";
        StartBtn.setOnAction( ae -> Main.mainLoop.start());
        assert QuitBtn != null : "fx:id=\"QuitBTn\" was not injected";
        QuitBtn.setOnAction( ae -> Platform.exit());
    }
}
