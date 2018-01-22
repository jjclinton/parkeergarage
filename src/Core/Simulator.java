package Core;

import Controllers.AbstractController;
import Controllers.Controller;
import Models.CarPark;
import Models.CarQueue;
import Views.AbstractView;
import Views.CarParkView;
import Views.CarQueueView;

import javax.swing.*;

public class Simulator {

    private CarPark carParkModel;
    private CarQueue carQueueModel;
    private JFrame screen;
    private JFrame screen2;
    private AbstractView carParkView;
    private AbstractView carQueueView;
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
        this.carQueueView = new CarQueueView(carParkModel);

        /**
         * Create the JFrame that will display the views
         * and add these views to this JFrame
         */
        screen = new JFrame("Car Park Simulation");
        screen.setSize(this.width, this.height);
        screen.setLayout(null);

        screen2 = new JFrame("Car Queue Simulation");
        screen2.setSize(300, 400);
        screen2.setLayout(null);

        /**
         * Add the views to the Core screen
         */
        screen.getContentPane().add(carParkView);
        screen.getContentPane().add(carParkController);

        carParkView.setBounds(0, 0, 1200, 600);

        screen2.getContentPane().add(carQueueView);

        carQueueView.setBounds(0, 0, 300, 400);

        /**
         * Show the Core screen, disable resizing and notify the views to update
         */
        screen.setVisible(true);
        screen.setResizable(false);

        screen2.setVisible(true);
        screen2.setResizable(true);

        carParkModel.notifyViews();
        carQueueModel.notifyViews();

        /**
         * Start running the simulation with 20 steps
         */
        for (int i = 0; i < 300000; i++) {
            carParkModel.tick();
            carQueueModel.tick();
        }
    }

}
