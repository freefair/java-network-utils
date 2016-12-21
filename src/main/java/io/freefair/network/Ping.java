package io.freefair.network;

import lombok.Builder;
import okio.BufferedSource;
import okio.Okio;

import java.io.IOException;
import java.nio.charset.Charset;

@Builder
public class Ping {

    int count;

    String host;

    public PingResult execute() throws IOException {
        String command = String.format("ping %s -c %d -W 4", host, count);

        Process process = Runtime.getRuntime().exec(command);

        BufferedSource processOut = Okio.buffer(Okio.source(process.getInputStream()));

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String output = processOut.readString(Charset.defaultCharset());

        return PingResult.parseOutput(output);
    }

}
