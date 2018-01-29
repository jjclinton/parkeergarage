package Models;


import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

public class CarPark extends AbstractModel{
    private static final Double COST = 2.80;
    private static final String AD_HOC = "1";
    private static final String PASS = "2";
    private static final String RES = "3";

    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;

    private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue entranceResArrQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;

    private HashMap<Location, Boolean> passReserved;
    private HashMap<Location, Boolean> reserved;
    private HashMap<Integer, Car> carsReserved;

    private Double profitTotal;
    private Double profitToday;
    private int dayOfYear = 0;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private String[] days;

    private Weather weather;


    // hashmap with all the locations for the cars
    private static HashMap<Location, Car> cars;


    /**
     * Constructor for model
     *
     * @param numberOfFloors Number of floors
     * @param numberOfRows Number of rows
     * @param numberOfPlaces Number of places
     * @param reservedForPassHolders Amount of places reserved for passholders
     */
    public CarPark(int numberOfFloors, int numberOfRows, int numberOfPlaces, int reservedForPassHolders) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;

        this.weather = new Weather();

        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        entranceResArrQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();

        this.profitToday = 0.0;
        this.profitTotal = 0.0;

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

        days = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", ""};
    }

    /**
     * Get number of floors.
     *
     * @return int number of floors in the car park.
     */
    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    /**
     * Get number of rows.
     *
     * @return int number of rows in the car park.
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Get number of places.
     *
     * @return int number of places in the car park.
     */
    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }


    /**
     * Get a car from a certain location in the car park.
     *
     * @param location Location object where to get the car from.
     * @return car object that is located at the given location or null if there is no car at the location.
     */
    public Car getCar(Location location) {
        if (!checkLocation(location)) {
            return null;
        }
        return cars.get(location);
    }

    /**
     * Check if a location is valid in the car park.
     *
     * @param location Location object to check.
     * @return boolean whether location is valid.
     */
    private boolean checkLocation(Location location) {
        if(location == null)
            return false;

        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();

        return !(floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces);
    }

    /**
     * Advance the carpark one tick
     */
    public void tick() {
        advanceTime();
        forward();
        updateViews();
        checkReservationCar();
        
        carsReadyToLeave();
        carsPaying();
        carsLeaving();

        carsArriving();
        carsEntering(entrancePassQueue);
        carsEntering(entranceCarQueue);
        // Pause.
        int tickPause = 1;
        try {
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tick all cars
     */
    private void forward(){
        for (Location key : cars.keySet()) {
            Car car = cars.get(key);
            if(car != null){
                car.tick();
            }

        }
    }

    /**
     * Advance the time by one minute
     */
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

            weather.nextDay(dayOfYear);
        }
        if (day > 6) {
            day -= 7;
        }
        if (dayOfYear > 364) {
            dayOfYear = 0;
        }
    }

    /**
     * Update all the views
     */
    private void updateViews(){
        // Update the car park Views.
        super.notifyViews();
    }

    /**
     * Generate random arriving cars
     */
    private void carsArriving(){
        int numberOfCars;
        int numberOfPassCars;
        int numberOfResCars;
        double multiplier = 1;

        switch (weather.getWeather()){
            case "Rainy":
                multiplier = 0.8;
            break;

            case "Snowy":
                multiplier = 0.6;
            break;
        }

        //check weather it's a holiday/festival or not
        switch (dayOfYear) {
            case 339:
                numberOfCars = getNumberOfCars((int) (300 * multiplier));
                numberOfPassCars = getNumberOfCars((int) (100 * multiplier));
                numberOfResCars = getNumberOfCars((int) (20 * multiplier));
                //number of cars arriving on a weekday
                break;
            default:
                if (day < 5) {
                    numberOfCars = getNumberOfCars((int) (80 * multiplier));
                    numberOfPassCars = getNumberOfCars((int) (50 * multiplier));
                    numberOfResCars = getNumberOfCars((int) (15 * multiplier));
                    //number of cars arriving on a weekday
                } else if (day == 5) {
                    numberOfCars = getNumberOfCars((int) (130 * multiplier));
                    numberOfPassCars = getNumberOfCars((int) (40 * multiplier));
                    numberOfResCars = getNumberOfCars((int) (20 * multiplier));
                    //number of cars arriving on a saturday
                } else {
                    numberOfCars = getNumberOfCars((int) (40 * multiplier));
                    numberOfPassCars = getNumberOfCars((int) (5 * multiplier));
                    numberOfResCars = getNumberOfCars((int) (1 * multiplier));
                    //number of cars arriving on a sunday
                }
                break;
        }
        addArrivingCars(numberOfCars, AD_HOC);
        addArrivingCars(numberOfPassCars, PASS);
        addArrivingCars(numberOfResCars, RES);
    }

    /**
     * Handle entering cars
     *
     * @param queue queue of cars entering
     */
    private void carsEntering(CarQueue queue){
        int enterSpeed = 7; // number of cars that can enter per minute
        // Remove car from the front of the queue and assign to a parking space.
        for (int i = 0; i < enterSpeed; i++) {
            Car car = queue.removeCar();
            Location freeLocation;
            if(car == null){
                break;
            }
            if (car instanceof ReservationCar) {
                freeLocation = car.getLocation();
            } else {
                freeLocation = this.getFirstFreeLocation(car);
            }
            this.setCarAt(freeLocation, car);
        }
    }


    /**
     * Handle leaving cars
     */
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

    /**
     * Handle current paying cars
     */
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

    /**
     * Handle leaving cars
     */
    private void carsLeaving(){
        // Let cars leave.
        int exitSpeed = 11; // number of cars that can leave per minute

        for (int i = 0; i < exitSpeed; i++) {
            Car car = exitCarQueue.removeCar();

            if (car == null) {
                break;
            }
        }
    }

    /**
     * Get number of cars
     *
     * @param AvgArrivalsPH Average arrival per hour
     * @return cars per hour
     */
    private int getNumberOfCars(int AvgArrivalsPH){
        Random random = new Random();

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = AvgArrivalsPH * 0.3;
        double numberOfCarsPerHour = AvgArrivalsPH + random.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);
    }

    /**
     * Set arriving cars
     *
     * @param numberOfCars Amount of cars
     * @param type type of car
     */
    private void addArrivingCars(int numberOfCars, String type){
        // Add the cars to the back of the queue.
        switch(type) {
            case AD_HOC:
                for (int i = 0; i < numberOfCars; i++) {
                    if (entranceCarQueue.carsInQueue() < 5) {
                        entranceCarQueue.addCar(new AdHocCar());
                    }else{
                        notification();
                    }
                }
                break;
            case PASS:
                for (int i = 0; i < numberOfCars; i++) {
                    if (entrancePassQueue.carsInQueue() < 5) {
                        entrancePassQueue.addCar(new ParkingPassCar());
                    }else{
                        notification();
                    }
                }
                break;
            case RES:
                for (int i = 0; i < numberOfCars; i++) {
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
                break;
        }
        updateViews();
    }

    /**
     * Check reservation cars
     */
    private void checkReservationCar() {
        String time = "" + day + hour + minute;
        if (carsReserved.get(Integer.parseInt(time)) != null) {
            entranceResArrQueue.addCar(carsReserved.remove(Integer.parseInt(time)));
            carsEntering(entranceResArrQueue);
        }
    }

    /**
     * Remove car at location
     *
     * @param location location of the car
     *
     * @return Car
     */
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

    /**
     * Set car at location
     *
     * @param location location of the car
     * @param car car object
     */
    private void setCarAt(Location location, Car car) {
        Car oldCar = cars.get(location);
        if (oldCar == null && !(car instanceof  ReservationCar)) {
            cars.put(location, car);
            car.setLocation(location);
            numberOfOpenSpots--;
        }
        if (oldCar == null && car instanceof ReservationCar) {
            cars.put(location, car);
            reserved.remove(location);
            numberOfOpenSpots--;
        }
    }

    /**
     * Get first free location for car
     *
     * @param forCar car object
     * @return location or null
     */
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

    /**
     * Get first leaving car
     *
     * @return car object
     */
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

    /**
     * Handle cars leaving the spot
     *
     * @param car Car object
     */
    private void carLeavesSpot(Car car){
        this.removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }

    /**
     * Get current day string
     * @return day
     */
    public String getCurrentDay() {
        return days[day];
    }

    /**
     * Check if location is pass reserved
     * @param location location to check
     * @return true or false
     */
    public Boolean isLocationPassReserved(Location location){
        return passReserved.get(location) != null;
    }

    /**
     * Get current hour
     *
     * @return current hour
     */
    public int getCurrentHour(){ return hour; }

    /**
     * Check if location is reserved
     *
     * @param location location object
     *
     * @return true or false
     */
    public Boolean isLocationReserved(Location location){
        return reserved.get(location) != null;
    }

    /**
     * Get total parked cars
     *
     * @return total parked cars
     */
    public int getTotalCars() {
        int total = 0;
        for(Location location : cars.keySet()) {
            if (cars.get(location) != null) {
                total++;
            }
        }

        return total;
    }

    /**
     * Get total parked reservation cars
     *
     * @return total parked reservation cars
     */
    public int getTotalReservationCars() {
        int total = 0;
        for(Location location : cars.keySet()) {
            if (cars.get(location) != null) {
                if(cars.get(location) instanceof ReservationCar)
                total++;
            }
        }

        return total;
    }

    /**
     * Get total parked passholders cars
     *
     * @return total parked pass holders
     */
    public int getTotalPassholderCars() {
        int total = 0;
        for(Location location : cars.keySet()) {
            if (cars.get(location) != null) {
                if(cars.get(location) instanceof ParkingPassCar)
                    total++;
            }
        }

        return total;
    }

    /**
     * Get total parked AdHoc cars
     *
     * @return get total parked AdHoc car
     */
    public int getTotalAdHocCars() {
        int total = 0;
        for(Location location : cars.keySet()) {
            if (cars.get(location) != null) {
                if(cars.get(location) instanceof AdHocCar)
                    total++;
            }
        }

        return total;
    }


    /**
     * Get today profit
     *
     * @return total today profit
     */
    public Double getTodayProfit(){
        return profitToday;
    }

    /**
     * Get current day as integer
     *
     * @return current day as integer
     */
    public int getCurrentIntDay(){
        return day;
    }

    /**
     * Get current weather
     *
     * @return Weather object
     */
    public Weather getWeather(){
        return weather;
    }

    /**
     * Make a new notification
     */
    public void notification(){
        try {
            // Open an audio input stream.
            URL url = this.getClass().getClassLoader().getResource("alert.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}