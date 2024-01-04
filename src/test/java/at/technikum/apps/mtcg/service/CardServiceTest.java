package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CardServiceTest
{

    @Test
    void getAllCards()
    {
        //given
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        PackageRepository packageRepositoryMock = mock(PackageRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);
        CardService cardService = new CardService(cardRepositoryMock, userRepositoryMock, packageRepositoryMock, authorizationTokenHelperMock);

        Request request = new Request();
        request.setAuthorizationToken("username-mtcgToken");

        User user = new User("username", "pw");
        List<RequestCard> cards = List.of(
                new RequestCard("id1", "card1", 10),
                new RequestCard("id2", "card2", 20),
                new RequestCard("id3", "card3", 30)
        );

        when(userRepositoryMock.findUserByUsername("username")).thenReturn(Optional.of(user));
        when(cardRepositoryMock.getAllCardsOfUser("username")).thenReturn(cards);
        when(authorizationTokenHelperMock.getUsernameFromToken(request)).thenReturn("username");

        //when
        Response response = cardService.getAllCards(request);

        //then
        assertEquals("[{\"Id\":\"id1\",\"Name\":\"card1\",\"Damage\":10.0},{\"Id\":\"id2\",\"Name\":\"card2\",\"Damage\":20.0},{\"Id\":\"id3\",\"Name\":\"card3\",\"Damage\":30.0}]",
                response.getBody());
    }

    @Test
    void getAllCardsEmpty()
    {
        //given
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        PackageRepository packageRepositoryMock = mock(PackageRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);
        CardService cardService = new CardService(cardRepositoryMock, userRepositoryMock, packageRepositoryMock, authorizationTokenHelperMock);

        Request request = new Request();
        request.setAuthorizationToken("username-mtcgToken");

        User user = new User("username", "pw");

        when(userRepositoryMock.findUserByUsername("username")).thenReturn(Optional.of(user));
        when(cardRepositoryMock.getAllCardsOfUser("username")).thenReturn(Collections.emptyList());
        when(authorizationTokenHelperMock.getUsernameFromToken(request)).thenReturn("username");

        //when
        Response response = cardService.getAllCards(request);

        //then
        assertEquals("{ \"response\": \"No Content\"}", response.getBody());
    }
}