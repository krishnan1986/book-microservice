package com.store.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.book.domain.Book;
import com.store.book.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;



    public BookController(BookService bookService) {

        this.bookService = bookService;
    }

    // create a new book endpoint

    @PostMapping(value = "/addBooks")
    public void createBook(@RequestBody String request){
        // to do
        // create a book repository and save it into H2 DB
        try {
            ObjectMapper mapper = new ObjectMapper();
            Book book1 = mapper.readValue(request, Book.class);
            bookService.createAnewBookInDB(book1);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @GetMapping(value = "/books")
    public List<Book> getAllBooks(){

        return bookService.getAllBooksFromDB();
    }


    //list of books written by an author

    @GetMapping(value = "/books/{authorName}")
    public List<Book> getAllBooksByAuthor(@PathVariable String authorName){
        return bookService.getBooksByAuthor(authorName);
    }


    @GetMapping(value = "/book/{id}")
    public Book getBookById(@PathVariable String id){
        return bookService.findBookByBookId(id);
    }

    @DeleteMapping(value = "/deleteBook/{id}")
    public void deleteBookById(@PathVariable String id){
        // to do

        bookService.deleteBookByid(id);
    }

    // update book by id
    @PutMapping(value = "/updateBook/{id}")
    public Book updateBook(@PathVariable String id, @RequestBody Book book){
        return bookService.updateBook(id,book);
    }


    //get author name of book
    @GetMapping(value = "/fetch/author/{name}")
    public String fetchAuthor(@PathVariable String name){
        return bookService.getAuthorName(name);
    }

    @GetMapping(value = "/fetch/authors")
    public List<Object>fetchAuthors(){
        return bookService.getAuthors();
    }
}
