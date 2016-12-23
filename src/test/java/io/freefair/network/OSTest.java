package io.freefair.network;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by larsgrefer on 22.12.16.
 */
public class OSTest {


    @Test
    public void isMac() throws Exception {
        String property = System.getProperty("os.name");

        System.out.println(property);
    }

}