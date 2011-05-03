package pl.mapgrid.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Action;

import pl.mapgrid.gui.actions.UIAction;

public class Actions {
	private final Map<Name, UIAction> actions = new HashMap<Name, UIAction>();
	public static enum Name {
		OPEN, OPEN_SHAPE, SAVE, OPEN_SETTINGS, SAVE_SETTINGS, DETECT_GRID, REMOVE_GRID, TOGGLE_GRID, 
		REVERT, ROTATE_CALIBRATED, UTM_GRID, TOGGLE_SETUP, REDRAW_SHAPES, EXIT, 
	};	
	
	public void set(Name name, UIAction action) {
		actions.put(name, action);
	}

	public Action get(Name name) {
		Action action = actions.get(name);
		if(action == null)
			throw new NullPointerException("Action "+name+" not configured");
		return action;
	}

	public String getDescription(Name name) {
		return get(name).toString();
	}

	public void reenable() {
		for (Entry<Name, UIAction> entry : actions.entrySet()) {
			UIAction action = entry.getValue();
			action.setEnabled(action.enabled());
		}
	}
}
