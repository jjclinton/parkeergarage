package Views;

import Controllers.Controller;
import Models.CarPark;

import java.awt.*;
import java.util.ArrayList;

public class StaticsView extends AbstractView {
    private CarPark carPark;

    private GraphPanel entranceCarQueueGraph;
    private ArrayList<Double> entranceCarQueueGraphData;

    private GraphPanel entrancePassQueueGraph;
    private ArrayList<Double> entrancePassQueueGraphData;

    private GraphPanel paymentCarQueueGraph;
    private ArrayList<Double> paymentCarQueueGraphData;

    private GraphPanel exitCarQueueGraph;
    private ArrayList<Double> exitCarQueueGraphData;

    private GraphPanel carsParkedGraph;
    private ArrayList<Double> carsParkedGraphData;

    private GraphPanel profitGraph;
    private ArrayList<Double> profitGraphData;

    /**
     * Constructor of CarParkView that expects a model belonging to this Views
     *
     * @param model AbstractModel that belongs to this Views
     */
    public StaticsView(CarPark model, Controller controller) {
        super(model, controller);

        carPark = model;

        carsParkedGraphData = new ArrayList<>();
        for(int hour = 0; hour <= 23; hour++){
            carsParkedGraphData.add(0.0);
        }
        carsParkedGraph = new GraphPanel(carsParkedGraphData, "Total cars parked");

        entranceCarQueueGraphData = new ArrayList<>();
        entrancePassQueueGraphData = new ArrayList<>();
        paymentCarQueueGraphData = new ArrayList<>();
        exitCarQueueGraphData = new ArrayList<>();
        for(int minute = 0; minute < 60; minute++) {
            entranceCarQueueGraphData.add(0.0);
            entrancePassQueueGraphData.add(0.0);
            paymentCarQueueGraphData.add(0.0);
            exitCarQueueGraphData.add(0.0);
        }
        entranceCarQueueGraph = new GraphPanel(entranceCarQueueGraphData, "Entrance car queue");
        entrancePassQueueGraph = new GraphPanel(entrancePassQueueGraphData, "Entrance passholders queue");
        paymentCarQueueGraph = new GraphPanel(paymentCarQueueGraphData, "Payment queue");
        exitCarQueueGraph = new GraphPanel(exitCarQueueGraphData, "Exit queue");


        profitGraphData = new ArrayList<>();
        for(int day = 0; day <= 6; day++){
            profitGraphData.add(0.0);
        }
        profitGraph = new GraphPanel(profitGraphData, "Profit per day");

        GridLayout grid = new GridLayout(0,2);
        setLayout(grid);
        add(carsParkedGraph);
        add(profitGraph);

        add(entranceCarQueueGraph);
        add(entrancePassQueueGraph);
        add(paymentCarQueueGraph);
        add(exitCarQueueGraph);
    }

    @Override
    public void updateView() {
        int currentHour = carPark.getCurrentHour();
        int minute = carPark.getMinute();
        int currentDay = carPark.getCurrentIntDay();

        double totalParkedCars = (double) carPark.getTotalCars();
        if (carsParkedGraphData != null) {
            carsParkedGraphData.set(currentHour, totalParkedCars);
            carsParkedGraph.setData(carsParkedGraphData);
        }

        double entranceCarQueue = (double) carPark.getEntranceCarQueue().carsInQueue();

        if (entranceCarQueueGraph != null) {
            entranceCarQueueGraphData.set(minute, entranceCarQueue);
            entranceCarQueueGraph.setData(entranceCarQueueGraphData);
        }

        double entrancePassQueue = (double) carPark.getEntrancePassQueue().carsInQueue();

        if (entrancePassQueueGraph != null) {
            entrancePassQueueGraphData.set(minute, entrancePassQueue);
            entrancePassQueueGraph.setData(entrancePassQueueGraphData);
        }

        double paymentQueue = (double) carPark.getEntrancePaymentQueue().carsInQueue();

        if (paymentCarQueueGraph != null) {
            paymentCarQueueGraphData.set(minute, paymentQueue);
            paymentCarQueueGraph.setData(paymentCarQueueGraphData);
        }

        double exitQueue = (double) carPark.getEntranceExitQueue().carsInQueue();

        if (exitCarQueueGraph != null) {
            exitCarQueueGraphData.set(minute, exitQueue);
            exitCarQueueGraph.setData(exitCarQueueGraphData);
        }


        double totalProfit = carPark.getTodayProfit();

        if (profitGraphData != null && totalProfit != 0.0) {
            profitGraphData.set(currentDay, totalProfit);

            profitGraph.setData(profitGraphData);
        }

        setVisible(true);
    }

}