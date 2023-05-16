package com.github.silverest.opticore.core.utils;

import java.util.Optional;

public record Left<T, U>(T value) implements Either<T, U> {
    public Optional<T> getLeft() {
        return Optional.of(value);
    }

    public Optional<U> getRight() {
        return Optional.empty();
    }
}
