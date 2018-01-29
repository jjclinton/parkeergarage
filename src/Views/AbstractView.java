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
	 * Constructor of AbstractView that expects a model and controller that are belonging to this view.
	 *
	 * @param model AbstractModel that belongs to this Views.
	 * @param controller ActionListener controller
	 */
	public AbstractView(AbstractModel model, ActionListener controller) {
		this.model = model;
		model.addView(this);

		this.controller = controller;

		setLayout(null);
	}

	/**
	 * Repaint all the views
	 */
	public void updateView() {
		repaint();
	}
}
