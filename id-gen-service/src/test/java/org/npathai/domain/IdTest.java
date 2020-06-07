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
    public void returnsIdIncrementedByOne(String current, String expectedNext) throws IdExhaustedException {
        assertThat(Id.fromEncoded(current).incrementAndGet().encode()).isEqualTo(expectedNext);
    }

    @Test
    public void throwsIdExhaustedExceptionWhenAllIdsAreExhausted() {
        assertThatThrownBy(() -> Id.fromEncoded("zzzzz").incrementAndGet()).isInstanceOf(IdExhaustedException.class);
    }

    @Test
    @Parameters(method = "dataFor_returnsIdIncrementedByNeededCount")
    public void returnsIdIncrementedByNeededCount(String currentId, int increment, String expectedId) throws IdExhaustedException {
        assertThat(Id.fromEncoded(currentId).incrementAndGet(increment).encode()).isEqualTo(expectedId);
    }

    public Object[][] dataFor_returnsIdIncrementedByNeededCount() {
        return new Object[][]{
                new Object[] { "AAAAA", 0, "AAAAA"},
                new Object[] { "AAAAA", 1, "AAAAB"},
                new Object[] { "AAAAA", 2, "AAAAC"},
                new Object[] { "AAAAA", 52, "AAABA"},
                new Object[] { "zzzzy", 1, "zzzzz"},
        };
    }

    @Test
    @Parameters(method = "dataFor_throwsIdExhaustedExceptionWhenCountExceedsLimit")
    public void throwsIdExhaustedExceptionWhenCountExceedsLimit(String currentId, int increment) {
        assertThatThrownBy(() -> Id.fromEncoded(currentId).incrementAndGet(increment))
                .isInstanceOf(IdExhaustedException.class);
    }

    public Object[][] dataFor_throwsIdExhaustedExceptionWhenCountExceedsLimit() {
        return new Object[][]{
                new Object[] { "zzzzA", 52},
                new Object[] { "zzzzy", 2},
                new Object[] { "zzzzy", 3},
                new Object[] { "zzzza", 52},
        };
    }
}