package Core;

import Controllers.AbstractController;
import Controllers.ButtonController;
import Controllers.Controller;
import Models.CarPark;
import Views.AbstractView;
import Views.CarParkView;
import Views.AboutView;
import Views.ButtonView;
import Views.StaticsView;

import javax.swing.*;

public class Simulator {

    private static CarPark carParkModel;

    private AbstractView carParkView;
    private AbstractView staticsView;
    private AbstractView buttonView;
    private AbstractView settingsView;

    public static JTabbedPane tabbedPane;

    public Simulator() {
        /**
         * Create the model, Views and Controllers that
         * we need for the Car Park Simulation
         */
        this.carParkModel = new CarPark(3, 6, 30, 75);

        Controller carParkController = new Controller(carParkModel);
        ButtonController buttonController = new ButtonController(carParkModel);

        this.carParkView = new CarParkView(carParkModel, carParkController);
        carParkView.setBounds(0, 0, 1200, 600);

        this.settingsView = new AboutView(carParkModel, carParkController);
        settingsView.setBounds(0, 0, 1200, 600);

        this.buttonView = new ButtonView(carParkModel, buttonController);
        buttonView.setBounds(0, 0, 1200, 600);

        this.staticsView = new StaticsView(carParkModel, carParkController);
        staticsView.setBounds(0, 0, 1200, 600);


        /**
         * Create the JFrame that will display the views
         * and add these views to this JFrame
         */
        JFrame screen = new JFrame("Car Park Simulation");
        screen.setSize(1200, 750);
        screen.setLayout(null);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        tabbedPane = new JTabbedPane();
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

        JComponent buttonPanel = new JPanel(false);
        buttonPanel.setLayout(null);
        buttonPanel.add(buttonView);

        JComponent settingsPanel = new JPanel(false);
        settingsPanel.setLayout(null);
        settingsPanel.add(settingsView);

        /**
         * Add each tab
         */
        tabbedPane.addTab("Simulator", mainPanel);
        tabbedPane.addTab("Statics", staticsPanel);
        tabbedPane.addTab("Button", buttonPanel);
        tabbedPane.addTab("Settings", settingsPanel);

        screen.getContentPane().add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        /**
         * Show the Core screen, disable resizing and notify the views to update
         */
        screen.setVisible(true);
        screen.setResizable(false);

        carParkModel.notifyViews();
    }
    public static void runSteps(int steps) {
        for (int i = 0; i < steps; i++) {
            carParkModel.tick();
        }
    }
}
