package com.booksdemo.demobooks.route;

import com.booksdemo.demobooks.Sevice.BookService;
import com.booksdemo.demobooks.entity.Books;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FirstRoute extends RouteBuilder {

    private final BookService bookService;

    public FirstRoute(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void configure() throws Exception {

       /* from("timer:simpletimer?period=2000")
                .transform().constant("Hello word")
                .to("log:simpletimer");

*/

        restConfiguration().component("servlet").port(8080);

        rest("/books")
                .get("/getBookDetails/{bookName}")
                .to("direct:getBookDetails")
                .get("/getAllBooks")
                .to("direct:getAllBooks")
                .post("/addBook")
                .to("direct:addBook");

        from("direct:getBookDetail")
                .routeId("getBookDetail")
                .process(exchange -> {
                    String bookName = exchange.getIn().getHeader("bookName", String.class);
                    Books book = bookService.findBookByName(bookName);
                    if (book != null) {
                        exchange.getMessage().setBody(book);
                    } else {
                        exchange.getMessage().setBody("Book not found for name: " + bookName);
                    }
                });

        from("direct:getAllBookDetails")
                .routeId("getAllBookDetails")
                .process(exchange -> {
                    Books book = (Books) bookService.getBookDetails();
                    exchange.getMessage().setBody(book);
                });

        from("direct:addBook")
                .routeId("addBook")
                .process(exchange -> {
                    Books newBook = exchange.getIn().getBody(Books.class);
                    bookService.addBook(newBook);
                    exchange.getMessage().setBody("Book added successfully");
                });

    }
}
