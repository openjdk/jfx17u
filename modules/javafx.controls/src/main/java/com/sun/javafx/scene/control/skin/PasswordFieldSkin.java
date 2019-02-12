// ******************************************************************
//
// PasswordFieldSkin.java
// Copyright 2019 PSI AG. All rights reserved.
// PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.PasswordFieldBehavior;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;

/**
 * @author created: pkruszczynski on 31.01.2019 15:22
 * @author last change: $Author: $ on $Date: $
 * @version $Revision: $
 */
public class PasswordFieldSkin extends TextFieldSkin
{
    /**
     * Creates a new TextFieldSkin instance, installing the necessary child
     * nodes into the Control {@link Control#getChildren() children} list, as
     * well as the necessary input mappings for handling key, mouse, etc events.
     *
     * @param control
     *     The control that this skin should be installed onto.
     */
    public PasswordFieldSkin( final PasswordField control )
    {
        super( control );
    }

    @Override
    protected TextFieldBehavior createBehavior( final TextField control )
    {
        return new PasswordFieldBehavior( (PasswordField)control );
    }
}
