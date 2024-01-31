package com.booksdemo.demobooks.route;

import com.booksdemo.demobooks.Exceptions.BookNotFoundException;
import com.booksdemo.demobooks.Sevice.BookService;
import com.booksdemo.demobooks.entity.Books;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class FirstRoute extends RouteBuilder {

    private final BookService bookService;

    public FirstRoute(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .dataFormatProperty("prettyPrint", "true");

        from("rest:get:/getDetails?produces=application/json")
                .outputType(Books.class)
                .process(exchange -> {
                    exchange.getMessage().setBody(bookService.getBookDetails());
                });


        rest("/books")
                .get("/getBookDetail/{bookName}").produces("application/json")
                .to("direct:getBookDetail")
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
                        throw new BookNotFoundException("Book not found for name: " + bookName);

                    }
                })
                .onException(BookNotFoundException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .setBody(constant("Book not found"))
                .end();

        from("direct:getAllBooks")
                .routeId("getAllBooks")
                .process(exchange -> {
//                     bookService.getBookDetails();
                    exchange.getMessage().setBody( bookService.getBookDetails());
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
