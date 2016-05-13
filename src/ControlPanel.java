import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;


public class ControlPanel implements Initializable {
    @FXML
    private Button StartBtn;
    @FXML
    private Button StopBtn;
    @FXML
    private Button QuitBtn;
    @FXML
    private Slider CarSlider;
    @FXML
    private Label CarLabel;
    @FXML
    private Slider IntersectSlider;
    @FXML
    private Label IntersectLabel;
    @FXML
    private Label TimeLabel;
    private DecimalFormat formatter = new DecimalFormat("#.0#####", DecimalFormatSymbols.getInstance( Locale.ENGLISH ));

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        //Initialize car slider
        assert CarSlider != null : "fx:id=\"CarSlider\" was not injected: check your FXML file.";
        CarSlider.valueProperty().addListener(
                (ObservableValue<? extends Number> prop, Number oldVal, Number newVal) -> {
                    if (CarSlider.isValueChanging()) {
                        CarLabel.setText("Number of cars: ...");
                    } else {
                        Main.numOfCars = (int) CarSlider.getValue();
                        CarLabel.setText("Number of cars: " + (int) CarSlider.getValue());
                    }
        });
        //Initialize intersection slider
        assert IntersectSlider != null : "fx:id=\"IntersectSlider\" was not injected: check your FXML file.";
        IntersectSlider.valueProperty().addListener(
                (ObservableValue<? extends Number> prop, Number oldVal, Number newVal) -> {
                    if (IntersectSlider.isValueChanging()) {
                        IntersectLabel.setText("Number of intersections: ...");
                    } else {
                        Main.numOfIntersections = (int) IntersectSlider.getValue();
                        IntersectLabel.setText("Number of intersections: " + (int) IntersectSlider.getValue());
                    }
                });
        //Initialize button controls
        assert StartBtn != null : "fx:id=\"StartBtn\" was not injected: check your FXML file.";
        StartBtn.setOnAction( ae -> Main.mainLoop.start());
        assert StopBtn != null : "fx:id=\"StopBtn\" was not injected: check your FXML file.";
        StopBtn.setOnAction( ae -> Main.mainLoop.stop());
        assert QuitBtn != null : "fx:id=\"QuitBTn\" was not injected: check your FXML file.";
        QuitBtn.setOnAction( ae -> Platform.exit());
        assert TimeLabel != null : "fx:id=\"TimeLabel\" was not injected: check your FXML file.";
        TimeLabel.setText( String.valueOf(MainLoop.globalTime));

    }

    public void setGlobalTime(double globalTime) {
        TimeLabel.setText("GlobalTime: " + formatter.format(globalTime));
    }
}
