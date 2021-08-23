package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.exception.BadRequestException;
import com.booking.recruitment.hotel.model.City;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.model.Ranking;
import com.booking.recruitment.hotel.repository.CityRepository;
import com.booking.recruitment.hotel.repository.HotelRepository;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
class DefaultHotelService implements HotelService {
  private final HotelRepository hotelRepository;
  private final CityRepository cityRepository;
  @Autowired
  DefaultHotelService(HotelRepository hotelRepository,CityRepository cityRepository) {
    this.hotelRepository = hotelRepository;
    this.cityRepository = cityRepository;
  }

  @Override
  public List<Hotel> getAllHotels() {
    List<Hotel> hotels=hotelRepository.findAll();
    List<Hotel> availableHotels=new ArrayList<>();
    if(hotels!=null) {
      availableHotels = hotels.stream().filter(hotel -> hotel.isDeleted() != true).collect(Collectors.toList());
    }
    return availableHotels;
  }

  @Override
  public List<Hotel> getHotelsByCity(Long cityId) {
    return hotelRepository.findAll().stream()
        .filter((hotel) -> cityId.equals(hotel.getCity().getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Hotel createNewHotel(Hotel hotel) {
    if (hotel.getId() != null) {
      throw new BadRequestException("The ID must not be provided when creating a new Hotel");
    }
    return hotelRepository.save(hotel);
  }

  @Override
  public Hotel getHotelById(Long hotelId) {
    Hotel hotel=null;
    Optional<Hotel> hotelOptional=hotelRepository.findById(hotelId);
    if(hotelOptional.isPresent())
      hotel=hotelOptional.get();
    return hotel;
  }

  @Override
  public void deleteHotelById(Long hotelId) {
    Hotel hotel=getHotelById(hotelId);
    if(hotel!=null){
      hotel.setDeleted(true);
      hotelRepository.save(hotel);
    }
  }
  @Override
  public List<Hotel> getClosestHotels(Long cityId, Integer distance) {
    List<Hotel> hotels=getAllHotels();
    Optional<City> city=cityRepository.findById(cityId);
    List<Ranking> distanceRank=new ArrayList<>();
    if (city.isPresent()) {
      for (Hotel hotel : hotels) {
        double dist = getDistance(city.get().getCityCentreLatitude(), hotel.getLatitude(),
                city.get().getCityCentreLongitude(),hotel.getLongitude());
        distanceRank.add(new Ranking(hotel,dist));
      }
    }
    Comparator<Ranking> compareByDistance = (Ranking o1, Ranking o2) ->
            o1.getDistance().compareTo( o2.getDistance() );
    Collections.sort(distanceRank,compareByDistance);
    List<Hotel> closestHotel=new ArrayList<>();
    int count=0;
    for(Ranking ranking:distanceRank){
      if(count>=3)
        break;
      closestHotel.add(ranking.getHotel());
      count++;
    }
    return closestHotel;
  }

  private static double getDistance(double lat1, double lat2, double lon1,
                                double lon2) {

    final int R = 6371; // Radius of the earth

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters

    double height = 0.0;

    distance = Math.pow(distance, 2) + Math.pow(height, 2);

    return Math.sqrt(distance);
  }
}
