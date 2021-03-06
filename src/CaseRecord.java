import java.util.PriorityQueue;

public class CaseRecord {
    public int caseNum;
    public int numOfCars;
    public int numOfIntersections;
    public int simEndTime;
    public PriorityQueue<CarRecord> carQueue;

    CaseRecord(int caseNum, int numOfCars, int numOfIntersections, int simEndTime) {
        this.caseNum = caseNum;
        this.numOfCars = numOfCars; //C
        this.numOfIntersections = numOfIntersections; //N
        this.simEndTime = simEndTime; //X
        carQueue = new PriorityQueue<>(new CompByStartTime());
    }
}
