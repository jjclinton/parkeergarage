package Models;

import Views.*;
import java.util.*;

public abstract class AbstractModel {
	private List<AbstractView> views;

	/**
	 * Constructor for AbstractModel.
	 */
	public AbstractModel() {
		views=new ArrayList<>();
	}

	/**
	 * Add view to model
	 * @param view View for model
	 */
	public void addView(AbstractView view) {
		views.add(view);
	}

	/**
	 * Notify all views
	 */
	public void notifyViews() {
		for(AbstractView v: views) v.updateView();
	}
}
