package org.ldemetrios.example;

import org.ldemetrios.noexcept.NoExcept;

public class Example {
    @NoExcept
    public static void riskyMethod() {
        throw new RuntimeException("Failure");
    }

    public static void main(String[] args) {
        new Thread(() -> {
            while(true) {
                // This prevents JVM from shutting down if main fails.
            }
        }).start();
        riskyMethod();
    }
}
