package com.sun.glass.ui.gtkmf;

import com.sun.glass.ui.gtk.GtkmfDnDClipboard;
import com.sun.glass.ui.gtk.GtkmfWindow;
import com.sun.javafx.scene.input.DragboardHelper;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.quantum.WindowStage;
import javafx.scene.input.Dragboard;

import java.util.function.BiConsumer;

import static com.sun.glass.ui.gtk.GtkmfApplication.gtkmfVerbose;
import static com.sun.glass.ui.gtkmf.FieldUtils.readField;

/**
 * Default GTK_MF {@link TKScene} configurator that can be configured to be used by
 * {@link com.sun.javafx.tk.quantum.ConfigurableQuantumToolkit} when start drag is handled.
 */
public class GtkmfTkSceneStartDragConfigurator implements BiConsumer< TKScene, Dragboard >
{
    @Override
    public void accept( TKScene aTKScene, Dragboard aDragboard )
    {
        TKClipboard gc = DragboardHelper.getPeer( aDragboard );
        if( gtkmfVerbose )
        {
            System.out.println( "---------------------------------------------------------------" );
            System.out.println( "modified start drag " );
        }

        WindowStage windowStage = (WindowStage)readField( aTKScene, "stage" );

        GtkmfWindow gtkWindow = (GtkmfWindow)windowStage.getPlatformWindow();
        var displayName = gtkWindow.getDisplayName();

        var systemAssistant = readField( gc, "systemAssistant" );
        if( gtkmfVerbose )
        {
            System.out.println( "systemAssistant: " + systemAssistant );
        }
        GtkmfDnDClipboard clipboard = (GtkmfDnDClipboard)readField( systemAssistant, "clipboard" );
        clipboard.setDisplayName( displayName );
    }
}
