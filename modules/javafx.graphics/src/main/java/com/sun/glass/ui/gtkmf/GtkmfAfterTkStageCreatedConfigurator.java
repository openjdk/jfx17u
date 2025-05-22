package com.sun.glass.ui.gtkmf;

import com.sun.javafx.tk.TKStage;
import javafx.stage.Window;

import java.util.function.BiConsumer;

/**
 * Default GTK_MF {@link TKStage} configurator that can be configured to be used by
 * {@link com.sun.javafx.tk.quantum.ConfigurableQuantumToolkit} just after {@link TKStage} is created.
 */
public class GtkmfAfterTkStageCreatedConfigurator implements BiConsumer< TKStage, Window >
{
    @Override
    public void accept( TKStage aTKStage, Window aWindow )
    {
        GtkmfInitialDisplayNamesHolder.INSTANCE.setCurrentInitialDisplayName( null );
    }
}
