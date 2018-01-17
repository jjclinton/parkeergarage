package Core;

import Controllers.AbstractController;
import Controllers.Controller;
import Models.CarPark;
import Models.CarQueue;
import Views.AbstractView;
import Views.CarParkView;

import javax.swing.*;

public class Simulator {

    private CarPark carParkModel;
    private CarQueue carQueueModel;
    private JFrame screen;
    private AbstractView carParkView;
    private AbstractController carParkController;
    private int width;
    private int height;


    public Simulator() {
        /**
         * Set the dimension for the application
         */
        this.width = 1200;
        this.height = 750;

        /**
         * Create the model, Views and Controllers that
         * we need for the Car Park Simulation
         */
        this.carParkModel = new CarPark(3, 6, 30);
        this.carQueueModel = new CarQueue();
        this.carParkController = new Controller(carParkModel);
        this.carParkView = new CarParkView(carParkModel);

        /**
         * Create the JFrame that will display the views
         * and add these views to this JFrame
         */
        screen = new JFrame("Car Park Simulation");
        screen.setSize(this.width, this.height);
        screen.setLayout(null);

        /**
         * Add the views to the Core screen
         */
        screen.getContentPane().add(carParkView);

        carParkView.setBounds(0, 0, 1200, 600);


        /**
         * Show the Core screen, disable resizing and notify the views to update
         */
        screen.setVisible(true);
        screen.setResizable(false);

        carParkModel.notifyViews();
        carQueueModel.notifyViews();

        /**
         * Start running the simulation with 20 steps
         */
        for (int i = 0; i < 300000; i++) {
            carParkModel.tick();
        }
    }

}
