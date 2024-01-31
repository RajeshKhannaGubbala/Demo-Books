package com.booksdemo.demobooks.Sevice;

import com.booksdemo.demobooks.entity.Books;
import org.springframework.stereotype.Service;

//import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
@Service
public class BookService {

    private static List<Books> list = new ArrayList<>();

    static{
       list.add(new Books(12,"science","khanna",345.0));
       list.add(new Books(14,"social","rajesh",445.0));
       list.add(new Books(13,"Maths","sai",232.0));
   }
public Books addBook(Books book){
        list.add(book);
       // list.add(book.setBook_Id(b);)
        return book;
}
    public Books findBookByName(String bookName) {

        Books book= list.stream()
                .filter(list -> list.getBook_name().equals(bookName))
                .findFirst()
                .orElse(null);

        return book;
    }

public List<Books> getBookDetails(){
        return list;
}

    }
