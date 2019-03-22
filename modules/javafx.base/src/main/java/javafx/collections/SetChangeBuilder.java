/*
 * Copyright (c) 2011, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javafx.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class SetChangeBuilder< E >
{

    private final ObservableSetBase< E > set;
    private int changeLock;
    private Set< E > added, removed;

    SetChangeBuilder( ObservableSetBase< E > set )
    {
        this.set = set;
    }

    private void checkAddRemoveList()
    {
        if( added == null )
        {
            added = new HashSet<>();
        }
        if( removed == null )
        {
            removed = new HashSet<>();
        }
    }

    private void checkState()
    {
        if( changeLock == 0 )
        {
            throw new IllegalStateException( "beginChange was not called on this builder" );
        }
    }

    public void nextRemove( E removed )
    {
        checkState();
        checkAddRemoveList();

        this.removed.add( removed );
    }

    public void nextRemove( Collection< ? extends E > removed )
    {
        checkState();
        checkAddRemoveList();

        this.removed.addAll( removed );
    }

    public void nextAdd( E added )
    {
        checkState();
        checkAddRemoveList();

        this.added.add( added );
    }

    public void nextAdd( Collection< ? extends E > added )
    {
        checkState();
        checkAddRemoveList();

        this.added.addAll( added );
        ;
    }

    private void commit()
    {
        final boolean addedNotEmpty = added != null && !added.isEmpty();
        final boolean removedNotEmpty = removed != null && !removed.isEmpty();
        if( changeLock == 0 && ( addedNotEmpty || removedNotEmpty ) )
        {
            if( addedNotEmpty && removedNotEmpty )
            {
                final ComplexChange< E > c =
                    new ComplexChange<>( set, Set.copyOf( added ), Set.copyOf( removed ) );
                set.fireValueChangedEvent( c );
                added.clear();
                removed.clear();
            }
            else if( addedNotEmpty )
            {
                if( added.size() == 1 )
                {
                    set.fireValueChangedEvent( new SimpleAddChange<>( set, added.iterator().next() ) );
                }
                else
                {
                    final ComplexChange< E > c = new ComplexChange<>( set, Set.copyOf( added ), Set.of() );
                    set.fireValueChangedEvent( c );
                }
                added.clear();
            }
            else
            {
                if( removed.size() == 1 )
                {
                    set.fireValueChangedEvent( new SimpleRemoveChange<>( set, removed.iterator().next() ) );
                }
                else
                {
                    final ComplexChange< E > c = new ComplexChange<>( set, Set.of(), Set.copyOf( removed ) );
                    set.fireValueChangedEvent( c );
                }
                removed.clear();
            }
        }
    }

    public void beginChange()
    {
        changeLock++;
    }

    public void endChange()
    {
        if( changeLock <= 0 )
        {
            throw new IllegalStateException( "Called endChange before beginChange" );
        }
        changeLock--;
        commit();
    }

    private static class ComplexChange< E > extends SetComplexChangeListener.Change< E >
    {

        private final Set< E > added;
        private final Set< E > removed;

        private ComplexChange( ObservableSet< E > set, Set< E > added, Set< E > removed )
        {
            super( set );
            this.added = added;
            this.removed = removed;
        }

        @Override
        public Set< E > getRemoved()
        {
            return removed;
        }

        @Override
        public Set< E > getAdded()
        {
            return added;
        }

        @Override
        public String toString()
        {
            return "{ " + "added: " + added + ", removed: " + removed + " }";
        }

    }

    private static class SimpleAddChange< E > extends SetChangeListener.Change< E >
    {

        private final E added;

        public SimpleAddChange( ObservableSet< E > set, E added )
        {
            super( set );
            this.added = added;
        }

        @Override
        public boolean wasAdded()
        {
            return true;
        }

        @Override
        public boolean wasRemoved()
        {
            return false;
        }

        @Override
        public E getElementAdded()
        {
            return added;
        }

        @Override
        public E getElementRemoved()
        {
            return null;
        }

        @Override
        public String toString()
        {
            return "added " + added;
        }

    }

    private static class SimpleRemoveChange< E > extends SetChangeListener.Change< E >
    {

        private final E removed;

        public SimpleRemoveChange( ObservableSet< E > set, E removed )
        {
            super( set );
            this.removed = removed;
        }

        @Override
        public boolean wasAdded()
        {
            return false;
        }

        @Override
        public boolean wasRemoved()
        {
            return true;
        }

        @Override
        public E getElementAdded()
        {
            return null;
        }

        @Override
        public E getElementRemoved()
        {
            return removed;
        }

        @Override
        public String toString()
        {
            return "removed " + removed;
        }

    }
}
