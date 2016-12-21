package io.freefair.network;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by larsgrefer on 21.12.16.
 */
public class PingTest {

    Ping ping;

    @Before
    public void setUp() throws Exception {
        ping = Ping.builder()
                .count(4)
                .host("8.8.8.8")
                .build();
    }

    @Test
    public void execute() throws Exception {
        PingResult execute = ping.execute();

        assertEquals(execute.getPacketLoss(), execute.getReceivedPackets()/execute.getTransmittedPackets(), 0.00001d);
    }

}