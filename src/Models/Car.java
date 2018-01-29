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
    public Car() {}

    /**
     * Get location of the car
     *
     * @return location Location object
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set the location of the car
     *
     * @param location Location object
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Get minutes left of the car
     *
     * @return minutes left
     */
    public int getMinutesLeft() {
        return minutesLeft;
    }

    /**
     * Set the minutes left of the car
     * @param minutesLeft minutes
     */
    public void setMinutesLeft(int minutesLeft) {
        this.minutesParked = minutesLeft;
        this.minutesLeft = minutesLeft;
    }

    /**
     * Get parked minutes
     *
     * @return minutes parked
     */
    public int getMinutesParked() {
        return minutesParked;
    }

    /**
     * Get if currently paying
     *
     * @return is paying
     */
    public boolean getIsPaying() {
        return isPaying;
    }

    /**
     * Check if car is paying
     *
     * @param isPaying is paying
     */
    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    /**
     * Check if car has to pay
     *
     * @return
     */
    public boolean getHasToPay() {
        return hasToPay;
    }

    /**
     * Set car to paying
     * @param hasToPay
     */
    public void setHasToPay(boolean hasToPay) {
        this.hasToPay = hasToPay;
    }

    /**
     * Set reservation time
     *
     * @param day current day
     * @param hour current hour
     * @param minute current minute
     */
    public void setReservationTime(int day, int hour, int minute) {
        String time = "" + day + hour + minute;
        reservationTime = Integer.parseInt(time);
    }

    /**
     * Get current reservation time
     *
     * @return time
     */
    public int getReservationTime() {
        return reservationTime;
    }

    /**
     * Tick car
     */
    public void tick() {
        minutesLeft--;
    }

    /**
     * Get the color
     *
     * @return color
     */
    public abstract Color getColor();

}