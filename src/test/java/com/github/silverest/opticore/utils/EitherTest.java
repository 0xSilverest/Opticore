package com.github.silverest.opticore.utils;

import static org.junit.Assert.*;

import com.github.silverest.opticore.core.utils.Either;
import java.util.Optional;
import org.junit.Test;

public class EitherTest {

  @Test
  public void testLeft() {
    String errorMessage = "Something went wrong";
    Either<String, Integer> result = Either.left(errorMessage);
    assertTrue(result.isLeft());
    assertFalse(result.isRight());
    assertEquals(Optional.of(errorMessage), result.getLeft());
  }

  @Test
  public void testRight() {
    int successCode = 200;
    Either<String, Integer> result = Either.right(successCode);
    assertFalse(result.isLeft());
    assertTrue(result.isRight());
    assertEquals(Optional.of(successCode), result.getRight());
  }

  @Test
  public void eitherMap() {
    Either<String, Integer> result = Either.right(200);
    Either<String, String> mappedResult = result.map(Object::toString);
    assertEquals(Either.right("200"), mappedResult);
  }

  @Test
  public void eitherFold() {
    Either<String, Integer> result = Either.right(200);
    String mappedResult =
        result.fold(
            left -> {
              throw new RuntimeException(left);
            },
            Object::toString);
    assertEquals("200", mappedResult);
  }

  @Test
  public void eitherFoldThrowError() {
    Either<String, Integer> result = Either.left("Boom!");
    Exception exception =
        assertThrows(
            RuntimeException.class,
            () ->
                result.fold(
                    left -> {
                      throw new RuntimeException(left);
                    },
                    Object::toString));
    String expectedMessage = "Boom!";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }
}
