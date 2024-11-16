import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.imageio.ImageIO;

public class SunPathChartApp extends JFrame {

    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private Color seriesColor = Color.CYAN;
    private int animationDelay = 50;
    private boolean animationsEnabled = true;
    private Color backgroundColor = Color.BLACK;

    public SunPathChartApp() {
        setTitle("Sun Path Tracker");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dataset = new XYSeriesCollection();

        chart = ChartFactory.createXYLineChart(
                "Sun Path Throughout the Day",
                "Time (hours)",
                "Light Intensity",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        applyDarkTheme(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open CSV File");
        openItem.addActionListener(e -> loadFile());
        menu.add(openItem);


        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(e -> openSettings());
        menu.add(settingsItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void applyDarkTheme(JFreeChart chart) {
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(backgroundColor);
        plot.setOutlinePaint(Color.GRAY);

        if (plot instanceof XYPlot) {
            XYPlot xyPlot = (XYPlot) plot;
            xyPlot.setDomainGridlinePaint(Color.GRAY);
            xyPlot.setRangeGridlinePaint(Color.GRAY);

            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesPaint(0, seriesColor);
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            xyPlot.setRenderer(renderer);

            DateAxis domainAxis = new DateAxis("Time (hours)");
            domainAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
            domainAxis.setLabelPaint(Color.WHITE);
            domainAxis.setTickLabelPaint(Color.WHITE);
            xyPlot.setDomainAxis(domainAxis);

            ValueAxis rangeAxis = xyPlot.getRangeAxis();
            if (rangeAxis instanceof NumberAxis) {
                rangeAxis.setLabelPaint(Color.WHITE);
                rangeAxis.setTickLabelPaint(Color.WHITE);
            }
        }

        chart.setBackgroundPaint(backgroundColor);
        chart.getTitle().setPaint(Color.WHITE);
        chart.getLegend().setBackgroundPaint(backgroundColor);
        chart.getLegend().setItemPaint(Color.WHITE);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            new LoadDatasetWorker(selectedFile).execute();
        }
    }


    private class LoadDatasetWorker extends SwingWorker<Void, XYSeries> {
        private File file;

        public LoadDatasetWorker(File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            loadDataset(file);
            return null;
        }

        @Override
        protected void process(List<XYSeries> chunks) {
            for (XYSeries series : chunks) {
                dataset.removeSeries(series);
                dataset.addSeries(series);
            }
        }

        @Override
        protected void done() {
            try {
                get();
                JOptionPane.showMessageDialog(SunPathChartApp.this, "File loaded successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(SunPathChartApp.this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void loadDataset(File file) throws IOException {
            dataset.removeAllSeries();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            XYSeries series = new XYSeries("Light Intensity");

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

                        series.add(timeInMillis, lightIntensity);
                        publish(series);
                        if (animationsEnabled) {
                            Thread.sleep(animationDelay);
                        }
                    } catch (ParseException | NumberFormatException | InterruptedException e) {
                        System.err.println("Error parsing line: " + line + " (" + e.getMessage() + ")");
                    }
                }
            }
        }
    }

    private void openSettings() {
        SettingsDialog settingsDialog = new SettingsDialog(this);
        settingsDialog.setVisible(true);
    }

    public void setSeriesColor(Color color) {
        this.seriesColor = color;
        applyDarkTheme(chart);
    }

    public Color getSeriesColor() {
        return seriesColor;
    }

    public void setAnimationDelay(int delay) {
        this.animationDelay = delay;
    }

    public int getAnimationDelay() {
        return animationDelay;
    }

    public void setAnimationsEnabled(boolean enabled) {
        this.animationsEnabled = enabled;
    }

    public boolean isAnimationsEnabled() {
        return animationsEnabled;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        applyDarkTheme(chart);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setChartTitle(String title) {
        chart.setTitle(title);
    }

    public String getChartTitle() {
        return chart.getTitle().getText();
    }

    public void setLegendVisible(boolean visible) {
        chart.getLegend().setVisible(visible);
    }

    public boolean isLegendVisible() {
        return chart.getLegend().isVisible();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SunPathChartApp chartApp = new SunPathChartApp();
            chartApp.setVisible(true);
        });
    }
}