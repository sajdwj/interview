package com.library.service;

import com.library.bean.Book;
import com.library.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BookService {
    @Autowired
    private BookDao bookDao;

    public ArrayList<Book> queryBook(String searchWord) {
        return bookDao.queryBook(searchWord);
    }

    public ArrayList<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    public boolean matchBook(String searchWord) {
        if(bookDao.matchBook(searchWord) > 0){
            return true;
        }
        else{
            return false;
        }

    }

    public boolean addBook(Book book) {
        if(bookDao.addBook(book) > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public Book getBook(Long bookId) {
        return bookDao.getBook(bookId);
    }

    public boolean editBook(Book book) {

        if(bookDao.editBook(book) > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean deleteBook(Long bookId) {
        if(bookDao.deleteBook(bookId) > 0){
            return true;
        }
        else{
            return false;
        }
    }

}
