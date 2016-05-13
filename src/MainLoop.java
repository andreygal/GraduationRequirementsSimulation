import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class MainLoop extends AnimationTimer {
    ArrayList<Car> cars = new ArrayList<>();
    Image carImage;
    GraphicsContext gc;

    final public double ISLAND_WIDTH  = 40;
    final public double ISLAND_HEIGHT = 40;
    final public double dashedMarkOffset = 28;

    public double canvasCenterX = Main.canvas.getWidth()/2.0;
    public double canvasCenterY = Main.canvas.getHeight()/2.0;

    static long   prevTime = 0;
    static double globalTime = 0.0;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public MainLoop() {
        carImage = new Image(getClass().getResourceAsStream("car.png"));

        gc = Main.canvas.getGraphicsContext2D();
    }

    @Override
    public void start() {
        cars.add(new Car (carImage, canvasCenterX - carImage.getWidth() / 2.0,
                                    canvasCenterY - carImage.getHeight() / 2.0,
                ISLAND_WIDTH / 2.0 + (dashedMarkOffset / 2.0) * 3 , "Test Car"));
        for (Car car : cars)
            car.startCar();
        System.out.println("Center is at " + canvasCenterX + " " + canvasCenterY);
        super.start();
    }

    @Override
    public void handle(long currentNanoTime) {
        System.out.println(prevTime + " GlobalTime: " + globalTime);

        //Dynamically draw the background
        //Draw the grass
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, Main.canvas.getWidth(), Main.canvas.getHeight());

        //Draw the asphalt
        gc.setFill(Color.BLACK);
        double outerBound = (Main.numOfCars + 1) * dashedMarkOffset + ISLAND_WIDTH / 2;
        gc.fillOval(canvasCenterX - outerBound, canvasCenterY - outerBound,
                outerBound * 2, outerBound * 2);

        //Draw the intersections
        gc.setFill(Color.BLACK);
        double degree = 0;
        double step = 360.0 / Main.numOfIntersections;
        for (int i = 0; i < Main.numOfIntersections; i++) {
            gc.save();
            Rotate rectRotate = new Rotate(degree, canvasCenterX, canvasCenterX);
            gc.setTransform(rectRotate.getMxx(),
                    rectRotate.getMyx(),
                    rectRotate.getMxy(),
                    rectRotate.getMyy(),
                    rectRotate.getTx(),
                    rectRotate.getTy());
            gc.fillRect(canvasCenterX - dashedMarkOffset / 2.0,
                    canvasCenterY - outerBound - 40,
                    dashedMarkOffset,
                    outerBound + 40);
            degree += step;
            gc.restore();
        }

        //draw the traffic circle island
        gc.setFill(Color.GRAY);
        gc.fillOval(canvasCenterX - ISLAND_WIDTH/2,
                canvasCenterY - ISLAND_HEIGHT/2, ISLAND_WIDTH, ISLAND_HEIGHT);

        //Draw the lanes
        double trackRadiusOfCurv;
        gc.setStroke(Color.WHITE);
        gc.setLineDashes(12f);
        for (int i = 1; i <= Main.numOfCars; i++) {
            trackRadiusOfCurv = (ISLAND_WIDTH / 2.0) + dashedMarkOffset * i;
            System.out.println("Drawing lanes.");
            gc.strokeOval(canvasCenterX - trackRadiusOfCurv,
                    canvasCenterY - trackRadiusOfCurv,
                    trackRadiusOfCurv * 2, trackRadiusOfCurv * 2);
        }

        //draw the cars
        for (Car car : cars) {
            car.render(gc);
        }

        if (prevTime == 0)
           globalTime = 0.0167;
        else {
            globalTime += ((currentNanoTime - prevTime) / 1000000000.0);
        }
        prevTime = currentNanoTime;
        pcs.firePropertyChange("globalTime", prevTime, globalTime);
    }

    @Override
    public void stop() {
        for (Car car : cars) {
            car.stopCar();
        }
        super.stop();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChnageListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }



}