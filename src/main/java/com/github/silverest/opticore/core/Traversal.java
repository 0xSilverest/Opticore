package com.github.silverest.opticore.core;

import java.util.Collection;
import java.util.function.Function;

public class Traversal<S extends Collection<?>, A> {
    final private Function<S, Collection<A>> view;

    private Traversal(Function<S, Collection<A>> view) {
        this.view = view;
    }

    public static <S extends Collection<?>, A> Traversal<S, A> of(Function<S, Collection<A>> view) {
        return new Traversal<>(view);
    }

    // toListOf :: Traversal' s a -> s -> [a]
    // toListOf : converts a value of type s to a list of values of type a
    public Collection<A> toListOf(S s) {
        return view.apply(s);
    }

}
