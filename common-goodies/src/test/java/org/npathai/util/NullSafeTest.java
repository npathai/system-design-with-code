package org.npathai.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class NullSafeTest {

    @Nested
    class IfNotNull {

        private ThrowingConsumer<String> mockConsumer;

        @BeforeEach
        public void initialize() {
            mockConsumer = mock(ThrowingConsumer.class);
        }

        @Test
        public void doesNotCallConsumerWhenObjectPassedIsNull() throws Exception {
            NullSafe.ifNotNull(null, mockConsumer);

            verifyZeroInteractions(mockConsumer);
        }

        @Test
        public void callsConsumerWhenObjectPassedInIsNotNull() throws Exception {
            String str = "Nulls suck";

            NullSafe.ifNotNull(str, mockConsumer);

            verify(mockConsumer).accept(same(str));
        }
    }
}