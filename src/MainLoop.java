import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class MainLoop extends AnimationTimer {
    ArrayList<Car> cars = new ArrayList<>();
    Image carImage;
    GraphicsContext gc;

    final private double CENTER_WIDTH  = 30;
    final private double CENTER_HEIGHT = 30;

    private double canvasCenterX = Main.canvas.getWidth()/2.0;
    private double canvasCenterY = Main.canvas.getHeight()/2.0;

    private long startTime = 0;
    private long elapsedTime = 0;
    static double currentTime = 0.0;
    static double globalTime = 0.0;

    public MainLoop() {
        carImage = new Image(getClass().getResourceAsStream("car.png"));
        gc = Main.canvas.getGraphicsContext2D();
    }

    @Override
    public void start() {
        cars.add(new Car (carImage, canvasCenterX - 12, canvasCenterY - 12, "Car 1"));
        for (Car car : cars)
            car.startCar();
        System.out.println("Center is at " + canvasCenterX + " " + canvasCenterY);
        super.start();
    }

    @Override
    public void handle(long currentNanoTime) {
        //currentTime += currentNanoTime / 1e9;
        startTime = currentNanoTime/1000000;
        System.out.println(currentTime + " " + globalTime);
        //background clears the canvas
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, Main.canvas.getWidth(), Main.canvas.getHeight());
        //Render the canvas
        //if( Math.floor(currentTime) > lastSecond) {

            System.out.println("Inside handle: rendering cars");
            //draw the traffic circle, lanes and intersections
            gc.setFill(Color.BLACK);
            gc.fillOval(canvasCenterX - CENTER_WIDTH/2,
                        canvasCenterY - CENTER_HEIGHT/4, CENTER_WIDTH, CENTER_HEIGHT);
            gc.strokeOval(canvasCenterX - 100, canvasCenterY - 100, 200, 200);
            //draw the cars
            for (Car car : cars) {
                car.render(gc);
            }
            //lastSecond++;
        elapsedTime = (System.currentTimeMillis() - startTime);
        globalTime += (elapsedTime / 1000.0);
        //if less than 16ms passed sleep for the difference
        if (elapsedTime < 16l) {
            System.out.println("Handle sleeping");
            try {
                Thread.sleep(16l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void stop() {
        for (Car car : cars) {
            car.stopCar();
        }
        super.stop();
    }


}