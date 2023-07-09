package com.library.service;

import com.library.bean.ReaderInfo;
import com.library.dao.ReaderCardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReaderCardService {
    @Autowired
    private ReaderCardDao readerCardDao;

    public boolean addReaderCard(ReaderInfo readerInfo, String password){
        if(readerCardDao.addReaderCard(readerInfo,password)>0) {
            return true;
        }
        else{
            return false;
        }
    }
    public boolean updatePassword(long readerId, String password){
        if(readerCardDao.resetPassword(readerId,password)>0) {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean deleteReaderCard(long readerId) {
        return readerCardDao.deleteReaderCard(readerId) > 0;
    }
}
