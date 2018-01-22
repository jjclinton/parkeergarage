package Models;

import java.util.LinkedList;
import java.util.Queue;

public class CarQueue extends AbstractModel
{
    private static Queue<Car> queue = new LinkedList<>();

    public boolean addCar(Car car) {
        return queue.add(car);
    }

    public Car removeCar() {
        return queue.poll();
    }

    public  int carsInQueue(){ return queue.size();}

    public void tick() {
        updateViews();
    }

    private void updateViews(){
        // Update the car park Views.
        super.notifyViews();
    }
}