package com.engwork.pgndb.exceptionhandling;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class OperationExceptionTest {

  @Test
  public void shouldReturnProperMessage() {
    // given
    OperationException operationException =
        new OperationException("Database creation", "Not enough memory");
    String expectedHeader = "Database creation has failed.";
    String expectedMessage = "Reason is: Not enough memory";

    // then
    assertThat(operationException.getHeaderMessage()).isEqualTo(expectedHeader);
    assertThat(operationException.getMainMessage()).isEqualTo(expectedMessage);
  }
}