package com.github.silverest.opticore;

import com.github.silverest.opticore.core.Iso;
import org.junit.Assert;
import org.junit.Test;

public class IsoTest {

  private record Age(int value) {}

  @Test
  public void viewReview() {
    Iso<Age, Integer> agePrism = Iso.of(Age::value, Age::new);
    Assert.assertEquals(30, agePrism.view(agePrism.review(30)).intValue());
  }

  @Test
  public void reviewView() {
    Iso<Age, Integer> agePrism = Iso.of(Age::value, Age::new);
    Assert.assertEquals(new Age(30), agePrism.review(agePrism.view(new Age(30))));
  }
}
