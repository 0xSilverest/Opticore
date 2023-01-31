package com.github.silverest.opticore;

import com.github.silverest.opticore.core.Lens;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LensTest {

    private record Person(String name, int age) {
    }

    @Test
    public void setThenGet() {
        Lens<Person, String> nameLens = Lens.of(Person::name, name -> person -> new Person(name, person.age()));
        Person person = new Person("John", 30);
        assertEquals("Jane", nameLens.get(nameLens.set("Jane", person)));
    }

    @Test
    public void getThenSet() {
        Lens<Person, String> nameLens = Lens.of(Person::name, name -> person -> new Person(name, person.age()));
        Person person = new Person("John", 30);
        assertEquals(person, nameLens.set(nameLens.get(person), person));
    }

    @Test
    public void setThenSet() {
        Lens<Person, String> nameLens = Lens.of(Person::name, name -> person -> new Person(name, person.age()));
        Person person = new Person("John", 30);
        assertEquals("John2", nameLens.get(nameLens.set("John2", nameLens.set("Jane", person))));
    }
}