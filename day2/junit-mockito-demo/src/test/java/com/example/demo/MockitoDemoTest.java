package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mockito Core Features Demonstration")
class MockitoDemoTest {

    // Simple custom interface to mock (avoids JDK encapsulation restrictions in Java 26+)
    interface SimpleCalculator {
        int add(int a, int b);
        String getOperationName(int id);
        void clearHistory();
    }

    // Simple custom class to spy (avoids JDK encapsulation restrictions in Java 26+)
    static class SimpleStack {
        private final List<String> elements = new ArrayList<>();

        public void push(String item) {
            elements.add(item);
        }

        public String pop() {
            if (elements.isEmpty()) return null;
            return elements.remove(elements.size() - 1);
        }

        public int size() {
            return elements.size();
        }
    }

    @Mock
    private SimpleCalculator mockedCalculator;

    @Spy
    private SimpleStack spiedStack = new SimpleStack();

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Test
    @DisplayName("Mock vs Spy Demonstration")
    void mockVsSpyDemo() {
        // --- Mock behavior ---
        mockedCalculator.add(10, 20); // Calling method on mock does nothing real, returns default (0)
        
        // Assert: default return value is 0
        assertThat(mockedCalculator.add(10, 20)).isZero();
        
        // Stub the mock
        when(mockedCalculator.add(10, 20)).thenReturn(30);
        assertThat(mockedCalculator.add(10, 20)).isEqualTo(30);

        // --- Spy behavior ---
        spiedStack.push("one"); // Calling real method modifies the underlying state
        
        // Assert: Size is actually 1 (since it's a spy delegating to real SimpleStack methods)
        assertThat(spiedStack.size()).isEqualTo(1);
        
        // Stub a spy using doReturn() (prevents calling real method during stubbing)
        Mockito.doReturn(100).when(spiedStack).size();
        assertThat(spiedStack.size()).isEqualTo(100);
    }

    @Test
    @DisplayName("Stubbing Multiple Consecutive Calls")
    void consecutiveStubbingDemo() {
        // Arrange
        when(mockedCalculator.getOperationName(anyInt()))
                .thenReturn("First Call")
                .thenReturn("Second Call")
                .thenThrow(new RuntimeException("Out of bounds"));

        // Act & Assert
        assertThat(mockedCalculator.getOperationName(0)).isEqualTo("First Call");
        assertThat(mockedCalculator.getOperationName(1)).isEqualTo("Second Call");
        assertThatThrownBy(() -> mockedCalculator.getOperationName(2))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Out of bounds");
    }

    @Test
    @DisplayName("Mockito Verification: Call Counts and Order")
    void verificationDemo() {
        // Act
        mockedCalculator.getOperationName(1);
        mockedCalculator.getOperationName(2);
        mockedCalculator.getOperationName(2);

        // Assert/Verify call counts
        verify(mockedCalculator, times(1)).getOperationName(1);
        verify(mockedCalculator, times(2)).getOperationName(2);
        verify(mockedCalculator, atLeastOnce()).getOperationName(2);
        verify(mockedCalculator, never()).getOperationName(999);
    }

    @Test
    @DisplayName("InOrder Verification of Call Sequencing")
    void inOrderVerificationDemo() {
        // Act
        mockedCalculator.getOperationName(1);
        mockedCalculator.getOperationName(2);

        // Assert InOrder
        InOrder inOrder = inOrder(mockedCalculator);
        inOrder.verify(mockedCalculator).getOperationName(1);
        inOrder.verify(mockedCalculator).getOperationName(2);
    }

    @Test
    @DisplayName("Stubbing Void Methods with doThrow")
    void stubbingVoidMethodsDemo() {
        // Arrange
        doThrow(new UnsupportedOperationException("Cannot clear history")).when(mockedCalculator).clearHistory();

        // Act & Assert
        assertThatThrownBy(() -> mockedCalculator.clearHistory())
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Cannot clear history");
    }

    @Test
    @DisplayName("Capturing Method Arguments using ArgumentCaptor")
    void argumentCaptorDemo() {
        // Act
        spiedStack.push("Captured Argument String");

        // Assert & Capture
        verify(spiedStack).push(stringCaptor.capture());
        
        String capturedValue = stringCaptor.getValue();
        assertThat(capturedValue).isEqualTo("Captured Argument String");
    }
}
