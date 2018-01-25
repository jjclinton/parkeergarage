package Views;

import Models.CarPark;

public class AboutView extends AbstractView {

    /**
     * Constructor of CarParkView that expects a model belonging to this Views
     *
     * @param model AbstractModel that belongs to this Views
     */
    public AboutView(CarPark model) {
        super(model);
    }

    @Override
    public void updateView() {
        setVisible(true);
        super.updateView();
    }

}