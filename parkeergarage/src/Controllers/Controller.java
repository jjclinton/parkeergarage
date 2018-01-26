package Controllers;


import Models.AbstractModel;
import Models.CarPark;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Controller extends AbstractController implements ActionListener
{
    private JButton speedButton;

    /**
     * Constructor of AbstractController with a model belong to this Controllers
     *
     * @param model AbstractModel that belongs to this Controllers
     */
    public Controller(AbstractModel model) {
        super(model);

        setLayout(null);

        speedButton = new JButton("100 steps");
        speedButton.setBounds(5,5,100,20);
        speedButton.addActionListener(this);
        add(speedButton);
    }

    /**
     * Method for setting steps in the simulator.
     */
    private void speedPressed() {
        CarPark carPark = (CarPark) super.model;

        for(int i =0; i < 100; i++){
            carPark.tick();
        }
    }

    /**
     * Method for performing actions.
     * @param actionEvent The action we are listening for.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if(actionEvent.getSource() == speedButton){
            this.speedPressed();
        }

    }
}
