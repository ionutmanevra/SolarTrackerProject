import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {

    private SunPathChartApp chartApp;

    public SettingsDialog(SunPathChartApp owner) {
        super(owner, "Settings", true);
        this.chartApp = owner;
        setSize(400, 400);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton colorButton = new JButton("Change Series Color");
        colorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        colorButton.setToolTipText("Change the color of the series in the chart");
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Series Color", chartApp.getSeriesColor());
            if (newColor != null) {
                chartApp.setSeriesColor(newColor);
            }
        });
        panel.add(colorButton);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel delayLabel = new JLabel("Animation Delay (ms):");
        delayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(delayLabel);

        JSpinner delaySpinner = new JSpinner(new SpinnerNumberModel(chartApp.getAnimationDelay(), 0, 5000, 100));
        delaySpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        delaySpinner.setToolTipText("Set the delay between animation frames in milliseconds");
        delaySpinner.addChangeListener(e -> chartApp.setAnimationDelay((int) delaySpinner.getValue()));
        panel.add(delaySpinner);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JCheckBox animationCheckBox = new JCheckBox("Enable Animations", chartApp.isAnimationsEnabled());
        animationCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        animationCheckBox.setToolTipText("Enable or disable animations in the chart");
        animationCheckBox.addActionListener(e -> chartApp.setAnimationsEnabled(animationCheckBox.isSelected()));
        panel.add(animationCheckBox);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton backgroundColorButton = new JButton("Change Background Color");
        backgroundColorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundColorButton.setToolTipText("Change the background color of the chart");
        backgroundColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Background Color", chartApp.getBackgroundColor());
            if (newColor != null) {
                chartApp.setBackgroundColor(newColor);
            }
        });
        panel.add(backgroundColorButton);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel titleLabel = new JLabel("Chart Title:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        JTextField titleField = new JTextField(chartApp.getChartTitle());
        titleField.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleField.setToolTipText("Set the title of the chart");
        titleField.addActionListener(e -> chartApp.setChartTitle(titleField.getText()));
        panel.add(titleField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JCheckBox legendCheckBox = new JCheckBox("Show Legend", chartApp.isLegendVisible());
        legendCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        legendCheckBox.setToolTipText("Show or hide the legend in the chart");
        legendCheckBox.addActionListener(e -> chartApp.setLegendVisible(legendCheckBox.isSelected()));
        panel.add(legendCheckBox);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton applyButton = new JButton("Apply");
        applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyButton.setToolTipText("Apply the changes");
        applyButton.addActionListener(e -> {
            chartApp.setSeriesColor(chartApp.getSeriesColor());
            chartApp.setAnimationDelay((int) delaySpinner.getValue());
            chartApp.setAnimationsEnabled(animationCheckBox.isSelected());
            chartApp.setBackgroundColor(chartApp.getBackgroundColor());
            chartApp.setChartTitle(titleField.getText());
            chartApp.setLegendVisible(legendCheckBox.isSelected());
        });
        panel.add(applyButton);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setToolTipText("Close the settings dialog");
        closeButton.addActionListener(e -> setVisible(false));
        panel.add(closeButton);

        add(panel);
    }
}