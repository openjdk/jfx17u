/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Interface that receives notifications of changes to an ObservableSet.
 *
 * @param <E>
 *     the element type
 *
 * @since JavaFX 2.1
 */
@FunctionalInterface
public interface SetComplexChangeListener< E >
{

    /**
     * Called after a change has been made to an ObservableSet.
     * This method is called on every elementary change (add/remove) once.
     * This means, complex changes like removeAll(Collection) or clear()
     * may result in more than one call of onChanged method.
     *
     * @param change
     *     the change that was made
     */
    void onChanged( Change< ? extends E > change );

    /**
     * An elementary change done to an ObservableSet.
     * Change contains information about an add or remove operation.
     * Note that adding element that is already in the set does not
     * modify the set and hence no change will be generated.
     *
     * @param <E>
     *     element type
     *
     * @since JavaFX 2.1
     */
    public abstract static class Change< E >
    {
        private List< SetChangeListener.Change< E > > cachedConversion = null;
        private final ObservableSet< E > set;

        /**
         * Constructs a new Change instance on the given list.
         *
         * @param list
         *     The list that was changed
         */
        public Change( ObservableSet< E > set )
        {
            this.set = set;
        }

        /**
         * The source list of the change.
         *
         * @return a list that was changed
         */
        public ObservableSet< E > getSet()
        {
            return set;
        }

        /**
         * An immutable list of removed/replaced elements. If no elements
         * were removed from the list, an empty list is returned.
         *
         * @return a list with all the removed elements
         *
         * @throws IllegalStateException
         *     if this Change instance is in initial state
         */
        public abstract Set< E > getRemoved();

        /**
         * Indicates if elements were added during this change.
         *
         * @return true if something was added to the list
         *
         * @throws IllegalStateException
         *     if this Change instance is in initial state
         */
        public boolean wasAdded()
        {
            return !getAdded().isEmpty();
        }

        /**
         * Returns a subList view of the list that contains only the elements added. This is actually a shortcut to
         * <code>c.getList().subList(c.getFrom(), c.getTo());</code>
         *
         * <pre>{@code
         * for (Node n : change.getAddedSubList()) {
         *       // do something
         * }
         * }</pre>
         *
         * @return the newly created sublist view that contains all the added elements.
         *
         * @throws IllegalStateException
         *     if this Change instance is in initial state
         */
        public abstract Set< E > getAdded();

        /**
         * Indicates if elements were removed during this change.
         * Note that using set will also produce a change with {@code wasRemoved()} returning
         * true. See {@link #wasReplaced()}.
         *
         * @return true if something was removed from the list
         *
         * @throws IllegalStateException
         *     if this Change instance is in initial state
         */
        public boolean wasRemoved()
        {
            return !getRemoved().isEmpty();
        }

        /**
         * Indicates if elements were replaced during this change.
         * This is usually true when set is called on the list.
         * Set operation will act like remove and add operation at the same time.
         * <p>
         * Usually, it's not necessary to use this method directly.
         * Handling remove operation and then add operation, as in the example in
         * the {@link ListChangeListener.Change} class javadoc, will effectively handle the set operation.
         *
         * @return same as {@code wasAdded() && wasRemoved()}
         *
         * @throws IllegalStateException
         *     if this Change instance is in initial state
         */
        public boolean wasReplaced()
        {
            return wasAdded() && wasRemoved();
        }

        /**
         * Returns the size of {@link #getRemoved()} list.
         *
         * @return the number of removed items
         *
         * @throws IllegalStateException
         *     if this Change instance is in initial state
         */
        public int getRemovedSize()
        {
            return getRemoved().size();
        }

        /**
         * Returns the size of the interval that was added.
         *
         * @return the number of added items
         *
         * @throws IllegalStateException
         *     if this Change instance is in initial state
         */
        public int getAddedSize()
        {
            return getAdded().size();
        }

        public List< SetChangeListener.Change< E > > getAsSingleChanges()
        {
            if( cachedConversion == null )
            {
                final ObservableSet< E > set = getSet();
                final List< SetChangeListener.Change< E > > ret =
                    new ArrayList<>( getRemovedSize() + getAddedSize() );
                for( final E e : getRemoved() )
                {
                    ret.add( new RemoveChange<>( set, e ) );
                }
                for( final E e : getAdded() )
                {
                    ret.add( new AddChange<>( set, e ) );
                }
                cachedConversion = ret;
            }
            return cachedConversion;
        }

        protected class RemoveChange< E > extends SetChangeListener.Change< E >
        {

            private final E removed;

            public RemoveChange( final ObservableSet< E > set, final E removed )
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
                return "RemoveChange{" + "removed=" + removed + '}';
            }
        }

        protected class AddChange< E > extends SetChangeListener.Change< E >
        {

            private final E added;

            public AddChange( final ObservableSet< E > set, final E added )
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
                return "AddChange{" + "added=" + added + '}';
            }
        }
    }
}
