// ******************************************************************
//
// TableViewSkinHelper.java
// Copyright 2019 PSI AG. All rights reserved.
// PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package javafx.scene.control.skin;

import javafx.scene.control.TableColumnBase;
import javafx.scene.layout.Region;

/**
 * @author created: pkruszczynski on 07.02.2019 15:10
 * @author last change: $Author: $ on $Date: $
 * @version $Revision: $
 */
public final class TableViewSkinHelper
{
    private TableViewSkinHelper()
    {
    }

    public static Region getColumnReorderLine( final TableViewSkinBase skin )
    {
        return skin.getColumnReorderLine();
    }

    public static Region getColumnReorderOverlay( final TableViewSkinBase skin )
    {
        return skin.getColumnReorderOverlay();
    }

    public static TableHeaderRow getTableHeaderRow( final TableViewSkinBase skin )
    {
        return skin.getTableHeaderRow();
    }

    /**
     * Resizes column to fit content width.<br/>
     * <strong>Works only for nonempty tables!</strong>
     *
     * @param tableSkin
     *     table skin of column
     * @param tc
     *     column to be resized
     */
    public static void resizeColumnToFitContent( TableViewSkinBase< ?, ?, ?, ?, ? > tableSkin,
        TableColumnBase< ?, ? > tc )
    {
        TableHeaderRow tableHeader = tableSkin.getTableHeaderRow();
        TableColumnHeader columnHeader = tableHeader.getColumnHeaderFor(tc);
        if (columnHeader != null) {
            columnHeader.resizeColumnToFitContent(-1);
        }
    }

    /**
     * Resizes column to fit header width.
     *
     * @param tableSkin
     *     table skin of column
     * @param tc
     *     column to be resized
     */
    public static void resizeColumnToFitHeader( TableViewSkinBase< ?, ?, ?, ?, ? > tableSkin,
        TableColumnBase< ?, ? > tc )
    {
        ExtendedTableSkinUtils.resizeColumnToFitHeader( tableSkin, tc );
    }

}
