package com.zubb.jannarongj.zzubbreceive;

import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by jannarong.j on 10/22/2018.
 */

public class MLocation {
    private String pill ="";
    private String lr = "";
    private String fr = "";
    private String ch = "";
    private String cloc = "";



    private Boolean curLoc = false;


    public String getCloc() {
        return cloc;
    }

    public void setCloc(String cloc) {
        if(getCh().equals("10")){
            this.cloc = "A"+cloc.substring(2,cloc.length());
        }else{
            this.cloc = cloc;
        }

    }

    private String pilC[] = new String[]{"1","2","3","4","5","6","7","8","9"};
    private String arLocation[] = new String[]
            {"1RL-17", "1RL-18", "1RL-19", "1RL-20", "1RL-21", "1RL-22", "1RL-23",
                    "1RL-24", "1RL-25", "1RL-26", "1RL-27", "1RL-28", "1RL-29", "1RR-17", "1RR-18", "1RR-19",
                    "1RR-20", "1RR-21", "1RR-22", "1RR-23", "1RR-24", "1RR-25", "1RR-26", "1RR-27", "1RR-28",
                    "1RR-29", "3FL-04", "3FL-05", "3FL-06", "3FL-07", "3FL-08", "3FL-09", "3FL-10", "3FL-11",
                    "3FL-12", "3FL-13", "3FL-14", "3FL-15", "3FR-04", "3FR-05", "3FR-06", "3FR-07", "3FR-08",
                    "3FR-09", "3FR-10", "3FR-11", "3FR-12", "3FR-13", "3FR-14", "3FR-15", "3RL-17", "3RL-18",
                    "3RL-19", "3RL-20", "3RL-21", "3RL-22", "3RL-23", "3RL-24", "3RL-25", "3RL-26", "3RL-27",
                    "3RL-28", "3RL-29", "3RL-30", "3RR-17", "3RR-18", "3RR-19", "3RR-20", "3RR-21", "3RR-22",
                    "3RR-23", "3RR-24", "3RR-25", "3RR-26", "3RR-27", "3RR-28", "3RR-29", "3RR-30", "4FL-02",
                    "4FL-03", "4FL-04", "4FL-05", "4FL-06", "4FL-07", "4FL-08", "4FL-09", "4FL-10", "4FL-11",
                    "4FL-12", "4FL-13", "4FL-14", "4FL-15", "4FR-02", "4FR-03", "4FR-04", "4FR-05", "4FR-06",
                    "4FR-07", "4FR-08", "4FR-09", "4FR-10", "4FR-11", "4FR-12", "4FR-13", "4FR-14", "4FR-15",
                    "4RL-17", "4RL-18", "4RL-19", "4RL-20", "4RL-21", "4RL-22", "4RL-23", "4RL-24", "4RL-25",
                    "4RL-26", "4RL-27", "4RL-28", "4RL-29", "4RL-30", "4RR-17", "4RR-18", "4RR-19", "4RR-20",
                    "4RR-21", "4RR-22", "4RR-23", "4RR-24", "4RR-25", "4RR-26", "4RR-27", "4RR-28", "4RR-29",
                    "4RR-30", "5FL-01", "5FL-02", "5FL-03", "5FL-04", "5FL-05", "5FL-06", "5FL-07", "5FL-08",
                    "5FL-09", "5FL-10", "5FL-11", "5FL-12", "5FL-13", "5FL-14", "5FL-15", "5FR-01", "5FR-02",
                    "5FR-03", "5FR-04", "5FR-05", "5FR-06", "5FR-07", "5FR-08", "5FR-09", "5FR-10", "5FR-11",
                    "5FR-12", "5FR-13", "5FR-14", "5FR-15", "5RL-17", "5RL-18", "5RL-19", "5RL-20", "5RL-21",
                    "5RL-22", "5RL-23", "5RL-24", "5RL-25", "5RL-26", "5RL-27", "5RL-28", "5RL-29", "5RL-30",
                    "5RL-31", "5RR-17", "5RR-18", "5RR-19", "5RR-20", "5RR-21", "5RR-22", "5RR-23", "5RR-24",
                    "5RR-25", "5RR-26", "5RR-27", "5RR-28", "5RR-29", "5RR-30", "5RR-31", "6RL-18", "6RL-19",
                    "6RL-20", "6RL-21", "6RL-22", "6RL-23", "6RL-24", "6RL-25", "6RL-26", "6RL-27", "6RL-28",
                    "6RL-29", "6RL-30", "6RL-31", "6RL-32", "6RR-18", "6RR-19", "6RR-20", "6RR-21", "6RR-22",
                    "6RR-23", "6RR-24", "6RR-25", "6RR-26", "6RR-27", "6RR-28", "6RR-29", "6RR-30", "6RR-31",
                    "6RR-32", "7RL-18", "7RL-19", "7RL-20", "7RL-21", "7RL-22", "7RL-23", "7RL-24", "7RL-25",
                    "7RL-26", "7RL-27", "7RL-28", "7RL-29", "7RL-30", "7RL-31", "7RL-32", "7RR-18", "7RR-19",
                    "7RR-20", "7RR-21", "7RR-22", "7RR-23", "7RR-24", "7RR-25", "7RR-26", "7RR-27", "7RR-28",
                    "7RR-29", "7RR-30", "7RR-31", "7RR-32", "8RL-17", "8RL-18", "8RL-19", "8RL-20", "8RL-21",
                    "8RL-22", "8RL-23", "8RL-24", "8RL-25", "8RL-26", "8RL-27", "8RL-28", "8RR-17", "8RR-18",
                    "8RR-19", "8RR-20", "8RR-21", "8RR-22", "8RR-23", "8RR-24", "8RR-25", "8RR-26", "8RR-27",
                    "8RR-28", "9RL-17", "9RL-18", "9RL-19", "9RL-20", "9RL-21", "9RL-22", "9RL-23", "9RL-24",
                    "9RL-25", "9RL-26", "9RL-27", "9RL-28", "9RR-17", "9RR-18", "9RR-19", "9RR-20", "9RR-21",
                    "9RR-22", "9RR-23", "9RR-24", "9RR-25", "9RR-26", "9RR-27", "9RR-28", "ARL-17", "ARL-18",
                    "ARL-19", "ARL-20", "ARL-21", "ARL-22", "ARL-23", "ARL-24", "ARL-25", "ARL-26", "ARL-27",
                    "ARL-28", "ARR-17", "ARR-18", "ARR-19", "ARR-20", "ARR-21", "ARR-22", "ARR-23", "ARR-24",
                    "ARR-25", "ARR-26", "ARR-27", "ARR-28"};

    List<String> locList = Arrays.asList(arLocation);
    List<String> numPilList = Arrays.asList(pilC);

    public Boolean getCurLoc() {
        return curLoc;
    }

    public void setCurLoc(Boolean curLoc) {
        this.curLoc = curLoc;
    }

    public Boolean checkLocation(String loc){
        if(loc.length()<6){
            return false;
        }else{
            if(locList.contains(loc)){
                setCurLoc(true);
                return true;
            }else{
                setCurLoc(false);
                return false;
            }
        }

    }

    public String getPill() {

        return pill;
    }

    public void setPill(String pill) {

        if(numPilList.contains(pill)){
            this.pill = "0"+pill;
        }else{
            this.pill = pill;
        }

    }

    public String getLr() {
        return lr;
    }

    public void setLr(String lr) {
        this.lr = lr;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }


}
