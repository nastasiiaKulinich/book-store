package com.example.bookstore;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book sherlockHolmes = new Book();
            sherlockHolmes.setTitle("Sherlock Holmes");
            sherlockHolmes.setAuthor("Arthur Conan Doyle");
            sherlockHolmes.setPrice(BigDecimal.valueOf(371));
            sherlockHolmes.setIsbn("978-3-16-148410-0");

            bookService.save(sherlockHolmes);

            System.out.println(bookService.findAll());
        };
    }
}
