package com.kenny.springmongodb.configs;

import com.kenny.springmongodb.entities.Address;
import com.kenny.springmongodb.entities.Hotel;
import com.kenny.springmongodb.entities.Review;
import com.kenny.springmongodb.repositories.HotelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DbSeeder implements CommandLineRunner {
    private HotelRepository hotelRepository;

    public DbSeeder(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Hotel hillton = new Hotel(
                "Hillton",
                33000,
                new Address("Nairobi", "Kenya"),
                Arrays.asList(
                        new Review("John", 8, false),
                        new Review("Mary", 7, true)
                )
        );

        Hotel sankara = new Hotel(
                "Sankara",
                45000,
                new Address("Nairobi", "Kenya"),
                Arrays.asList(
                        new Review("Teddy", 9, true)
                )
        );

        Hotel merica = new Hotel(
                "Merica",
                20000,
                new Address("Nakuru", "Kenya"),
                new ArrayList<>()
        );

        // drop all hotels
        this.hotelRepository.deleteAll();

        //add our hotels to the database
        List<Hotel> hotels = Arrays.asList(hillton, sankara, merica);
        this.hotelRepository.insert(hotels); //using save will update the data if the id exists while insert does only an insertion.
    }
}
