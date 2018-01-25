package Views;
import Core.Simulator;
import Models.CarPark;
import java.awt.event.*;
import javax.swing.*;

public class ButtonView extends AbstractView {
    private JButton startDefault;
    private JButton button1;
    private JButton button100;

    private JLabel statusLabel;

    public ButtonView(CarPark model) {
        super(model);


        statusLabel = new JLabel(" ");
        statusLabel.setSize(350,25);
        statusLabel.setLocation(25, 50);
        add(statusLabel);

        startDefault = new JButton("Start default");
        startDefault.setSize(110, 25);
        startDefault.setLocation(25, 20);
        add(startDefault);

        button1 = new JButton("1 step");
        button1.setSize(110, 25);
        button1.setLocation(275, 20);
        add(button1);

        button100 = new JButton("100 steps");
        button100.setSize(110, 25);
        button100.setLocation(150, 20);
        add(button100);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("1 step.");
                Simulator.tabbedPane.setSelectedIndex(0);
                Simulator.runSteps(1);
            }
        });

        button100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("100 steps.");
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

        startDefault.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("300000 steps");
                button1.setVisible(false);
                button100.setVisible(false);
                Simulator.tabbedPane.setSelectedIndex(0);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Simulator.runSteps(300000);
                            }
                        },
                        1
                );
            }
        });

    }

    @Override
    public void updateView() {

        setVisible(true);
        super.updateView();
    }
}
