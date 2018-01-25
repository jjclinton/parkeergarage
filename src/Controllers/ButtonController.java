package Controllers;


import Core.Simulator;
import Models.AbstractModel;
import Models.CarPark;
import Views.ButtonView;

public class ButtonController extends AbstractController
{
    /**
     * Constructor of AbstractController with a model belong to this Controllers
     *
     * @param model AbstractModel that belongs to this Controllers
     */
    public ButtonController(CarPark model) {
        super(model);
    }

    public void actionPerformed(java.awt.event.ActionEvent e){
        switch(e.getActionCommand()){
            case "Start default":
                ButtonView.setButtons("default");

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
            break;

            case "1 step":
                ButtonView.setButtons("1step");

                Simulator.tabbedPane.setSelectedIndex(0);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Simulator.runSteps(1);
                            }
                        },
                        1
                );
            break;

            case "100 steps":
                ButtonView.setButtons("100steps");

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
            break;
        }
    }
}
