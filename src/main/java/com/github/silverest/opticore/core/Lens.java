package com.github.silverest.opticore.core;

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

    public static <A, B> Lens<A, B> of(Function<A, B> getter, Function<B, Function<A, A>> setter) {
        return new Lens<>(getter, setter);
    }

    public static <A, B> Lens<A, B> of(Function<A, B> getter, BiFunction<B, A, A> setter) {
        return new Lens<>(getter, b -> a -> setter.apply(b, a));
    }

    public static <A, B> Lens<A, B> of(Function<A, B> getter, BiConsumer<B, A> setter) {
        return new Lens<>(getter, b -> a -> {
            setter.accept(b, a);
            return a;
        });
    }

    public B get(A a) {
        return getter.apply(a);
    }

    public A set(B b, A a) {
        return setter.apply(b).apply(a);
    }

    public A modify(Function<B, B> f, A a) {
        return set(f.apply(get(a)), a);
    }

    public <C> Lens<A, C> compose(Lens<B, C> other) {
        return Lens.of(
                a -> other.getter.apply(getter.apply(a)),
                c -> a -> setter.apply(other.setter.apply(c).apply(getter.apply(a))).apply(a)
        );
    }
}
