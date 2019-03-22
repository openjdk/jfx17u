package javafx.collections;

import java.util.Collection;

import com.sun.javafx.collections.SetListenerHelper;
import javafx.beans.InvalidationListener;

/**
 * @author created: pkruszczynski on 22.03.2019 13:54
 * @author last change: $Author: $ on $Date: $
 * @version $Revision: $
 */
public abstract class ObservableSetBase< E > implements ObservableSet< E >
{
    private final SetChangeBuilder< E > changeBuilder = new SetChangeBuilder<>( this );
    private SetListenerHelper< E > listenerHelper;

    protected void fireValueChangedEvent( final SetChangeListener.Change< ? extends E > change )
    {
        SetListenerHelper.fireValueChangedEvent( listenerHelper, change );
    }

    protected void fireValueChangedEvent( final SetComplexChangeListener.Change< ? extends E > change )
    {
        SetListenerHelper.fireValueChangedEvent( listenerHelper, change );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener( InvalidationListener listener )
    {
        listenerHelper = SetListenerHelper.addListener( listenerHelper, listener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener( InvalidationListener listener )
    {
        listenerHelper = SetListenerHelper.removeListener( listenerHelper, listener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener( SetChangeListener< ? super E > observer )
    {
        listenerHelper = SetListenerHelper.addListener( listenerHelper, observer );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener( SetChangeListener< ? super E > observer )
    {
        listenerHelper = SetListenerHelper.removeListener( listenerHelper, observer );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener( final SetComplexChangeListener< ? super E > observer )
    {
        listenerHelper = SetListenerHelper.addListener( listenerHelper, observer );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener( final SetComplexChangeListener< ? super E > observer )
    {
        listenerHelper = SetListenerHelper.removeListener( listenerHelper, observer );
    }

    /**
     * Begins a change block.
     *
     * Must be called before any of the {@code next*} methods is called.
     * For every {@code beginChange()}, there must be a corresponding {@link #endChange() } call.
     * <p>{@code beginChange()} calls can be nested in a {@code beginChange()}/{@code endChange()} block.
     *
     * @see #endChange()
     */
    protected final void beginChange()
    {
        changeBuilder.beginChange();
    }

    /**
     * Ends the change block.
     *
     * If the block is the outer-most block for the {@code ObservableList}, the
     * {@code Change} is constructed and all listeners are notified.
     * <p> Ending a nested block doesn't fire a notification.
     *
     * @see #beginChange()
     */
    protected final void endChange()
    {
        changeBuilder.endChange();
    }

    /**
     * Returns true if there are some listeners registered for this list.
     *
     * @return true if there is a listener for this list
     */
    protected final boolean hasListeners()
    {
        return SetListenerHelper.hasListeners( listenerHelper );
    }

    /**
     * Adds a new remove operation to the change.
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     *
     * @param removed
     *     items that were removed
     */
    protected final void nextRemove( Collection< ? extends E > removed )
    {
        changeBuilder.nextRemove( removed );
    }

    /**
     * Adds a new remove operation to the change.
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     *
     * @param removed
     *     the item that was removed
     */
    protected final void nextRemove( E removed )
    {
        changeBuilder.nextRemove( removed );
    }

    /**
     * Adds a new add operation to the change.
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     *
     * @param added
     *     the item that was added
     */
    protected final void nextAdd( E added )
    {
        changeBuilder.nextAdd( added );
    }

    /**
     * Adds a new add operation to the change.
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     *
     * @param added
     *     items that were added
     */
    protected final void nextAdd( Collection< ? extends E > added )
    {
        changeBuilder.nextAdd( added );
    }

}
