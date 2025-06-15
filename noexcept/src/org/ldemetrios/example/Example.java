package org.ldemetrios.example;

import org.ldemetrios.noexcept.NoExcept;

import java.util.List;

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
        List<Point> points = List.of(
                new Point(1, 2),
                new Point(3, 4)
        );
        System.out.println(points.stream().sorted().toList());
    }

    static record Point(int x, int y) implements Comparable<Point> {
        @Override
        @NoExcept
        public int compareTo(Point point) {
            throw new RuntimeException("Failure");
        }
    }
}
