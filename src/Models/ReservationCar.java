package Models;

import java.awt.*;
import java.util.Random;

public class ReservationCar extends Car {
    private final Color COLOR = Color.green;

    /**
     * Constructor of ReservationCar
     */
    public ReservationCar() {
        Random random = new Random();
        int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
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
