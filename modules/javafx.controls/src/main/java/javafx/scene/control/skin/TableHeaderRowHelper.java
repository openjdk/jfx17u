// ******************************************************************
//
// TableViewSkinHelper.java
// Copyright 2019 PSI AG. All rights reserved.
// PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package javafx.scene.control.skin;

import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.Region;

/**
 * @author created: pkruszczynski on 07.02.2019 15:10
 * @author last change: $Author: $ on $Date: $
 * @version $Revision: $
 */
public final class TableHeaderRowHelper
{
    private TableHeaderRowHelper()
    {
    }

    public static BooleanProperty getColumnReorderLine( final TableHeaderRow aTableHeaderRow )
    {
        return aTableHeaderRow.reorderingProperty();
    }

    public static Region getDragHeader( final TableHeaderRow aTableHeaderRow )
    {
        return aTableHeaderRow.getDragHeader();
    }

}
