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
    final static double dashedMarkOffset = 28;

    //public double canvasCenterX = Main.canvas.getWidth() / 2.0;
    //public double canvasCenterY = Main.canvas.getHeight() / 2.0;

    static long   prevTime = 0;
    static double globalTime = 0.0;
    static int    globTimeLimit;
    
    static double outerBound;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    CaseRecord currentCase;
    int currCaseIndex = 0;
    int numOfIntersections;
    int numOfCars;
    double enterInterTimeOffset = 3.0;

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
        globTimeLimit = currentCase.simEndTime;
        numOfIntersections = currentCase.numOfIntersections;
        numOfCars = currentCase.numOfCars;
        
        //calculate the radial limit of the traffic circle 
        outerBound = (numOfCars + 1) * dashedMarkOffset + ISLAND_WIDTH / 2;
        
        //initialize degree offset arrays
        for (int i = 0; i < numOfIntersections; i++) {
            intersectDegree.add(90.0 + (360.0 / numOfIntersections) * i);
            intersectRads.add(-Math.toRadians(intersectDegree.get(i)));
        }
       
        /*test car
        cars.add(new Car (carImage, canvasCenterX - carImage.getWidth() / 2.0,
                                    canvasCenterY - carImage.getHeight() / 2.0,
                ISLAND_WIDTH / 2.0 + (dashedMarkOffset / 2.0) * 3 , 1));
        for (Car car : cars)
            car.startCar();*/
        
        System.out.println("Center is at " + Main.canvasCenterX + " " + Main.canvasCenterY);
        test();
        super.start();
    }

    @Override
    public void handle(long currentNanoTime) {
        System.out.println(prevTime + " GlobalTime: " + globalTime);
        //peek at the queue and see if the next car is read to enter the traffic circle
        if (currentCase.carQueue.peek().startTime >= Math.floor(globalTime - enterInterTimeOffset)) {
            CarRecord cr = currentCase.carQueue.poll();
            cars.add(new Car(carImage, 1, cr.startIntersection, cr.endIntersection, numOfIntersections));
        }

        //Dynamically draw the background
        //Draw the grass
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
            gc.fillRect(Main.canvasCenterX - dashedMarkOffset / 2.0,
                        Main.canvasCenterY - outerBound - 40,
                             dashedMarkOffset,
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
            trackRadiusOfCurv = (ISLAND_WIDTH / 2.0) + dashedMarkOffset * i;
            System.out.println("Drawing lanes.");
            gc.strokeOval(Main.canvasCenterX - trackRadiusOfCurv,
                    Main.canvasCenterY - trackRadiusOfCurv,
                    trackRadiusOfCurv * 2, trackRadiusOfCurv * 2);
        }

        //draw the cars
        for (Car car : cars) {
            car.render(gc);
        }



        globalTime += ((currentNanoTime - prevTime) / 1000000000.0);
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
            //found the bug sereg. i'm an idiot. queue size decreases as elements are removed. first thought the initial size would be somehow stored
            int size = caseRec.carQueue.size();
            for (int j = 0; j < size; j++) {
                System.out.println(caseRec.carQueue.size());
                cr = caseRec.carQueue.poll();
                System.out.println("s " + cr.startIntersection +
                        " e " + cr.endIntersection +
                        " t " + cr.startTime);
            }
        }
    }


}
