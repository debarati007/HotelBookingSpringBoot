package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.model.City;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.repository.CityRepository;
import com.booking.recruitment.hotel.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class DefaultHotelServiceTest {
    HotelRepository hotelRepository=null;
    CityRepository cityRepository=null;
    DefaultHotelService defaultHotelService=null;

    @BeforeEach
    public void setUp(){
        hotelRepository=mock(HotelRepository.class);
        cityRepository=mock(CityRepository.class);
        defaultHotelService=new DefaultHotelService(hotelRepository,cityRepository);

    }
    @Test
    void testAllHotelsForNoHotelInRepositoryShouldReturnEmptyList() {
        when(hotelRepository.findAll()).thenReturn(null);
        assertEquals(Collections.emptyList(),defaultHotelService.getAllHotels());
        verify(hotelRepository,times(1)).findAll();
    }
    @Test
    void testGetAllHotelsForDeletedHotelShouldReturnEmptyList(){
        Hotel hotel=new Hotel();
        hotel.setDeleted(true);
        when(hotelRepository.findAll()).thenReturn(Collections.singletonList(hotel));
        assertEquals(Collections.emptyList(),defaultHotelService.getAllHotels());
        verify(hotelRepository,times(1)).findAll();
    }

    @Test
    void testGetAllHotelsShouldReturnListOfAllNonDeletedHotels(){
        List<Hotel> hotels=new ArrayList<>();
        Hotel hotel=new Hotel();
        hotels.add(hotel);
        when(hotelRepository.findAll()).thenReturn(hotels);
        assertEquals(1,defaultHotelService.getAllHotels().size());
    }

    @Test
    void testGetHotelByIdShouldReturnHotelIfPresent(){
        Hotel hotel=new Hotel();
        hotel.setId((long) 1);
        when(hotelRepository.findById((long)1)).thenReturn(Optional.of(hotel));
        assertEquals(hotel,defaultHotelService.getHotelById((long)1));
    }

    @Test
    void testGetHotelByIdShouldReturnNullIfHotelNotPresent(){
        Hotel hotel=null;
        when(hotelRepository.findById((long)230)).thenReturn(Optional.ofNullable(hotel));
        assertNull(defaultHotelService.getHotelById((long)230));
    }

    @Test
    void testGetClosestHotelsShouldReturnClosestThreeHotelsIfPresent(){
        Hotel hotel1=new Hotel();
        hotel1.setLatitude(52.379577);
        hotel1.setLongitude(80);
        Hotel hotel2=new Hotel();
        hotel2.setLatitude(67);
        hotel2.setLongitude(39);
        Hotel hotel3=new Hotel();
        hotel3.setLatitude(90.577);
        hotel3.setLongitude(200);
        List<Hotel> hotelList= Arrays.asList(hotel1,hotel2,hotel3);
        when(defaultHotelService.getAllHotels()).thenReturn(hotelList);
        City city=new City();
        city.setId((long)20);
        city.setCityCentreLatitude(40);
        city.setCityCentreLongitude(50);
        int distance=10;
        when(cityRepository.findById((long)20)).thenReturn(Optional.of(city));
        assertEquals(3,defaultHotelService.getClosestHotels((long)20,distance).size());
    }

}