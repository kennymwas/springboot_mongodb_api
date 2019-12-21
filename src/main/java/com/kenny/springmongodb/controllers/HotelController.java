package com.kenny.springmongodb.controllers;

import com.kenny.springmongodb.entities.Hotel;
import com.kenny.springmongodb.entities.QHotel;
import com.kenny.springmongodb.entities.ResponseWrapper;
import com.kenny.springmongodb.repositories.HotelRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private HotelRepository hotelRepository;

    public HotelController(HotelRepository hotelRepository) {

        this.hotelRepository = hotelRepository;
    }

    /**
     * Gets All Hotels
     * @return hotels
     */
    @GetMapping("/all")
    public List<Hotel> getAll(){
        List<Hotel> hotels = this.hotelRepository.findAll();

        return hotels;
    }

    /**
     * Creating A New Hotel
     * using insert will only create a new data without providing an id "does only an insertion."
     * @param hotel
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<ResponseWrapper<Hotel>> create(@RequestBody Hotel hotel){
        ResponseWrapper response = new ResponseWrapper();

        response.setMessage("Hotel Created Successfully");
        response.setCode(201);
        response.setData(this.hotelRepository.insert(hotel));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updating  Hotel
     * using save will update the data if the id exists."insert or update a document."
     * @param hotel
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity<ResponseWrapper<Hotel>> update(@RequestBody Hotel hotel){
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("Hotel Updated Successfully");
        response.setCode(200);
        response.setData(this.hotelRepository.save(hotel));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Getting a hotel by id
     * @param id
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public Hotel getById(@PathVariable("id") String id){
        Optional<Hotel> hotel = this.hotelRepository.findById(id);

        return hotel.get();
    }

    /**
     * deleting a hotel by id
     * @param id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Hotel>> deleteById(@PathVariable("id") String id){
        ResponseWrapper response = new ResponseWrapper();

        this.hotelRepository.deleteById(id);
        response.setMessage("Hotel Deleted Successfully");
        response.setCode(200);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Getting a list of hotels with a maxprice
     * @param maxPrice
     * @return ResponseEntity
     */
    @GetMapping("/price/{maxPrice}")
    public ResponseEntity<ResponseWrapper<List<Hotel>>> getByPricePerNight(@PathVariable("maxPrice") int maxPrice){
        ResponseWrapper response = new ResponseWrapper();

        List<Hotel> hotels = this.hotelRepository.findByPricePerNightLessThan(maxPrice);
        response.setData(hotels);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * Getting a list of hotels within a specified city
     * @param city
     * @return ResponseEntity
     */
    @GetMapping("/address/{city}")
    public ResponseEntity<ResponseWrapper<List<Hotel>>> getByCity(@PathVariable("city") String city){
        ResponseWrapper response = new ResponseWrapper();
        List<Hotel> hotels = this.hotelRepository.findByCity(city);

        response.setData(hotels);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Getting a list of hotels within a specified country
     * @param country
     * @return ResponseEntity
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<ResponseWrapper<List<Hotel>>> getByCountry(@PathVariable("country") String country){
        ResponseWrapper response = new ResponseWrapper();
        // create a query class (QHotel)
        QHotel qHotel = new QHotel("hotel");

        // using the query class we can create the filters
        BooleanExpression filterByCountry = qHotel.address.country.eq(country);

        // we can then pass the filters to the findAll() method
        List<Hotel> hotels = (List<Hotel>) this.hotelRepository.findAll(filterByCountry);

        response.setData(hotels);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Getting a list of hotels with a maxPrice = 100 and minRating = 7
     * @return ResponseEntity
     */
    @GetMapping("/recommended")
    public ResponseEntity<ResponseWrapper<List<Hotel>>> getRecommended(){
        final int maxPrice = 100;
        final int minRating = 7;

        ResponseWrapper response = new ResponseWrapper();
        // create a query class (QHotel)
        QHotel qHotel = new QHotel("hotel");

        // using the query class we can create the filters
        BooleanExpression filterByPrice = qHotel.pricePerNight.lt(maxPrice);
        BooleanExpression filterByRating = qHotel.reviews.any().rating.gt(minRating);

        // we can then pass the filters to the findAll() method
        List<Hotel> hotels = (List<Hotel>) this.hotelRepository.findAll(filterByPrice.and(filterByRating));

        response.setData(hotels);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
