package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import java.io.Closeable;
import java.util.Optional;
import java.util.Scanner;

class StdinReader implements Closeable {

    private final Scanner scanner = new Scanner(System.in);

    public Optional<Integer> readInt() {
        try {
            return Optional.of(Integer.parseInt(scanner.nextLine()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void close() {
        scanner.close();
    }

}
