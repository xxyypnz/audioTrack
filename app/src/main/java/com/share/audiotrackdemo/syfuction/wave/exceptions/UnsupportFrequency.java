package com.share.audiotrackdemo.syfuction.wave.exceptions;

public class UnsupportFrequency extends Exception {
    public UnsupportFrequency(int freq) {
        super("Invalid Freq: " + freq + ". (Must between 20 and 12000)");
    }
}
