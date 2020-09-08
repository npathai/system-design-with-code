package org.npathai.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IdTest {

    @Test
    public void idIsOfFiveCharsInLength() {
        assertThat(Id.first().encode().length()).isEqualTo(5);
    }

    @Test
    public void firstIdIsAllAs() {
        assertThat(Id.first().encode()).isEqualTo("AAAAA");
    }

    @Test
    public void canBuildIdFromEncodedValue() {
        assertThat(Id.fromEncoded("AAAAB").encode()).isEqualTo("AAAAB");
    }

    @ParameterizedTest
    @CsvSource({
            "AAAAA,AAAAB",
            "AAAAZ,AAAAa",
            "AAAAz,AAABA",
            "yzzzz,zAAAA"
    })
    public void returnsIdIncrementedByOne(String current, String expectedNext) throws IdExhaustedException {
        assertThat(Id.fromEncoded(current).incrementAndGet().encode()).isEqualTo(expectedNext);
    }

    @Test
    public void throwsIdExhaustedExceptionWhenAllIdsAreExhausted() {
        assertThatThrownBy(() -> Id.fromEncoded("zzzzz").incrementAndGet()).isInstanceOf(IdExhaustedException.class);
    }

    @ParameterizedTest
    @MethodSource("dataFor_returnsIdIncrementedByNeededCount")
    public void returnsIdIncrementedByNeededCount(String currentId, int increment, String expectedId) throws IdExhaustedException {
        assertThat(Id.fromEncoded(currentId).incrementAndGet(increment).encode()).isEqualTo(expectedId);
    }

    public static List<Arguments> dataFor_returnsIdIncrementedByNeededCount() {
        return List.of(
                Arguments.of( "AAAAA", 0, "AAAAA"),
                Arguments.of( "AAAAA", 1, "AAAAB"),
                Arguments.of( "AAAAA", 2, "AAAAC"),
                Arguments.of( "AAAAA", 52, "AAABA"),
                Arguments.of( "zzzzy", 1, "zzzzz")
        );
    }

    @ParameterizedTest
    @MethodSource("dataFor_throwsIdExhaustedExceptionWhenCountExceedsLimit")
    public void throwsIdExhaustedExceptionWhenCountExceedsLimit(String currentId, int increment) {
        assertThatThrownBy(() -> Id.fromEncoded(currentId).incrementAndGet(increment))
                .isInstanceOf(IdExhaustedException.class);
    }

    public static List<Arguments> dataFor_throwsIdExhaustedExceptionWhenCountExceedsLimit() {
        return List.of(
                Arguments.of( "zzzzA", 52),
                Arguments.of( "zzzzy", 2),
                Arguments.of( "zzzzy", 3),
                Arguments.of( "zzzza", 52)
        );
    }
}