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

    public A view(S s) {
        return view.apply(s);
    }

    public S review(A a) {
        return review.apply(a);
    }
}
