package com.library.controller;

import com.library.bean.ReaderCard;
import com.library.bean.ReaderInfo;
import com.library.service.LoginService;
import com.library.service.ReaderCardService;
import com.library.service.ReaderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class ReaderController {
    @Autowired
    private ReaderInfoService reader_Info_Service;

    @Autowired
    private LoginService login_Service;

    @Autowired
    private ReaderCardService reader_Card_Service;

    private ReaderInfo getReaderInfo(long readerId, String name, String sex, String birth, String address, String phone) {
        ReaderInfo readerInfo = new ReaderInfo();
        Date date = new Date();
        try {
            SimpleDateFormat df = new SimpleDateFormat("xxx-xx-xx");
            date = df.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        readerInfo.setPhone(phone);
        readerInfo.setSex(sex);
        readerInfo.setBirth(date);
        readerInfo.setAddress(address);
        readerInfo.setName(name);
        readerInfo.setReaderId(readerId);
        return readerInfo;
    }

    @RequestMapping("allreaders.html")
    public ModelAndView allBooks() {
        ModelAndView allBooks1 = new ModelAndView("admin_readers");
        ArrayList<ReaderInfo> readers = reader_Info_Service.readerInfos();
        allBooks1.addObject("readers", readers);
        return allBooks1;
    }

    @RequestMapping("reader_delete.html")
    public String readerDelete(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        long readerId = Long.parseLong(request.getParameter("readerId"));
        Boolean a =reader_Info_Service.deleteReaderInfo(readerId) && reader_Card_Service.deleteReaderCard(readerId);
        if (a) {
            redirectAttributes.addFlashAttribute("succ", "删除成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "删除失败！");
        }
        return "redirect:/allreaders.html";
    }

    @RequestMapping("/reader_info.html")
    public ModelAndView toReaderInfo(HttpServletRequest request) {
        ReaderCard reader_Card = (ReaderCard) request.getSession().getAttribute("readercard");
        ReaderInfo reader_Info = reader_Info_Service.getReaderInfo(reader_Card.getReaderId());
        ModelAndView reader_Info1 = new ModelAndView("reader_info");
        reader_Info1.addObject("readerinfo", reader_Info);
        return reader_Info1;
    }

    @RequestMapping("reader_edit.html")
    public ModelAndView readerInfoEdit(HttpServletRequest request) {
        long reader_Id = Long.parseLong(request.getParameter("readerId"));
        ReaderInfo reader_Info = reader_Info_Service.getReaderInfo(reader_Id);
        ModelAndView reader_edit1= new ModelAndView("admin_reader_edit");
        reader_edit1.addObject("readerInfo", reader_Info);
        return reader_edit1;
    }

    @RequestMapping("reader_edit_do.html")
    public String readerInfoEditDo(HttpServletRequest request, String name, String sex, String birth, String address, String phone, RedirectAttributes redirectAttributes) {
        long readerId = Long.parseLong(request.getParameter("readerId"));
        ReaderInfo readerInfo = getReaderInfo(readerId, name, sex, birth, address, phone);
        boolean b = reader_Info_Service.editReaderInfo(readerInfo) &&reader_Info_Service.editReaderCard(readerInfo);
        if (b) {
            redirectAttributes.addFlashAttribute("succ", "读者信息修改成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "读者信息修改失败！");
        }
        return "redirect:/allreaders.html";
    }

    @RequestMapping("reader_add.html")
    public ModelAndView readerInfoAdd() {
        return new ModelAndView("admin_reader_add");
    }

    @RequestMapping("reader_add_do.html")
    public String readerInfoAddDo(String name, String sex, String birth, String address, String phone, String password, RedirectAttributes redirectAttributes) {
        ReaderInfo reader_Info = getReaderInfo(0, name, sex, birth, address, phone);
        long readerId = reader_Info_Service.addReaderInfo(reader_Info);
        reader_Info.setReaderId(readerId);
        boolean c = readerId > 0 && reader_Card_Service.addReaderCard(reader_Info, password);
        if (c) {
            redirectAttributes.addFlashAttribute("succ", "添加读者信息成功！");
        } else {
            redirectAttributes.addFlashAttribute("succ", "添加读者信息失败！");
        }
        return "redirect:/allreaders.html";
    }

    @RequestMapping("reader_info_edit.html")
    public ModelAndView readerInfoEditReader(HttpServletRequest request) {
        ReaderCard reader_Card = (ReaderCard) request.getSession().getAttribute("readercard");
        ReaderInfo reader_Info = reader_Info_Service.getReaderInfo(reader_Card.getReaderId());
        ModelAndView modelAndView = new ModelAndView("reader_info_edit");
        modelAndView.addObject("readerinfo", reader_Info);
        return modelAndView;
    }

    @RequestMapping("reader_edit_do_r.html")
    public String readerInfoEditDoReader(HttpServletRequest request, String name, String sex, String birth, String address, String phone, RedirectAttributes redirectAttributes) {
        ReaderCard reader_Card = (ReaderCard) request.getSession().getAttribute("readercard");
        ReaderInfo reader_Info = getReaderInfo(reader_Card.getReaderId(), name, sex, birth, address, phone);
        boolean d =reader_Info_Service.editReaderInfo(reader_Info) && reader_Info_Service.editReaderCard(reader_Info);
        if (d) {
            ReaderCard readerCardNew = login_Service.findReaderCardByReaderId(reader_Info.getReaderId());
            request.getSession().setAttribute("readercard", readerCardNew);
            redirectAttributes.addFlashAttribute("succ", "信息修改成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "信息修改失败！");
        }
        return "redirect:/reader_info.html";
    }
}
