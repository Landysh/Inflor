/*
 * ------------------------------------------------------------------------
 *  Copyright 2016 by Aaron Hart
 *  Email: Aaron.Hart@gmail.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 * ---------------------------------------------------------------------
 *
 * Created on December 14, 2016 by Aaron Hart
 */
package fleur.core.plots;

import java.util.Optional;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer.FillType;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.util.ShapeUtilities;

import fleur.core.data.FCSDimension;
import fleur.core.data.FCSFrame;
import fleur.core.data.Histogram1D;
import fleur.core.transforms.AbstractTransform;
import fleur.core.transforms.TransformSet;
import fleur.core.utils.FCSUtilities;
import fleur.core.utils.PlotUtils;

public class HistogramPlot extends AbstractFCChart {

  public HistogramPlot(String priorUUID, ChartSpec spec) {
    super(priorUUID, spec);
    this.spec = spec;
  }

  public HistogramPlot(ChartSpec spec) {
    this(null, spec);
  }

  @Override
  public void setSpec(ChartSpec newSpec) {
    this.spec = newSpec;
  }

  @Override
  public JFreeChart createChart(FCSFrame dataFrame, TransformSet transforms) {

    Optional<FCSDimension> domainDimension =
        FCSUtilities.findCompatibleDimension(dataFrame, spec.getDomainAxisName());

    AbstractTransform transform = transforms.get(domainDimension.get().getShortName());
    double[] transformedData = transform.transform(domainDimension.get().getData());

    Histogram1D hist = new Histogram1D(transformedData, transform.getMinTranformedValue(),
        transform.getMaxTransformedValue(), ChartingDefaults.BIN_COUNT);

    DefaultXYDataset dataset = new DefaultXYDataset();
    dataset.addSeries(dataFrame.getDisplayName(), hist.getData());

    ValueAxis domainAxis = PlotUtils.createAxis(domainDimension.get().getDisplayName(), transform);
    ValueAxis rangeAxis = new NumberAxis(spec.getRangeAxisName());
    FillType fillType = FillType.TO_ZERO;
    XYItemRenderer renderer = new XYSplineRenderer(1, fillType);
    renderer.setSeriesShape(0, ShapeUtilities.createDiamond(Float.MIN_VALUE));// Make the points
                                                                              // invisible
    XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
    return new JFreeChart(plot);
  }
}
