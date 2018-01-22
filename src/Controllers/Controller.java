package Controllers;


import Models.AbstractModel;
import Models.CarPark;
import javafx.fxml.FXMLLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Controller extends AbstractController
{
    /**
     * Constructor of AbstractController with a model belong to this Controllers
     *
     * @param model AbstractModel that belongs to this Controllers
     */
    public Controller(AbstractModel model) {
        super(model);
        setLayout(null);
    }
}
