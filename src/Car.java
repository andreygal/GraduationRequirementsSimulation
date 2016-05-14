import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Car implements Runnable {

    //angular velocity in rads/s
    static double  angularVelocity;
    static double  centerX;
    static double  centerY;
    static double  imageWidth;
    static double  imageHeight;
    static Image   carImage;
    private double positionX;
    private double positionY;
    private double radius = 34;
    private double startStopRadius;
    private int startIntersection;
    //update arc is the subtended arc after which update is called.
    //we want to update the image every 2 pixels
    private double updateArc      =  2.0 / radius;
    //interval is in seconds
    private double updateInterval = (updateArc/Main.arcAngle);
    private long updateIntervalMilli = (long)(updateInterval * 1000.0);

    boolean moving;
    Thread carThread;

    public Car(Image carImage, double centerX, double centerY, double radius, int startIntersection) {
        this.carImage = carImage;
        this.startIntersection = startIntersection;
        this.centerX = centerX;
        this.centerY = centerY;
        this.imageWidth = carImage.getWidth();
        this.imageHeight = carImage.getHeight();
        this.radius = radius;
        this.startStopRadius = MainLoop.outerBound + 20;
        //set the starting position to be the intersection with traffic circle
        this.positionX = centerX + startStopRadius * Math.cos(MainLoop.intersectRads.get(startIntersection - 1));
        this.positionY = centerY + startStopRadius * Math.sin(MainLoop.intersectRads.get(startIntersection - 1));
        //cars move counter-clockwise
        this.angularVelocity = -( (2 * Math.PI) / ((double) Main.numOfIntersections));
        carThread = new Thread(this);
    }

    public void startCar() {
        System.out.println("Starting car thread");
        carThread.start();
    }

    public void update(double time) {
        double offset = MainLoop.intersectRads.get(startIntersection - 1);
        positionX = centerX + radius * Math.cos(offset + time * angularVelocity);
        positionY = centerY + radius * Math.sin(offset + time * angularVelocity);
        //System.out.println("Updating position x " + positionX + " y " + positionY);
    }

    public void stopCar() {
        moving = false;
    }

    private void enterIntersection() throws InterruptedException {
        double prevTime = MainLoop.globalTime;
        double startTime = MainLoop.globalTime;
        double timeToLane = Math.floor(prevTime + 20) - prevTime;
        double radReductionRate = (startStopRadius - radius) / timeToLane;
        double currRadius = startStopRadius;
        System.out.println("Moving to lane");
        while(MainLoop.globalTime - startTime < timeToLane) {
            System.out.println("Loop");
                currRadius -= (radReductionRate * (MainLoop.globalTime - prevTime));
                //calculate new position based on reduced radius
                positionX = centerX + currRadius * Math.cos(MainLoop.intersectRads.get(startIntersection - 1));
                positionY = centerY + currRadius * Math.sin(MainLoop.intersectRads.get(startIntersection - 1));
                prevTime = MainLoop.globalTime;
                Thread.sleep(20);
        }
    }



    public void exitIntersection() {}


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

        try {
            enterIntersection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
