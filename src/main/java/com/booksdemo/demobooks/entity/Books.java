package com.booksdemo.demobooks.entity;




public class Books {

    private long book_Id;

    public long getBook_Id() {
        return book_Id;
    }

    public void setBook_Id(long book_Id) {
        this.book_Id = book_Id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private String book_name;

    public Books(long book_Id, String book_name, String author_name, double price) {
        this.book_Id = book_Id;
        this.book_name = book_name;
        this.author_name = author_name;
        this.price = price;
    }

    private String author_name;
    private double price;

    public Books() {

    }
}
