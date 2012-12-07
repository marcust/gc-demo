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

import java.util.Random;

/**
 * @author Marcus Thiesen (marcus.thiesen@freiheit.com) (initial creation)
 */
public class RandomUtil {

    private final static Random RANDOM = new Random();
    
    public static void fill( byte[] buffer ) {
        RANDOM.nextBytes( buffer );
    }

    public static int nextInt( int i ) {
        return RANDOM.nextInt( i );
    }

}
