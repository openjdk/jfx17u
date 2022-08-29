// ******************************************************************
//
// ExtendedTableSkinUtils.java
// Copyright 2019 PSI AG. All rights reserved.
// PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package javafx.scene.control.skin;

import java.util.List;

import com.sun.javafx.scene.control.Properties;
import com.sun.javafx.scene.control.TableColumnBaseHelper;
import com.sun.javafx.scene.control.TreeTableViewBackingList;
import com.sun.javafx.scene.control.skin.Utils;

import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Region;
import javafx.util.Callback;

/**
 * @author created: pkruszczynski on 09.08.2019 14:09
 * @author last change: $Author: $ on $Date: $
 * @version $Revision: $
 */
class ExtendedTableSkinUtils
{

    @SuppressWarnings( "unchecked" )
    public static void resizeColumnToFitHeader( TableViewSkinBase< ?, ?, ?, ?, ? > tableSkin,
        TableColumnBase< ?, ? > tc )
    {
        if( !tc.isResizable() )
        {
            return;
        }

        Object control = tableSkin.getSkinnable();
        if( control instanceof TableView )
        {
            resizeColumnToFitHeader( (TableView)control, (TableColumn)tc, tableSkin );
        }
        else if( control instanceof TreeTableView )
        {
            resizeColumnToFitHeader( (TreeTableView)control, (TreeTableColumn)tc, tableSkin );
        }
    }

    private static < T, S > void resizeColumnToFitHeader( TableView< T > tv, TableColumn< T, S > tc,
        TableViewSkinBase tableSkin )
    {
        if( !tc.isResizable() )
        {
            return;
        }

        double maxWidth = getHeaderWidth( tc, tableSkin );

        if( tv.getColumnResizePolicy() == TableView.CONSTRAINED_RESIZE_POLICY && tv.getWidth() > 0 )
        {

            if( maxWidth > tc.getMaxWidth() )
            {
                maxWidth = tc.getMaxWidth();
            }

            int size = tc.getColumns()
                .size();
            if( size > 0 )
            {
                resizeColumnToFitHeader( tableSkin, tc.getColumns()
                    .get( size - 1 ) );
                return;
            }

            TableSkinUtils.resizeColumn( tableSkin, tc, Math.round( maxWidth - tc.getWidth() ) );
        }
        else
        {
            TableColumnBaseHelper.setWidth( tc, maxWidth );
        }
    }

    private static < T, S > void resizeColumnToFitHeader( TreeTableView< T > ttv, TreeTableColumn< T, S > tc,
        TableViewSkinBase tableSkin )
    {
        if( !tc.isResizable() )
        {
            return;
        }

        double maxWidth = getHeaderWidth( tc, tableSkin );

        if( ttv.getColumnResizePolicy() == TreeTableView.CONSTRAINED_RESIZE_POLICY && ttv.getWidth() > 0 )
        {

            if( maxWidth > tc.getMaxWidth() )
            {
                maxWidth = tc.getMaxWidth();
            }

            int size = tc.getColumns()
                .size();
            if( size > 0 )
            {
                resizeColumnToFitHeader( tableSkin, tc.getColumns()
                    .get( size - 1 ) );
                return;
            }

            TableSkinUtils.resizeColumn( tableSkin, tc, Math.round( maxWidth - tc.getWidth() ) );
        }
        else
        {
            TableColumnBaseHelper.setWidth( tc, maxWidth );
        }
    }

    private static < T, S > double getHeaderWidth( final TableColumn< T, S > tc, final TableViewSkinBase tableSkin )
    {
        TableColumnHeader header = tableSkin.getTableHeaderRow()
            .getColumnHeaderFor( tc );
        double headerTextWidth = Utils.computeTextWidth( header.label.getFont(), tc.getText(), -1 );
        Node graphic = header.label.getGraphic();
        double headerGraphicWidth =
            graphic == null ? 0 : graphic.prefWidth( -1 ) + header.label.getGraphicTextGap();
        double headerWidth = headerTextWidth + headerGraphicWidth + 10 + header.snappedLeftInset()
            + header.snappedRightInset();
        return headerWidth;
    }

    private static < T, S > double getHeaderWidth( final TreeTableColumn< T, S > tc,
        final TableViewSkinBase tableSkin )
    {
        TableColumnHeader header = tableSkin.getTableHeaderRow()
            .getColumnHeaderFor( tc );
        double headerTextWidth = Utils.computeTextWidth( header.label.getFont(), tc.getText(), -1 );
        Node graphic = header.label.getGraphic();
        double headerGraphicWidth =
            graphic == null ? 0 : graphic.prefWidth( -1 ) + header.label.getGraphicTextGap();
        return headerTextWidth + headerGraphicWidth + 10 + header.snappedLeftInset()
            + header.snappedRightInset();
    }

    @SuppressWarnings( "unchecked" )
    public static void resizeColumnToFitVisibleContent( TableViewSkinBase< ?, ?, ?, ?, ? > tableSkin,
        TableColumnBase< ?, ? > tc, int maxRows )
    {
        if( !tc.isResizable() )
        {
            return;
        }

        int startRow = tableSkin.getVirtualFlow().getFirstVisibleCellWithinViewport() != null
                ? tableSkin.getVirtualFlow().getFirstVisibleCellWithinViewport().getIndex()
                : 0;

        if ( maxRows < 0 )
        {
            maxRows = tableSkin.getVirtualFlow().getLastVisibleCellWithinViewport() != null
                    ? tableSkin.getVirtualFlow().getLastVisibleCellWithinViewport().getIndex() - startRow
                    : -1;
        }

        Object control = tableSkin.getSkinnable();
        if( control instanceof TableView )
        {
            resizeColumnToFitContent( (TableView)control, (TableColumn)tc, tableSkin, startRow, maxRows );
        }
        else if( control instanceof TreeTableView )
        {
            resizeColumnToFitContent( (TreeTableView)control, (TreeTableColumn)tc, tableSkin, startRow,
                maxRows );
        }
    }

    public static < T, S > void resizeColumnToFitContent( TreeTableView< T > ttv, TreeTableColumn< T, S > tc,
        TableViewSkinBase tableSkin, int startRow, int maxRows )
    {
        List< ? > items = new TreeTableViewBackingList( ttv );
        if( items.isEmpty() )
            return;

        Callback cellFactory = tc.getCellFactory();
        if( cellFactory == null )
            return;

        TreeTableCell< T, S > cell = (TreeTableCell)cellFactory.call( tc );
        if( cell == null )
            return;

        // set this property to tell the TableCell we want to know its actual
        // preferred width, not the width of the associated TableColumnBase
        cell.getProperties()
            .put( Properties.DEFER_TO_PARENT_PREF_WIDTH, Boolean.TRUE );

        // determine cell padding
        double padding = 10;
        Node n = cell.getSkin() == null ? null
            : cell.getSkin()
                .getNode();
        if( n instanceof Region )
        {
            Region r = (Region)n;
            padding = r.snappedLeftInset() + r.snappedRightInset();
        }

        TreeTableRow< T > treeTableRow = new TreeTableRow<>();
        treeTableRow.updateTreeTableView( ttv );

        int rows = maxRows == -1 ? items.size() : Math.min( items.size(), startRow + maxRows );
        double maxWidth = 0;
        for( int row = startRow; row < rows; row++ )
        {
            treeTableRow.updateIndex( row );
            treeTableRow.updateTreeItem( ttv.getTreeItem( row ) );

            cell.updateTableColumn( tc );
            cell.updateTreeTableView( ttv );
            cell.updateTableRow( treeTableRow );
            cell.updateIndex( row );

            if( (cell.getText() != null && !cell.getText()
                .isEmpty()) || cell.getGraphic() != null )
            {
                tableSkin.getChildren()
                    .add( cell );
                cell.applyCss();

                double w = cell.prefWidth( -1 );

                maxWidth = Math.max( maxWidth, w );
                tableSkin.getChildren()
                    .remove( cell );
            }
        }

        // dispose of the cell to prevent it retaining listeners (see RT-31015)
        cell.updateIndex( -1 );

        double headerWidth = getHeaderWidth( tc, tableSkin );
        maxWidth = Math.max( maxWidth, headerWidth );

        // RT-23486
        maxWidth += padding;
        if( ttv.getColumnResizePolicy() == TreeTableView.CONSTRAINED_RESIZE_POLICY && ttv.getWidth() > 0 )
        {

            if( maxWidth > tc.getMaxWidth() )
            {
                maxWidth = tc.getMaxWidth();
            }

            int size = tc.getColumns()
                .size();
            if( size > 0 )
            {
                TableColumnHeader columnHeader = tableSkin.getTableHeaderRow()
                    .getColumnHeaderFor( tc.getColumns()
                        .get( size - 1 ) );
                if( columnHeader != null )
                {
                    columnHeader.resizeColumnToFitContent( maxRows );
                }
                return;
            }

            TableSkinUtils.resizeColumn( tableSkin, tc, Math.round( maxWidth - tc.getWidth() ) );
        }
        else
        {
            TableColumnBaseHelper.setWidth( tc, maxWidth );
        }
    }

    public static < T, S > void resizeColumnToFitContent( TableView< T > tv, TableColumn< T, S > tc,
        TableViewSkinBase tableSkin, int startRow, int maxRows )
    {
        double cellMaxWidth = getContentWidth( tv, tc, tableSkin, startRow, maxRows );
        double headerWidth = getHeaderWidth( tc, tableSkin );
        double maxWidth = Math.max( cellMaxWidth, headerWidth );

        if( tv.getColumnResizePolicy() == TableView.CONSTRAINED_RESIZE_POLICY && tv.getWidth() > 0 )
        {
            if( maxWidth > tc.getMaxWidth() )
            {
                maxWidth = tc.getMaxWidth();
            }

            int size = tc.getColumns()
                .size();
            if( size > 0 )
            {
                TableColumnHeader columnHeader = tableSkin.getTableHeaderRow()
                    .getColumnHeaderFor( tc.getColumns()
                        .get( size - 1 ) );
                if( columnHeader != null )
                {
                    columnHeader.resizeColumnToFitContent( maxRows );
                }
                return;
            }

            TableSkinUtils.resizeColumn( tableSkin, tc, Math.round( maxWidth - tc.getWidth() ) );
        }
        else
        {
            TableColumnBaseHelper.setWidth( tc, maxWidth );
        }
    }

    static < T, S > double getContentWidth( final TableView< T > tv, final TableColumn< T, S > tc,
        final TableViewSkinBase tableSkin, final int startRow, final int maxRows )
    {
        List< ? > items = tv.getItems();
        if( items == null || items.isEmpty() )
            return -1;

        Callback/*<TableColumn<T, ?>, TableCell<T,?>>*/ cellFactory = tc.getCellFactory();
        if( cellFactory == null )
            return -1;

        TableCell< T, ? > cell = (TableCell< T, ? >)cellFactory.call( tc );
        if( cell == null )
            return -1;

        // set this property to tell the TableCell we want to know its actual
        // preferred width, not the width of the associated TableColumnBase
        cell.getProperties()
            .put( Properties.DEFER_TO_PARENT_PREF_WIDTH, Boolean.TRUE );

        // determine cell padding
        double padding = 10;
        Node n = cell.getSkin() == null ? null
            : cell.getSkin()
                .getNode();
        if( n instanceof Region )
        {
            Region r = (Region)n;
            padding = r.snappedLeftInset() + r.snappedRightInset();
        }

        int rows = maxRows == -1 ? items.size() : Math.min( items.size(), startRow + maxRows );
        double maxWidth = 0;
        for( int row = startRow; row < rows; row++ )
        {
            cell.updateTableColumn( tc );
            cell.updateTableView( tv );
            cell.updateIndex( row );

            if( (cell.getText() != null && !cell.getText()
                .isEmpty()) || cell.getGraphic() != null )
            {
                tableSkin.getChildren()
                    .add( cell );
                cell.applyCss();
                maxWidth = Math.max( maxWidth, cell.prefWidth( -1 ) );
                tableSkin.getChildren()
                    .remove( cell );
            }
        }
        // RT-23486
        maxWidth += padding;

        // dispose of the cell to prevent it retaining listeners (see RT-31015)
        cell.updateIndex( -1 );
        return maxWidth;
    }

}
