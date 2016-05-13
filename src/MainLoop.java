import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class MainLoop extends AnimationTimer {
    ArrayList<Car> cars = new ArrayList<>();
    Image carImage;
    GraphicsContext gc;

    final public double ISLAND_WIDTH  = 40;
    final public double ISLAND_HEIGHT = 40;

    public double canvasCenterX = Main.canvas.getWidth()/2.0;
    public double canvasCenterY = Main.canvas.getHeight()/2.0;

    static long   prevTime = 0;
    static double globalTime = 0.0;

    public MainLoop() {
        carImage = new Image(getClass().getResourceAsStream("car.png"));
        gc = Main.canvas.getGraphicsContext2D();
    }

    @Override
    public void start() {
        cars.add(new Car (carImage, canvasCenterX - carImage.getWidth()  / 2.0,
                                    canvasCenterY - carImage.getHeight() / 2.0, "Car 1"));
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
        double outerBound = (Main.numOfCars + 1) * 30.0 + ISLAND_WIDTH / 2;
        gc.fillOval(canvasCenterX - outerBound, canvasCenterY - outerBound,
                outerBound * 2, outerBound * 2);
        //draw the traffic circle, lanes and intersections
        gc.setFill(Color.GRAY);
        gc.fillOval(canvasCenterX - ISLAND_WIDTH/2,
                canvasCenterY - ISLAND_HEIGHT/2, ISLAND_WIDTH, ISLAND_HEIGHT);


        //Draw the lane center marks
        double trackRadiusOfCurv;
        gc.setStroke(Color.WHITE);
        gc.setLineDashes(12f);
        for (int i = 1; i <= Main.numOfCars; i++) {
            trackRadiusOfCurv = (ISLAND_WIDTH / 2.0) + 30.0 * i;
            System.out.println("Drawing lanes.");
            gc.strokeOval(canvasCenterX - trackRadiusOfCurv,
                    canvasCenterY - trackRadiusOfCurv,
                    trackRadiusOfCurv * 2, trackRadiusOfCurv * 2);
        }
        //draw the cars
        for (Car car : cars) {
            car.render(gc);
        }
            //lastSecond++;
        if (prevTime == 0)
           globalTime = 0.0167;
        else {
            globalTime += ((currentNanoTime - prevTime) / 1000000000.0);
        }
        prevTime = currentNanoTime;
    }

    @Override
    public void stop() {
        for (Car car : cars) {
            car.stopCar();
        }
        super.stop();
    }



}