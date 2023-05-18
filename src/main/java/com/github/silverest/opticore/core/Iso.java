package com.github.silverest.opticore.core;

import java.util.function.Function;

public class Iso<S, A> {
    private final Function<S, A> view;
    private final Function<A, S> review;

    private Iso(Function<S, A> view, Function<A, S> review) {
        this.view = view;
        this.review = review;
    }

    public static <S, A> Iso<S, A> of(Function<S, A> view, Function<A, S> review) {
        return new Iso<>(view, review);
    }

    // view :: Iso' s a -> s -> a
    // view : converts a value of type s to a value of type a
    public A view(S s) {
        return view.apply(s);
    }

    // review :: Iso' s a -> a -> s
    // review : converts a value back from type a to a value of type s
    public S review(A a) {
        return review.apply(a);
    }

    // over :: Iso' s a -> (a -> a) -> s -> s
    // over : maps a function over the value of type a
    public A over(Function<A, A> f, S s) {
        return f.apply(view(s));
    }

    // andThen :: Iso' s a -> Iso' a b -> Iso' s b
    // andThen : composes two isomorphisms
    public <B> Iso<S, B> andThen(Iso<A, B> other) {
        return Iso.of(s -> other.view(view(s)), b -> review(other.review(b)));
    }

    // compose :: Iso' a b -> Iso' s a -> Iso' s b
    // compose : composes two isomorphisms in the opposite order
    public <B> Iso<B, A> compose(Iso<B, S> other) {
        return other.andThen(this);
    }

    // reverse :: Iso' s a -> Iso' a s
    // reverse : reverses an isomorphism
    public Iso<A, S> reverse() {
        return Iso.of(review, view);
    }
}
