package com.example.testremote;

/**
 * Created by user on 2016-06-06.
 */
public class NoticeWanted {

    private String _noticeWantedNum;
    private String _noticeWantedContent;
    private String _noticeWantedTitle;
    private String _noticeWantedWritingDate;
    private String _noticeWantedWriter;


    public NoticeWanted(String noticeWantedNum, String noticeWantedWriter, String noticeWantedContent, String noticeWantedTitle, String noticeWantedWritingDate){
        this._noticeWantedNum = noticeWantedNum;
        this._noticeWantedContent = noticeWantedContent;
        this._noticeWantedTitle = noticeWantedTitle;
        this._noticeWantedWritingDate = noticeWantedWritingDate;
        this._noticeWantedWriter = noticeWantedWriter;

    }

   // public void setnoticeWantedNum(String noticeWantedNum){this._noticeWantedNum = noticeWantedNum;}
    public void setnoticeWantedWritingDate(String noticeWantedWritingDate){this._noticeWantedWritingDate = noticeWantedWritingDate;}
    public void setnoticeWantedWriter(String noticeWantedWriter){this._noticeWantedWriter = noticeWantedWriter;}
    public void setnoticeWantedContent(String noticeWantedContent){this._noticeWantedContent = noticeWantedContent;}
    public void setnoticeWantedTitle(String noticeWantedTitle){this._noticeWantedTitle = noticeWantedTitle;}

    public String getnoticeWantedNum(){return _noticeWantedNum;}
    public String getnoticeWantedWriter(){return _noticeWantedWriter;}
    public String getnoticeWantedWritingDate(){return _noticeWantedWritingDate;}
    public String getnoticeWantedContent(){return _noticeWantedContent;}
    public String getnoticeWantedTitle(){return _noticeWantedTitle;}
}
