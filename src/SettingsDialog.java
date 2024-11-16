import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {

    private SunPathChartApp chartApp;

    public SettingsDialog(SunPathChartApp owner) {
        super(owner, "Settings", true);
        this.chartApp = owner;
        setSize(400, 300);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Settings");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        JButton colorButton = new JButton("Change Series Color");
        colorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Series Color", chartApp.getSeriesColor());
            if (newColor != null) {
                chartApp.setSeriesColor(newColor);
            }
        });
        panel.add(colorButton);

        JLabel delayLabel = new JLabel("Animation Delay (ms):");
        delayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(delayLabel);

        JSpinner delaySpinner = new JSpinner(new SpinnerNumberModel(chartApp.getAnimationDelay(), 0, 5000, 100));
        delaySpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        delaySpinner.addChangeListener(e -> chartApp.setAnimationDelay((int) delaySpinner.getValue()));
        panel.add(delaySpinner);

        JCheckBox animationCheckBox = new JCheckBox("Enable Animations", chartApp.isAnimationsEnabled());
        animationCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        animationCheckBox.addActionListener(e -> chartApp.setAnimationsEnabled(animationCheckBox.isSelected()));
        panel.add(animationCheckBox);

        JButton backgroundColorButton = new JButton("Change Background Color");
        backgroundColorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Background Color", chartApp.getBackgroundColor());
            if (newColor != null) {
                chartApp.setBackgroundColor(newColor);
            }
        });
        panel.add(backgroundColorButton);

        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> setVisible(false));
        panel.add(closeButton);

        add(panel);
    }
}