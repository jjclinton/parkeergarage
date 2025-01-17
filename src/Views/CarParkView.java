package Views;

import Controllers.Controller;
import Models.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CarParkView extends AbstractView
{
    // image of the car park
    private CarPark carPark;
    private Image carParkImage;
    private JLabel dayLabel;
    private Image weatherImage;
    private String lastWeather;

    /**
     * Constructor of CarParkView that expects a model belonging to this Views
     *
     * @param model AbstractModel that belongs to this View
     * @param controller Controller that belongs to this View
     */
    public CarParkView(CarPark model, Controller controller) {
        super(model, controller);
        carPark = model;
        //show current day
        dayLabel = new JLabel("Current day: " + model.getCurrentDay());
        dayLabel.setSize(500, 15);
        dayLabel.setLocation(0, 0);
        add(dayLabel);
    }

    /**
     * Paint components
     *
     * @param g Graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        if (carParkImage == null) {
            return;
        }

        g.drawImage(carParkImage, 0, 0, null);
    }

    /**
     * Set weather image
     *
     * @param weather Weather object
     * @param g Graphics object
     */
    private void generateWeatherImage(String weather, Graphics g){
        try {
            weatherImage = ImageIO.read(this.getClass().getClassLoader().getResource(weather + ".png"));
        } catch (IOException ex) {

        }
    }

    /**
     * Update views.
     */
    @Override
    public void updateView() {
        int floorNrX = 150;
        //show current day
        currentDay();

        Dimension currentSize = getSize();

        // create a new car park image if the size has changed.
        carParkImage = createImage(currentSize.width, currentSize.height);

        Graphics graphics = carParkImage.getGraphics();
        drawLegend(graphics);
        drawWeather(graphics);


        for (int floor = 0; floor < carPark.getNumberOfFloors(); floor++) {
            drawFloorNumber(graphics, floor,floorNrX);
            floorNrX = floorNrX + 260;
            for (int row = 0; row < carPark.getNumberOfRows(); row++) {
                for (int place = 0; place < carPark.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = carPark.getCar(location);
                    Color color = Color.WHITE;

                    if(carPark.isLocationPassReserved(location)){
                        color = Color.cyan;
                    }
                    if (carPark.isLocationReserved(location)) {
                        color = Color.pink;
                    }

                    if(car != null){
                        color = car.getColor();
                    }


                    drawPlace(graphics, location, color);
                }
            }
        }

        setVisible(true);
        super.updateView();
    }

    /**
     * Paint a place on this car park Views in a given color.
     */
    private void drawPlace(Graphics graphics, Location location, Color color) {
        graphics.setColor(color);
        graphics.fillRect(
                location.getFloor() * 260 + (1 + (int)Math.floor(location.getRow() * 0.5)) * 75 + (location.getRow() % 2) * 20,
                60 + location.getPlace() * 10,
                20 - 1,
                10 - 1
        ); // TODO use dynamic size or constants
    }

    /**
     * Draw floor numbers
     *
     * @param graphics Graphics object
     * @param currentFloor floor number
     * @param x X position
     */
    private void drawFloorNumber(Graphics graphics, int currentFloor, int x) {
        graphics.setColor(Color.BLACK);
        graphics.drawString("Floor " + currentFloor, x, 30);
    }

    /**
     * Draw the weather
     *
     * @param graphics Graphics object
     */
    private void drawWeather(Graphics graphics){
        String weather = carPark.getWeather().getWeather();

        if(!weather.equals(lastWeather)){
            System.out.println(weather);
            lastWeather = weather;
            generateWeatherImage(weather, graphics);
        }

        graphics.drawImage(weatherImage, 10, 10, 70, 70, this);
    }

    /**
     * Draw the legend
     *
     * @param graphics Graphics object
     */
    private void drawLegend(Graphics graphics)  {
        //draws a white rectangle
        graphics.setColor(Color.WHITE);
        graphics.fillRect(75, 370, 18, 9);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Empty parking space", 100, 379);
        //draws a red rectangle
        graphics.setColor(Color.RED);
        graphics.fillRect(320, 370, 18, 9);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Occupied parking space", 345, 379);
        //draws a cyan rectangle
        graphics.setColor(Color.CYAN);
        graphics.fillRect(75, 387, 18, 9);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Empty pass holder parking space", 100, 396);
        //draws a blue rectangle
        graphics.setColor(Color.BLUE);
        graphics.fillRect(320, 387, 18, 9);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Occupied pass holder parking space", 345, 396);
        //draws a pink rectangle
        graphics.setColor(Color.pink);
        graphics.fillRect(75, 404, 18, 9);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Reserved space", 100, 414);
        //draws a green rectangle
        graphics.setColor(Color.green);
        graphics.fillRect(320, 404, 18, 9);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Occupied reserved parking space", 345, 414);
    }

    /**
     * Handle current day label
     */
    private void currentDay() {
        dayLabel.setText("Current day: " + carPark.getCurrentDay());
    }
}
