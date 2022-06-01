package src;

import java.util.ArrayList;
 import org.jfree.chart.ChartFactory;
 import org.jfree.chart.ChartPanel;
 import org.jfree.chart.JFreeChart;
 import org.jfree.chart.plot.PlotOrientation;
 import org.jfree.data.xy.XYSeries;
 import org.jfree.data.xy.XYSeriesCollection;

 import org.jfree.ui.ApplicationFrame;
 import org.jfree.ui.RefineryUtilities;


public class XYSeriesDemo extends ApplicationFrame {

/**
 * A demonstration application showing an XY series containing a null value.
 *
 * @param title  the frame title.
 */
public XYSeriesDemo(final String title, ArrayList<double[]> s) {

    super(title);
    final XYSeries series = new XYSeries("Random Data");
    
    for (double [] i:s) {
        series.add(i[0],i[1]);
        
    }
    
    
    final XYSeriesCollection data = new XYSeriesCollection(series);
    final JFreeChart chart = ChartFactory.createXYLineChart(
        "GRAFICA ",
        "GENERACIONES", 
        "COSTE", 
        data,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );

    final ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    setContentPane(chartPanel);

}


// ****************************************************************************
// * JFREECHART DEVELOPER GUIDE                                               *
// * The JFreeChart Developer Guide, written by David Gilbert, is available   *
// * to purchase from Object Refinery Limited:                                *
// *                                                                          *
// * http://www.object-refinery.com/jfreechart/guide.html                     *
// *                                                                          *
// * Sales are used to provide funding for the JFreeChart project - please    * 
// * support us so that we can continue developing free software.             *
// ****************************************************************************

/**
 * Starting point for the demonstration application.
 *
 * @param args  ignored.
 */
public static void main(final String[] args) {

    

}

}