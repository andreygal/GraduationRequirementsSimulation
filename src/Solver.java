
public class Solver {
    public static CarRecord solve(CaseRecord caseRecord) {

            int c = caseRecord.numOfCars;
            int x = caseRecord.simEndTime;
            int n = caseRecord.numOfIntersections;
            CarRecord[] cars = new CarRecord[caseRecord.carQueue.size()];
            caseRecord.carQueue.toArray(cars);
            int myStartTime = -1, myStartIntersection = -1;

            int[][] A = new int[c][4];
            for(int i = 0; i < c; i++) {
                A[i][0] = cars[i].startIntersection - 1;           //start intersection
                A[i][1] = cars[i].endIntersection - 1;             //end intersection
                A[i][2] = cars[i].startTime;                       //start time
                A[i][3] = A[i][2] + ((A[i][1] - A[i][0] + n) % n); //finish time
            }
            int ans = 0;
            for(int t0 = 0; t0 <= x; t0++)
                for(int n0 = 0; n0 < n; n0++) {
                    T1: for(int t1 = t0; t1 <= x; t1++) {
                        boolean must_leave = false;

                        int my_pos = n0 - (t1 - t0);
                        while(my_pos < 0) my_pos += n;
                        for(int i = 0; i < c; i++) {
                            //if the car has not entered or it already left, check next
                            if(t1 < A[i][2] || t1 > A[i][3]) continue;
                            int pos = (A[i][0] + t1 - A[i][2]) % n;

                            //System.out.println(pos + " " + my_pos);

                            if(pos == my_pos) break T1;
                            if(pos != A[i][1] && (pos + 1) % n == my_pos) must_leave = true;
                        }
                        ans = Math.max(ans, t1 - t0);
                        myStartTime = t0;
                        myStartIntersection = n;
                        if(must_leave) break T1;
                    }
                }
            return new CarRecord(myStartIntersection, myStartTime + ans, myStartTime, true);
        }
    }
