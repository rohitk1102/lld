import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
 
 
public class Lot {
    PriorityQueue<ParkingSpace> handicapParkingSpaceQueue;
    PriorityQueue<ParkingSpace> normalParkingSpaceQueue;
    HashMap<Integer, ParkingSpace> occupiedSpace = new HashMap<Integer, ParkingSpace>();
     
    public Lot(int noHandicapParking, int totalNoParking) {
        int num = 0;
        handicapParkingSpaceQueue = new PriorityQueue<ParkingSpace>(noHandicapParking, new ParkingComparator());
        normalParkingSpaceQueue = new PriorityQueue<ParkingSpace>(totalNoParking - noHandicapParking, new ParkingComparator());
        for (int i = 0; i < noHandicapParking; i++) {
            handicapParkingSpaceQueue.add(new HandicapParkingSpace(num++));
        }
        for (int i = noHandicapParking; i < totalNoParking; i++) {
            normalParkingSpaceQueue.add(new ParkingSpace(num++));
        }
    }
     
     
    //take the first available space and park
    public boolean park(Car c) {
        if (c.isHandicap()) { //get the first handicap space if possible
            if (!handicapParkingSpaceQueue.isEmpty()) {
                ParkingSpace takenSpace = handicapParkingSpaceQueue.remove();
                occupiedSpace.put(c.getNum(), takenSpace);
                takenSpace.take();
                return true;
            }
        }
        if (!normalParkingSpaceQueue.isEmpty()) {
            ParkingSpace takenSpace = normalParkingSpaceQueue.remove();
            occupiedSpace.put(c.getNum(), takenSpace);
            takenSpace.take();
            return true;
        }
        return false;
    }
     
    //valte parking get the car for the customer
    public long unpark(Car c) {
        if (!occupiedSpace.containsKey(c.getNum())) {
            return -1; //no such car in this lot
        }
        ParkingSpace freeSpace = occupiedSpace.remove(c.getNum());
        if (freeSpace.type == ParkingSpace.TYPE.HANDICAP) {
            handicapParkingSpaceQueue.add(freeSpace);
        }
        else {
            normalParkingSpaceQueue.add(freeSpace);
        }
        return freeSpace.leave();
    }
     
    public boolean isNormalFull(){
        return normalParkingSpaceQueue.isEmpty();
    }
     
    public boolean isHandicapFull() {
        return handicapParkingSpaceQueue.isEmpty();
    }
     
    //comparator to sort the parking space in num
    class ParkingComparator implements Comparator<ParkingSpace> {
 
        @Override
        public int compare(ParkingSpace arg0, ParkingSpace arg1) {
            if (arg0.num < arg1.num)
                return -1;
            else if (arg0.num > arg1.num){
                return 1;
            }
            return 0;
        }
         
    }
    public static void main(String [] args) {
        Car c1 = new Car(1111, true); //handicap
        Car c2 = new Car(2222, false); // normal
        Car c3 = new Car(3333, false); // normal
        Car c4 = new Car(4444, false); // normal
        Lot lot1 = new Lot(2, 3);
        lot1.park(c1);
        lot1.park(c2);
        lot1.park(c3);
        lot1.unpark(c1);
        lot1.park(c4);
        lot1.unpark(c2);
        lot1.park(c3);
    }
}
