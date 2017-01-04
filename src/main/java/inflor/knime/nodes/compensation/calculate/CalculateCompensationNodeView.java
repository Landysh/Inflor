package main.java.inflor.knime.nodes.compensation.calculate;

import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeView</code> for the "CalculateCompensation" Node.
 * This node attempts to construct a compensation matrix automatically using heuristics to estimate sample roles and Theil-Sen estimation to calculate individual spillover values. 
 *
 * @author Aaron Hart
 */
public class CalculateCompensationNodeView extends NodeView<CalculateCompensationNodeModel> {

  // Folder containing FCS Files.
  static final String CFGKEY_PATH = "Path";
  static final String DEFAULT_PATH = "None";

  private final SettingsModelString mPath = new SettingsModelString(CFGKEY_PATH, DEFAULT_PATH);
  
    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link CalculateCompensationNodeModel})
     */
    protected CalculateCompensationNodeView(final CalculateCompensationNodeModel nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        CalculateCompensationNodeModel nodeModel = 
            (CalculateCompensationNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

