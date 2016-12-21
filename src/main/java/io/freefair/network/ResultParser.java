package io.freefair.network;

import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;

import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor(staticName = "of")
public class ResultParser<T> {

    private final Pattern pattern;
    private final BiFunction<T, Matcher, T> reader;

    public static <X> ResultParser<X> of(@Language("RegExp") String regex, BiFunction<X, Matcher, X> reader) {
        return of(Pattern.compile(regex), reader);
    }

    public T fill(T result, String output) {
        Matcher matcher = pattern.matcher(output);
        if(matcher.find()) {
            return reader.apply(result, matcher);
        } else {
            return result;
        }
    }

}
