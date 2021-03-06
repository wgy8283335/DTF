package com.dtf.manager.log;

import java.io.Serializable;

public class LogMetadata implements Serializable {
    
    private int position;
    
    private int length;
    
    private long timeStamp;
    
    public LogMetadata(final int position, final int length, final long timeStamp) {
        this.position = position;
        this.length = length;
        this.timeStamp = timeStamp;
    }
    
    /**
     * Get position of record in async-request.bin.
     * 
     * @return position of record in async-request.bin
     */
    public int getPosition() {
        return position;
    }

    /**
     * Get length of record in async-request.bin.
     * 
     * @return length of record in async-request.bin
     */
    public int getLength() {
        return length;
    }

    /**
     * Get time stamp.
     * 
     * @return time stamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }
}
