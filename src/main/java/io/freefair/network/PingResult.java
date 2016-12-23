package io.freefair.network;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

@Data
public class PingResult {

    private String host;
    private String ip;

    private List<Response> responses = new LinkedList<>();

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

    @Data
    @Builder
    public static class Response {
        private int bytes;
        private String host;
        private int icmpSeq;
        private int ttl;
        private Duration time;
    }
}
