package Views;

import Controllers.AbstractController;
import Models.AbstractModel;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class AbstractView extends JPanel
{
	// all views have a certain model
	protected AbstractModel model;
	protected ActionListener controller;

	/**
	 * Constructor of AbstractView that expects a model belonging to this Views.
	 *
	 * @param model AbstractModel that belongs to this Views.
	 */
	public AbstractView(AbstractModel model, ActionListener controller) {
		this.model = model;
		model.addView(this);

		this.controller = controller;

		// we use absolute positioning so we can set the layout to null
		setLayout(null);
	}

	public void updateView() {
		repaint();
	}

}
