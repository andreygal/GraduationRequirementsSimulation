import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;


public class ControlPanel implements Initializable {
    @FXML
    private Button OpenBtn;
    @FXML
    private Button StartBtn;
    @FXML
    private Button StopBtn;
    @FXML
    private Button QuitBtn;
    @FXML
    private Label StatusLabel;
    @FXML
    private Label TimeLabel;

    private final DecimalFormat formatter = new DecimalFormat("#.0#####", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    private final FileChooser fileChooser = new FileChooser();
    private File inputFile;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Google Input Files", "*.in"));
        //Initialize button controls
        assert OpenBtn != null : "fx:id=\"StartBtn\" was not injected: check your FXML file";
        OpenBtn.setOnAction(ae -> {
            File oldFile = inputFile;
            inputFile = fileChooser.showOpenDialog(new Stage());
            System.out.println("Opening file");
            this.pcs.firePropertyChange("inputFile", oldFile, inputFile);
        });
        assert StartBtn != null : "fx:id=\"StartBtn\" was not injected: check your FXML file.";
        StartBtn.setOnAction(ae -> Main.mainLoop.start());
        assert StopBtn != null : "fx:id=\"StopBtn\" was not injected: check your FXML file.";
        StopBtn.setOnAction(ae -> Main.mainLoop.stop());
        assert QuitBtn != null : "fx:id=\"QuitBTn\" was not injected: check your FXML file.";
        QuitBtn.setOnAction(ae -> Platform.exit());
        assert TimeLabel != null : "fx:id=\"TimeLabel\" was not injected: check your FXML file.";
        TimeLabel.setText(String.valueOf(MainLoop.globalTime));

    }

    public void setGlobalTime(double globalTime) {
        TimeLabel.setText("GlobalTime: " + formatter.format(globalTime));
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChnageListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}




