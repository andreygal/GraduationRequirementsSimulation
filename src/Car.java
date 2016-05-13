import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Car implements Runnable {

    //angular velocity in rads/s
    static double  angularVelocity;
    static double  centerX;
    static double  centerY;
    static double  imageWidth;
    static double  imageHeight;
    static Image   carImage;
    private String carName;
    private double positionX;
    private double positionY;
    private double radius = 100.0;
    //update arc is the subtended arc after which update is called.
    //we want to update the image every 2 pixels
    private double updateArc      =  2.0 / radius;
    //interval is in seconds
    private double updateInterval = (updateArc/Main.arcAngle);
    private long updateIntervalMilli = (long)(updateInterval * 1000.0);

    boolean moving;
    Thread carThread;

    public Car(Image carImage, double centerX, double centerY, String carName) {
        this.carImage = carImage;
        this.carName = carName;
        this.centerX = centerX;
        this.centerY = centerY;
        this.imageWidth = carImage.getWidth();
        this.imageHeight = carImage.getHeight();
        this.angularVelocity = (2 * Math.PI) / ((double) Main.numOfIntersections);
        carThread = new Thread(this, carName);
    }
    public void startCar() {
        System.out.println("Starting car thread");
        carThread.start();
    }
    public void update(double time) {

        positionX = centerX + radius * Math.cos(time * angularVelocity);
        positionY = centerY + radius * Math.sin(time * angularVelocity);
        //System.out.println("Updating position x " + positionX + " y " + positionY);
    }

    public void stopCar() {
        moving = false;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(carImage, positionX, positionY);

    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, carImage.getWidth(), carImage.getHeight());
    }

    public boolean intersects(Car anotherCar) {
        return anotherCar.getBoundary().intersects(this.getBoundary());
    }

    @Override
    public void run() {
        moving = true;
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        while(moving) {
            if (elapsedTime >= updateIntervalMilli) {
                update(MainLoop.globalTime);
                elapsedTime = 0;
                startTime = System.currentTimeMillis();
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            //System.out.println("Inside car run: updating car position");
            //System.out.println("Position " + positionX + ", " + positionY);
        }

    }
}
