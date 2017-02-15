package main.java.inflor.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jfree.chart.JFreeChart;

import main.java.inflor.core.data.FCSFrame;
import main.java.inflor.core.gates.AbstractGate;
import main.java.inflor.core.gates.GateUtilities;
import main.java.inflor.core.plots.AbstractFCChart;
import main.java.inflor.core.plots.ChartSpec;
import main.java.inflor.core.plots.FCSChartPanel;
import main.java.inflor.core.plots.PlotUtils;
import main.java.inflor.core.utils.BitSetUtils;
import main.java.inflor.core.utils.ChartUtils;
import main.java.inflor.core.utils.FCSUtilities;

@SuppressWarnings("serial")
public class TreeCellPlotRenderer extends DefaultTreeCellRenderer {
  
  String[] columnNames = new String[] {"Name", "Count", "Frequency of Parent"};
  FCSFrame referenceData;
  
  
  public TreeCellPlotRenderer(FCSFrame dataFrame) {
    super();
    referenceData = dataFrame;
  }

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
      boolean expanded, boolean leaf, int row, boolean hasFocus) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
    node.breadthFirstEnumeration();
    
    List<AbstractGate> gates = extractGates(node.getUserObjectPath());
    if (referenceData!=null){
      BitSet mask = GateUtilities.applyGatingPath(referenceData, gates);
      //node is root
      if (node.isRoot()) {
        String[][] tableRow = new String[][]{{referenceData.getDisplayName(), Integer.toString(referenceData.getRowCount()), "-"}};
        JTable table = new JTable(tableRow, columnNames);
        formatTable(selected, table);
        return table;
      //node is a gate
      } else if (node.getUserObject() instanceof AbstractGate) {
        AbstractGate gate = (AbstractGate) node.getUserObject();
        String[][] tableRow = new String[][]{{gate.getLabel(), Integer.toString(mask.cardinality()), BitSetUtils.frequencyOfParent(mask, 2)}};
        JTable table = new JTable(tableRow, columnNames);
        formatTable(selected, table);
        return table;
      //node is plot
      } else if (node.getUserObject() instanceof ChartSpec) {
        FCSFrame filteredFrame = FCSUtilities.filterFrame(mask, referenceData);
        ChartSpec spec = (ChartSpec) node.getUserObject();
        AbstractFCChart plot = PlotUtils.createPlot(spec);
        JFreeChart chart = plot.createChart(filteredFrame);
        formatChart(selected, expanded, leaf, chart);
        FCSChartPanel panel = new FCSChartPanel(chart, spec, filteredFrame);
        List<AbstractGate> siblingGates = findSiblingGates(node);
        siblingGates
          .stream()
          .filter(gate -> ChartUtils.gateIsCompatibleWithChart(gate, spec))
          .map(ChartUtils::createAnnotation)
          .forEach(panel::createGateAnnotation);
        panel.setPreferredSize(new Dimension(220, 200));
        return panel;
    } 
    }
   return new JLabel("Unsupported node type.");
  }

  private void formatTable(boolean selected, JTable table) {
    if (selected){
      table.setRowSelectionInterval(0, 0);
    }
  }

  private void formatChart(boolean selected, boolean expanded, boolean leaf, JFreeChart chart) {
    if (!expanded) {
      chart.setBackgroundPaint(Color.LIGHT_GRAY);
    } else {
      chart.setBackgroundPaint(Color.WHITE);
    }
    if (leaf) {
      chart.setBackgroundPaint(Color.WHITE);
    }
    if (selected) {
      chart.setBorderPaint(Color.LIGHT_GRAY);
      chart.setBorderVisible(true);
    }
  }

  private List<AbstractGate> findSiblingGates(DefaultMutableTreeNode node) {
    List<AbstractGate> gates = new ArrayList<>();
    int siblingCount = node.getParent().getChildCount();
    for (int i=0;i<siblingCount;i++){
      DefaultMutableTreeNode siblingNode = (DefaultMutableTreeNode) node.getParent().getChildAt(i); 
      if (siblingNode.getUserObject() instanceof AbstractGate){
        gates.add((AbstractGate)siblingNode.getUserObject());
      }
    }
    return gates;
  }

  private List<AbstractGate> extractGates(Object[] userObjectPath) {
    List<AbstractGate> gates = new ArrayList<>();
    for (Object o:userObjectPath){
      if (o instanceof AbstractGate){
        gates.add((AbstractGate) o);
      }
    }
    return gates;
  }
}
