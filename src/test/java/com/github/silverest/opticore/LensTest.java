package com.github.silverest.opticore;

import com.github.silverest.opticore.core.Lens;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LensTest {

  private record Person(String name, int age, Address address) {}

  private record Address(String street, int number) {}

  @Test
  public void setThenGet() {
    Lens<Person, String> nameLens =
        Lens.of(Person::name, name -> person -> new Person(name, person.age(), person.address()));
    Person person = new Person("John", 30, new Address("Main Street", 1));
    assertEquals("Jane", nameLens.get(nameLens.set("Jane", person)));
  }

  @Test
  public void getThenSet() {
    Lens<Person, String> nameLens =
        Lens.of(Person::name, name -> person -> new Person(name, person.age(), person.address()));
    Person person = new Person("John", 30, new Address("Main Street", 1));
    assertEquals(person, nameLens.set(nameLens.get(person), person));
  }

  @Test
  public void setThenSet() {
    Lens<Person, String> nameLens =
        Lens.of(Person::name, name -> person -> new Person(name, person.age(), person.address()));
    Person person = new Person("John", 30, new Address("Main Street", 1));
    assertEquals("John2", nameLens.get(nameLens.set("John2", nameLens.set("Jane", person))));
  }

  @Test
  public void andThen() {
    Lens<Person, Address> addressLens =
        Lens.of(
            Person::address, address -> person -> new Person(person.name(), person.age(), address));
    Lens<Address, String> streetLens =
        Lens.of(Address::street, street -> address -> new Address(street, address.number()));

    Person person = new Person("John", 30, new Address("Main Street", 1));
    assertEquals(
        "Main Street 2",
        addressLens
            .andThen(streetLens)
            .get(addressLens.andThen(streetLens).set("Main Street 2", person)));
  }

  @Test
  public void compose() {
    Lens<Person, Address> addressLens =
        Lens.of(
            Person::address, address -> person -> new Person(person.name(), person.age(), address));
    Lens<Address, String> streetLens =
        Lens.of(Address::street, street -> address -> new Address(street, address.number()));

    Person person = new Person("John", 30, new Address("Main Street", 1));
    assertEquals(
        "Main Street 2",
        streetLens
            .compose(addressLens)
            .get(streetLens.compose(addressLens).set("Main Street 2", person)));
  }
}