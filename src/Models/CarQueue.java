package Models;

import java.util.LinkedList;
import java.util.Queue;

public class CarQueue extends AbstractModel
{
    private static Queue<Car> queue = new LinkedList<>();

    public void addCar(Car car) {
        queue.add(car);
    }

    public Car removeCar() {
        return queue.poll();
    }

    public int carsInQueue(){ System.out.println(queue.size()); return queue.size();}

    public void tick() {
        updateViews();
    }

    private void updateViews(){
        // Update the car park Views.
        super.notifyViews();
    }
}