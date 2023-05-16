package com.github.silverest.opticore.core.utils;

import java.util.Optional;
import java.util.function.Function;

public sealed interface Either<T, U> permits Left, Right {

  Optional<T> getLeft();
  Optional<U> getRight();

  static <T, U> Either<T, U> left(T value) {
    return new Left<>(value);
  }

  static <T, U> Either<T, U> right(U value) {
    return new Right<>(value);
  }

  default boolean isLeft() {
    return this instanceof Left;
  }

  default boolean isRight() {
    return this instanceof Right;
  }

  default <V> V fold(Function<T, V> fa, Function<U, V> fb) {
    switch (this) {
      case Left<T, U> left -> {
        return fa.apply(left.value());
      }
      case Right<T, U> right -> {
        return fb.apply(right.value());
      }
      default -> throw new IllegalStateException("Unexpected value: " + this);
    }
  }

  default <V> Either<T, V> map(Function<U, V> f) {
    return fold(Either::left, x -> Either.right(f.apply(x)));
  }

  default <V> Either<V, U> mapLeft(Function<T, V> f) {
      return fold(x -> Either.left(f.apply(x)), Either::right);
  }
}

