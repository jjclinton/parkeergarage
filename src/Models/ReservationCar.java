package Models;

import java.awt.*;
import java.util.Random;

public class ReservationCar extends Car {
    private Color COLOR;

    public ReservationCar() {
        Random random = new Random();
        int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
        COLOR = Color.pink;
    }

    public void setColor(Color color) {COLOR = color;}

    public Color getColor(){
        return COLOR;
    }
}
