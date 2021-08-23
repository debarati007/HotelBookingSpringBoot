package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
  private final HotelService hotelService;

  @Autowired
  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getAllHotels() {
    return hotelService.getAllHotels();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Hotel createHotel(@RequestBody Hotel hotel) {
    return hotelService.createNewHotel(hotel);
  }

  @GetMapping(value = "/{id}")
  private Hotel getHotel(@PathVariable (value = "id") Integer id) {
    Long hotelId= id.longValue();
    return hotelService.getHotelById(hotelId);
  }
  @DeleteMapping(value = "/{id}")
  private void deleteHotelById(@PathVariable (value = "id") Integer id){
    Long hotelId= id.longValue();
    hotelService.deleteHotelById(hotelId);
  }
  @GetMapping(value = "/search/{cityId}")
  private List<Hotel> getClosestHotels(@PathVariable(value = "cityId") Integer cityId,@RequestParam Integer sortBy){
    Long id=cityId.longValue();
    return hotelService.getClosestHotels(id,sortBy);
  }

}
