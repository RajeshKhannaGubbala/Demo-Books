package com.booksdemo.demobooks.route;

import com.booksdemo.demobooks.Exceptions.BookNotFoundException;
import com.booksdemo.demobooks.Sevice.BookService;
import com.booksdemo.demobooks.entity.Books;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
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
        errorHandler(defaultErrorHandler().maximumRedeliveries(3));


        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .dataFormatProperty("prettyPrint", "true");


        rest("/books")
                .get("/getBookDetail/{bookName}").produces("application/json")
                .to("direct:getBookDetail")
                .get("/getAllBooks")
                .to("direct:getAllBooks")
                .post("/addBook").consumes("application/json").type(Books.class)
                .to("direct:addBook")
        .get("/getExternalMessage").to("direct:getExternalMessage");

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

                    exchange.getMessage().setBody(bookService.addBook(newBook));
                });
        from("direct:getExternalMessage").setHeader(Exchange.HTTP_METHOD,constant("GET")).to("http://localhost:9090/rest/books/getAllBooks?bridgeEndpoint=true")
                .log("Response from Books: ${body}").unmarshal().json(JsonLibrary.Jackson);;

    }
}
