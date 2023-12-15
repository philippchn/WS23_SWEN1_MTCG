package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
import at.technikum.apps.mtcg.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest
{
    @Test
    void save() throws SQLException
    {
        //given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);
        User user = new User("Name", "Password");

        doNothing().when(userRepositoryMock).saveUser(user);

        //when
        userService.save(user);

        //then
        verify(userRepositoryMock).saveUser(new User("Name", "Password"));
    }

    @Test
    void getUserDataByUsername() throws SQLException
    {
        //given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);

        UserData testData = new UserData("TestName", "TestBio", "TestImage");

        when(userRepositoryMock.findUserDataByUsername("ExistingName")).thenReturn(Optional.of(testData));
        when(userRepositoryMock.findUserDataByUsername("NotExistingName")).thenReturn(Optional.empty());

        //when
        Optional<UserData> result = userService.getUserDataByUsername("ExistingName");

        //then
        assertTrue(result.isPresent());
        verify(userRepositoryMock).findUserDataByUsername("ExistingName");
    }

    @Test
    void getUserDataByNonExistingUsername() throws SQLException
    {
        //given
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        when(userRepository.findUserDataByUsername("NotExistingName")).thenReturn(Optional.empty());

        //when
        Optional<UserData> result = userService.getUserDataByUsername("NotExistingName");

        //then
        assertTrue(result.isEmpty());
        verify(userRepository).findUserDataByUsername("NotExistingName");
    }
}