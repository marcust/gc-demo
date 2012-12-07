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

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.text.StyleContext.SmallAttributeSet;


/**
 * @author Marcus Thiesen (marcus.thiesen@freiheit.com) (initial creation)
 */
public class Main {

    private static class AverageCounter {
        private final long _timeSum;
        private final long _count;
        private static long NEXT_PRINT = System.currentTimeMillis() + 10000;
        
        private AverageCounter( long timeSum, long count ) {
            _timeSum = timeSum;
            _count = count;
        }
        
        public static AverageCounter create() {
            return new AverageCounter( 0, 0 );
        }
      
        public AverageCounter add( final long time ) {
            return new AverageCounter( _timeSum + time, _count + 1 );
        }

        public void printStat() {
            final double average = (double)_timeSum / (double)_count;
            System.out.printf( "%.2f Millis\n", average );
            try {
                final FileOutputStream fos = new FileOutputStream( "/tmp/timings", true );
                fos.write( ("" + average + "\n").getBytes( Charset.defaultCharset() ) );
                fos.close();

            } catch ( final Exception e ) {
                e.printStackTrace();
            }
        }
        
    }
    
    /**
     * @author Marcus Thiesen (marcus.thiesen@freiheit.com) (initial creation)
     */
    private static class DoSmallStuff implements Runnable {

        private static final AtomicReference<AverageCounter> COUNTER = new AtomicReference<Main.AverageCounter>( AverageCounter.create() );
        
        @Override
        public void run() {
            final SizedObject[] children = new SizedObject[ 1000 ];
            
            for ( int i = 0; i < children.length; i++ ) {
                children[i] = SizedObject.createSmall();
            }
            
            final long start = System.currentTimeMillis();
            
            for ( int i = 0; i < children.length; i++ ) {
                children[i].accessAllFields();
            }
            
            final long duration = System.currentTimeMillis() - start;
            
            AverageCounter old = COUNTER.get();
            while ( !COUNTER.compareAndSet( old, old.add( duration ) ) ) {
                old = COUNTER.get();
            }
            
        }

    }

    public static void main( final String... args ) {
        final int numOfSmallObjectWorkers = 6;
        final int numOfLargeObjectWorkers = 2;
        
        for ( int i = 0; i < numOfLargeObjectWorkers; i++ ) {
            new LongLivingObjectThread().start();
        }
        
        final ScheduledExecutorService statPrinter = Executors.newScheduledThreadPool( 1 );
        statPrinter.scheduleAtFixedRate( new Runnable() {

            @Override
            public void run() {
                AverageCounter averageCounter = DoSmallStuff.COUNTER.get();
                averageCounter.printStat();
            }} , 10, 10, TimeUnit.SECONDS );
        
        
        int threadCount = 0; 

        threadCount += numOfSmallObjectWorkers;
        System.out.println("Starte " + numOfSmallObjectWorkers + " mehr threads. Insgesamt: " + threadCount );

        final ScheduledExecutorService smallTaskExecutor = Executors.newScheduledThreadPool( numOfSmallObjectWorkers );
        for ( int i = 0; i < numOfSmallObjectWorkers; i++ ) {
            smallTaskExecutor.scheduleAtFixedRate( new DoSmallStuff(), 200 + RandomUtil.nextInt( 200 ), 200 + RandomUtil.nextInt( 100 ), TimeUnit.MILLISECONDS );
        }

        try {
            Thread.sleep( 5 * 60 * 1000 );
        } catch ( InterruptedException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }
    
}
