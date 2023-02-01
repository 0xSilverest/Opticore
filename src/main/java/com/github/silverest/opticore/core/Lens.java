package com.github.silverest.opticore.core;

import java.nio.file.Files;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Lens <A, B> {
    private Function<A, B> getter;
    private Function<B, Function<A, A>> setter;

    private Lens(Function<A, B> getter, Function<B, Function<A, A>> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public static <A, B> Lens<A, B> of(final Function<A, B> getter, final Function<B, Function<A, A>> setter) {
        return new Lens<>(getter, setter);
    }

    public static <A, B> Lens<A, B> of(final Function<A, B> getter, final BiFunction<B, A, A> setter) {
        return new Lens<>(getter, b -> a -> setter.apply(b, a));
    }

    public B get(final A a) {
        return getter.apply(a);
    }

    public A set(final B b, final A a) {
        return setter.apply(b).apply(a);
    }

    public A modify(final Function<B, B> f, A a) {
        return set(f.apply(get(a)), a);
    }

    // andThen :: Lens a b -> Lens b c -> Lens a c
    public <C> Lens<A, C> andThen(final Lens<B, C> other) {
        return Lens.of(
                a -> other.get(get(a)),
                c -> a -> modify(b -> other.set(c, b), a)
        );
    }

    // compose :: Lens a b -> Lens c a -> Lens c b
    public <C> Lens<C, B> compose(final Lens<C, A> other) {
        return Lens.of(
                c -> get(other.get(c)),
                b -> c -> other.modify(a -> set(b, a), c)
        );
    }
}
