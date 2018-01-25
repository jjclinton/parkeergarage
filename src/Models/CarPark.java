package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CarPark extends AbstractModel{
    private static final Double COST = 2.80;
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

    private static HashMap<Location, Boolean> carsReserved;

    private static Double profitTotal;
    private static Double profitToday;
    private int dayOfYear = 0;
    private static int day = 0;
    private static int hour = 0;
    private int minute = 0;
    private static String[] days;

    private Boolean state;


    // hashmap with all the locations for the cars
    private static HashMap<Location, Car> cars;

    public CarPark(int numberOfFloors, int numberOfRows, int numberOfPlaces, int reservedForPassHolders) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;

        this.entranceCarQueue = new CarQueue();
        this.entrancePassQueue = new CarQueue();
        this.paymentCarQueue = new CarQueue();
        this.exitCarQueue = new CarQueue();

        this.profitToday = 0.0;
        this.profitTotal = 0.0;

        cars = new HashMap<>();
        carsReserved = new HashMap<>();

        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    numberOfOpenSpots++;

                    if(numberOfOpenSpots < reservedForPassHolders){
                        carsReserved.put(location, true);
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

    public CarQueue getEntrancePaymentQueue() {
        return paymentCarQueue;
    }

    public CarQueue getEntranceExitQueue() {
        return exitCarQueue;
    }

    public CarQueue getEntranceCarQueue() {
        return entranceCarQueue;
    }

    public CarQueue getEntrancePassQueue() {
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
        
        carsReadyToLeave();
        carsPaying();
        carsLeaving();

        carsArriving();
        carsEntering(entrancePassQueue);
        carsEntering(entranceCarQueue);

        // Pause.
        int tickPause = 4;
        try {
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

            profitTotal = profitTotal + profitToday;
            profitToday = 0.0;
        }
        if (day > 6) {
            day -= 7;
        }
        if (dayOfYear > 364) {
            dayOfYear = 0;
        }
    }

    private void updateViews(){
        // Update the car park Views.
        super.notifyViews();
    }

    private void carsArriving(){
        int numberOfCars;
        int numberOfPassCars;
        //check weather it's a holiday/festival or not
        switch (dayOfYear) {
            case 339:
                numberOfCars = getNumberOfCars(300);
                numberOfPassCars = getNumberOfCars(100);
                //number of cars arriving on a weekday
                break;
            default:
                if (day < 5) {
                    numberOfCars = getNumberOfCars(100);
                    numberOfPassCars = getNumberOfCars(50);
                    //number of cars arriving on a weekday
                } else if (day == 5) {
                    numberOfCars = getNumberOfCars(200);
                    numberOfPassCars = getNumberOfCars(20);
                    //number of cars arriving on a saturday
                } else {
                    numberOfCars = getNumberOfCars(40);
                    numberOfPassCars = getNumberOfCars(5);
                    //number of cars arriving on a sunday
                }
                break;
        }
        addArrivingCars(numberOfCars, AD_HOC);
        addArrivingCars(numberOfPassCars, PASS);
    }

    private void carsEntering(CarQueue queue){
        int enterSpeed = 4; // number of cars that can enter per minute
        // Remove car from the front of the queue and assign to a parking space.
        for (int i = 0; i < enterSpeed; i++) {
            Car car = queue.removeCar();

            if(car == null){
                break;
            }

            Location freeLocation = this.getFirstFreeLocation(car);
            this.setCarAt(freeLocation, car);
        }
    }


    private void carsReadyToLeave(){
        // Add leaving cars to the payment queue.
        Car car = this.getFirstLeavingCar();

        while (car != null) {
            if (car.getHasToPay()){
                car.setIsPaying(true);
                paymentCarQueue.addCar(car);
            } else {
                carLeavesSpot(car);
            }
            car = this.getFirstLeavingCar();
        }
    }

    private void carsPaying(){
        // Let cars pay.
        int paymentSpeed = 7; // number of cars that can pay per minute

        for (int i = 0; i < paymentSpeed; i++) {
            Car car = paymentCarQueue.removeCar();

            if (car == null) {
                break;
            }

            if(car.getHasToPay()){
                profitToday = profitToday + (COST * (car.getMinutesParked() /60));
            }

            carLeavesSpot(car);
        }
    }

    private void carsLeaving(){
        // Let cars leave.
        int exitSpeed = 6; // number of cars that can leave per minute

        for (int i = 0; i < exitSpeed; i++) {
            Car car = exitCarQueue.removeCar();

            if (car == null) {
                break;
            }
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


    private Location getFirstFreeLocation(Car forCar) {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Boolean reservedLocation = carsReserved.get(location);
                    Car car = cars.get(location);

                    if (car == null) {
                        if(reservedLocation == null){
                            return location;
                        }else{
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

    public int getCurrentHour(){ return hour; }

    public static Boolean isLocationReserved(Location location){
        return carsReserved.get(location) != null;
    }

    public int getTotalCars() {
        int total = 0;
        for(Location location : cars.keySet()) {
            if (cars.get(location) != null) {
                total++;
            }
        }

        return total;
    }

    public Double getTodayProfit(){
        return profitToday;
    }

    public int getCurrentDayOfYear(){
       return dayOfYear;
    }

    public int getCurrentIntDay(){
        return day;
    }

    public int getMinute(){
        return minute;
    }
}