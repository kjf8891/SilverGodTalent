package com.example.testremote;

/**
 * Created by seyeon on 2017-10-19.
 */

public class Club {
    private String ClubNum;
    private String ClubTitle;
    private String ClubDate;
    private String ClubCity;//자세한 위치는 강의 세부정보안에 넣고 시도/구정도의 위치정보만 강의 목록에서 보여질 수 있게

    public Club(String num, String title, String date, String city){
        this.ClubNum = num;
        this.ClubTitle = title;
        this.ClubDate = date;
        this.ClubCity = city;
    }
//    public Club(String title, String date, String city){
//        this.ClubTitle = title;
//        this.ClubDate = date;
//        this.ClubCity = city;
//    }

    public Club(String num, String title, String city){
        this.ClubTitle = title;
        this.ClubNum = num;
        this.ClubCity = city;
    }
    public Club(String title){
        this.ClubTitle = title;
    }

    public void setClubNum(String num){this.ClubNum = num;}
    public void setClubTitle(String title){this.ClubTitle = title;}
    public void setClubDate(String date){this.ClubDate = date;}
    public void setClubCity(String city){this.ClubCity = city;}

    public String getClubNum(){return ClubNum;}
    public String getClubTitle(){return ClubTitle;}
    public String getClubDate(){return ClubDate;}
    public String getClubCity(){return ClubCity;}
}
