package be.spitech.expocalc.swing;

import java.text.ParseException;
import java.util.List;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.commons.lang.math.Fraction;

import be.spitech.expocalc.ShutterSpeeds;
import be.spitech.expocalc.swing.SpinnerShutterSpeedsModel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.FormLayout;


public class SpinnerShutterSpeedsModelDemo {

	public static void main(String[] args) {
		JFrame frame = new JFrame(".:SpinnerShutterSpeedsModel Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ShutterSpeeds shutterSpeeds = ShutterSpeeds.STANDARD;
		
		// initialise models
		PresentationModel<ShutterSpeeds> presentationModel = new PresentationModel<ShutterSpeeds>(shutterSpeeds);
		SpinnerShutterSpeedsModel model = new SpinnerShutterSpeedsModel(shutterSpeeds);
		SpinnerAdapterFactory.connect(model, presentationModel.getModel("value"), shutterSpeeds.getValue());

		BoundedRangeAdapter boundedRangeAdapter = new BoundedRangeAdapter(
				new ShutterSpeedConverter(
						presentationModel.getModel("value"), 
						shutterSpeeds.getSpeeds()), 
						0, 0, shutterSpeeds.size() - 1);

		// initialise components
		JSpinner spinner = new JSpinner(model);
		JSlider slider = new JSlider(boundedRangeAdapter);
		
		// build content
		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("fill:d:grow, 3dlu, d")/*, new FormDebugPanel()*/);
		
		builder.appendSeparator("Shutter Speeds");
		spinner.setEditor(new ShutterSpeedEditor(spinner));
		
		builder.append(slider);
		builder.append(spinner);
		builder.nextLine();
		
		frame.getContentPane().add(builder.getPanel());
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
     * Converts ShutterSpeed to Integers and vice-versa.
     */
    public static final class ShutterSpeedConverter extends AbstractConverter {

        private final List<Fraction> speeds;

        public ShutterSpeedConverter(
                ValueModel subject, List<Fraction> speeds) {
            super(subject);
            this.speeds = speeds;
        }

        /**
         * Converts the subject's value and returns a
         * corresponding <code>Integer</code> value using the multiplier.
         *
         * @param subjectValue  the subject's value
         * @return the converted subjectValue
         * @throws ClassCastException if the subject value is not of type
         *     <code>Fraction</code>
         */
        @Override
        public Object convertFromSubject(Object subjectValue) {
            return speeds.indexOf(subjectValue);
        }

        /**
         * Converts a <code>Fraction</code> using the speed's list
         * and sets it as new value.
         *
         * @param newValue  the <code>Fraction</code> object that shall
         *     be converted
         * @throws ClassCastException if the new value is not of type
         *     <code>Integer</code>
         * @throws ArrayIndexOutOfBoundsException if the new value is wrong
         */
        public void setValue(Object newValue) {
            subject.setValue(speeds.get(((Integer) newValue)));
        }

    }
	
	public static class ShutterSpeedEditor extends JSpinner.DefaultEditor {
		
		public ShutterSpeedEditor(JSpinner spinner) {
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
