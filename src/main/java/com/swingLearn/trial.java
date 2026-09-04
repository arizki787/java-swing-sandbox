package com.swingLearn;

import javax.swing.JFrame;
import java.sql.SQLException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class trial extends JFrame {

    public trial() {
        super("Department Salary Chart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // see note below
        setSize(600, 400);
        setLocationRelativeTo(null);

        DepartmentDAO dao = new DepartmentDAO();
        DefaultCategoryDataset dataset;
        try {
            dataset = dao.getDeptSalary();
        } catch (SQLException ex) {
            ex.printStackTrace();
            dataset = new DefaultCategoryDataset(); // empty fallback so chart still renders
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Total Salary Based on Departments",
                "Department Name",
                "Salary",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, true
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        add(chartPanel);
    }
}