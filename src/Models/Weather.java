package Models;

import java.util.ArrayList;
import java.util.Random;

public class Weather {
    private ArrayList<String> types;
    private String currentWeather;

    /**
     * Constructor of the weather.
     */
    public Weather() {
        types = new ArrayList<>();
        types.add("Sunny");
        types.add("Rainy");
        types.add("Snowy");

        currentWeather = "Sunny";
    }

    /**
     * Generate weather based on day of year
     *
     * @param dayOfYear day of the year
     */
    public void nextDay(int dayOfYear) {
        Random random = new Random();
        int size = types.size();
        // Add snow to winter period
        if(dayOfYear < 59 || dayOfYear > 336){
            size = size - 1;
        }

        int index = random.nextInt(size);


        currentWeather = types.get(index);
    }

    /**
     * Get current weather
     *
     * @return weather string
     */
    public String getWeather(){
        return currentWeather;
    }
}
