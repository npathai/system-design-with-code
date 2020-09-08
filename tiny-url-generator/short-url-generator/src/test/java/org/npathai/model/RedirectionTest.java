package org.npathai.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RedirectionTest {

    @Test
    public void redirectionIsAnonymousWhenUserIsNotAssociatedWithIt() {
        Redirection redirection = new Redirection("AAAAA", "www.google.com", 0L, 0L);
        assertThat(redirection.isAnonymous()).isTrue();
    }

    @Test
    public void redirectionIsNotAnonymousWhenUserIsAssociatedWithIt() {
        Redirection redirection = new Redirection("AAAAA", "www.google.com", 0L, 0L,
                "test");
        assertThat(redirection.isAnonymous()).isFalse();
    }

}