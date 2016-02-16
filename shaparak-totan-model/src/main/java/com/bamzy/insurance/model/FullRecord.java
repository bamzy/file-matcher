package com.bamzy.insurance.model;

/**
 * Created by saeed on 1/25/15.
 */

public class FullRecord {
    private TotanRecord totanRecord = null;
    private ShaparakRecord shaparakRecord = null;

    public FullRecord() {
    }

    public TotanRecord getTotanRecord() {
        return totanRecord;
    }

    public void setTotanRecord(TotanRecord totanRecord) {
        this.totanRecord = totanRecord;
    }

    public ShaparakRecord getShaparakRecord() {
        return shaparakRecord;
    }

    public void setShaparakRecord(ShaparakRecord shaparakRecord) {
        this.shaparakRecord = shaparakRecord;
    }
}
