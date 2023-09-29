// ******************************************************************
//
//  Copyright 2023 PSI Software SE. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package com.sun.javafx.stage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import com.sun.javafx.tk.Toolkit;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 * Creates and returns wrapper list on top of provided observable list that checks if current thread is
 * Javafx thread while adding or removing listener.
 *
 * @param <T>
 *     The type of List to be wrapped
 */
public class CheckFxThreadOnListenerModificationObservableList< T > implements ObservableList< T >
{
    private final ObservableList< T > delegate;

    /**
     * Creates wrapped list.
     *
     * @param aDelegate
     *     an ObservableList that is to be wrapped
     */
    public CheckFxThreadOnListenerModificationObservableList( final ObservableList< T > aDelegate )
    {
        delegate = aDelegate;
    }

    @Override
    public void addListener( final ListChangeListener< ? super T > listener )
    {
        Toolkit.getToolkit().checkFxUserThread();
        delegate.addListener( listener );
    }

    @Override
    public void removeListener( final ListChangeListener< ? super T > listener )
    {
        Toolkit.getToolkit().checkFxUserThread();
        delegate.removeListener( listener );
    }

    @Override
    public boolean addAll( final T... elements )
    {
        return delegate.addAll( elements );
    }

    @Override
    public boolean setAll( final T... elements )
    {
        return delegate.setAll( elements );
    }

    @Override
    public boolean setAll( final Collection< ? extends T > col )
    {
        return delegate.setAll( col );
    }

    @Override
    public boolean removeAll( final T... elements )
    {
        return delegate.removeAll( elements );
    }

    @Override
    public boolean retainAll( final T... elements )
    {
        return delegate.retainAll( elements );
    }

    @Override
    public void remove( final int from, final int to )
    {
        delegate.remove( from, to );
    }

    @Override
    public FilteredList< T > filtered( final Predicate< T > predicate )
    {
        return delegate.filtered( predicate );
    }

    @Override
    public SortedList< T > sorted( final Comparator< T > comparator )
    {
        return delegate.sorted( comparator );
    }

    @Override
    public SortedList< T > sorted()
    {
        return delegate.sorted();
    }

    @Override
    public int size()
    {
        return delegate.size();
    }

    @Override
    public boolean isEmpty()
    {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains( final Object o )
    {
        return delegate.contains( o );
    }

    @Override
    public Iterator< T > iterator()
    {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray()
    {
        return delegate.toArray();
    }

    @Override
    public < T1 > T1[] toArray( final T1[] a )
    {
        return delegate.toArray( a );
    }

    @Override
    public boolean add( final T aT )
    {
        return delegate.add( aT );
    }

    @Override
    public boolean remove( final Object o )
    {
        return delegate.remove( o );
    }

    @Override
    public boolean containsAll( final Collection< ? > c )
    {
        return delegate.containsAll( c );
    }

    @Override
    public boolean addAll( final Collection< ? extends T > c )
    {
        return delegate.addAll( c );
    }

    @Override
    public boolean addAll( final int index, final Collection< ? extends T > c )
    {
        return delegate.addAll( index, c );
    }

    @Override
    public boolean removeAll( final Collection< ? > c )
    {
        return delegate.removeAll( c );
    }

    @Override
    public boolean retainAll( final Collection< ? > c )
    {
        return delegate.retainAll( c );
    }

    @Override
    public void replaceAll( final UnaryOperator< T > operator )
    {
        delegate.replaceAll( operator );
    }

    @Override
    public void sort( final Comparator< ? super T > c )
    {
        delegate.sort( c );
    }

    @Override
    public void clear()
    {
        delegate.clear();
    }

    @Override
    public boolean equals( final Object o )
    {
        return delegate.equals( o );
    }

    @Override
    public int hashCode()
    {
        return delegate.hashCode();
    }

    @Override
    public T get( final int index )
    {
        return delegate.get( index );
    }

    @Override
    public T set( final int index, final T element )
    {
        return delegate.set( index, element );
    }

    @Override
    public void add( final int index, final T element )
    {
        delegate.add( index, element );
    }

    @Override
    public T remove( final int index )
    {
        return delegate.remove( index );
    }

    @Override
    public int indexOf( final Object o )
    {
        return delegate.indexOf( o );
    }

    @Override
    public int lastIndexOf( final Object o )
    {
        return delegate.lastIndexOf( o );
    }

    @Override
    public ListIterator< T > listIterator()
    {
        return delegate.listIterator();
    }

    @Override
    public ListIterator< T > listIterator( final int index )
    {
        return delegate.listIterator( index );
    }

    @Override
    public List< T > subList( final int fromIndex, final int toIndex )
    {
        return delegate.subList( fromIndex, toIndex );
    }

    @Override
    public Spliterator< T > spliterator()
    {
        return delegate.spliterator();
    }

    @Override
    public < T1 > T1[] toArray( final IntFunction< T1[] > generator )
    {
        return delegate.toArray( generator );
    }

    @Override
    public boolean removeIf( final Predicate< ? super T > filter )
    {
        return delegate.removeIf( filter );
    }

    @Override
    public Stream< T > stream()
    {
        return delegate.stream();
    }

    @Override
    public Stream< T > parallelStream()
    {
        return delegate.parallelStream();
    }

    @Override
    public void forEach( final Consumer< ? super T > action )
    {
        delegate.forEach( action );
    }

    @Override
    public void addListener( final InvalidationListener listener )
    {
        Toolkit.getToolkit().checkFxUserThread();
        delegate.addListener( listener );
    }

    @Override
    public void removeListener( final InvalidationListener listener )
    {
        Toolkit.getToolkit().checkFxUserThread();
        delegate.removeListener( listener );
    }
}
