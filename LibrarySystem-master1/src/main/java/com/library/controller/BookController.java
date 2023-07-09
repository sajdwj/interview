package com.library.controller;

import com.library.bean.Book;
import com.library.bean.Lend;
import com.library.bean.ReaderCard;
import com.library.service.BookService;
import com.library.service.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private LendService lendService;

    private Date getDate(String pubstr) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(pubstr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    @RequestMapping("/querybook1.html")
    public ModelAndView queryBookDo(String searchWord) {
        boolean a = bookService.matchBook(searchWord);
        if (a) {
            ModelAndView book_data = new ModelAndView("admin_books");
            ArrayList<Book> books = bookService.queryBook(searchWord);
            book_data.addObject("books", books);
            return book_data;
        } else {
            return new ModelAndView("admin_books", "error", "管理员：选择图书不存在");
        }
    }

    @RequestMapping("/reader_querybook_do.html")
    public ModelAndView readerQueryBookDo(String searchWord) {
        boolean b = bookService.matchBook(searchWord);
        if (b) {
            ModelAndView book_data = new ModelAndView("reader_books");
            ArrayList<Book> books = bookService.queryBook(searchWord);
            book_data.addObject("books", books);
            return book_data;
        } else {
            return new ModelAndView("reader_books", "error", "读者：选择图书不存在");
        }
    }

    @RequestMapping("/admin_books.html")
    public ModelAndView adminBooks() {
        ModelAndView all_book = new ModelAndView("admin_books");
        ArrayList<Book> books = bookService.getAllBooks();
        all_book.addObject("books", books);
        return all_book;
    }

    @RequestMapping("/book_add.html")
    public ModelAndView addBook() {
        return new ModelAndView("admin_book_add");//返回添加信息表单
    }

    @RequestMapping("/book_add_do.html")
    public String addBookDo(@RequestParam(value = "pubstr") String pubstr, Book book, RedirectAttributes redirectAttributes) {
        book.setPubdate(getDate(pubstr));//返回书的pubdata
        boolean c =bookService.addBook(book);
        if (c) {
            redirectAttributes.addFlashAttribute("succ", "添加成功！");
        } else {
            redirectAttributes.addFlashAttribute("succ", "添加失败！");
        }
        return "redirect:/admin_books.html";
    }

    @RequestMapping("/updatebook.html")
    public ModelAndView bookEdit(HttpServletRequest request) {
        Book book = bookService.getBook(Long.parseLong(request.getParameter("bookId")));
        ModelAndView new_book = new ModelAndView("admin_book_edit");
        new_book.addObject("detail", book);
        return new_book;
    }

    @RequestMapping("/book_edit_do.html")
    public String bookEditDo(@RequestParam(value = "pubstr") String pubstr, Book book, RedirectAttributes redirectAttributes) {
        book.setPubdate(getDate(pubstr));
        boolean d = bookService.editBook(book);
        if (d) {
            redirectAttributes.addFlashAttribute("succ", "修改成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "修改失败！");
        }
        return "redirect:/admin_books.html";
    }

    @RequestMapping("/admin_book_detail.html")
    public ModelAndView adminBookDetail(HttpServletRequest request) {
        Book book = bookService.getBook(Long.parseLong(request.getParameter("bookId")));
        ModelAndView detail_book1 = new ModelAndView("admin_book_detail");
        detail_book1.addObject("detail", book);
        return detail_book1;
    }

    @RequestMapping("/reader_book_detail.html")
    public ModelAndView readerBookDetail(HttpServletRequest request) {
        Book book = bookService.getBook(Long.parseLong(request.getParameter("bookId")));
        ModelAndView detail_book2 = new ModelAndView("reader_book_detail");
        detail_book2.addObject("detail", book);
        return detail_book2;
    }

    @RequestMapping("/admin_header.html")
    public ModelAndView admin_header() {
        return new ModelAndView("admin_header");
    }

    @RequestMapping("/reader_header.html")
    public ModelAndView reader_header() {
        return new ModelAndView("reader_header");
    }

    @RequestMapping("/reader_books.html")
    public ModelAndView readerBooks(HttpServletRequest request) {

        ReaderCard readerCard = (ReaderCard) request.getSession().getAttribute("readercard");
        ArrayList<Book> books = bookService.getAllBooks();
        ArrayList<Lend> myAllLendList = lendService.myLendList(readerCard.getReaderId());
        ArrayList<Long> myLendList = new ArrayList<>();
        for (Lend lend : myAllLendList) {
            // 是否已归还
            if (lend.getBackDate() == null) {
                myLendList.add(lend.getBookId());
            }
        }
        ModelAndView modelAndView = new ModelAndView("reader_books");
        modelAndView.addObject("books", books);
        modelAndView.addObject("myLendList", myLendList);
        return modelAndView;
    }
}
