package io.freefair.network;

import lombok.extern.slf4j.Slf4j;
import okio.BufferedSource;
import okio.Okio;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Lars Grefer
 */
@Slf4j
public class Command {

    static String execute(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);

        BufferedSource processOut = Okio.buffer(Okio.source(process.getInputStream()));

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            log.error("Error waiting vor Process", e);
        }

        return processOut.readString(Charset.defaultCharset());
    }
}
