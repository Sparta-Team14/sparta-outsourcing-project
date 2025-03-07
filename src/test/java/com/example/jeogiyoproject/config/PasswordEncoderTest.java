package com.example.jeogiyoproject.config;

import com.example.jeogiyoproject.global.config.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class PasswordEncoderTest {

    @InjectMocks
    private PasswordEncoder passwordEncoder;

    @Test
    void password_matches_working() {
        // given
        String rawPassword = "test12345";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        // when
        boolean passwordmatches = passwordEncoder.matches(rawPassword, encodedPassword);
        // then
        assertTrue(passwordmatches);
    }
}
