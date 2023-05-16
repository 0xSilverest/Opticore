package com.github.silverest.opticore.core;

import com.github.silverest.opticore.core.utils.Either;

import java.util.Optional;
import java.util.function.Function;

// Prism' s a : A Prism<S, A> represents a partial isomorphism
//              between two types S and A, where S is the source
//              type and A is the target type. It provides a way
//              to view or interpret a value of type S as a value
//              of type A, and also a way to construct a value of
//              type S from a value of type A.
public class Prism<S, A> {
  private final Function<S, Optional<A>> preview;
  private final Function<A, S> review;

  private Prism(Function<S, Optional<A>> preview, Function<A, S> review) {
    this.preview = preview;
    this.review = review;
  }

  public static <S, A> Prism<S, A> of(Function<S, Optional<A>> preview, Function<A, S> review) {
    return new Prism<>(preview, review);
  }

  // preview :: Prism' s a -> s -> Maybe a
  // preview : attempts to extract a value of type a from the input value,
  //           if it succeeds, it returns the value wrapped in an Optional,
  //           otherwise it returns an empty Optional.
  public Optional<A> preview(S s) {
    return preview.apply(s);
  }

  // review :: Prism' s a -> a -> s
  // review : constructs a value of type s from a value of type a
  public S review(A a) {
    return review.apply(a);
  }

  // matching :: Prism s t a b -> s -> Either t a
  // matching : attempts to match the input value against the prism,
  //            if it matches, it returns the value wrapped as the Right,
  //            otherwise it returns the original input value wrapped as
  //            the Left.
  public Either<S, A> matching(S s) {
    return preview(s).map(Either::<S, A>right).orElseGet(() -> Either.left(s));
  }
}