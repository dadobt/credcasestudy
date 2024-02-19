package com.example.qred.casestudy.casestudy.service;

import com.example.qred.casestudy.casestudy.models.ApplicationUser;
import com.example.qred.casestudy.casestudy.repository.ApplicationUserRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceUnitTest {

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Test
    void shouldLoadUserDetailsByUsername() {
        ApplicationUser applicationUser = createApplicationUserMock();
        UserDetails expectedUserDetails = new User(applicationUser.getUsername(), applicationUser.getPassword(), new ArrayList<>());

        when(applicationUserRepository.findByUsername(eq("Bob"))).thenReturn(applicationUser);

        UserDetails actualUserDetails = myUserDetailsService.loadUserByUsername("Bob");

        assertNotNull(actualUserDetails);
        assertSame(expectedUserDetails.getUsername(), actualUserDetails.getUsername());
        assertSame(expectedUserDetails.getPassword(), actualUserDetails.getPassword());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIsNotFound() {
        when(applicationUserRepository.findByUsername(eq("Bob"))).thenReturn(null);

        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("Bob");
        });

        Assertions.assertEquals("Username: 'Bob' not found", thrown.getMessage());
    }

    @NotNull
    private static ApplicationUser createApplicationUserMock() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(1L);
        applicationUser.setUsername("Bob");
        applicationUser.setPassword("Password");
        applicationUser.setRole("userRole");
        return applicationUser;
    }
}