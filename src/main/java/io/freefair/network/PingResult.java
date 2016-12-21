package io.freefair.network;

import lombok.Data;

import java.time.Duration;

@Data
public class PingResult {

    private String host;
    private String ip;

    private int transmittedPackets;
    private int receivedPackets;
    private double packetLoss;
    private Duration time;

    private Duration minRoundTripTime;
    private Duration avgRoundTripTime;
    private Duration maxRoundTripTime;
    private Duration mdevRoundTripTime;

    static PingResult parseOutput(String output) {
        return PingResultBuilder.fromOutput(output);
    }

}
