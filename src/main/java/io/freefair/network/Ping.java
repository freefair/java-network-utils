package io.freefair.network;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Duration;

@SuppressWarnings("WeakerAccess")
@Setter
@Getter
@Slf4j
public class Ping {

    private int count = 4;

    private String host = "8.8.8.8";

    private Duration waittime = Duration.ofSeconds(4);

    private Duration deadline;

    private Short ttl;

    public PingResult execute() throws IOException {

        StringBuilder commandBuilder = new StringBuilder("ping");

        if (OS.isWindows()) {
            commandBuilder.append(" -n ").append(count);
        } else {
            commandBuilder.append(" -c ").append(count);
        }

        if (waittime != null) {
            if (OS.isMac()) {
                commandBuilder.append(" -W ").append(waittime.toMillis());
            } else if (OS.isWindows()) {
                commandBuilder.append(" -w ").append(waittime.toMillis());
            } else {
                commandBuilder.append(" -W ").append(waittime.getSeconds());
            }
        }

        if (deadline != null) {
            if (OS.isMac()) {
                commandBuilder.append(" -t ").append(deadline.getSeconds());
            } else if (OS.isWindows()) {
                log.info("Deadline is not supported on Windows");
            } else {
                commandBuilder.append(" -w ").append(deadline.getSeconds());
            }
        }

        if (ttl != null) {
            if (OS.isMac()) {
                commandBuilder.append(" -m ").append(ttl);
            } else if (OS.isWindows()) {
                commandBuilder.append(" -i ").append(ttl);
            } else {
                commandBuilder.append(" -t ").append(ttl);
            }
        }

        commandBuilder.append(" ").append(host);

        String command = commandBuilder.toString();

        String output = Command.execute(command);

        return PingResult.parseOutput(output);
    }

}
