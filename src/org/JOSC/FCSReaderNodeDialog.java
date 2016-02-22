package org.JOSC;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "FCSReader" Node.
 * It will do stuff	
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Landysh
 */
public class FCSReaderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring FCSReader node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected FCSReaderNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentFileChooser(
                new SettingsModelString(
                    FCSReaderNodeModel.CFGKEY_FileLocation,
                    FCSReaderNodeModel.DEFAULT_FileLocation),
                "foo",
                "fcs"                	
        		));
                    
    }
}

