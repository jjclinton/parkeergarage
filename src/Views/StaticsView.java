package Views;

import Controllers.Controller;
import Models.CarPark;

import java.awt.*;
import java.util.ArrayList;

public class StaticsView extends AbstractView {
    private CarPark carPark;

    private GraphPanel totalAdHocGraph;
    private ArrayList<Double> totalAdHocGraphData;

    private GraphPanel totalReservationsGraph;
    private ArrayList<Double> totalReservationsGraphData;

    private GraphPanel totalPassholdersGraph;
    private ArrayList<Double> totalPassholdersGraphData;

    private GraphPanel carsParkedGraph;
    private ArrayList<Double> carsParkedGraphData;

    private GraphPanel profitGraph;
    private ArrayList<Double> profitGraphData;

    /**
     * Constructor of CarParkView that expects a model belonging to this Views
     *
     * @param model AbstractModel that belongs to this View
     * @param controller Controller that belongs to this View
     */
    public StaticsView(CarPark model, Controller controller) {
        super(model, controller);

        carPark = model;

        carsParkedGraphData = new ArrayList<>();
        totalAdHocGraphData = new ArrayList<>();
        totalReservationsGraphData = new ArrayList<>();
        totalPassholdersGraphData = new ArrayList<>();

        for(int hour = 0; hour <= 23; hour++){
            carsParkedGraphData.add(0.0);
            totalAdHocGraphData.add(0.0);
            totalReservationsGraphData.add(0.0);
            totalPassholdersGraphData.add(0.0);
        }
        carsParkedGraph = new GraphPanel(carsParkedGraphData, "Total cars parked");

        totalAdHocGraph = new GraphPanel(totalAdHocGraphData, "Total regular cars");
        totalReservationsGraph = new GraphPanel(totalReservationsGraphData, "Total reservations parked");
        totalPassholdersGraph = new GraphPanel(totalPassholdersGraphData, "Total passholders parked");


        profitGraphData = new ArrayList<>();
        for(int day = 0; day <= 6; day++){
            profitGraphData.add(0.0);
        }
        profitGraph = new GraphPanel(profitGraphData, "Profit per day");

        GridLayout grid = new GridLayout(0,2);
        setLayout(grid);
        add(carsParkedGraph);
        add(profitGraph);
        add(totalAdHocGraph);
        add(totalPassholdersGraph);
        add(totalReservationsGraph);
    }

    /**
     * Update views.
     */
    @Override
    public void updateView() {
        int currentHour = carPark.getCurrentHour();
        int currentDay = carPark.getCurrentIntDay();

        double totalParkedCars = (double) carPark.getTotalCars();
        if (carsParkedGraphData != null) {
            carsParkedGraphData.set(currentHour, totalParkedCars);
            carsParkedGraph.setData(carsParkedGraphData);
        }

        double totalAdHocCars = (double) carPark.getTotalAdHocCars();
        if (totalAdHocGraphData != null) {
            totalAdHocGraphData.set(currentHour, totalAdHocCars);
            totalAdHocGraph.setData(totalAdHocGraphData);
        }

        double totalReservationCars = (double) carPark.getTotalReservationCars();
        if (totalReservationsGraph != null) {
            totalReservationsGraphData.set(currentHour, totalReservationCars);
            totalReservationsGraph.setData(totalReservationsGraphData);
        }

        double totalPassholdersCars = (double) carPark.getTotalPassholderCars();
        if (totalPassholdersGraph != null) {
            totalPassholdersGraphData.set(currentHour, totalPassholdersCars);
            totalPassholdersGraph.setData(totalPassholdersGraphData);
        }


        double totalProfit = carPark.getTodayProfit();

        if (profitGraphData != null && totalProfit != 0.0) {
            profitGraphData.set(currentDay, totalProfit);

            profitGraph.setData(profitGraphData);
        }

        setVisible(true);
    }

}