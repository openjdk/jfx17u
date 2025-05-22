package com.sun.glass.ui.gtk;

import com.sun.glass.events.WindowEvent;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.glass.ui.gtkmf.GtkmfInitialDisplayNamesHolder;

public class GtkmfWindow extends Window {

    private String initialDisplayName;

    private native void _moveWindowToDisplay(long ptr, String aNewDisplayName);

    public void moveWindowToDisplay(String aNewDisplayName) {
        long r = getRealRawHandle();
        if ( GtkmfApplication.gtkmfVerbose ) {
            System.out.println("GtkmfWindow:moveWindowToDisplay: " + r);
        }
        _moveWindowToDisplay(r, aNewDisplayName);
    }

    private native String _getDisplayName(long ptr);

    public String getDisplayName() {
        long r = getRealRawHandle();
        return _getDisplayName(r);
    }

    private long getRealRawHandle() {
        return super.getRawHandle();
    }

    public void setInitialDisplayName( String aInitialDisplayName ){
        if ( GtkmfApplication.gtkmfVerbose ) {
            System.out.println("GtkmfWindow.setInitialDisplayName: " + aInitialDisplayName);
        }
        initialDisplayName = aInitialDisplayName;
    }

    protected native long _createWindowImpl(long ownerPtr, long screenPtr, int mask, String aInitialDisplayName);

    @Override
    protected long _createWindow(long ownerPtr, long screenPtr, int mask){
        if (initialDisplayName == null) {
            initialDisplayName = GtkmfInitialDisplayNamesHolder.INSTANCE.getCurrentInitialDisplayName();
            if ( GtkmfApplication.gtkmfVerbose ) {
                System.out.println("GtkmfWindow._createWindow: explicit initialDisplayName is null, querying toolkit...");
            }
        }
        if ( GtkmfApplication.gtkmfVerbose ) {
            System.out.println("GtkmfWindow._createWindow: initialDisplayName = " + initialDisplayName);
        }
        return _createWindowImpl(ownerPtr, screenPtr, mask, initialDisplayName);
    }

    // ORIGINAL

    public GtkmfWindow(Window owner, Screen screen, int styleMask) {
        super(owner, screen, styleMask);
    }

    protected GtkmfWindow(long parent) {
        super(parent);
    }

    @Override
    protected native long _createChildWindow(long parent);

    @Override
    protected native boolean _close(long ptr);

    @Override
    protected native boolean _setView(long ptr, View view);

    @Override
    protected boolean _setMenubar(long ptr, long menubarPtr) {
        //TODO is it needed?
        return true;
    }

    private native void minimizeImpl(long ptr, boolean minimize);

    private native void maximizeImpl(long ptr, boolean maximize, boolean wasMaximized);

    private native void setBoundsImpl(long ptr, int x, int y, boolean xSet, boolean ySet, int w, int h, int cw, int ch);

    private native void setVisibleImpl(long ptr, boolean visible);

    @Override
    protected native boolean _setResizable(long ptr, boolean resizable);

    @Override
    protected native boolean _requestFocus(long ptr, int event);

    @Override
    protected native void _setFocusable(long ptr, boolean isFocusable);

    @Override
    protected native boolean _grabFocus(long ptr);

    @Override
    protected native void _ungrabFocus(long ptr);

    @Override
    protected native boolean _setTitle(long ptr, String title);

    @Override
    protected native void _setLevel(long ptr, int level);

    @Override
    protected native void _setAlpha(long ptr, float alpha);

    @Override
    protected native boolean _setBackground(long ptr, float r, float g, float b);

    @Override
    protected native void _setEnabled(long ptr, boolean enabled);

    @Override
    protected native boolean _setMinimumSize(long ptr, int width, int height);

    @Override
    protected native boolean _setMaximumSize(long ptr, int width, int height);

    @Override
    protected native void _setIcon(long ptr, Pixels pixels);

    @Override
    protected native void _toFront(long ptr);

    @Override
    protected native void _toBack(long ptr);

    @Override
    protected native void _enterModal(long ptr);

    @Override
    protected native void _enterModalWithWindow(long dialog, long window);

    @Override
    protected native void _exitModal(long ptr);

    protected native long _getNativeWindowImpl(long ptr);

    private native boolean isVisible(long ptr);

    @Override
    protected boolean _setVisible(long ptr, boolean visible) {
        setVisibleImpl(ptr, visible);
        return isVisible(ptr);
    }

    @Override
    protected boolean _minimize(long ptr, boolean minimize) {
        minimizeImpl(ptr, minimize);
        notifyStateChanged(WindowEvent.MINIMIZE);
        return minimize;
    }

    @Override
    protected boolean _maximize(long ptr, boolean maximize,
                                boolean wasMaximized) {
        maximizeImpl(ptr, maximize, wasMaximized);
        notifyStateChanged(WindowEvent.MAXIMIZE);
        return maximize;
    }

    private native void _showOrHideChildren(long ptr, boolean show);

    protected void notifyStateChanged(final int state) {
        if (state == WindowEvent.MINIMIZE) {
            _showOrHideChildren(getNativeHandle(), false);
        } else if (state == WindowEvent.RESTORE) {
            _showOrHideChildren(getNativeHandle(), true);
        }
        switch (state) {
            case WindowEvent.MINIMIZE:
            case WindowEvent.MAXIMIZE:
            case WindowEvent.RESTORE:
                notifyResize(state, getWidth(), getHeight());
                break;
            default:
                System.err.println("Unknown window state: " + state);
                break;
        }
    }

    @Override
    protected void _setCursor(long ptr, Cursor cursor) {
        if (cursor.getType() == Cursor.CURSOR_CUSTOM) {
            _setCustomCursor(ptr, cursor);
        } else {
            _setCursorType(ptr, cursor.getType());
        }
    }

    private native void _setCursorType(long ptr, int type);

    private native void _setCustomCursor(long ptr, Cursor cursor);

    @Override
    protected native int _getEmbeddedX(long ptr);

    @Override
    protected native int _getEmbeddedY(long ptr);

    /**
     * The lowest level (X11) window handle.
     * (Used in prism to create GLContext)
     *
     * @return X11 Window handle is returned.
     */
    @Override
    public long getNativeWindow() {
        return _getNativeWindowImpl(super.getNativeWindow());
    }

    private native void _setGravity(long ptr, float xGravity, float yGravity);

    @Override
    protected void _setBounds(long ptr, int x, int y, boolean xSet, boolean ySet, int w, int h, int cw, int ch, float xGravity, float yGravity) {
        _setGravity(ptr, xGravity, yGravity);
        setBoundsImpl(ptr, x, y, xSet, ySet, w, h, cw, ch);

        if ((w <= 0) && (cw > 0) || (h <= 0) && (ch > 0)) {
            final int[] extarr = new int[4];
            getFrameExtents(ptr, extarr);

            // TODO: ((w <= 0) && (cw <= 0)) || ((h <= 0) && (ch <= 0))
            notifyResize(WindowEvent.RESIZE,
                    ((w <= 0) && (cw > 0)) ? cw + extarr[0] + extarr[1]
                            : w,
                    ((h <= 0) && (ch > 0)) ? ch + extarr[2] + extarr[3]
                            : h);
        }
    }

    private native void getFrameExtents(long ptr, int[] extarr);

    @Override
    protected void _requestInput(long ptr, String text, int type, double width, double height,
                                 double Mxx, double Mxy, double Mxz, double Mxt,
                                 double Myx, double Myy, double Myz, double Myt,
                                 double Mzx, double Mzy, double Mzz, double Mzt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void _releaseInput(long ptr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getRawHandle() {
        long ptr = super.getRawHandle();
        return ptr == 0L ? 0L : _getNativeWindowImpl(ptr);
    }

}
