package Core;

import Controllers.AbstractController;
import Controllers.Controller;
import Models.CarPark;
import Models.CarQueue;
import Views.AbstractView;
import Views.CarParkView;

import javax.swing.*;

public class Simulator {

    public Simulator() {

        /**
         * Create the model, Views and Controllers that
         * we need for the Car Park Simulation
         */
        CarPark carParkModel = new CarPark(3, 6, 30);
        CarQueue carQueueModel = new CarQueue();
        AbstractView carParkView = new CarParkView(carParkModel);

        /**
         * Create the JFrame that will display the views
         * and add these views to this JFrame
         */
        JFrame screen = new JFrame("Car Park Simulation");
        screen.setSize(1200, 750);
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
