package com.baar.springbootjunit.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

class PersonNotFoundExceptionTest {
  @Test
  void testNewPersonNotFoundException() {
    // Arrange
    String errorMessage = "An error occurred";

    // Act
    PersonNotFoundException actualPersonNotFoundException =
        new PersonNotFoundException(errorMessage);

    // Assert
    assertEquals("An error occurred", actualPersonNotFoundException.getLocalizedMessage());
    assertEquals("An error occurred", actualPersonNotFoundException.getMessage());
    assertNull(actualPersonNotFoundException.getCause());
    assertEquals(0, actualPersonNotFoundException.getSuppressed().length);
  }

  @Test
  void testNewPersonNotFoundExceptionWithCause() {
    // Arrange
    String errorMessage = "An error occurred";
    Throwable cause = mock(Throwable.class);

    // Act
    PersonNotFoundException actualPersonNotFoundException =
        new PersonNotFoundException(errorMessage, cause);

    // Assert
    assertEquals(errorMessage, actualPersonNotFoundException.getLocalizedMessage());
    assertEquals(errorMessage, actualPersonNotFoundException.getMessage());
    assertEquals(cause, actualPersonNotFoundException.getCause());
    assertEquals(0, actualPersonNotFoundException.getSuppressed().length);
  }
}
