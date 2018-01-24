package Views;
import Core.Simulator;
import Models.CarPark;
import java.awt.event.*;
import javax.swing.*;

public class ButtonView extends AbstractView {
    private JButton reset;
    private JButton button1;
    private JButton button100;

    private JLabel statusLabel;

    private CarPark carParkModel;

    public ButtonView(CarPark model) {
        super(model);

        carParkModel = new CarPark(3, 6, 30);

        statusLabel = new JLabel(" ");
        statusLabel.setSize(350,25);
        statusLabel.setLocation(25, 50);
        add(statusLabel);

        button1 = new JButton("1 stap");
        button1.setSize(110, 25);
        button1.setLocation(25, 20);
        add(button1);

        button100 = new JButton("100 stappen");
        button100.setSize(110, 25);
        button100.setLocation(150, 20);
        add(button100);

        reset = new JButton("Reset");
        reset.setSize(110, 25);
        reset.setLocation(275, 20);
        add(reset);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("1 stap per seconde.");
                //(CarPark.steps) = 1000;
                Simulator.tabbedPane.setSelectedIndex(0);
                Simulator.runSteps(1);
            }
        });

        button100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("100 stappen per seconde.");
                //(CarPark.steps) = 10;
                Simulator.tabbedPane.setSelectedIndex(0);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Simulator.runSteps(100);
                            }
                        },
                        1
                );
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText(" ");
                //(CarPark.steps) = 100;
            }
        });

    }

    @Override
    public void updateView() {

        setVisible(true);
        super.updateView();
    }
}
