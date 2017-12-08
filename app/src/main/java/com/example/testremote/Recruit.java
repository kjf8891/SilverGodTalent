package com.example.testremote;

/**
 * Created by user on 2016-06-06.
 */
public class Recruit {

    private String _noticeRecruitNum;
    private String _noticeRecruitContent;
    private String _noticeRecruitTitle;
    private String _noticeRecruitWritingDate;
    private String _noticeRecruitWriter;
    private String _Rlatitude;
    private String _Rlongitude;
    private String _noticeRecruitCity;


    public Recruit(String noticeRecruitNum, String noticeRecruitWriter, String noticeRecruitContent, String noticeRecruitTitle, String noticeRecruitWritingDate){
        this._noticeRecruitNum = noticeRecruitNum;
        this._noticeRecruitContent = noticeRecruitContent;
        this._noticeRecruitTitle = noticeRecruitTitle;
        this._noticeRecruitWritingDate = noticeRecruitWritingDate;
        this._noticeRecruitWriter = noticeRecruitWriter;

    }

    public Recruit(String noticeRecruitNum, String title, String date, String city,String lati, String longi){
        this._noticeRecruitNum = noticeRecruitNum;
        this._noticeRecruitCity = city;
        this._noticeRecruitTitle = title;
        this._noticeRecruitWritingDate = date;
        this._Rlatitude = lati;
        this._Rlongitude = longi;

    }


    // public void setnoticeRecruitNum(String noticeRecruitNum){this._noticeRecruitNum = noticeRecruitNum;}
    public void setnoticeRecruitWritingDate(String noticeRecruitWritingDate){this._noticeRecruitWritingDate = noticeRecruitWritingDate;}
    public void setnoticeRecruitWriter(String noticeRecruitWriter){this._noticeRecruitWriter = noticeRecruitWriter;}
    public void setnoticeRecruitContent(String noticeRecruitContent){this._noticeRecruitContent = noticeRecruitContent;}
    public void setnoticeRecruitTitle(String noticeRecruitTitle){this._noticeRecruitTitle = noticeRecruitTitle;}

    public String getnoticeRecruitNum(){return _noticeRecruitNum;}
    public String getnoticeRecruitWriter(){return _noticeRecruitWriter;}
    public String getnoticeRecruitWritingDate(){return _noticeRecruitWritingDate;}
    public String getnoticeRecruitContent(){return _noticeRecruitContent;}
    public String getnoticeRecruitTitle(){return _noticeRecruitTitle;}
}
