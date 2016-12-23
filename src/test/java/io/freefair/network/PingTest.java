package io.freefair.network;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by larsgrefer on 21.12.16.
 */
public class PingTest {

    @Test
    public void testGoogleDns() throws Exception {
        PingResult execute = new Ping()
                .setCount(4)
                .setHost("8.8.8.8")
                .execute();

        assertEquals(4, execute.getReceivedPackets());
        assertEquals(4, execute.getTransmittedPackets());
        assertEquals(execute.getPacketLoss(), 1-(execute.getReceivedPackets()/execute.getTransmittedPackets()), 0.00001d);
    }

    @Test
    public void testFail() throws IOException {
        PingResult execute = new Ping()
                .setHost("1.2.3.4")
                .setCount(4)
                .execute();

        assertEquals(1, execute.getPacketLoss(), 0.00001d);
        assertEquals(4, execute.getTransmittedPackets());
        assertEquals(0, execute.getReceivedPackets());
    }

}