package com.baar.springbootjunit.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

class PersonExistsExceptionTest {
    @Test
    void testNewPersonExistsException() {
      // Arrange
      String errorMessage = "Person already exists";

      // Act
      PersonExistsException actualPersonExistsException = new PersonExistsException(errorMessage);

      // Assert
      assertEquals(errorMessage, actualPersonExistsException.getLocalizedMessage());
      assertEquals(errorMessage, actualPersonExistsException.getMessage());
      assertNull(actualPersonExistsException.getCause());
      assertEquals(0, actualPersonExistsException.getSuppressed().length);
    }

    @Test
    void testNewPersonExistsExceptionWithCause() {
      // Arrange
      String errorMessage = "Person already exists";
      Throwable cause = mock(Throwable.class);

      // Act
      PersonExistsException actualPersonExistsException = new PersonExistsException(errorMessage, cause);

      // Assert
      assertEquals(errorMessage, actualPersonExistsException.getLocalizedMessage());
      assertEquals(errorMessage, actualPersonExistsException.getMessage());
      assertEquals(cause, actualPersonExistsException.getCause());
      assertEquals(0, actualPersonExistsException.getSuppressed().length);
    }

  }

