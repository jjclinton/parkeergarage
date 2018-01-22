package Views;

import Models.CarPark;

import javax.swing.*;

public class StaticsView extends AbstractView {
    private JLabel entranceCarQueueLabel;
    private JLabel entrancePassQueueLabel;
    private JLabel paymentCarQueueLabel;
    private JLabel exitCarQueueLabel;

    /**
     * Constructor of CarParkView that expects a model belonging to this Views
     *
     * @param model AbstractModel that belongs to this Views
     */
    public StaticsView(CarPark model) {
        super(model);

        entranceCarQueueLabel = new JLabel("Entrance car queue: ");
        entranceCarQueueLabel.setSize(500, 15);
        entranceCarQueueLabel.setLocation(0, 0);
        add(entranceCarQueueLabel);


        entrancePassQueueLabel = new JLabel("Entrance passholders car queue: ");
        entrancePassQueueLabel.setSize(500, 15);
        entrancePassQueueLabel.setLocation(0, 15);
        add(entrancePassQueueLabel);


        paymentCarQueueLabel = new JLabel("Payment car queue: ");
        paymentCarQueueLabel.setSize(500, 15);
        paymentCarQueueLabel.setLocation(0, 30);
        add(paymentCarQueueLabel);

        exitCarQueueLabel = new JLabel("Exit car queue: ");
        exitCarQueueLabel.setSize(500, 15);
        exitCarQueueLabel.setLocation(0, 45);
        add(exitCarQueueLabel);
    }

    @Override
    public void updateView() {
        int entranceCarQueue = CarPark.getEntranceCarQueue().carsInQueue();
        entranceCarQueueLabel.setText("Entrance car queue: " + entranceCarQueue);

        int entrancePassQueue = CarPark.getEntrancePassQueue().carsInQueue();
        entrancePassQueueLabel.setText("Entrance passholders car queue: " + entrancePassQueue);

        int paymentQueue = CarPark.getEntrancePaymentQueue().carsInQueue();
        paymentCarQueueLabel.setText("Payment car queue: " + paymentQueue);

        int exitQueue = CarPark.getEntranceExitQueue().carsInQueue();
        exitCarQueueLabel.setText("Exit car queue: " + exitQueue);


        setVisible(true);
    }
}