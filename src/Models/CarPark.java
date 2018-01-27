package Models;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CarPark extends AbstractModel{
    private static final String AD_HOC = "1";
    private static final String PASS = "2";
    private static final String RES = "3";

    private static int numberOfFloors;
    private static int numberOfRows;
    private static int numberOfPlaces;
    private static int numberOfOpenSpots;

    private static CarQueue entranceCarQueue;
    private static CarQueue entrancePassQueue;
    private static CarQueue entranceResArrQueue;
    private static CarQueue paymentCarQueue;
    private static CarQueue exitCarQueue;

    private static HashMap<Location, Boolean> passReserved;
    private static HashMap<Location, Boolean> reserved;
    private static HashMap<Integer, Car> carsReserved;


    private int dayOfYear = 0;
    private static int day = 0;
    private int hour = 0;
    private int minute = 0;
    private static String[] days;

    // hashmap with all the locations for the cars
    private static HashMap<Location, Car> cars;

    public CarPark(int numberOfFloors, int numberOfRows, int numberOfPlaces, int reservedForPassHolders) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;

        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        entranceResArrQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();

        cars = new HashMap<>();
        passReserved = new HashMap<>();
        reserved = new HashMap<>();
        carsReserved = new HashMap<>();

        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    numberOfOpenSpots++;

                    if(numberOfOpenSpots < reservedForPassHolders){
                        passReserved.put(location, true);
                    }

                    cars.put(location,null);
                }
            }
        }

        days = new String[] {"Monday", "Thuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", ""};
    }

    /**
     * Get number of floors.
     *
     * @return int number of floors in the car park.
     */
    public static int getNumberOfFloors() {
        return numberOfFloors;
    }

    /**
     * Get number of rows.
     *
     * @return int number of rows in the car park.
     */
    public static int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Get number of places.
     *
     * @return int number of places in the car park.
     */
    public static int getNumberOfPlaces() {
        return numberOfPlaces;
    }


    /**
     * Get a car from a certain location in the car park.
     *
     * @param location Location object where to get the car from.
     * @return car object that is located at the given location or null if there is no car at the location.
     */
    public static Car getCar(Location location) {
        if (!checkLocation(location)) {
            return null;
        }
        return cars.get(location);
    }

    private int getNumberOfOpenSpots(){
        return this.numberOfOpenSpots;
    }

    public static CarQueue getEntrancePaymentQueue() {
        return paymentCarQueue;
    }

    public static CarQueue getEntranceExitQueue() {
        return exitCarQueue;
    }

    public static CarQueue getEntranceCarQueue() {
        return entranceCarQueue;
    }

    public static CarQueue getEntrancePassQueue() {
        return entrancePassQueue;
    }
    /**
     * Check if a location is valid in the car park.
     *
     * @param location Location object to check.
     * @return boolean whether location is valid.
     */
    private static boolean checkLocation(Location location) {
        if(location == null)
            return false;

        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();

        return !(floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces);
    }

    public void tick() {
        advanceTime();
        forward();
        handleExit();
        updateViews();
        checkReservationCar();
        // Pause.
        int tickPause = 4;
        try {
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handleEntrance();
    }

    private void forward(){
        for (Location key : cars.keySet()) {
            Car car = cars.get(key);
            if(car != null){
                car.tick();
            }

        }
    }

    private void advanceTime(){
        // Advance the time by one minute.
        minute++;
        if (minute > 59) {
            minute -= 60;
            hour++;
        }
        if (hour > 23) {
            hour -= 24;
            day++;
            dayOfYear++;
        }
        if (day > 6) {
            day -= 7;
        }
        if (dayOfYear > 364) {
            dayOfYear = 0;
        }

    }

    private void handleEntrance(){
        carsArriving();
        carsEntering(entrancePassQueue);
        carsEntering(entranceCarQueue);
        carsEntering(entranceResArrQueue);
    }

    private void handleExit(){
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }

    private void updateViews(){
        // Update the car park Views.
        super.notifyViews();
    }

    private void carsArriving(){
        int numberOfCars;
        int numberOfPassCars;
        int numberOfResCars;
        //check weather it's a holiday/festival or not
        switch (dayOfYear) {
            case 339:
                numberOfCars = getNumberOfCars(300);
                numberOfPassCars = getNumberOfCars(100);
                numberOfResCars = getNumberOfCars(20);
                //number of cars arriving on a weekday
                break;
            default:
                if (day < 5) {
                    numberOfCars = getNumberOfCars(80);
                    numberOfPassCars = getNumberOfCars(50);
                    numberOfResCars = getNumberOfCars(15);
                    //number of cars arriving on a weekday
                } else if (day == 5) {
                    numberOfCars = getNumberOfCars(130);
                    numberOfPassCars = getNumberOfCars(40);
                    numberOfResCars = getNumberOfCars(20);
                    //number of cars arriving on a saturday
                } else {
                    numberOfCars = getNumberOfCars(40);
                    numberOfPassCars = getNumberOfCars(5);
                    numberOfResCars = getNumberOfCars(1);
                    //number of cars arriving on a sunday
                }
                break;
        }
        addArrivingCars(numberOfCars, AD_HOC);
        addArrivingCars(numberOfPassCars, PASS);
        addArrivingCars(numberOfResCars, RES);
    }

    private void carsEntering(CarQueue queue){
        int i=0;
        int enterSpeed = 6; // number of cars that can enter per minute
        // Remove car from the front of the queue and assign to a parking space.
        while (queue.carsInQueue()>0 && this.getNumberOfOpenSpots() > 0 && i < enterSpeed) {
            Car car = queue.removeCar();
            Location freeLocation;
            if (car instanceof ReservationCar) {
                freeLocation = car.getLocation();
            } else {
                freeLocation = this.getFirstFreeLocation(car);
            }
            this.setCarAt(freeLocation, car);
            i++;
        }
    }


    private void carsReadyToLeave(){
        // Add leaving cars to the payment queue.
        Car car = this.getFirstLeavingCar();
        while (car!=null) {
            if (car.getHasToPay()){
                car.setIsPaying(true);
                paymentCarQueue.addCar(car);
            }
            else {
                carLeavesSpot(car);
            }
            car = this.getFirstLeavingCar();
        }
    }

    private void carsPaying(){
        // Let cars pay.
        int i=0;
        int paymentSpeed = 7; // number of cars that can pay per minute
        while (paymentCarQueue.carsInQueue()>0 && i < paymentSpeed){
            Car car = paymentCarQueue.removeCar();
            // TODO Handle payment.
            carLeavesSpot(car);
            i++;
        }
    }

    private void carsLeaving(){
        // Let cars leave.
        int i=0;
        int exitSpeed = 11; // number of cars that can leave per minute
        while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            exitCarQueue.removeCar();
            i++;
        }
    }

    private int getNumberOfCars(int AvgArrivalsPH){
        Random random = new Random();


        // Calculate the number of cars that arrive this minute.
        double standardDeviation = AvgArrivalsPH * 0.3;
        double numberOfCarsPerHour = AvgArrivalsPH + random.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);
    }

    private void addArrivingCars(int numberOfCars, String type){
        // Add the cars to the back of the queue.
        switch(type) {
            case AD_HOC:
                for (int i = 0; i < numberOfCars; i++) {
                    if (entranceCarQueue.carsInQueue() < 3) {
                        entranceCarQueue.addCar(new AdHocCar());
                    }
                }
                break;
            case PASS:
                for (int i = 0; i < numberOfCars; i++) {
                    if (entrancePassQueue.carsInQueue() < 3) {
                        entrancePassQueue.addCar(new ParkingPassCar());
                    }
                }
                break;
            case RES:
                for (int i = 0; i < numberOfCars; i++) {
                    if (this.getNumberOfOpenSpots()>0) {
                        Car car = new AdHocCar();
                        int resDay = new Random().nextInt(6);
                        int resHour = new Random().nextInt(23);
                        int resMin = new Random().nextInt(59);
                        Location location = getFirstFreeLocation(car);
                        reserved.put(location, true);
                        ReservationCar resArr = new ReservationCar();
                        resArr.setReservationTime(resDay, resHour, resMin);
                        resArr.setLocation(location);
                        carsReserved.put(resArr.getReservationTime(), resArr);
                    }
                }
                break;
        }
        updateViews();
    }

    private void checkReservationCar() {
        String time = "" + day + hour + minute;
        if (carsReserved.get(Integer.parseInt(time)) != null) {
            entranceResArrQueue.addCar(carsReserved.remove(Integer.parseInt(time)));
        }
    }
    private Car removeCarAt(Location location) {
        if (!checkLocation(location)) {
            return null;
        }

        Car car = cars.get(location);

        if(car != null){
            car.setLocation(null);
        }

        cars.put(location, null);
        numberOfOpenSpots++;

        return car;
    }

    private void setCarAt(Location location, Car car) {
        Car oldCar = cars.get(location);
        if (oldCar == null && !(car instanceof  ReservationCar)) {
            cars.put(location, car);
            car.setLocation(location);
            numberOfOpenSpots--;
        }
        if (car instanceof ReservationCar) {
            System.out.println("reserved");
            cars.put(location, car);
            reserved.remove(location);
            numberOfOpenSpots--;
        }
    }


    private Location getFirstFreeLocation(Car forCar) {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Boolean passLocation = passReserved.get(location);
                    Car car = cars.get(location);

                    if (car == null) {
                        if(passLocation == null){
                            if (reserved.get(location) == null) {
                                return location;
                            } else {
                                if (forCar instanceof  ReservationCar) {
                                    return location;
                                }
                            }
                        }
                        else {
                            if(forCar instanceof ParkingPassCar){
                                return location;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Car getFirstLeavingCar() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = cars.get(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                        return car;
                    }
                }
            }
        }
        return null;
    }

    private void carLeavesSpot(Car car){
        this.removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }

    public static String getCurrentDay() {
        return days[day];
    }

    public static Boolean isLocationPassReserved(Location location){
        return passReserved.get(location) != null;
    }
    public static Boolean isLocationReserved(Location location){
        return reserved.get(location) != null;
    }
}