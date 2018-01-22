package Views;
import Models.CarPark;
import java.awt.event.*;
import javax.swing.*;

public class ButtonView extends AbstractView {
    private JButton button1;
    private JButton button100;
    private JLabel statusLabel;

    public ButtonView(CarPark model) {
        super(model);

        statusLabel = new JLabel("label");
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

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Button 1 clicked.");
            }
        });
        button100.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Button 100 clicked.");
            }
        });

    }

    @Override
    public void updateView() {

        setVisible(true);
        super.updateView();
    }
}
