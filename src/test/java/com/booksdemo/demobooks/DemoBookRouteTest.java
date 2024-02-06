package com.booksdemo.demobooks;

import com.booksdemo.demobooks.Sevice.BookService;
import com.booksdemo.demobooks.entity.Books;
import com.booksdemo.demobooks.route.FirstRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWith;
import org.junit.jupiter.api.Test;



import org.apache.camel.test.junit5.CamelTestSupport;

import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DemoBookRouteTest extends CamelTestSupport {

    public class MyAdviceWithTest extends CamelTestSupport {
        @Override
        public boolean isUseAdviceWith() {
            return true; // turn on advice with
        }
    }
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new FirstRoute(new BookService());
    }



    @Test
    public void testGetAllBooks() throws Exception {

        ArrayList<Books> list = new ArrayList<>();
        list.add(new Books(12,"science","khanna",345.0));
        AdviceWith.adviceWith(context, "getAllBooks",a ->
                a.weaveById("getBooks").replace().to("mock:getAllBooksResult"));
                     //   process( exchange -> { exchange.getIn().setBody(list, Books.class);}));

        // Start the context
        context.start();

        // Test the route
        template.sendBody("direct:getAllBooks", list);

        // Assert the result using the mock endpoint

        assertMockEndpointsSatisfied();

       List<Books> list2 = (List<Books>) getMockEndpoint("mock:getAllBooksResult").getExchanges().get(0).getIn().getBody();

        assertEquals(345.0, list2.get(0).getPrice());
    }
    @Test
    public void testGetBookDetails() throws Exception {

        ArrayList<Books> list = new ArrayList<>();
        list.add(new Books(12,"science","khanna",345.0));
        list.add(new Books(17,"social","sai",9980.0));
        AdviceWith.adviceWith(context, "getBookDetail",a ->
                a.weaveById("getDetailsOfBook").replace().to("mock:getBooksDetailsResult"));
        //   process( exchange -> { exchange.getIn().setBody(list, Books.class);}));

        // Start the context
        context.start();

        template.sendBodyAndHeader("direct:getBookDetail",list,"bookName","social");


        // Assert the result using the mock endpoint

        assertMockEndpointsSatisfied();

        List<Books> list2 = (List<Books>) getMockEndpoint("mock:getBooksDetailsResult").getExchanges().get(0).getIn().getBody();

        assertEquals(2, list2.size());
    }
    @Test
    public void testAddBooksDetails() throws Exception {
        Books book= new Books(12,"science","khanna",345.0);

        AdviceWith.adviceWith(context, "addBook",a ->
                a.weaveById("SaveBook").replace().to("mock:getAddBookDetailsResult"));


        // Start the context
        context.start();

        // Test the route

        template.sendBody("direct:addBook",book);


        // Assert the result using the mock endpoint

        assertMockEndpointsSatisfied();

        Books book1 = (Books) getMockEndpoint("mock:getAddBookDetailsResult").getExchanges().get(0).getIn().getBody();

        assertEquals(12, book1.getBook_Id());
    }


}
