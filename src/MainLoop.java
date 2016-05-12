import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class MainLoop extends AnimationTimer {
    ArrayList<Car> cars = new ArrayList<>();
    Image carImage;
    GraphicsContext gc;

    final private double CENTER_WIDTH  = 50;
    final private double CENTER_HEIGHT = 50;
    
    private double canvasCenterX = Main.canvas.getWidth()/2.0;
    private double canvasCenterY = Main.canvas.getHeight()/2.0;
    
    static double currentTime = 0.0;
    static long lastSecond = 0;

    public MainLoop() {
        carImage = new Image(getClass().getResourceAsStream("car.png"));
        gc = Main.canvas.getGraphicsContext2D();
    }

    @Override
    public void start() {
        cars.add(new Car (carImage, 250.0, 250.0, "Car 1"));
        for (Car car : cars)
            car.startCar();

        super.start();
    }

    @Override
    public void handle(long currentNanoTime) {
        currentTime += currentNanoTime / 1e9;
        System.out.println(currentTime + " " + lastSecond);
        if( Math.floor(currentTime) > lastSecond) {
            System.out.println("Inside handle: rendering cars");
            for (Car car : cars) {
                car.render(gc);
            }

            lastSecond++;
        }


        gc.setFill(Color.BLACK);
        gc.fillOval(225, 225, CENTER_WIDTH, CENTER_HEIGHT);
        //https://jaxenter.com/tutorial-a-glimpse-at-javafxs-canvas-api-105696.html
        //https://www.w3.org/TR/SVG/paths.html
        //http://www.hameister.org/JavaFX_Dartboard.html
        //use paths to draw the circles, then fill them with gradients
//        for (int i = 1; i <= Main.numOfCars) {
//
//        }
        gc.strokeOval(150, 150, 200, 200);

        //for(int i = 0; i < cars.size(); i++);
        //gc.drawImage(image, cars.get(i).getX(), cars.get(i).getY());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
