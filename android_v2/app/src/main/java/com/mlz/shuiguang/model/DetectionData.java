package com.mlz.shuiguang.model;

import android.content.Context;

import com.mlz.shuiguang.R;

import java.util.Date;

/**
 * Created by dehualai on 12/28/16.
 */

public class DetectionData {

    public static String HAND = "hand";
    public static String FACE = "face";
    public static String EYES = "eyes";
    public static String PRE_NURSING = "pre-nursing";
    public static String POST_NURSING = "post-nursing";
    // required
    private String bodyPart;
    private Date detectionTime;
    private float value;
    private String pre_post;
    // optional
    private String city;
    private String transmission_status;

    public DetectionData(DetectionDataBuilder builder){
        this.bodyPart = builder.bodyPart;
        this.detectionTime = builder.detectionTime;
        this.value = builder.value;
        this.pre_post = builder.pre_post;
        this.city = builder.city;
        this.transmission_status = builder.transmission_status;
    }

    /**
     *  Builder Design Pattern
     * */
    public static class DetectionDataBuilder {
        private String bodyPart;
        private Date detectionTime;
        private float value;
        private String pre_post;
        private String city;
        private String transmission_status;

        public DetectionDataBuilder(String bodyPart, Date detectionTime, float value , String pre_post) {
            this.bodyPart = bodyPart;
            this.detectionTime = detectionTime;
            this.value = value;
            this.pre_post = pre_post;
        }
        public DetectionDataBuilder city(String city){
            this.city = city;
            return this;
        }
        public  DetectionDataBuilder transmission_status(String transmission_status){
            this.transmission_status = transmission_status;
            return this;
        }

        public DetectionData build(){
            return new DetectionData(this);
        }
    }


    public String getBodyPart() {
        return bodyPart;
    }



    public Date getDetectionTime() {
        return detectionTime;
    }

    public float getValue() {
        return value;
    }

    public String getPre_post() {
        return pre_post;
    }

    public String getCity() {
        return city;
    }

    public String getTransmission_status(){return transmission_status; }

    public String toString(){
        return getBodyPart() + " , " +getPre_post() + " , " + (int)getValue() + "%";
    }
}
