package com.sun.javafx.tk.quantum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.sun.javafx.tk.TKDragSourceListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKStage;

import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public final class ConfigurableQuantumToolkit extends QuantumToolkit
{
    public static final boolean configurableQuantumVerbose = AccessController.doPrivileged(
        (PrivilegedAction< Boolean >)() -> Boolean.getBoolean( "configurableQuantum.verbose" ) );

    private final Consumer< Window > beforeTkStageCreatedConfigurator;
    private final BiConsumer< TKStage, Window > afterTkStageCreatedConfigurator;
    private final BiConsumer< TKScene, Dragboard > tkSceneStartDragConfigurator;

    public ConfigurableQuantumToolkit()
    {
        tkSceneStartDragConfigurator = createTkSceneStartDragConfigurator();
        beforeTkStageCreatedConfigurator = createBeforeTkStageCreatedConfigurator();
        afterTkStageCreatedConfigurator = createAfterTkStageCreatedConfigurator();
    }

    @Override
    public void startDrag( TKScene scene, Set< TransferMode > tm, TKDragSourceListener l,
        Dragboard dragboard )
    {
        tkSceneStartDragConfigurator.accept( scene, dragboard );
        super.startDrag( scene, tm, l, dragboard );
    }

    @Override
    public TKStage createTKStage( Window peerWindow, boolean securityDialog, StageStyle stageStyle,
        boolean primary, Modality modality, TKStage owner, boolean rtl, AccessControlContext acc )
    {
        beforeTkStageCreatedConfigurator.accept( peerWindow );
        TKStage tkStage =
            super.createTKStage( peerWindow, securityDialog, stageStyle, primary, modality, owner, rtl, acc );
        afterTkStageCreatedConfigurator.accept( tkStage, peerWindow );
        return tkStage;
    }

    @Override
    public TKStage createTKStage( Window peerWindow, boolean securityDialog, StageStyle stageStyle,
        boolean primary, Modality modality, TKStage owner, boolean rtl, boolean minimizable, boolean closable,
        AccessControlContext acc )
    {
        beforeTkStageCreatedConfigurator.accept( peerWindow );
        TKStage tkStage = super.createTKStage( peerWindow, securityDialog, stageStyle, primary, modality,
            owner, rtl, minimizable, closable, acc );
        afterTkStageCreatedConfigurator.accept( tkStage, peerWindow );
        return tkStage;
    }

    private BiConsumer< TKStage, Window > createAfterTkStageCreatedConfigurator()
    {
        return createConfigurator( "configurableQuantumToolkit.afterTkStageCreatedConfigurator.className" );
    }

    private Consumer< Window > createBeforeTkStageCreatedConfigurator()
    {
        return createConfigurator( "configurableQuantumToolkit.beforeTkStageCreatedConfigurator.className" );
    }

    private BiConsumer< TKScene, Dragboard > createTkSceneStartDragConfigurator()
    {
        return createConfigurator( "configurableQuantumToolkit.tkSceneStartDragConfigurator.className" );
    }

    private < T > T createConfigurator( String configuratorKey )
    {
        if( configurableQuantumVerbose )
        {
            System.out.printf( "creating configurator for key [%s]\n", configuratorKey );
        }
        var classNameFromProperty = getClassNameFromProperty( configuratorKey );
        if( classNameFromProperty.isPresent() )
        {
            return instantiateConfigurator( classNameFromProperty.get() );
        }

        var classNameFromFile = readClassNameFromFile( configuratorKey );
        return instantiateConfigurator( classNameFromFile );
    }

    private static Optional< String > getClassNameFromProperty( String aKey )
    {
        return Optional.ofNullable( System.getProperty( aKey ) )
            .filter( s -> !s.isEmpty() );
    }

    private String readClassNameFromFile( String fileName )
    {
        try (InputStream stream = ConfigurableQuantumToolkit.class.getClassLoader()
            .getResourceAsStream( fileName ))
        {
            if( stream == null )
            {
                throw new IllegalStateException(
                    String.format( "resource not found using fileName=[%s]", fileName ) );
            }
            InputStreamReader inputStreamReader = new InputStreamReader( stream, StandardCharsets.UTF_8 );
            BufferedReader bufferedReader = new BufferedReader( inputStreamReader );
            return bufferedReader.readLine();
        }
        catch( IOException aE )
        {
            throw new IllegalStateException( String.format( "cannot read [%s] configurator file", fileName ),
                aE );
        }
    }

    private < T > T instantiateConfigurator( String className )
    {
        try
        {
            if( configurableQuantumVerbose )
            {
                System.out.printf( "instantiating configurator class [%s]\n", className );
            }
            return (T)Class.forName( className )
                .getDeclaredConstructor()
                .newInstance();
        }
        catch( InstantiationException | IllegalAccessException | InvocationTargetException
            | NoSuchMethodException | ClassNotFoundException | ClassCastException aE )
        {
            throw new IllegalStateException(
                String.format( "cannot instantiate configurator using className=[%s]", className ), aE );
        }
    }
}
