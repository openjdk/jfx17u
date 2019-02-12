// ******************************************************************
//
// ComboBoxHelper.java
// Copyright 2019 PSI AG. All rights reserved.
// PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package javafx.scene.control.skin;

import javafx.scene.control.ListView;

/**
 * @author created: pkruszczynski on 31.01.2019 15:07
 * @author last change: $Author: $ on $Date: $
 * @version $Revision: $
 */
public final class ComboBoxHelper
{

    private ComboBoxHelper()
    {
    }

    public static ListView< ? > getListView( final ComboBoxListViewSkin< ? > aSkin )
    {
        return aSkin.getListView();
    }

}
