package org.example.poketrade.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.poketrade.TestConstants;
import org.junit.jupiter.api.Test;

class CurrentUserProviderTest {

    private final CurrentUserProvider provider = new CurrentUserProvider();

    @Test
    void getCurrentUserId_shouldReturnCurrentTrainerId() {
        Long userId = provider.getCurrentUserId();

        assertThat(userId).isEqualTo(TestConstants.TRAINER_ID);
    }
}
