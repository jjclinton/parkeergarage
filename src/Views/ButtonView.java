package Views;
import Controllers.ButtonController;
import Core.Simulator;
import Models.AbstractModel;
import Models.CarPark;
import java.awt.event.*;
import javax.swing.*;

public class ButtonView extends AbstractView {
    private static JButton startDefault;
    private static JButton button1;
    private static JButton button100;
    private static JButton reset;

    private static JLabel statusLabel;

    private AbstractModel carParkModel;

    public ButtonView(CarPark model, ActionListener controller) {
        super(model, controller);

        carParkModel = model;

        statusLabel = new JLabel("Start the simulation");
        statusLabel.setSize(350,25);
        statusLabel.setLocation(25, 50);
        add(statusLabel);

        startDefault = new JButton("Start default");
        startDefault.setSize(110, 25);
        startDefault.setLocation(25, 20);
        add(startDefault);

        button1 = new JButton("1 step");
        button1.setSize(110, 25);
        button1.setLocation(150, 20);
        add(button1);

        button100 = new JButton("100 steps");
        button100.setSize(110, 25);
        button100.setLocation(275, 20);
        add(button100);

        reset = new JButton("reset");
        reset.setSize(110, 25);
        reset.setLocation(400, 20);
        add(reset);

        startDefault.addActionListener(controller);
        button1.addActionListener(controller);
        button100.addActionListener(controller);

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    @Override
    public void updateView() {
        setVisible(true);
        super.updateView();
    }

    public static void setButtons(String status){
        switch (status){
            case "default":
                statusLabel.setText("300000 steps.");
                button1.setVisible(false);
                button100.setVisible(false);
                break;
            case "1step":
                statusLabel.setText("1 step.");
                break;
            case "100steps":
                statusLabel.setText("100 steps.");
                break;
        }
    }
}
