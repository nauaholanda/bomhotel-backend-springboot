package br.com.nauaholanda.bomHotel.service;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.nauaholanda.bomHotel.enumeration.UserRole;
import br.com.nauaholanda.bomHotel.exception.UserAuthenticationFailedException;
import br.com.nauaholanda.bomHotel.exception.UserNotFoundException;
import br.com.nauaholanda.bomHotel.model.User;
import br.com.nauaholanda.bomHotel.repository.UserRepository;

@ExtendWith(SpringExtension.class)
public class LoginServiceImplTest {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	LoginServiceImpl loginService;
	
	@DisplayName("Login method should return a registered user")
	@Test
	void loginMethodShouldReturnARegisteredUser() {
		User userToLogin = new User(null, "username", "password", null, new ArrayList<>(), null);
		User userOnDB = new User(1L, userToLogin.getUsername(), userToLogin.getPassword(), "Name", new ArrayList<>(), null);
		
		Mockito.when(userRepository.findByUsername(userToLogin.getUsername())).thenReturn(Optional.of(userOnDB));
		
		User expectedUser = new User(1L, userToLogin.getUsername(), userToLogin.getPassword(), "Name", new ArrayList<>(), UserRole.CUSTOMER);
		Assertions.assertEquals(expectedUser, loginService.login(userToLogin));
	}
	
	@DisplayName("Login method should throw a User not found exception")
	@Test
	void loginMethodShouldThrowAUserNotFoundException() {
		User userToLogin = new User(null, "username", "password", null, new ArrayList<>(), null);
		
		Mockito.when(userRepository.findByUsername(userToLogin.getUsername())).thenReturn(Optional.empty());
		
		String returnedMessage = "";
		try {
			loginService.login(userToLogin);
		} catch (UserNotFoundException e) {
			returnedMessage = e.getMessage();
		}

		String expectedMessage = "User " + userToLogin.getUsername() + " not found!";
		Assertions.assertEquals(expectedMessage, returnedMessage);
	}
	
	@DisplayName("Login method should throw a User authentication failed exception")
	@Test
	void loginMethodShouldThrowAUserAuthenticationException() {
		User userToLogin = new User(null, "username", "password", null, new ArrayList<>(), null);
		User userOnDB = new User(1L, userToLogin.getUsername(), "another_password", "Name", new ArrayList<>(), null);
		
		Mockito.when(userRepository.findByUsername(userToLogin.getUsername())).thenReturn(Optional.of(userOnDB));
		
		String returnedMessage = "";
		try {
			loginService.login(userToLogin);
		} catch (UserAuthenticationFailedException e) {
			returnedMessage = e.getMessage();
		}

		String expectedMessage = "Authentication failed for user " + userToLogin.getUsername() + "!";
		Assertions.assertEquals(expectedMessage, returnedMessage);
	}
	
}
