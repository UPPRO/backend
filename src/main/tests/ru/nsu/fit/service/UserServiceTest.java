package ru.nsu.fit.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.nsu.fit.database.entities.Token;
import ru.nsu.fit.database.entities.User;
import ru.nsu.fit.database.repositories.TokenRepository;
import ru.nsu.fit.database.repositories.UserRepository;
import ru.nsu.fit.database.types.Role;
import ru.nsu.fit.exception.LogoutException;
import ru.nsu.fit.exception.RegistrationException;
import ru.nsu.fit.mock.MockTokenRepository;
import ru.nsu.fit.mock.MockUserRepository;
import ru.nsu.fit.web.login.AuthData;
import ru.nsu.fit.web.navigation.UserPublicDTO;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    private UserRepository userRepository = new MockUserRepository();
    private TokenRepository tokenRepository = new MockTokenRepository();
    private UserService userService = new UserService(userRepository, tokenRepository);


    @Before
    public void defaultUser() {
        userRepository.deleteAll();
        userRepository.save(new User("default", "default", Role.USER));
    }

    @Test
    public void register() {
        try {
            User user = userService.register(new AuthData("user", "user"));
            Assert.assertEquals(user.getLogin(), "user");
            Assert.assertNotNull(user.getId());
        } catch (RegistrationException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = RegistrationException.class)
    public void registerFailed() throws RegistrationException {
        userService.register(new AuthData("default", "default"));
    }

    @Test
    public void loadUser() throws RegistrationException {
        userService.loadUserByUsername("default");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserFailed() throws RegistrationException {
        userService.loadUserByUsername("WrongName");
    }

    @Test
    public void initialize(){
        try {
            userService.initializeUsers();
            userService.loadUserByUsername("root");
        }
        catch (UsernameNotFoundException e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void login() throws Exception {
        Token token = userService.login(new AuthData("default", "default"));
        User user = userService.getUser(token.getData());
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getLogin(), "default");
    }

    @Test(expected = AuthenticationException.class)
    public void loginFailed() throws Exception {
        userService.login(new AuthData("WrongName", "WrongPassword"));
    }

    @Test(expected = AuthenticationException.class)
    public void loginFailedPassword() throws Exception {
        userService.login(new AuthData("default", "WrongPassword"));
    }

    @Test
    public void logout() throws Exception {
        Token token = userService.login(new AuthData("default", "default"));
        userService.logout(token.getData());
    }

    @Test(expected = LogoutException.class)
    public void logoutFailed() throws Exception {
        userService.logout("IncorrectToken");
    }

    @Test
    public void getAllUsers() {
        List<User> users = userService.getAllUsers();
        Assert.assertEquals(users.size(), 1);
        Assert.assertEquals(users.get(0).getLogin(), "default");
    }

    @Test
    public void changeRole() {
        User user = userService.changeRole(new UserPublicDTO(new User("default", "", Role.ADMINISTRATOR)));
        Assert.assertEquals(user.getRole(), Role.ADMINISTRATOR);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void changeRoleFailed() {
        userService.changeRole(new UserPublicDTO(new User("WrongName", "", Role.ADMINISTRATOR)));
    }

    @Test
    public void changePassword() {
        AuthData authData = new AuthData("default", "pass");
        userService.changePassword(authData);
        userService.login(authData);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void changePasswordFailed() {
        userService.changePassword(new AuthData("WrongName", ""));
    }
}
