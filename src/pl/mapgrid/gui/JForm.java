package pl.mapgrid.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Field;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import layout.SpringUtilities;
import pl.mapgrid.Doc;
import pl.mapgrid.MaskGrid.Config;

public class JForm extends JPanel {
	private final Object[] beans;

	private static class TextFieldListener implements FocusListener {
		private final Field field;
		private final Object bean;

		public TextFieldListener(Object bean, Field field) {
			this.bean = bean;
			this.field = field;
		}

		@Override
		public void focusGained(FocusEvent paramFocusEvent) {
		}

		@Override
		public void focusLost(FocusEvent e) {
			JTextField textField  = (JTextField) e.getComponent();
			try {
				String value = textField.getText();
				Class<?> type = field.getType();
				if(type == Double.TYPE) 
					field.setDouble(bean, Double.parseDouble(value));
				else if(type == Integer.TYPE)
					field.setInt(bean, Integer.parseInt((value)));
				else if(type.isAssignableFrom(Color.class) ) {
					Color c;
					if("".equals(value.trim()))
						c = null;
					else
						c = new Color(Integer.parseInt(value, 16));
					field.set(bean, c);
				}
			}catch (Exception ex) {
				System.err.println(ex);
			}
			textField.setText(getString(bean, field));
		}
	}

	public JForm(Object... beans) throws Exception {
		super(new SpringLayout());
		this.beans = beans;
		int rows = 0;
		for (Object bean : beans) {
			Field[] fields = bean.getClass().getFields();
			for (Field field : fields) {
				Doc doc = field.getAnnotation(Doc.class);
				if (doc == null)
					continue;
				String text = getString(bean, field);
				JTextField textField = new JTextField(text);
				textField.addFocusListener(new TextFieldListener(bean, field));
				textField.setHorizontalAlignment(JTextField.RIGHT);
				textField.setMaximumSize(textField.getMinimumSize());
				JLabel label = new JLabel("<html>" + doc.value() + "</html>");
				Dimension preferredSize = label.getPreferredSize();
				preferredSize.width = 300;
				label.setPreferredSize(preferredSize);
				label.setLabelFor(textField);
				
				add(label);
				add(textField);
				rows += 2;
			}
		}
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(1,Integer.MAX_VALUE));
		add(panel);
		++rows;
		SpringUtilities.makeCompactGrid(this, rows, 1, 5, 5, 5, 5);
	}

	private static String getString(Object bean, Field field) {
		try {
			Object value = field.get(bean);
			if(value == null) {
				return "";
			} else if(value instanceof Color) {
				Color c = (Color) value;
				return Integer.toHexString(c.getRGB());
			}else { 
				return value.toString();
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
