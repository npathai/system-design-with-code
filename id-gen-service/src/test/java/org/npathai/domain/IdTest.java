package org.npathai.domain;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(JUnitParamsRunner.class)
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

    @Test
    @Parameters({
            "AAAAA,AAAAB",
            "AAAAZ,AAAAa",
            "AAAAz,AAABA",
            "yzzzz,zAAAA"
    })
    public void nextReturnsIdIncrementedByOne(String current, String expectedNext) throws IdExhaustedException {
        assertThat(Id.fromEncoded(current).next().encode()).isEqualTo(expectedNext);
    }

    @Test
    public void shouldThrowIdExhaustedExceptionWhenAllIdsAreExhausted() {
        assertThatThrownBy(() -> Id.fromEncoded("zzzzz").next()).isInstanceOf(IdExhaustedException.class);
    }
}