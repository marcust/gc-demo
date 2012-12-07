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

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @author Marcus Thiesen (marcus.thiesen@freiheit.com) (initial creation)
 */
public class LongLivingObjectThread extends Thread {

    private long _creationTime = System.currentTimeMillis();
    private long _timeToLive = TimeUnit.MINUTES.toMillis( 15 );
    
    private LinkedList<SizedObject> _biggerObjets = new LinkedList<SizedObject>();
    
    @Override
    public void run() {
        while ( true ) {
            
            _biggerObjets.add( SizedObject.createBig() );

            if ( _biggerObjets.size() > 100000 ) {
                System.out.println("Clear bigger objects");
                _biggerObjets.clear();
            }
            
            
            try {
                Thread.sleep( 10 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }        

        //System.out.println("Created " + _biggerObjets.size() +  " objects" );
    }

    
    
}
