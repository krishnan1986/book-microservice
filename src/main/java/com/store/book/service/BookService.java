package com.store.book.service;



import com.store.book.domain.Book;
import com.store.book.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class BookService {


    private final BookRepository bookRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    RestTemplate restTemplate = new RestTemplate();


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // get all books from H2 DB
    public List<Book> getAllBooksFromDB(){
        return (List<Book>) bookRepository.findAll();
    }


    // create a new book from request
    public void createAnewBookInDB(Book book){
       // book.setId(new Random().nextLong());
        bookRepository.save(book);
    }
    // getting book by id
    public Book findBookByBookId(String id){
       Optional<Book> book = bookRepository.findById(Long.valueOf(id));

       if(book.isPresent()){
           return book.get();
       }
       else{
          throw new RuntimeException("No books can be found");
       }
    }

    //delete by id

    public void deleteBookByid(String id){
        bookRepository.deleteById(Long.valueOf(id));
    }

    // update by id
    public Book updateBook(String id, Book book){
        Optional<Book> existingRecord =bookRepository.findById(Long.valueOf(id));
        Book originalBook = null;
        if(existingRecord.isPresent()){
           originalBook=existingRecord.get();
        }
        //update original book properties
        originalBook.setPrice(book.getPrice());
        originalBook.setEdition(book.getEdition());

        bookRepository.save(originalBook);
        return originalBook;
    }

    public String getAuthorName(String name) {
        Book book = bookRepository.findByTitle(name);

        List<ServiceInstance> instances =discoveryClient.getInstances("AUTHOR-SERVICE");
        String serviceUri=instances.get(0).getUri().toString();
      return  restTemplate.getForObject( serviceUri+"/fetch/book/"+book.getAuthorId(),String.class);
    }

    public List getAuthors() {
        String serviceUri= getServiceURI();
         return restTemplate.getForObject(serviceUri+"/getAuthors",List.class);

    }

    public List<Book> getBooksByAuthor(String authorName) {
        String serviceURI=getServiceURI();
        Long authorId= restTemplate.getForObject(serviceURI+"/"+authorName,Long.class);
       return bookRepository.findByAuthorId(authorId.toString());
    }

    public String getServiceURI(){
         List<ServiceInstance> instances =discoveryClient.getInstances("author-service");
         log.info("service uri instances"+instances.get(0).getUri().toString());
        return instances.get(0).getUri().toString();

    }
}
