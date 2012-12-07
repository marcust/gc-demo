/*
 * $Id$
 * (c) Copyright 2012 freiheit.com technologies GmbH
 *
 * Created on 07.12.2012 by Marcus Thiesen (marcus.thiesen@freiheit.com)
 *
 * This file contains unpublished, proprietary trade secret information of
 * freiheit.com technologies GmbH. Use, transcription, duplication and
 * modification are strictly prohibited without prior written consent of
 * freiheit.com technologies GmbH.
 */
package org.thiesen.demo;

/**
 * @author Marcus Thiesen (marcus.thiesen@freiheit.com) (initial creation)
 */
public class SizedObject {

    private final Object[] _buffer;
    
    private SizedObject( int size ) {
        _buffer = new Object[ size ];
        for ( int i = 0; i < _buffer.length; i++ ) {
            _buffer[i] = new Object();
        }
    
    }
    
    public int accessAllFields() {
        int size = 0;
        
        for ( int i = 0; i < _buffer.length; i++ ) {
            size += _buffer[i].hashCode();
        }
        
        return size;
    }
    
    public static SizedObject createSmall() {
        return new SizedObject( 10 + RandomUtil.nextInt( 90 ) );
    }
    
    public static SizedObject createBig() {
        return new SizedObject( 100 + RandomUtil.nextInt( 900 ) );
    }
    
}
