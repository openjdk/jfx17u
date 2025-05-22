package com.sun.glass.ui.gtkmf;

import com.sun.javafx.tk.TKStage;
import javafx.stage.Window;

import java.util.function.Consumer;

/**
 * Default GTK_MF {@link TKStage} configurator that can be configured to be used by
 * {@link com.sun.javafx.tk.quantum.ConfigurableQuantumToolkit} just before {@link TKStage} is created.
 */
public class GtkmfBeforeTkStageCreatedConfigurator implements Consumer< Window >
{
    @Override
    public void accept( Window aWindow )
    {
        GtkmfInitialDisplayNamesHolder instance = GtkmfInitialDisplayNamesHolder.INSTANCE;
        var initialDisplayName = instance.getWindowInitialDisplayName( aWindow );
        instance.setCurrentInitialDisplayName( initialDisplayName );
    }
}
