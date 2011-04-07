package pl.mapgrid.actions;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;

public class Actions {
	private Map<Name, Action> actions = new HashMap<Name, Action>();
	public static enum Name {
		OPEN, EXIT, REMOVE_GRID, DETECT_GRID
	};	
	
	public void set(Name name, Action action) {
		actions.put(name, action);
	}

	public Action get(Name name) {
		Action action = actions.get(name);
		if(action == null)
			throw new NullPointerException("Action "+name+" not configured");
		return action;
	};
}
