package com.share.audiotrackdemo.syfuction.wave.exceptions;

public class UnsupportPeriod extends Exception {
    public UnsupportPeriod(int period) {
        super("Invalid period: " + period + ". (Must greater than 0)");
    }
}
