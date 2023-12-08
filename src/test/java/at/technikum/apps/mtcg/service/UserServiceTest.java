package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
import at.technikum.apps.mtcg.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.*;
import static org.mockito.Mockito.*;

class UserServiceTest
{
    @Test
    void save() throws SQLException
    {
        //given
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);
        User user = new User("Name", "Password");

        doNothing().when(userRepository).save(user);

        //when
        userService.save(user);

        //then
        verify(userRepository).save(new User("Name", "Password"));
    }

    @Test
    void getUserDataByUsername() throws SQLException {
        //given
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        UserData testData = new UserData("TestName", "TestBio", "TestImage");

        when(userRepository.findUserDataByUsername("ExistingName")).thenReturn(Optional.of(testData));
        when(userRepository.findUserDataByUsername("NotExistingName")).thenReturn(Optional.empty());

        //when
        Optional<UserData> result1 = userService.getUserDataByUsername("ExistingName");
        Optional<UserData> result2 = userService.getUserDataByUsername("NotExistingName");

        //then
        assertTrue(result1.isPresent());
        assertTrue(result2.isEmpty());
        verify(userRepository).findUserDataByUsername("ExistingName");
        verify(userRepository).findUserDataByUsername("NotExistingName");
    }
}