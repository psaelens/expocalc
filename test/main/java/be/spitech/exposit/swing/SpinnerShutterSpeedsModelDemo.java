package be.spitech.exposit.swing;

import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.commons.lang.math.Fraction;

import be.spitech.exposit.ShutterSpeeds;


public class SpinnerShutterSpeedsModelDemo {

	public static void main(String[] args) {
		JFrame frame = new JFrame(".:SpinnerShutterSpeedsModel Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpinnerShutterSpeedsModel model = new SpinnerShutterSpeedsModel(ShutterSpeeds.STANDARD);

		JSpinner spinner = new JSpinner(model);
		spinner.setEditor(new ShutterSpeedsEditor(spinner));
		frame.getContentPane().add(spinner);
		frame.pack();
		frame.setVisible(true);
	}
	
	static class ShutterSpeedsEditor extends JSpinner.DefaultEditor {
		
		public ShutterSpeedsEditor(JSpinner spinner) {
			super(spinner);
			getTextField().setFormatterFactory(new DefaultFormatterFactory(new AbstractFormatter() {
				
				@Override
				public String valueToString(Object value) throws ParseException {
					return value.toString();
				}
				
				@Override
				public Object stringToValue(String text) throws ParseException {
					return Fraction.getFraction(text);
				}
			}));
		}
	}
}
