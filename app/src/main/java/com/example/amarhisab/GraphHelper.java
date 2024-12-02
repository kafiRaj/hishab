package com.example.amarhisab;

import android.graphics.Color;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

public class GraphHelper {

    public static void setupBarChart(GraphView graphView, double[] incomeData, double[] expenseData, List<String> months) {

        // Create DataPoint arrays for income and expense data
        DataPoint[] incomeDataPoints = new DataPoint[months.size()];
        DataPoint[] expenseDataPoints = new DataPoint[months.size()];

        for (int i = 0; i < months.size(); i++) {
            incomeDataPoints[i] = new DataPoint(i + 1, incomeData[i]);  // Shift x-axis by 1 for padding
            expenseDataPoints[i] = new DataPoint(i + 1, expenseData[i]);
        }

        // Create and add series
        BarGraphSeries<DataPoint> incomeSeries = new BarGraphSeries<>(incomeDataPoints);
        BarGraphSeries<DataPoint> expenseSeries = new BarGraphSeries<>(expenseDataPoints);
        graphView.addSeries(incomeSeries);
        graphView.addSeries(expenseSeries);

        // Customize series appearance
        incomeSeries.setColor(Color.BLUE);
        expenseSeries.setColor(Color.RED);
        incomeSeries.setSpacing(50);
        expenseSeries.setSpacing(50);

        // Configure the viewport
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0); // Add padding before the first data point
        graphView.getViewport().setMaxX(months.size() + 1); // Add padding after the last data point

        // Set up labels
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    int index = (int) value - 1; // Adjust for shifted x-axis
                    return (index >= 0 && index < months.size()) ? months.get(index) : ""; // Show month labels
                }
                return super.formatLabel(value, isValueX); // Default formatting for Y values
            }
        });
        graphView.getGridLabelRenderer().setNumHorizontalLabels(months.size()); // Ensure all months are shown

        // Disable automatic padding
        graphView.getGridLabelRenderer().setPadding(20); // Optional: adjust this value for finer control

        // Disable scaling and scrolling to keep the chart fixed
        graphView.getViewport().setScalable(false);
        graphView.getViewport().setScalableY(false);
    }
}
