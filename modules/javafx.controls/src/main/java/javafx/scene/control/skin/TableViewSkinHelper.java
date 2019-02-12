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

    public static void resizeColumnToFitContent( TableViewSkinBase< ?, ?, ?, ?, ? > tableSkin,
        TableColumnBase< ?, ? > tc )
    {
        TableSkinUtils.resizeColumnToFitContent( tableSkin, tc, -1 );
    }

}
