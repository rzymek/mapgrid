package pl.mapgrid.actions.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Action;

public class Actions {
	private Map<Name, UIAction> actions = new HashMap<Name, UIAction>();
	public static enum Name {
		OPEN, SAVE, DETECT_GRID, REMOVE_GRID, TOGGLE_GRID, REVERT, TOGGLE_SETUP, EXIT
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
