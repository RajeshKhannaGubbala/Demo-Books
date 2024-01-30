package com.booksdemo.demobooks.Sevice;

import com.booksdemo.demobooks.entity.Books;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class BookService {

    private static List<Books> list = new ArrayList<>();
    @PostConstruct
    public void initDB(){
        list.add(new Books(12,"science","khanna",345.0));
        list.add(new Books(14,"social","rajesh",445.0));
        list.add(new Books(13,"Maths","sai",232.0));
    }
public Books addBook(Books book){
        list.add(book);
        return book;
}
    public Books findBookByName(String bookName) {
        Books book = new Books();
        return list.stream()
                .filter(list -> book.getBook_name().equals(bookName))
                .findFirst()
                .orElse(null);
    }

public List<Books> getBookDetails(){
        return list;
}

    }
