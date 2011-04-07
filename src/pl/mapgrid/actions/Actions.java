package pl.mapgrid.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;

public class Actions {
	private Map<Name, Action> actions = new HashMap<Name, Action>();
	private Map<Action, List<JComponent>> using = new HashMap<Action, List<JComponent>>();
	public static enum Name {
		OPEN, EXIT, REMOVE_GRID, DETECT_GRID
	};	
	
	public void set(Name name, Action action) {
		actions.put(name, action);
	}

	private Action get(Name name) {
		Action action = actions.get(name);
		if(action == null)
			throw new NullPointerException("Action "+name+" not configured");
		return action;
	}

	public void assign(JButton component, Name name) {
		component.setAction(actions.get(name));
		Action action = get(name);
		List<JComponent> list = using.get(action);
		if(list == null)
			using.put(action, list = new ArrayList<JComponent>()); 
		list.add(component);
	}

	public void setEnabled(Action action, boolean enabled) {
		List<JComponent> list = using.get(action);
		if(list == null)
			return;
		for (JComponent component : list) 
			component.setEnabled(enabled);
	};
}
