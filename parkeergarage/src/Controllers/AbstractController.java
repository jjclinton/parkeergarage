package Controllers;


import Models.AbstractModel;

import javax.swing.*;

public abstract class AbstractController extends JPanel {

    // A Controllers should have a certain instance of the AbstractModel
    protected AbstractModel model;

    /**
     * Constructor of AbstractController with a model belong to this Controllers
     * @param model AbstractModel that belongs to this Controllers
     */
    public AbstractController(AbstractModel model) {
        this.model = model;
    }
}
