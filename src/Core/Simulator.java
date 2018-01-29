package Core;

import Controllers.ButtonController;
import Controllers.Controller;
import Models.CarPark;
import Views.AbstractView;
import Views.CarParkView;
import Views.ButtonView;
import Views.StaticsView;

import javax.swing.*;

public class Simulator {

    private static CarPark carParkModel;

    private AbstractView carParkView;
    private AbstractView staticsView;
    private AbstractView buttonView;

    public static JTabbedPane tabbedPane;
    public static JFrame screen;
    public static boolean pause;

    /**
     * Simulator instance
     */
    public Simulator() {
        this.carParkModel = new CarPark(3, 6, 30, 75);
        pause = false;

        Controller carParkController = new Controller(carParkModel);
        ButtonController buttonController = new ButtonController(carParkModel);

        this.carParkView = new CarParkView(carParkModel, carParkController);
        carParkView.setBounds(0, 0, 1200, 600);

        this.buttonView = new ButtonView(carParkModel, buttonController);
        buttonView.setBounds(0, 0, 1200, 600);

        this.staticsView = new StaticsView(carParkModel, carParkController);
        staticsView.setBounds(0, 0, 1200, 600);


        /**
         * Create the JFrame that will display the views
         * and add these views to this JFrame
         */
        screen = new JFrame("Car Park Simulation");
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


        /**
         * Add each tab
         */
        tabbedPane.addTab("Simulator", mainPanel);
        tabbedPane.addTab("Statics", staticsPanel);
        tabbedPane.addTab("Button", buttonPanel);

        screen.getContentPane().add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        /**
         * Show the Core screen, disable resizing and notify the views to update
         */
        screen.setVisible(true);
        screen.setResizable(false);

        carParkModel.notifyViews();
    }

    /**
     * Run the model a given steps
     * @param steps amount of steps
     */
    public static void runSteps(int steps) {
        for (int i = 0; i < steps; i++) {
            carParkModel.tick();
            if (pause) {
                break;
            }
        }
    }
}
