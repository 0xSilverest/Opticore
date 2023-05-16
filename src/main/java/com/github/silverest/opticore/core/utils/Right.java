package com.github.silverest.opticore.core.utils;

import java.util.Optional;

public record Right<T, U>(U value) implements Either<T, U> {
    public Optional<T> getLeft() {
        return Optional.empty();
    }

    public Optional<U> getRight() {
        return Optional.of(value);
    }
}
