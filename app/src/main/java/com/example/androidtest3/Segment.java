package com.example.androidtest3;

public class Segment {
    float segStartX;
    float segStartY;
    float segEndX;
    float segEndY;
    String segColor;
    float segRadio;

    public Segment() {
        this.segStartX = 0;
        this.segStartY = 0;
        this.segEndX = 0;
        this.segEndY = 0;
        this.segColor = "#000000";
        this.segRadio = 5;
    }

    public Segment(float segStartX, float segStartY, float segEndX, float segEndY) {
        this.segStartX = segStartX;
        this.segStartY = segStartY;
        this.segEndX = segEndX;
        this.segEndY = segEndY;
        this.segColor = "#000000";
        this.segRadio = 5;
    }

    public Segment(float segStartX, float segStartY, float segEndX, float segEndY, String segColor, float segRadio) {
        this.segStartX = segStartX;
        this.segStartY = segStartY;
        this.segEndX = segEndX;
        this.segEndY = segEndY;
        this.segColor = segColor;
        this.segRadio = segRadio;
    }

    public float getSegStartX() {
        return segStartX;
    }

    public void setSegStartX(float segStartX) {
        this.segStartX = segStartX;
    }

    public float getSegStartY() {
        return segStartY;
    }

    public void setSegStartY(float segStartY) {
        this.segStartY = segStartY;
    }

    public float getSegEndX() {
        return segEndX;
    }

    public void setSegEndX(float segEndX) {
        this.segEndX = segEndX;
    }

    public float getSegEndY() {
        return segEndY;
    }

    public void setSegEndY(float segEndY) {
        this.segEndY = segEndY;
    }

    public String getSegColor() {
        return segColor;
    }

    public void setSegColor(String segColor) {
        this.segColor = segColor;
    }

    public float getSegRadio() {
        return segRadio;
    }

    public void setSegRadio(float segRadio) {
        this.segRadio = segRadio;
    }
}
