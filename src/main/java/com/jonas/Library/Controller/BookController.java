package com.jonas.Library.Controller;

import com.jonas.Library.Repository.BookRepository;
import com.jonas.Library.payload.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    // Konstruktör för att injicera BookRepository
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Endpoint för att lägga till en bok
    @PostMapping("")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        // Sparar boken och får tillbaka den sparade boken
        Book respBook = bookRepository.save(book);
        return ResponseEntity.status(201).body(respBook); // Returnera boken till klienten med status 201 (Created)
    }

    // Endpoint för att hämta alla böcker
    @GetMapping("")
    public ResponseEntity<List<Book>> getBooks() {
        // Hämtar alla böcker från databasen
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.status(200).body(books); // Returnera böckerna till klienten med status 200 (OK)
    }

    // Endpoint för att hämta en specifik bok med ett visst ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getOneBook(@PathVariable long id) {
        // Hämtar en specifik bok med det angivna ID:et, eller en tom bok om den inte finns
        Book book = bookRepository.findById(id).orElse(new Book());
        if (book.getId() != null) return ResponseEntity.status(200).body(book); // Returnera boken om den finns med status 200 (OK)
        else return ResponseEntity.status(204).body(book); // Returnera tom bok om den inte finns med status 204 (No Content)
    }

    // Endpoint för att redigera en specifik bok med ett visst ID
    @PatchMapping("/{id}")
    public ResponseEntity<Book> editOneBook (
            @PathVariable long id,
            @RequestBody Book payload
    ) {
        // Hämtar den valda boken från databasen
        Book book = bookRepository.findById(id).orElse(new Book());
        if (book.getId() == null) return ResponseEntity.status(404).body(book); // Returnera 404 (Not Found) om boken inte finns

        // Uppdatera bokens titel och författare med den nya informationen
        if (payload.getTitle() != null) book.setTitle(payload.getTitle());
        if (payload.getAuthor() != null) book.setAuthor(payload.getAuthor());
        if (payload.getAudioBook() != null) book.setAudioBook(payload.getAudioBook());
        bookRepository.save(book); // Spara den uppdaterade boken till databasen

        return ResponseEntity.status(200).body(book); // Returnera den uppdaterade boken med status 200 (OK)
    }

    // Endpoint för att ta bort en bok med ett visst ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        try {
            // Hämtar den valda boken från databasen
            Book book = bookRepository.findById(id).orElseThrow();

            // Radera den valda boken från databasen
            bookRepository.deleteById(id);

            // Returnera en framgångsrik response med status 200 (OK)
            return ResponseEntity.status(200).body("The selected item has been removed");

        } catch(NoSuchElementException e) {
            // Returnera en felmeddelande om boken inte hittades med status 404 (Not Found)
            return ResponseEntity.status(404).body("Det valda objektet finns inte. Kontrollera ID värdet i URL");
        }
    }

    // Ny endpoint för att lägga till en ny bok
    @PostMapping("/add")
    public ResponseEntity<Book> addNewBook(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam boolean audioBook) {

        // Skapa en ny bok med angiven titel, författare och information om det är en ljudbok
        Book newBook = new Book();
        newBook.setTitle(title);
        newBook.setAuthor(author);
        newBook.setAudioBook(audioBook);

        // Spara den nya boken till databasen
        Book savedBook = bookRepository.save(newBook);

        // Returnera den sparade boken med status 201 (Created)
        return ResponseEntity.status(201).body(savedBook);
    }
}
