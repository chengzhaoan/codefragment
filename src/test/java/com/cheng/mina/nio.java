package com.cheng.mina;



import org.junit.Test;

import java.nio.channels.Selector;

/**
 * Created by cheng on 2018/6/3.
 */
public class nio  {
    @Test
    public void test1(){

        final int MAXSIZE=1000;
        Selector[] sels = new Selector[ MAXSIZE];
        try{
            for( int i = 0 ;i< MAXSIZE ;++i ) {
                sels[i] = Selector.open();
                Thread.sleep(1000*10);
            }
        }catch( Exception ex ){
            ex.printStackTrace();
        }
    }
}
