package Views;

import Controllers.Controller;
import Models.CarPark;

public class AboutView extends AbstractView {

    /**
     * Constructor of CarParkView that expects a model belonging to this Views
     *
     * @param model AbstractModel that belongs to this Views
     */
    public AboutView(CarPark model, Controller controller) {
        super(model, controller);
    }

    @Override
    public void updateView() {
        setVisible(true);
        super.updateView();
    }

}