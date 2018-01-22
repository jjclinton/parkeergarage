package Views;

import Models.CarPark;

import java.awt.*;

public class SettingsView extends AbstractView {

    /**
     * Constructor of CarParkView that expects a model belonging to this Views
     *
     * @param model AbstractModel that belongs to this Views
     */
    public SettingsView(CarPark model) {
        super(model);
    }

    @Override
    public void updateView() {

        setVisible(true);
        super.updateView();
    }

}