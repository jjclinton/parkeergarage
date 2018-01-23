package Models;

import java.util.HashMap;
import java.util.Random;

public class CarPark extends AbstractModel{
    private static final String AD_HOC = "1";
    private static final String PASS = "2";

    private static int numberOfFloors;
    private static int numberOfRows;
    private static int numberOfPlaces;
    private static int numberOfOpenSpots;

    private static CarQueue entranceCarQueue;
    private static CarQueue entrancePassQueue;
    private static CarQueue paymentCarQueue;
    private static CarQueue exitCarQueue;

    public static int steps = 100;

    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    // hashmap with all the locations for the cars
    private static HashMap<Location, Car> cars;

    public CarPark(int numberOfFloors, int numberOfRows, int numberOfPlaces) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;
        this.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;

        this.entranceCarQueue = new CarQueue();
        this.entrancePassQueue = new CarQueue();
        this.paymentCarQueue = new CarQueue();
        this.exitCarQueue = new CarQueue();

        cars = new HashMap<>();
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    cars.put(location,null);
                }
            }
        }
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
        // Pause.
        int tickPause = steps;
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
        while (minute > 59) {
            minute -= 60;
            hour++;
        }
        while (hour > 23) {
            hour -= 24;
            day++;
        }
        while (day > 6) {
            day -= 7;
        }

    }

    private void handleEntrance(){
        carsArriving();
        carsEntering(entrancePassQueue);
        carsEntering(entranceCarQueue);
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
        int weekDayArrivals= 100; // average number of arriving cars per hour
        int weekendArrivals = 200; // average number of arriving cars per hour
        int weekDayPassArrivals= 50; // average number of arriving cars per hour
        int weekendPassArrivals = 5; // average number of arriving cars per hour


        int numberOfCars = getNumberOfCars(weekDayArrivals, weekendArrivals);
        addArrivingCars(numberOfCars, AD_HOC);
        numberOfCars = getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, PASS);
    }

    private void carsEntering(CarQueue queue){
        int i=0;
        int enterSpeed = 3; // number of cars that can enter per minute
        // Remove car from the front of the queue and assign to a parking space.
        while (queue.carsInQueue()>0 && this.getNumberOfOpenSpots() > 0 && i < enterSpeed) {
            Car car = queue.removeCar();
            Location freeLocation = this.getFirstFreeLocation();
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
        int exitSpeed = 5; // number of cars that can leave per minute
        while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            exitCarQueue.removeCar();
            i++;
        }
    }

    private int getNumberOfCars(int weekDay, int weekend){
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = day < 5
                ? weekDay
                : weekend;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);
    }

    private void addArrivingCars(int numberOfCars, String type){
        // Add the cars to the back of the queue.
        switch(type) {
            case AD_HOC:
                for (int i = 0; i < numberOfCars; i++) {
                    entranceCarQueue.addCar(new AdHocCar());
                }
                break;
            case PASS:
                for (int i = 0; i < numberOfCars; i++) {
                    entrancePassQueue.addCar(new ParkingPassCar());
                }
                break;
        }
        updateViews();
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
        if (oldCar == null) {
            cars.put(location, car);
            car.setLocation(location);
            numberOfOpenSpots--;
        }
    }


    private Location getFirstFreeLocation() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = cars.get(location);

                    if (car == null) {
                        return location;
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
}