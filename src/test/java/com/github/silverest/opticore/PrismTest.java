package com.github.silverest.opticore;

import static org.junit.Assert.*;

import com.github.silverest.opticore.core.Prism;
import com.github.silverest.opticore.core.utils.Either;
import java.util.Optional;
import org.junit.Test;

public class PrismTest {

  @Test
  public void testPreviewWithMatchingValue() {
    Prism<String, Integer> prism =
        Prism.of(
            s -> {
              try {
                int value = Integer.parseInt(s);
                return Optional.of(value);
              } catch (NumberFormatException e) {
                return Optional.empty();
              }
            },
            String::valueOf);

    String input = "123";
    Optional<Integer> result = prism.preview(input);
    assertTrue(result.isPresent());
    assertEquals(Optional.of(123), result);
  }

  @Test
  public void testPreviewWithNonMatchingValue() {
    Prism<String, Integer> prism =
        Prism.of(
            s -> {
              try {
                int value = Integer.parseInt(s);
                return Optional.of(value);
              } catch (NumberFormatException e) {
                return Optional.empty();
              }
            },
            String::valueOf);

    String input = "abc";
    Optional<Integer> result = prism.preview(input);
    assertFalse(result.isPresent());
  }

  @Test
  public void testReview() {
    Prism<String, Integer> prism =
        Prism.of(
            s -> {
              try {
                int value = Integer.parseInt(s);
                return Optional.of(value);
              } catch (NumberFormatException e) {
                return Optional.empty();
              }
            },
            String::valueOf);

    int value = 456;
    String result = prism.review(value);
    assertEquals("456", result);
  }

  @Test
  public void testMatchingWithMatchingValue() {
    Prism<String, Integer> prism =
        Prism.of(
            s -> {
              try {
                int value = Integer.parseInt(s);
                return Optional.of(value);
              } catch (NumberFormatException e) {
                return Optional.empty();
              }
            },
            String::valueOf);

    String input = "789";
    Either<String, Integer> result = prism.matching(input);
    assertTrue(result.isRight());
    assertEquals(Optional.of(789), result.getRight());
  }

  @Test
  public void testMatchingWithNonMatchingValue() {
    Prism<String, Integer> prism =
        Prism.of(
            s -> {
              try {
                int value = Integer.parseInt(s);
                return Optional.of(value);
              } catch (NumberFormatException e) {
                return Optional.empty();
              }
            },
            String::valueOf);

    String input = "xyz";
    Either<String, Integer> result = prism.matching(input);
    assertTrue(result.isLeft());
    assertEquals(Optional.of(input), result.getLeft());
  }
}
