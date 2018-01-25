package Views;

import Models.*;

import javax.swing.*;
import java.awt.*;

public class CarParkView extends AbstractView
{
    // image of the car park
    private Image carParkImage;
    private JLabel dayLabel;

    /**
     * Constructor of CarParkView that expects a model belonging to this Views
     *
     * @param model AbstractModel that belongs to this Views
     */
    public CarParkView(CarPark model) {
        super(model);
        //show current day
        dayLabel = new JLabel("Current day: " + CarPark.getCurrentDay());
        dayLabel.setSize(500, 15);
        dayLabel.setLocation(0, 0);
        add(dayLabel);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (carParkImage == null) {
            return;
        }

        g.drawImage(carParkImage, 0, 0, null);
    }

    @Override
    public void updateView() {
        int floorNrX = 150;
        //show current day
        currentDay();

        Dimension currentSize = getSize();

        // create a new car park image if the size has changed.
        carParkImage = createImage(currentSize.width, currentSize.height);

        Graphics graphics = carParkImage.getGraphics();


        for (int floor = 0; floor < CarPark.getNumberOfFloors(); floor++) {
            drawFloorNumber(graphics, floor,floorNrX);
            floorNrX = floorNrX + 260;
            for (int row = 0; row < CarPark.getNumberOfRows(); row++) {
                for (int place = 0; place < CarPark.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = CarPark.getCar(location);
                    Color color = Color.WHITE;

                    if(CarPark.isLocationReserved(location)){
                        color = Color.cyan;
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

    private void drawFloorNumber(Graphics graphics, int currentFloor, int x) {
        graphics.setColor(Color.BLACK);
        graphics.drawString("Floor " + currentFloor, x, 30);
    }
    private void currentDay() {
        dayLabel.setText("Current day: " + CarPark.getCurrentDay());
    }
}
