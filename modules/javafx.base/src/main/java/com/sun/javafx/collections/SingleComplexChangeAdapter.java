package com.sun.javafx.collections;

import java.util.Set;

import javafx.collections.SetChangeListener;
import javafx.collections.SetComplexChangeListener;

/**
 * @author created: pkruszczynski on 22.03.2019 12:56
 * @author last change: $Author: $ on $Date: $
 * @version $Revision: $
 */
public class SingleComplexChangeAdapter< E > extends SetComplexChangeListener.Change< E >
{

    final Set< E > addedItem, removedItem;

    public SingleComplexChangeAdapter( final SetChangeListener.Change< E > change )
    {
        super( change.getSet() );
        if( change.wasAdded() )
        {
            addedItem = Set.of( change.getElementAdded() );
            removedItem = Set.of();
        }
        else
        {
            addedItem = Set.of();
            removedItem = Set.of( change.getElementRemoved() );
        }
    }

    @Override
    public Set< E > getRemoved()
    {
        return removedItem;
    }

    @Override
    public Set< E > getAdded()
    {
        return addedItem;
    }

    @Override
    public String toString()
    {
        return "{ " + "added: " + addedItem + ", removed: " + removedItem + " }";
    }
}
