package com.jobby.authorization;

import java.util.stream.Stream;

public class TestStreams {
    public static Stream<String> blankStringList(){
        return Stream.of("", " ", "  ", "    ", "\r", "\n", "\n\r   ", "    \n\r", "       ");
    }
}
