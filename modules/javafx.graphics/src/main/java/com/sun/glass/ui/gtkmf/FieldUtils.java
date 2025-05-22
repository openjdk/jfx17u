package com.sun.glass.ui.gtkmf;

import java.lang.reflect.Field;

class FieldUtils
{
    static Object readField( Object aObject, String aFieldName )
    {
        if( aObject == null )
        {
            return null;
        }
        try
        {
            var field = getField( aObject, aFieldName );
            if( field == null )
            {
                return null;
            }
            field.setAccessible( true );
            return field.get( aObject );
        }
        catch( IllegalAccessException | NoSuchFieldException aE )
        {
            throw new RuntimeException( aE );
        }
    }

    private static Field getField( Object aObject, String aFieldName ) throws NoSuchFieldException
    {
        Class< ? > cls = aObject != null ? aObject.getClass() : null;
        Field field = null;
        while( cls != null && field == null )
        {
            try
            {
                field = cls.getDeclaredField( aFieldName );
            }
            catch( NoSuchFieldException | SecurityException aE )
            {
                field = null;
            }
            cls = cls.getSuperclass();
        }

        return field;
    }
}
