package com.library.service;

import com.library.bean.ReaderInfo;
import com.library.dao.ReaderInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReaderInfoService {
    @Autowired
    private ReaderInfoDao readerInfoDao;

    public ArrayList<ReaderInfo> readerInfos() {
        return readerInfoDao.getAllReaderInfo();
    }

    public boolean deleteReaderInfo(long readerId) {
        if(readerInfoDao.deleteReaderInfo(readerId) > 0) {
            return true;
        }
        else{
            return false;
        }
    }

    public ReaderInfo getReaderInfo(long readerId) {
        return readerInfoDao.findReaderInfoByReaderId(readerId);
    }

    public boolean editReaderInfo(ReaderInfo readerInfo) {
        if(readerInfoDao.editReaderInfo(readerInfo) > 0) {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean editReaderCard(ReaderInfo readerInfo) {
        if(readerInfoDao.editReaderCard(readerInfo) > 0) {
            return true;
        }
        else{
            return false;
        }
    }

    public long addReaderInfo(ReaderInfo readerInfo) {
        return readerInfoDao.addReaderInfo(readerInfo);
    }
}
