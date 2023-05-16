package com.github.silverest.opticore.core;

import java.util.function.BiFunction;
import java.util.function.Function;

// Lens a b : represents a functional reference to a part B
//            within a larger structure A. It allows you to
//            focus on a specific field or property of type
//            B within an object of type A and perform various
//            operations on it.
public class Lens<A, B> {
  private final Function<A, B> getter;
  private final Function<B, Function<A, A>> setter;

  private Lens(Function<A, B> getter, Function<B, Function<A, A>> setter) {
    this.getter = getter;
    this.setter = setter;
  }

  public static <A, B> Lens<A, B> of(
      final Function<A, B> getter, final Function<B, Function<A, A>> setter) {
    return new Lens<>(getter, setter);
  }

  public static <A, B> Lens<A, B> of(
      final Function<A, B> getter, final BiFunction<B, A, A> setter) {
    return new Lens<>(getter, b -> a -> setter.apply(b, a));
  }

  // get :: Lens a b -> a -> b
  // Get: Gets the field of the lens.
  public B get(final A a) {
    return getter.apply(a);
  }

  // set :: Lens a b -> b -> a -> a
  // Set: Sets the value of the lens to the given value and returns a new object.
  public A set(final B b, final A a) {
    return setter.apply(b).apply(a);
  }

  // modify :: Lens a b -> (b -> b) -> a -> a
  // modify: Modifies the field focused on by the lens using
  //         the given function and returns a new object.
  public A modify(final Function<B, B> f, A a) {
    return set(f.apply(get(a)), a);
  }


  // Copy: does as the JVM copy but instead
  //       of modifying through the address, it
  //       returns a new object with the modified
  //       value.
  @SafeVarargs
  public static <A> A copy(A source, Function<A, A>... modifiers) {
    A result = source;
    for (Function<A, A> modifier : modifiers) {
      result = modifier.apply(result);
    }
    return result;
  }

  // andThen :: Lens a b -> Lens b c -> Lens a c
  // AndThen: Composes two lenses to create a new Lens
  //          that focuses from A to C.
  // Example usage:
  // record Person(String name, int age, Address address) {}
  // record Address(String street, String city) {}
  // Lens<Person, String> nameLens = Lens.of(Person::name, (name, person) -> person.withName(name));
  // Lens<Address, String> cityLens = Lens.of(Address::city, (city, address) ->
  // address.withCity(city));
  //
  // Lens<Person, String> personCityLens = nameLens.andThen(cityLens);
  // Person person = new Person("John", 30, new Address("Main Street", "New York"));
  //
  // Get the city of the person's address after accessing the name
  // String city = personCityLens.get(person);
  // System.out.println(city);
  // Output: New York
  //
  // Set a new city for the person's address after accessing the name
  // Person modifiedPerson = personCityLens.set("Los Angeles", person);
  // System.out.println(modifiedPerson);
  // Output: Person[name=John, age=30, address=Address[street=Main Street, city=Los Angeles]]
  public <C> Lens<A, C> andThen(final Lens<B, C> other) {
    return Lens.of(a -> other.get(get(a)), c -> a -> modify(b -> other.set(c, b), a));
  }

  // compose :: Lens a b -> Lens c a -> Lens c b
  // Compose: Composes two lenses to create a new Lens
  //          that focuses from C to B.
  // Example usage:
  // record Person(String name, int age, Address address) {}
  // record Address(String street, String city) {}
  // Lens<Person, String> nameLens = Lens.of(Person::name, (name, person) -> person.withName(name));
  // Lens<Address, String> cityLens = Lens.of(Address::city, (city, address) -> address.withCity(city));
  //
  // Person person = new Person("John", 30, new Address("Main Street", "New York"));
  //
  // Get the city of the person's address
  // String city = personCityLens.get(person);
  // System.out.println(city);
  // Output: New York
  //
  // Set a new city for the person's address
  // Person modifiedPerson = personCityLens.set("Los Angeles", person);
  // System.out.println(modifiedPerson);
  // Output: Person[name=John, age=30, address=Address[street=Main Street, city=Los Angeles]]
  public <C> Lens<C, B> compose(final Lens<C, A> other) {
    return Lens.of(c -> get(other.get(c)), b -> c -> other.modify(a -> set(b, a), c));
  }
}
