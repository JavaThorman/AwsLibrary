package com.jonas.Library;

import com.jonas.Library.Controller.BookController;
import com.jonas.Library.Repository.BookRepository;
import com.jonas.Library.payload.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookControllerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBooks() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        ResponseEntity<List<Book>> responseEntity = bookController.getBooks();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    public void testGetOneBook() {
        long id = 1L;
        Book book = new Book();
        book.setId(id);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        ResponseEntity<Book> responseEntity = bookController.getOneBook(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(id, responseEntity.getBody().getId());
    }

    @Test
    public void testAddBook() {
        Book bookToAdd = new Book();
        bookToAdd.setTitle("Test Title");
        bookToAdd.setAuthor("Test Author");
        bookToAdd.setAudioBook(false);

        when(bookRepository.save(bookToAdd)).thenReturn(bookToAdd);

        ResponseEntity<Book> responseEntity = bookController.addBook(bookToAdd);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(bookToAdd, responseEntity.getBody());
    }

    @Test
    public void testDeleteBook() {
        long id = 1L;
        Book bookToDelete = new Book();
        bookToDelete.setId(id);

        when(bookRepository.findById(id)).thenReturn(Optional.of(bookToDelete));

        ResponseEntity<String> responseEntity = bookController.deleteBook(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The selected item has been removed", responseEntity.getBody());
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    public void testEditBook() {
        long id = 1L;
        Book existingBook = new Book();
        existingBook.setId(id);
        existingBook.setTitle("Existing Title");
        existingBook.setAuthor("Existing Author");
        existingBook.setAudioBook(false);

        Book updatedBook = new Book();
        updatedBook.setId(id);
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setAudioBook(true);

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);

        ResponseEntity<Book> responseEntity = bookController.editOneBook(id, updatedBook);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedBook, responseEntity.getBody());
    }
}

