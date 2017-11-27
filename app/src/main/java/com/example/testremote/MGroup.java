package com.example.testremote;

/**
 * Created by seyeon on 2017-10-19.
 */

public class MGroup {
    private String MentoringNum;
    private String MentoringTitle;
    private String MentoringDate;
    private String MentoringCity;//자세한 위치는 강의 세부정보안에 넣고 시도/구정도의 위치정보만 강의 목록에서 보여질 수 있게

    public MGroup(String num, String title, String date, String city){
        this.MentoringNum = num;
        this.MentoringTitle = title;
        this.MentoringDate = date;
        this.MentoringCity = city;
    }

    public void setMentoringNum(String num){this.MentoringNum = num;}
    public void setMentoringTitle(String title){this.MentoringTitle = title;}
    public void setMentoringDate(String date){this.MentoringDate = date;}
    public void setMentoringCity(String city){this.MentoringCity = city;}

    public String getMentoringNum(){return MentoringNum;}
    public String getMentoringTitle(){return MentoringTitle;}
    public String getMentoringDate(){return MentoringDate;}
    public String getMentoringCity(){return MentoringCity;}
}
