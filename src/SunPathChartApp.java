import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SunPathChartApp extends JFrame {

    private DefaultCategoryDataset dataset;

    public SunPathChartApp() {
        setTitle("Sun Path Tracker");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        dataset = new DefaultCategoryDataset();

        JFreeChart chart = ChartFactory.createLineChart(
                "Sun Path Throughout the Day",
                "Time (hours)",
                "Light Intensity",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open CSV File");
        openItem.addActionListener(e -> loadFile());
        menu.add(openItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                loadDataset(selectedFile);
                JOptionPane.showMessageDialog(this, "File loaded successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadDataset(File file) throws IOException {
        dataset.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//format ajustabil
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                try {
                    Date date = dateFormat.parse(values[0]);
                    long timeInMillis = date.getTime();

                    double lightIntensity = Double.parseDouble(values[1]);

                    dataset.addValue(lightIntensity, "Light Intensity", Long.toString(timeInMillis));
                } catch (ParseException | NumberFormatException e) {//incomplete data, sensor error
                    System.err.println("Error parsing line: " + line + " (" + e.getMessage() + ")");
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SunPathChartApp chartApp = new SunPathChartApp();
            chartApp.setVisible(true);
        });
    }
}