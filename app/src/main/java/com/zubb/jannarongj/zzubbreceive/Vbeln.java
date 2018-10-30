package com.zubb.jannarongj.zzubbreceive;

/**
 * Created by jannarong.j on 10/19/2018.
 */

public class Vbeln {


    public String getVbeln_name() {
        return vbeln_name;
    }

    public void setVbeln_name(String vbeln_name) {
        this.vbeln_name = vbeln_name;
    }

    public String getArktx() {
        return arktx;
    }

    public void setArktx(String arktx) {
        this.arktx = arktx;
    }

    public String getCarlicense() {
        return carlicense;
    }

    public void setCarlicense(String carlicense) {
        this.carlicense = carlicense;
    }

    public String getPosnr() {
        return posnr;
    }

    public void setPosnr(String posnr) {
        this.posnr = posnr;
    }

    private String vbeln_name,arktx,carlicense,posnr;

    public Vbeln(String vbeln_name,String arktx,String carlicense,String posnr){
        this.vbeln_name=vbeln_name;
        this.arktx=arktx;
        this.carlicense = carlicense;
        this.posnr = posnr;
    }
}
