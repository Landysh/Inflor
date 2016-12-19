package io.landysh.inflor.main.knime.nodes.fcs.read;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "FCSReader" Node. It will do stuff
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows creation of a simple
 * dialog with standard components. If you need a more complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Aaron Hart
 */
public class ReadFCSNodeDialog extends DefaultNodeSettingsPane {

  /**
   * New pane for configuring FCSReader node dialog.
   */
  public ReadFCSNodeDialog() {
    super();

    addDialogComponent(new DialogComponentFileChooser(
        new SettingsModelString(ReadFCSTableNodeModel.CFGKEY_FileLocation,
            ReadFCSTableNodeModel.DEFAULT_FileLocation),
        "foo", "fcs"));
  }
}