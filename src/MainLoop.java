import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class MainLoop extends AnimationTimer {
    //Logic
    //current active car list
    ArrayList<Car> cars;
    ArrayList<Double> intersectDegree;
    static ArrayList<Double> intersectRads;
    //Graphics
    Image carImage;
    GraphicsContext gc;

    final static double ISLAND_WIDTH  = 40;
    final static double ISLAND_HEIGHT = 40;
    //check the carImage dimension for setting the proper offset
    final static double laneWidth = 28;
    final static double enterInterTimeOffset = 3.0;

    //we are starting at a negative time to allow the cars to move to their starting positions
    static long   prevTime = 0;
    static double globalTime = - 5.0;
    static double globTimeLimit;
    //the outer bound of the traffic circle
    static double outerBound;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    private CaseRecord currentCase;
    private int currCaseIndex = 0;
    private int numOfIntersections;
    private int numOfCars;
    private int carCounter = 0;

    public MainLoop() {
        carImage = new Image(getClass().getResourceAsStream("car.png"));
        cars = new ArrayList<>();
        intersectDegree = new ArrayList<>();
        intersectRads = new ArrayList<>();
        gc = Main.canvas.getGraphicsContext2D();
    }

    @Override
    public void start() {
        prevTime = System.nanoTime();

         //get the next case and initialize case parameters
        currentCase = Main.cases.get(currCaseIndex);
        currCaseIndex++;
        //it starts N seconds earlier and should finish N seconds later to allow time to enter and leave
        globTimeLimit = (double) currentCase.simEndTime + enterInterTimeOffset + 1;
        numOfIntersections = currentCase.numOfIntersections;
        numOfCars = currentCase.numOfCars;

        //calculate the radial limit of the traffic circle
        outerBound = (numOfCars + 1) * laneWidth + ISLAND_WIDTH / 2;

        //initialize degree offset arrays that are used for calculating starting positions
        for (int i = 0; i < numOfIntersections; i++) {
            intersectDegree.add(90.0 + (360.0 / numOfIntersections) * i);
            intersectRads.add(-Math.toRadians(intersectDegree.get(i)));
        }
        //start individual car threads
        for (Car car : cars)
            car.startCar();
        
        //System.out.println("Center is at " + Main.canvasCenterX + " " + Main.canvasCenterY);
        //System.out.println("Size of priority queue " + currentCase.carQueue.size());
        //test(); //test the input data
        super.start();
    }

    @Override
    public void handle(long currentNanoTime) {
        System.out.println(prevTime + " GlobalTime: " + globalTime);
        //peek at the queue and see if the next car is read to enter the traffic circle
        if ((currentCase.carQueue.peek() != null) &&
                (globalTime + enterInterTimeOffset >= currentCase.carQueue.peek().startTime)) {
            CarRecord cr = currentCase.carQueue.poll();
            cars.add(new Car(carImage, carCounter,  cr.startIntersection, cr.endIntersection, numOfIntersections));
            cars.get(carCounter).startCar();
            carCounter++;
        }

        //Dynamically draw the background
        //Draw the grass. Add a grass texture fill in the production version.
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, Main.canvas.getWidth(), Main.canvas.getHeight());

        //Draw the asphalt
        gc.setFill(Color.BLACK);
        gc.fillOval(Main.canvasCenterX - outerBound, Main.canvasCenterY - outerBound,
                outerBound * 2, outerBound * 2);

        //Draw the intersections
        gc.setFill(Color.BLACK);
        for (int i = 1; i <= numOfIntersections; i++) {
            gc.save();
            Rotate rectRotate = new Rotate(-intersectDegree.get(i - 1) + 90, Main.canvasCenterX, Main.canvasCenterY);
            gc.setTransform(rectRotate.getMxx(),
                    rectRotate.getMyx(),
                    rectRotate.getMxy(),
                    rectRotate.getMyy(),
                    rectRotate.getTx(),
                    rectRotate.getTy());
            gc.fillRect(Main.canvasCenterX - laneWidth / 2.0,
                        Main.canvasCenterY - outerBound - 40,
                             laneWidth,
                             outerBound + 40);
            gc.strokeText(String.valueOf(i), Main.canvasCenterX, Main.canvasCenterY - outerBound - 50);
            gc.restore();
        }

        //draw the traffic circle island
        gc.setFill(Color.GRAY);
        gc.fillOval(Main.canvasCenterX - ISLAND_WIDTH/2,
                Main.canvasCenterY - ISLAND_HEIGHT/2, ISLAND_WIDTH, ISLAND_HEIGHT);

        //Draw the lanes
        double trackRadiusOfCurv;
        gc.setStroke(Color.WHITE);
        gc.setLineDashes(12f);
        for (int i = 1; i <= numOfCars; i++) {
            trackRadiusOfCurv = (ISLAND_WIDTH / 2.0) + laneWidth * i;
            System.out.println("Drawing lanes.");
            gc.strokeOval(Main.canvasCenterX - trackRadiusOfCurv,
                    Main.canvasCenterY - trackRadiusOfCurv,
                    trackRadiusOfCurv * 2, trackRadiusOfCurv * 2);
        }

        //draw the cars
        for (Car car : cars) {
            if (car.isMoving())
                 car.render(gc);
        }



        globalTime += ((currentNanoTime - prevTime) / 1000000000.0);
        prevTime = currentNanoTime;
        //if (globalTime > 0)
        pcs.firePropertyChange("globalTime", prevTime, globalTime);
        if (globalTime >= globTimeLimit) {
            System.out.println("Time limit reached. Stopping simulation");
            this.stop();
        }
    }

    @Override
    public void stop() {
        for (Car car : cars) {
            car.stopCar();
        }
        intersectDegree.clear();
        intersectRads.clear();
        carCounter = 0;
        cars.clear();
        globalTime = -5.0;
        prevTime = 0;
        pcs.firePropertyChange("globalTime", prevTime, globalTime);
        super.stop();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void test() {
        System.out.println("Testing data...");
        for (CaseRecord caseRec : Main.cases) {
            System.out.println("Case num " + caseRec.caseNum +
                    " Num of cars " + caseRec.numOfCars +
                    " Num of inter " + caseRec.numOfIntersections +
                    " Sim End Time " + caseRec.simEndTime);
            System.out.println("Queue size " +  caseRec.carQueue.size());
            CarRecord cr;
            int size = caseRec.carQueue.size();
            for (int j = 0; j < size; j++) {
                cr = caseRec.carQueue.poll();
                System.out.println("s " + cr.startIntersection +
                        " e " + cr.endIntersection +
                        " t " + cr.startTime);
            }
        }
    }

    public int getNumCar() {
        return currentCase.carQueue.size();
    }

    public int getCurrCaseIndex() {
       return currCaseIndex;
    }

    public int getAlotTime() {
        return currentCase.simEndTime;
    }
}
