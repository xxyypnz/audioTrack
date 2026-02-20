package com.share.audiotrackdemo.syfuction.wave.exceptions;

public class InvalidCalibrationFileFormat extends Exception {
    public InvalidCalibrationFileFormat(int lineNum, String Content) {
        super("Invalid Format: '" + Content + "'. (line:" + lineNum + ")");
    }
}
