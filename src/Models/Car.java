package Models;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public abstract class Car extends AbstractModel
{

    private Location location;
    private int minutesLeft;
    private int minutesParked;
    private boolean isPaying;
    private boolean hasToPay;
    private int reservationTime;


    /**
     * Constructor for objects of class Car
     */
    public Car() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public void setMinutesLeft(int minutesLeft) {
        this.minutesParked = minutesLeft;
        this.minutesLeft = minutesLeft;
    }

    public int getMinutesParked() {
        return minutesParked;
    }

    public boolean getIsPaying() {
        return isPaying;
    }

    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    public boolean getHasToPay() {
        return hasToPay;
    }

    public void setHasToPay(boolean hasToPay) {
        this.hasToPay = hasToPay;
    }

    public void setReservationTime(int day, int hour, int minute) {
        String time = "" + day + hour + minute;
        reservationTime = Integer.parseInt(time);
    }

    public int getReservationTime() {
        return reservationTime;
    }

    public void tick() {
        minutesLeft--;
    }

    public abstract Color getColor();

}