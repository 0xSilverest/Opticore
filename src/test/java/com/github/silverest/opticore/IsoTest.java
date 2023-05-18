package com.github.silverest.opticore;

import com.github.silverest.opticore.core.Iso;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IsoTest {

  private record Celsius(double value) {}

  private record Fahrenheit(double value) {}

  @Test
  public void celsiusToFahrenheitViewAndReview() {
    Iso<Celsius, Fahrenheit> celsiusFahrenheitIso =
        Iso.of(
            celsius -> new Fahrenheit(celsius.value() * 9 / 5 + 32),
            fahrenheit -> new Celsius((fahrenheit.value() - 32) * 5 / 9));

    Celsius celsius = new Celsius(100);
    Fahrenheit fahrenheit = celsiusFahrenheitIso.view(celsius);

    assertEquals(212, fahrenheit.value(), 0.0001);

    Celsius celsius2 = celsiusFahrenheitIso.review(fahrenheit);

    assertEquals(100, celsius2.value(), 0.0001);
  }

  @Test
  public void celsiusToFahrenheitModify() {
    Iso<Celsius, Fahrenheit> celsiusFahrenheitIso =
        Iso.of(
            celsius -> new Fahrenheit(celsius.value() * 9 / 5 + 32),
            fahrenheit -> new Celsius((fahrenheit.value() - 32) * 5 / 9));

    Celsius celsius = new Celsius(100);
    Fahrenheit fahrenheit = celsiusFahrenheitIso.over(f -> new Fahrenheit(f.value() + 1), celsius);

    assertEquals(213, fahrenheit.value(), 0.0001);
  }

  @Test
  public void isoAndThen() {
    Iso<Celsius, Fahrenheit> celsiusFahrenheitIso =
        Iso.of(
            celsius -> new Fahrenheit(celsius.value() * 9 / 5 + 32),
            fahrenheit -> new Celsius((fahrenheit.value() - 32) * 5 / 9));

    Iso<Fahrenheit, Double> fahrenheitDoubleIso = Iso.of(Fahrenheit::value, Fahrenheit::new);

    Iso<Celsius, Double> celsiusDoubleIso = celsiusFahrenheitIso.andThen(fahrenheitDoubleIso);

    Celsius celsius = new Celsius(100);
    Double d = celsiusDoubleIso.view(celsius);

    assertEquals(212, d, 0.0001);
  }
}
