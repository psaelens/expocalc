package be.spitech.expocalc.swing;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BoundedRangeModel;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.commons.lang.math.Fraction;

import be.spitech.expocalc.Aperture;
import be.spitech.expocalc.Factor;
import be.spitech.expocalc.ISO;
import be.spitech.expocalc.ShutterSpeeds;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;


public class ExpoCalcDemo {

	// TODO 
	class Exposure {
		
	}
	
	// TODO 
	class ExposureModel {
	
		public FactorUpdateHandler<Double> getApertureUpdateHandler() {
			return null;
		}
		
		// ...
	}

	static class FactorModel<T, F extends Factor<T>> {
		F factor;
		FactorUpdateHandler<T> updateHandler;
		JSpinner spinner;
		JSlider slider;
		
		public FactorModel(F factor) {
			this.factor = factor;
			this.updateHandler = new FactorUpdateHandler<T>(factor);
			this.spinner = new JSpinner(new SpinnerFactorModel<T>(factor));
			this.slider = new JSlider(new FactorBoundedRangeModel<T>(factor));
		}
	}
	
	public static void main(String[] args) {
		
//		for (int i=1;i<=12;++i){
//			double facteur = i/2;
//			double mod2 = i%2==0d?1:i%2*SQRT_2;
//			double result = pow(2, facteur) *mod2;
//			System.out.println("facteur : "+facteur+" squart : "+ mod2 +" = "+result);
//			private static final double SQRT_2 = sqrt(2);
//
//			import static java.lang.Math.pow;
//			import static java.lang.Math.sqrt;
		
		JFrame frame = new JFrame(".:ExpoCalc Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final ShutterSpeeds shutterSpeeds = ShutterSpeeds.STANDARD;
		shutterSpeeds.setValue(shutterSpeeds.getScale().get(shutterSpeeds.getScale().size() / 2));
		final Aperture aperture = Aperture.STANDARD;
		aperture.setValue(aperture.getScale().get(aperture.getScale().size() / 2));
		
		// initialise models
//		PresentationModel<ShutterSpeeds> presentationModel = new PresentationModel<ShutterSpeeds>(shutterSpeeds);
//		PresentationModel<Aperture> presentationModel2 = new PresentationModel<Aperture>(aperture);
		
		final FactorModel<Integer, ISO> isoModel = new FactorModel<Integer, ISO>(ISO.STANDARD);
		isoModel.factor.setValue(isoModel.factor.getScale().get(isoModel.factor.getScale().size() / 2));
		final FactorUpdateHandler<Double> apertureUpdateHandler = new FactorUpdateHandler<Double>(aperture);
		final FactorUpdateHandler<Fraction> shutterUpdateHandler = new FactorUpdateHandler<Fraction>(shutterSpeeds);
		
		
		SpinnerModel model = new SpinnerFactorModel<Fraction>(shutterSpeeds);
		BoundedRangeModel boundedRangeAdapter = new FactorBoundedRangeModel<Fraction>(shutterSpeeds);
		
		SpinnerFactorModel<Double> model2 = new SpinnerFactorModel<Double>(aperture);
		BoundedRangeModel boundedRangeAdapter2 = new FactorBoundedRangeModel<Double>(aperture);

		// initialise components
		final JSpinner shutterSpinner = new JSpinner(model);
		final JSlider shutterSlider = new JSlider(boundedRangeAdapter);
		final JSpinner apertureSpinner = new JSpinner(model2);
		final JSlider apertureSlider = new JSlider(boundedRangeAdapter2);
		
		// build content
		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("fill:p:grow, 3dlu, p")/*, new FormDebugPanel()*/);
		builder.setDefaultDialogBorder();
		
		final JComponent shutterSpeedsSeparator = builder.appendSeparator("Shutter Speeds");
		shutterSpinner.setEditor(new ShutterSpeedEditor(shutterSpinner));
		
		builder.append(shutterSlider);
		builder.append(shutterSpinner);
		builder.nextLine();
		
		final JComponent apertureSeparator = builder.appendSeparator("Aperture");
//		spinner.setEditor(new ShutterSpeedEditor(spinner));
		
		builder.append(apertureSlider);
		builder.append(apertureSpinner);
		builder.nextLine();
		
		final JComponent isoSeparator = builder.appendSeparator("ISO");
//		spinner.setEditor(new ShutterSpeedEditor(spinner));
		
		builder.append(isoModel.slider);
		builder.append(isoModel.spinner);
		builder.nextLine();
		
		ActionListener editAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// remove all handler
				shutterSpeeds.removePropertyChangeListener("step", apertureUpdateHandler);
				shutterSpeeds.removePropertyChangeListener("step", isoModel.updateHandler);
				aperture.removePropertyChangeListener("step", shutterUpdateHandler);
				aperture.removePropertyChangeListener("step", isoModel.updateHandler);
				isoModel.factor.removePropertyChangeListener("step", apertureUpdateHandler);
				isoModel.factor.removePropertyChangeListener("step", shutterUpdateHandler);
				if ("shutter".equals(e.getActionCommand())) {
					aperture.addPropertyChangeListener("step", shutterUpdateHandler);
					isoModel.factor.addPropertyChangeListener("step", shutterUpdateHandler);
					shutterSpinner.setEnabled(false);
					shutterSlider.setEnabled(false);
					apertureSpinner.setEnabled(true);
					apertureSlider.setEnabled(true);
					isoModel.slider.setEnabled(true);
					isoModel.spinner.setEnabled(true);
				} else if("fstop".equals(e.getActionCommand())) {
					shutterSpeeds.addPropertyChangeListener("step", apertureUpdateHandler);
					isoModel.factor.addPropertyChangeListener("step", apertureUpdateHandler);
					apertureSpinner.setEnabled(false);
					apertureSlider.setEnabled(false);
					shutterSpinner.setEnabled(true);
					shutterSlider.setEnabled(true);
					isoModel.slider.setEnabled(true);
					isoModel.spinner.setEnabled(true);
				} else if("iso".equals(e.getActionCommand())) {
					shutterSpeeds.addPropertyChangeListener("step", isoModel.updateHandler);
					aperture.addPropertyChangeListener("step", isoModel.updateHandler);
					apertureSpinner.setEnabled(true);
					apertureSlider.setEnabled(true);
					shutterSpinner.setEnabled(true);
					shutterSlider.setEnabled(true);
					isoModel.slider.setEnabled(false);
					isoModel.spinner.setEnabled(false);
				} else if("edit".equals(e.getActionCommand())) {
					shutterSpinner.setEnabled(true);
					shutterSlider.setEnabled(true);
					apertureSpinner.setEnabled(true);
					apertureSlider.setEnabled(true);
					isoModel.slider.setEnabled(true);
					isoModel.spinner.setEnabled(true);
				}
			}
		};
		
		ButtonGroup buttonGroup = new ButtonGroup();
		JRadioButton shutterButton = new JRadioButton("Shutter");
		shutterButton.setActionCommand("shutter");
		shutterButton.addActionListener(editAction);
		JRadioButton fStopButton = new JRadioButton("f/#");
		fStopButton.setActionCommand("fstop");
		fStopButton.addActionListener(editAction);
		JRadioButton isoButton = new JRadioButton("ISO");
		isoButton.setActionCommand("iso");
		isoButton.addActionListener(editAction);
		JRadioButton editButton = new JRadioButton("Edit");
		editButton.setActionCommand("edit");
		editButton.addActionListener(editAction);
		editButton.setSelected(true);
		
		buttonGroup.add(shutterButton);
		buttonGroup.add(fStopButton);
		buttonGroup.add(isoButton);
		buttonGroup.add(editButton);
		
		builder.append(buildCenteredBar(shutterButton, fStopButton, isoButton, editButton), builder.getColumnCount());
		
		frame.getContentPane().add(builder.getPanel());
		frame.pack();
		frame.setVisible(true);
	}
	
	
	private static Component buildCenteredBar(AbstractButton... buttons) {
		JPanel panel = new JPanel(new GridLayout(1, buttons.length));
		for(AbstractButton button : buttons) {
			panel.add(button);
		}
		return panel;
	}


	// TODO Bug : what happened when reach min or max ? 
	// For now just stop update Factor, but should disable edition of the other factor
	// more complicated : only disable edition that provides this factor out of bound 
	private static final class FactorUpdateHandler<T> implements
			PropertyChangeListener {
		private final Factor<T> factor;

		private FactorUpdateHandler(Factor<T> factor) {
			this.factor = factor;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ("step".equals(evt.getPropertyName())) {
				Integer step = (Integer) evt.getNewValue();
				if (step > 0) {
					for (int i = 0; i < step; i++) {
						T nextValue = factor.getNextValue();
						if (nextValue != null)
							factor.setValue(nextValue);
					}
				} else {
					for (int i = step; i < 0; i++) {
						T previousValue = factor.getPreviousValue();
						if (previousValue != null)
							factor.setValue(previousValue);
					}
				}
			}
		}
	}

	/**
     * Converts ShutterSpeed to Integers and vice-versa.
     */
    public static final class ScaleConverter<T> extends AbstractConverter {

        private final List<T> scale;

        public ScaleConverter(
                ValueModel subject, List<T> scale) {
            super(subject);
            this.scale = scale;
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
            return scale.indexOf(subjectValue);
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
            subject.setValue(scale.get(((Integer) newValue)));
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
