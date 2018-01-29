package Models;

import java.util.LinkedList;
import java.util.Queue;

public class CarQueue extends AbstractModel
{
    private static Queue<Car> queue = new LinkedList<>();

    /**
     * Add a car to the queue
     *
     * @param car Car object
     */
    public void addCar(Car car) {
        queue.add(car);
    }

    /**
     * Remove a car from the queue
     *
     * @return car Car object
     */
    public Car removeCar() {
        return queue.poll();
    }

    /**
     * Get the size of the queue
     *
     * @return size of queue
     */
    public int carsInQueue(){ return queue.size();}
}