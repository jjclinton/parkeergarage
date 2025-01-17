package Controllers;


import Core.Simulator;
import Models.AbstractModel;
import Models.CarPark;
import Views.ButtonView;

import java.awt.event.ActionEvent;

public class ButtonController extends AbstractController
{
    /**
     * Constructor of AbstractController with a model belong to this Controllers
     *
     * @param model AbstractModel that belongs to this Controllers
     *
     *              Give the amount of steps you want and execute it
     */
    public ButtonController(CarPark model) {
        super(model);
    }

    public void actionPerformed(java.awt.event.ActionEvent e){
        switch(e.getActionCommand()){
            case "Start default":
                ButtonView.setButtons("default");
                Simulator.tabbedPane.setSelectedIndex(0);
                Simulator.pause = false;
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
                Simulator.pause = false;
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
                Simulator.pause = false;
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

            case "exit":
                ButtonView.setButtons("exit");
                Simulator.tabbedPane.setSelectedIndex(0);
                System.exit(0);
            break;

            case "pause":
                ButtonView.setButtons("pause");
                Simulator.tabbedPane.setSelectedIndex(0);
                Simulator.pause = true;
            break;
            }
        }
    }
