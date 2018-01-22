package Views;

import Models.Car;
import Models.CarPark;
import Models.CarQueue;
import Models.Location;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

public class CarQueueView extends AbstractView {
    // image of the car park
    private Image carParkImage;
    private Dimension size;
    /**
     * Constructor of CarParkView that expects a model belonging to this Views
     *
     * @param model AbstractModel that belongs to this Views
     */
    public CarQueueView(CarPark model) {
        super(model);
        this.size = new Dimension(300, 400);
    }

    /**
     * Overridden. Tell the GUI manager how big we would like to be.
     */
    public Dimension getPreferredSize() {
        return size;
    }

    /**
     * Overriden. The car park Views component needs to be redisplayed. Copy the
     * internal image to screen.
     */
    public void paintComponent(Graphics g) {
        if (carParkImage == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(carParkImage, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
        }
    }

    @Override
    public void updateView() {
        int floorNrX = 150;

        // create a new car park image if the size has changed.
        carParkImage = createImage(size.width, size.height);

        Graphics graphics = carParkImage.getGraphics();

        drawCarQueue(graphics);

        setVisible(true);
        super.updateView();
    }


    private void drawCarQueue(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.drawString("Cars in queue ", 50, 30);

    }

}