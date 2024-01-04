package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest
{
    @Test
    void getUserDataByUsername()
    {
        //given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);
        UserService userService = new UserService(userRepositoryMock, authorizationTokenHelperMock);

        Request request = new Request();
        request.setRoute("/users/username");
        request.setAuthorizationToken("username-mtcgToken");

        String username = "username";
        UserData userdata = new UserData(username, "bio", "image");

        when(userRepositoryMock.findUserDataByUsername("username")).thenReturn(Optional.of(userdata));
        when(authorizationTokenHelperMock.invalidToken(any())).thenReturn(false);

        //when
        Response response = userService.getUserDataByUsername(request);

        //then
        assertEquals("{\"Name\":\"username\",\"Bio\":\"bio\",\"Image\":\"image\"}", response.getBody());
    }

    @Test
    void getUserDataByUsernameNotExistingUsername()
    {
        //given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);
        UserService userService = new UserService(userRepositoryMock, authorizationTokenHelperMock);

        Request request = new Request();
        request.setRoute("/users/notExistingUsername");
        request.setAuthorizationToken("notExistingUsername-mtcgToken");

        when(userRepositoryMock.findUserDataByUsername("username")).thenReturn(Optional.empty());
        when(authorizationTokenHelperMock.invalidToken(any())).thenReturn(false);

        //when
        Response response = userService.getUserDataByUsername(request);

        //then
        assertEquals(204, response.getStatusCode());
    }
    @Test
    void getUserDataByUsernameInvalidToken()
    {
        //given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);
        UserService userService = new UserService(userRepositoryMock, authorizationTokenHelperMock);

        Request request = new Request();
        request.setRoute("/users/username");
        request.setAuthorizationToken("invalid-mtcgToken");

        when(userRepositoryMock.findUserDataByUsername("username")).thenReturn(Optional.empty());
        when(authorizationTokenHelperMock.invalidToken(any())).thenReturn(true);

        //when
        Response response = userService.getUserDataByUsername(request);

        //then
        assertEquals(401, response.getStatusCode());
    }

    @Test
    void findAll()
    {
        //given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);
        UserService userService = new UserService(userRepositoryMock, authorizationTokenHelperMock);

        when(userRepositoryMock.findAll()).thenReturn(List.of(
                new User("user1", "pw1"),
                new User("user2", "pw2"),
                new User("user3", "pw3")
        ));

        //when
        Response response = userService.findAll();

        //then
        assertEquals("[{\"Username\":\"user1\",\"Password\":\"pw1\"},{\"Username\":\"user2\",\"Password\":\"pw2\"},{\"Username\":\"user3\",\"Password\":\"pw3\"}]"
        , response.getBody());
    }

    @Test
    void findAllEmptyUsers()
    {
        //given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);
        UserService userService = new UserService(userRepositoryMock, authorizationTokenHelperMock);

        when(userRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        //when
        Response response = userService.findAll();

        //then
        assertEquals("[]", response.getBody());
    }
}