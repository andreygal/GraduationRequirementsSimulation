import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;
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
    @FXML
    private Slider DilationSlider;

    private final DecimalFormat formatter = new DecimalFormat("#.0#####", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    private final FileChooser fileChooser = new FileChooser();
    private File inputFile;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Google Input Files", "*.in"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        //Initialize button controls
        assert OpenBtn != null : "fx:id=\"StartBtn\" was not injected: check your FXML file";
        OpenBtn.setOnAction(ae -> {
            File oldFile = inputFile;
            inputFile = fileChooser.showOpenDialog(new Stage());
            System.out.println("Opening file");
            this.pcs.firePropertyChange("inputFile", oldFile, inputFile);
        });

        assert StartBtn != null : "fx:id=\"StartBtn\" was not injected: check your FXML file.";
        StartBtn.setOnAction(ae -> {
            Main.mainLoop.start();
            StopBtn.setDisable(false);
            StartBtn.setDisable(true);
            setStatusLabel(Main.mainLoop.getCurrCaseIndex(),
                    Main.mainLoop.getNumCar(),
                    Main.mainLoop.getAlotTime());

        });

        assert StopBtn != null : "fx:id=\"StopBtn\" was not injected: check your FXML file.";
        StopBtn.setDisable(true);
        StopBtn.setOnAction(ae -> {
            Main.mainLoop.stop();
            StartBtn.setDisable(false);
            StopBtn.setDisable(true);
        });

        assert QuitBtn != null : "fx:id=\"QuitBTn\" was not injected: check your FXML file.";
        QuitBtn.setOnAction(ae -> {
            Main.mainLoop.stop();
            Platform.exit();
        });

        assert TimeLabel != null : "fx:id=\"TimeLabel\" was not injected: check your FXML file.";
        TimeLabel.setText(String.valueOf(MainLoop.globalTime));

        DilationSlider.valueProperty().addListener((property, oldValue, newValue) -> {
            Main.mainLoop.setTimeDilation(DilationSlider.getValue());
            System.out.println("Dilation value is " + DilationSlider.getValue() );
        });

        Objects.requireNonNull(DilationSlider).setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                String label = "";
                if (value == 1) {
                    label = "Real Time";
                } else if (value == 2) {
                    label = "x 1/2";
                } else if (value == 3) {
                    label = "x 1/3";
                }
                return label;
            }
            @Override
            public Double fromString(String string) {
                return null; // Not used
            }
        });
    }



    public void setGlobalTime(double globalTime) {
        TimeLabel.setText("GlobalTime: " + formatter.format(globalTime));
    }

    public void setStatusLabel(int caseNum, int numOfCars, int allottedTime) {
        StatusLabel.setText("Case Number: " + caseNum + "\nNumber of Cars: " + numOfCars
                             + "\nAllotted Time: " + allottedTime);
    }

    public void resetStart() { StartBtn.setDisable(false);}
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}




