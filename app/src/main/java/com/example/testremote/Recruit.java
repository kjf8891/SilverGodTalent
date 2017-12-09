package com.example.testremote;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by user on 2016-06-06.
 */
public class Recruit {

    private String _noticeRecruitNum;
    private String _noticeRecruitContent;
    private String _noticeRecruitTitle;
    private String _noticeRecruitWorkingDate;
    private String _noticeRecruitWriter;
    private String _Rlatitude;
    private String _Rlongitude;
    private String _noticeRecruitCity;
    private String _RInterest;


    public Recruit(String noticeRecruitNum, String noticeRecruitWriter, String noticeRecruitContent, String noticeRecruitTitle, String noticeRecruitWorkingDate, String noticeInterest){
        this._noticeRecruitNum = noticeRecruitNum;
        this._noticeRecruitContent = noticeRecruitContent;
        this._noticeRecruitTitle = noticeRecruitTitle;
        this._noticeRecruitWorkingDate = noticeRecruitWorkingDate;
        this._noticeRecruitWriter = noticeRecruitWriter;
        this._RInterest = noticeInterest;

    }

    public Recruit(String noticeRecruitNum, String title, String date, String city,String lati, String longi,String noticeInterest){
        this._noticeRecruitNum = noticeRecruitNum;
        this._noticeRecruitCity = city;
        this._noticeRecruitTitle = title;
        this._noticeRecruitWorkingDate = date;
        this._Rlatitude = lati;
        this._Rlongitude = longi;
        this._RInterest = noticeInterest;

    }


    // public void setnoticeRecruitNum(String noticeRecruitNum){this._noticeRecruitNum = noticeRecruitNum;}
    public void setnoticeRecruitWorkingDate(String noticeRecruitWorkingDate){this._noticeRecruitWorkingDate = noticeRecruitWorkingDate;}
    public void setnoticeRecruitWriter(String noticeRecruitWriter){this._noticeRecruitWriter = noticeRecruitWriter;}
    public void setnoticeRecruitContent(String noticeRecruitContent){this._noticeRecruitContent = noticeRecruitContent;}
    public void setnoticeRecruitTitle(String noticeRecruitTitle){this._noticeRecruitTitle = noticeRecruitTitle;}

    public String getnoticeRecruitNum(){return _noticeRecruitNum;}
    public String getnoticeRecruitWriter(){return _noticeRecruitWriter;}
    public String getnoticeRecruitWorkingDate(){return _noticeRecruitWorkingDate;}
    public String getnoticeRecruitContent(){return _noticeRecruitContent;}
    public String getnoticeRecruitTitle(){return _noticeRecruitTitle;}
    public LatLng getLatLng(){

        LatLng position = new LatLng(Double.parseDouble(_Rlatitude),Double.parseDouble(_Rlongitude));

        return position;
    }
    public String getnoticeRecruitCity(){return _noticeRecruitCity;}
    public String getnoticeInterest(){return _RInterest;}
}