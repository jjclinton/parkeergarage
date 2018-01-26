package Views;

import Models.CarPark;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonView extends AbstractView {
    private Button button1;
    private Button button100;

    /**
     * Constructor for objects of class CarPark
     */
    public ButtonView(CarPark model) {
        super(model);

        button1 = new Button();
        button1.setLabel("1 stap");

        button100 = new Button();
        button100.setLabel("100 stappen");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.tick();
            }
        });

        button100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for(int i =0; i < 100; i++){
                    model.tick();
                }
            }
        });

        Container contentPane = getRootPane();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(button1, gbc);
        contentPane.add(button100, gbc);
        setVisible(true);


    }

        /**
         * Overridden. Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }
    }
