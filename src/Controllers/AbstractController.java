package Controllers;


import Models.AbstractModel;


public abstract class AbstractController implements java.awt.event.ActionListener {

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
