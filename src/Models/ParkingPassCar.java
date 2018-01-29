package Models;

import java.util.Random;
import java.awt.*;

public class ParkingPassCar extends Car {
    private static final Color COLOR=Color.blue;

    /**
     * Constructor of parkingPassCar
     */
    public ParkingPassCar() {
        Random random = new Random();
        int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
    }

    /**
     * Get the color of the car.
     *
     * @return color Color of car
     */
    public Color getColor(){
        return COLOR;
    }
}
