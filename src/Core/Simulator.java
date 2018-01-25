package Core;

import Controllers.AbstractController;
import Controllers.Controller;
import Models.CarPark;
import Views.AbstractView;
import Views.CarParkView;
import Views.SettingsView;
import Views.StaticsView;

import javax.swing.*;

public class Simulator {

    private CarPark carParkModel;

    private AbstractController carParkController;

    private AbstractView carParkView;
    private AbstractView staticsView;
    private AbstractView settingsView;



    public Simulator() {
        /**
         * Create the model, Views and Controllers that
         * we need for the Car Park Simulation
         */
        this.carParkModel = new CarPark(3, 6, 30, 75);

        this.carParkController = new Controller(carParkModel);

        this.carParkView = new CarParkView(carParkModel);
        carParkView.setBounds(0, 0, 1200, 600);

        this.settingsView = new SettingsView(carParkModel);
        settingsView.setBounds(0, 0, 1200, 600);

        this.staticsView = new StaticsView(carParkModel);
        staticsView.setBounds(0, 0, 1200, 600);


        /**
         * Create the JFrame that will display the views
         * and add these views to this JFrame
         */
        JFrame screen = new JFrame("Car Park Simulation");
        screen.setSize(1200, 750);
        screen.setLayout(null);


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setSize(1200, 750);

        /**
         * Add each tab panel
         */
        JComponent mainPanel = new JPanel(false);
        mainPanel.setLayout(null);
        mainPanel.add(carParkView);

        JComponent staticsPanel = new JPanel(false);
        staticsPanel.setLayout(null);
        staticsPanel.add(staticsView);


        JComponent settingsPanel = new JPanel(false);
        settingsPanel.setLayout(null);
        settingsPanel.add(settingsView);

        /**
         * Add each tab
         */
        tabbedPane.addTab("Simulator", mainPanel);
        tabbedPane.addTab("Statics", staticsPanel);
        tabbedPane.addTab("Settings", settingsPanel);

        screen.getContentPane().add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        /**
         * Show the Core screen, disable resizing and notify the views to update
         */
        screen.setVisible(true);
        screen.setResizable(false);

        carParkModel.notifyViews();

        /**
         * Start running the simulation with 20 steps
         */
        for (int i = 0; i < 300000; i++) {
            carParkModel.tick();
        }
    }

}
