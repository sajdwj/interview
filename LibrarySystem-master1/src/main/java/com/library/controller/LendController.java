package com.library.controller;

import com.library.bean.ReaderCard;
import com.library.service.BookService;
import com.library.service.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LendController {
    @Autowired
    private BookService bookService;
    @Autowired
    private LendService lendService;


    @RequestMapping("/deletebook.html")
    public String deleteBook(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        boolean a =bookService.deleteBook(Long.parseLong(request.getParameter("bookId")));
        if (a) {
            redirectAttributes.addFlashAttribute("succ", "删除成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "删除失败！");
        }
        return "redirect:/admin_books.html";
    }

    @RequestMapping("/lendlist.html")
    public ModelAndView lendList(HttpServletRequest request) {
        ModelAndView lendList1 = new ModelAndView("admin_lend_list");
        lendList1.addObject("list", lendService.lendList());
        return lendList1;
    }

    @RequestMapping("/mylend.html")
    public ModelAndView myLend(HttpServletRequest request) {
        ReaderCard readerCard = (ReaderCard) request.getSession().getAttribute("readercard");
        ModelAndView mylend1 = new ModelAndView("reader_lend_list");
        mylend1.addObject("list", lendService.myLendList(readerCard.getReaderId()));
        return mylend1;
    }

    @RequestMapping("/deletelend.html")
    public String deleteLend(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (lendService.deleteLend(Long.parseLong(request.getParameter("serNum"))) > 0) {
            redirectAttributes.addFlashAttribute("succ", "删除成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "删除失败！");
        }
        return "redirect:/lendlist.html";
    }

    @RequestMapping("/lendbook.html")
    public String bookLend(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        long bookId = Long.parseLong(request.getParameter("bookId"));
        long readerId = ((ReaderCard) request.getSession().getAttribute("readercard")).getReaderId();
        boolean b =lendService.lendBook(bookId, readerId);
        if (b) {
            redirectAttributes.addFlashAttribute("succ", "借阅成功！");
        } else {
            redirectAttributes.addFlashAttribute("succ", "借阅成功！");
        }
        return "redirect:/reader_books.html";
    }

    @RequestMapping("/returnbook.html")
    public String bookReturn(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        long bookId = Long.parseLong(request.getParameter("bookId"));
        long readerId = ((ReaderCard) request.getSession().getAttribute("readercard")).getReaderId();
        boolean c = lendService.returnBook(bookId, readerId);
        if (c) {
            redirectAttributes.addFlashAttribute("succ", "归还成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "归还失败！");
        }
        return "redirect:/reader_books.html";
    }
}
