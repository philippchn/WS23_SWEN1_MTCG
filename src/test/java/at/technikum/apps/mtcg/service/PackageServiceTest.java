package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.DBCard;
import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class PackageServiceTest
{
    @Test
    void savePackage() throws SQLException
    {
        //given
        PackageRepository packageRepositoryMock = mock(PackageRepository.class);
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        PackageService packageService = new PackageService(packageRepositoryMock, cardRepositoryMock, userRepositoryMock);

        RequestCard[] requestCards = new RequestCard[5];
        requestCards[0] = new RequestCard("1", "WaterGoblin", 55);
        DBCard dbCard1 = new DBCard("1", "WaterGoblin", 55, true, "water");
        requestCards[1] = new RequestCard("2", "RegularTroll", 55);
        requestCards[2] = new RequestCard("3", "WaterGoblin", 55);
        requestCards[3] = new RequestCard("4", "WaterSpell", 55);
        requestCards[4] = new RequestCard("5", "RegularSpell", 55);

        doNothing().when(cardRepositoryMock).saveCard(any());

        //when
        packageService.savePackage(requestCards);

        //then
        verify(cardRepositoryMock, times(5)).saveCard(any());
        verify(cardRepositoryMock).saveCard(dbCard1);
    }

    @Test
    void getPackage() throws SQLException {
        //given
        PackageRepository packageRepositoryMock = mock(PackageRepository.class);
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        PackageService packageService = new PackageService(packageRepositoryMock, cardRepositoryMock, userRepositoryMock);

        List<RequestCard> requestCards = new ArrayList<>();
        requestCards.add(0, new RequestCard("1", "WaterGoblin", 55));
        requestCards.add(1, new RequestCard("2", "RegularTroll", 55));
        requestCards.add(2, new RequestCard("3", "WaterGoblin", 55));
        requestCards.add(3, new RequestCard("4", "WaterSpell", 55));
        requestCards.add(4, new RequestCard("5", "RegularSpell", 55));

        when(packageRepositoryMock.getAllAvailablePackageId()).thenReturn(List.of(1,2,3,4,5));
        doNothing().when(packageRepositoryMock).buyPackage(eq("username"), anyInt());
        when(packageRepositoryMock.getCardsFromPackage(anyInt())).thenReturn(requestCards);

        //when
        List<RequestCard> result = packageService.buyPackage("username");

        //then
        assertEquals(result, requestCards);
        verify(packageRepositoryMock).getAllAvailablePackageId();
        verify(packageRepositoryMock).buyPackage(eq("username"), anyInt());
        verify(packageRepositoryMock).getCardsFromPackage(anyInt());
    }
}