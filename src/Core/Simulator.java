package Core;

import Controllers.AbstractController;
import Controllers.Controller;
import Models.CarPark;
import Views.AbstractView;
import Views.CarParkView;
import Views.CarQueueView;

import javax.swing.*;

public class Simulator {

    private CarPark carParkModel;
    private JFrame screen;
    private JFrame screen2;
    private AbstractView carParkView;
    private AbstractView carQueueView;
    private AbstractController carParkController;


    public Simulator() {

        /**
         * Create the model, Views and Controllers that
         * we need for the Car Park Simulation
         */
        this.carParkModel = new CarPark(3, 6, 30);
        this.carParkController = new Controller(carParkModel);
        this.carParkView = new CarParkView(carParkModel);
        this.carQueueView = new CarQueueView(carParkModel);


        /**
         * Create the JFrame that will display the views
         * and add these views to this JFrame
         */
        JFrame screen = new JFrame("Car Park Simulation");
        screen.setSize(1200, 750);
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

        /**
         * Start running the simulation with 20 steps
         */
        for (int i = 0; i < 300000; i++) {
            carParkModel.tick();
        }
    }

}
