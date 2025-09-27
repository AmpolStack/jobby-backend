package com.jobby.authorization;

import java.util.Base64;
import java.util.stream.Stream;

public class TestStreams {
    public static Stream<String> blankStringList(){
        return Stream.of("", " ", "  ", "    ", "\r", "\n", "\n\r   ", "    \n\r", "       ");
    }

    public static Stream<String> nonValidBase64stringList(){
        return Stream.of("ab***", "12345$", "!!!!", "abcde%", "1234 56", "abcd=ef", "abcde-f" );
    }
}
