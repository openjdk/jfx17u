package com.sun.glass.ui.gtkmf;

import javafx.stage.Window;

import java.util.WeakHashMap;

/**
 * Singleton that holds information about initial display names of {@link Window}.
 */
public enum GtkmfInitialDisplayNamesHolder
{
    INSTANCE;

    private String currentInitialDisplayName;
    private final WeakHashMap< Window, String > windowToInitialDisplayName = new WeakHashMap<>();

    public String getCurrentInitialDisplayName()
    {
        return currentInitialDisplayName;
    }

    public void setCurrentInitialDisplayName( String aInitialDisplayName )
    {
        currentInitialDisplayName = aInitialDisplayName;
    }

    public void setWindowInitialDisplayName( Window aWindow, String aInitialDisplayName )
    {
        windowToInitialDisplayName.put( aWindow, aInitialDisplayName );
    }

    public String getWindowInitialDisplayName( Window aWindow )
    {
        return windowToInitialDisplayName.get( aWindow );
    }
}
