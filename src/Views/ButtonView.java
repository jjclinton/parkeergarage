package Views;
import Models.AbstractModel;
import Models.CarPark;
import java.awt.event.*;
import javax.swing.*;

public class ButtonView extends AbstractView {
    private static JButton startDefault;
    private static JButton pause;
    private static JButton button1;
    private static JButton button100;
    private static JButton exit;

    private static JLabel statusLabel;

    private AbstractModel carParkModel;

    /**
     * Constructor of ButtonView
     *
     * @param model AbstractModel that belongs to this View
     * @param controller Controller that belongs to this View
     */
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

        pause = new JButton("pause");
        pause.setSize(110, 25);
        pause.setLocation(25,20);
        pause.setVisible(false);
        add(pause);

        button1 = new JButton("1 step");
        button1.setSize(110, 25);
        button1.setLocation(150, 20);
        add(button1);

        button100 = new JButton("100 steps");
        button100.setSize(110, 25);
        button100.setLocation(275, 20);
        add(button100);

        exit = new JButton("exit");
        exit.setSize(110, 25);
        exit.setLocation(400, 20);
        add(exit);

        startDefault.addActionListener(controller);
        button1.addActionListener(controller);
        button100.addActionListener(controller);
        exit.addActionListener(controller);
        pause.addActionListener(controller);
    }

    /**
     * Update views.
     */
    @Override
    public void updateView() {
        setVisible(true);
        super.updateView();
    }

    /**
     * Set button text
     *
     * @param status set status
     */
    public static void setButtons(String status){
        switch (status){
            case "default":
                statusLabel.setText("300000 steps.");
                button1.setVisible(false);
                button100.setVisible(false);
                startDefault.setVisible(false);
                pause.setVisible(true);
                break;
            case "1step":
                statusLabel.setText("1 step.");
                break;
            case "100steps":
                statusLabel.setText("100 steps.");
                break;
            case "pause":
                statusLabel.setText("paused");
                button1.setVisible(true);
                button100.setVisible(true);
                startDefault.setVisible(true);
                pause.setVisible(false);
                break;
        }
    }
}