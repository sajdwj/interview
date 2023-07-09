package com.library.service;

import com.library.bean.Lend;
import com.library.dao.LendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LendService {
    @Autowired
    private LendDao lendDao;

    public boolean returnBook(long bookId, long readerId){
        boolean a =lendDao.returnBookOne(bookId, readerId)>0;
        boolean b =lendDao.returnBookTwo(bookId)>0;
        return a && b;
    }

    public boolean lendBook(long bookId,long readerId){
        boolean c =lendDao.lendBookOne(bookId,readerId)>0;
        boolean d =lendDao.lendBookTwo(bookId)>0;
        return c && d;
    }

    public ArrayList<Lend> lendList(){
        return lendDao.lendList();
    }
    public ArrayList<Lend> myLendList(long readerId){
        return lendDao.myLendList(readerId);
    }

    public int deleteLend(long serNum) {
        return lendDao.deleteLend(serNum);
    }

}
