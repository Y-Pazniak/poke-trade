package org.example.poketrade.security;

import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public Long getCurrentUserId() {
        return 1L;
    }
}
