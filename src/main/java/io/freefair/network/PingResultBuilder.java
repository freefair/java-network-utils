package io.freefair.network;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

class PingResultBuilder {

    private static final BiFunction<PingResult, Matcher, PingResult> roundTripTimeParser = (result, matcher) -> {
        double minRTT = Double.parseDouble(matcher.group(1));
        double avgRTT = Double.parseDouble(matcher.group(2));
        double maxRTT = Double.parseDouble(matcher.group(3));
        double mdevRTT = Double.parseDouble(matcher.group(4));

        result.setMinRoundTripTime(Duration.ofNanos((long) (1000 * 1000 * minRTT)));
        result.setAvgRoundTripTime(Duration.ofNanos((long) (1000 * 1000 * avgRTT)));
        result.setMaxRoundTripTime(Duration.ofNanos((long) (1000 * 1000 * maxRTT)));
        result.setMdevRoundTripTime(Duration.ofNanos((long) (1000 * 1000 * mdevRTT)));
        return result;
    };

    private static final List<ResultParser<PingResult>> resultParsers = Arrays.asList(

            /* GNU Ping (Debian, etc.) */
            ResultParser.of("PING (.*) \\((.*?)\\)", (result, matcher) -> {
                result.setHost(matcher.group(1));
                result.setIp(matcher.group(2));
                return result;
            }),
            ResultParser.of("(.*) packets transmitted, (.*) received, (.*)% packet loss, time (.*)ms", (result, matcher) -> {
                int transmittedPackets = Integer.parseInt(matcher.group(1));
                result.setTransmittedPackets(transmittedPackets);

                int receivedPackets = Integer.parseInt(matcher.group(2));
                result.setReceivedPackets(receivedPackets);

                double packetLoss = 0.01 * Integer.valueOf(matcher.group(3));
                result.setPacketLoss(packetLoss);

                Duration time = Duration.of(Long.parseLong(matcher.group(4)), ChronoUnit.MILLIS);
                result.setTime(time);

                return result;
            }),
            ResultParser.of("rtt min\\/avg\\/max\\/mdev = (.*)\\/(.*)\\/(.*)\\/(.*) ms", roundTripTimeParser),

            /*BSD Ping (MacOS)*/
            ResultParser.of("(.*) packets transmitted, (.*) packets received, (.*)% packet loss", (result, matcher) -> {
                int transmittedPackets = Integer.parseInt(matcher.group(1));
                result.setTransmittedPackets(transmittedPackets);

                int receivedPackets = Integer.parseInt(matcher.group(2));
                result.setReceivedPackets(receivedPackets);
                double packetLoss = 0.01 * Double.parseDouble(matcher.group(3));
                result.setPacketLoss(packetLoss);

                return result;
            }),
            ResultParser.of("round-trip min\\/avg\\/max\\/stddev = (.*)\\/(.*)\\/(.*)\\/(.*) ms", roundTripTimeParser),
            ResultParser.of("(.*) bytes from (.*): icmp_seq=(.*) ttl=(.*) time=(.*) ms", (result, matcher) -> {
                int bytes = Integer.parseInt(matcher.group(1));
                String host = matcher.group(2);
                int icmpSeq = Integer.parseInt(matcher.group(3));
                int ttl = Integer.parseInt(matcher.group(4));
                Duration time = Duration.ofNanos((long) (1000 * 1000 * Double.parseDouble(matcher.group(5))));

                PingResult.Response response = PingResult.Response.builder()
                        .bytes(bytes)
                        .host(host)
                        .icmpSeq(icmpSeq)
                        .ttl(ttl)
                        .time(time)
                        .build();

                result.getResponses().add(response);

                return result;
            })
    );

    static PingResult fromOutput(String output) {
        PingResult pingResult = new PingResult();

        resultParsers.forEach(rp -> rp.fill(pingResult, output));

        return pingResult;
    }

}
