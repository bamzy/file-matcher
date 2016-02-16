package com.bamzy.insurance.model;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by saeed on 1/25/15.
 */
@Component
public class Repository {
    private final Map<RecordKey, FullRecord> unMatched = new HashMap<RecordKey, FullRecord>();
    private final ArrayBlockingQueue<FullRecord> matched = new ArrayBlockingQueue<FullRecord>(10000);

    private void blockedPutInMatchedQueue(FullRecord fullRecord) {
        boolean flag = false;
        while (!flag) {
            try {
                matched.put(fullRecord);
                flag = true;
            } catch (InterruptedException e) {
                flag = false;
            }
        }
    }

    private boolean nonBlockedPutInMatchedQueue(FullRecord fullRecord) {
        return matched.offer(fullRecord);
    }

    /**
     * @return next matched FullRecord, null if there is no matched in queue.
     */
    public FullRecord next() {
        return matched.poll();
    }

    /**
     * This function puts the matched back in the system,
     *
     * @param fullRecord The record to put back.
     * @return true if putting back was successful, false otherwise.
     */
    public boolean revert(FullRecord fullRecord) {
        return nonBlockedPutInMatchedQueue(fullRecord);
    }

    /**
     * Inserts a TotanRecord to the repository. This function will be blocked till insertion.<br>
     * Block may cause for tow reasons:<br>
     * 1- inner locks <br>
     * 2- Queue is full
     *
     * @param totanRecord The record to be inserted.
     */
    public void insertTotanRecord(TotanRecord totanRecord) {
        synchronized (unMatched) {
            innerTotanInsert(totanRecord);
        }
    }

    /**
     * Inserts a Bulk (Array) of TotanRecord to the repository. This function will be blocked till insertion.<br>
     * Block may cause for tow reasons:<br>
     * 1- inner locks <br>
     * 2- Queue is full
     *
     * @param totanRecords The bulk of records to be inserted.
     */
    public void bulkInsertTotanRecord(TotanRecord[] totanRecords) {
        synchronized (unMatched) {
            for (TotanRecord totanRecord : totanRecords) {
                innerTotanInsert(totanRecord);
            }
        }
    }


    private void innerTotanInsert(TotanRecord totanRecord) {
        RecordKey key = totanRecord.getKey();
        if (unMatched.containsKey(key)) {
            System.out.printf("Matched Tot: " + key);
            FullRecord fullRecord = unMatched.get(key);
            if (fullRecord.getTotanRecord() != null) {
                throw new DuplicateKeyException("Duplicate Totan Record with key= [" + key.toString() + "]");
            }
            unMatched.remove(key);
            fullRecord.setTotanRecord(totanRecord);
            blockedPutInMatchedQueue(fullRecord);
        } else {
            System.out.printf("NOTTT Matched: " + key);
            FullRecord fullRecord = new FullRecord();
            fullRecord.setTotanRecord(totanRecord);
            unMatched.put(key, fullRecord);
        }
        log();
    }

    /**
     * Inserts a ShaparakRecord to the repository. This function will be blocked till insertion.<br>
     * Block may cause for tow reasons:<br>
     * 1- inner locks <br>
     * 2- Queue is full
     *
     * @param shaparakRecord The record to be inserted
     */
    public void insertShaparakRecord(ShaparakRecord shaparakRecord) {
        synchronized (unMatched) {
            innerShaparakInsert(shaparakRecord);
        }
    }

    /**
     * Inserts a Bulk (Array) of ShaparakRecords to the repository. This function will be blocked till insertion.<br>
     * Block may cause for tow reasons:<br>
     * 1- inner locks <br>
     * 2- Queue is full
     *
     * @param shaparakRecords The bulk of records to be inserted.
     */
    public void bulkInsertShaparakRecord(ShaparakRecord[] shaparakRecords) {
        synchronized (unMatched) {
            for (ShaparakRecord shaparakRecord : shaparakRecords) {
                innerShaparakInsert(shaparakRecord);
            }
        }
    }

    private void innerShaparakInsert(ShaparakRecord shaparakRecord) {
        RecordKey key = shaparakRecord.getKey();
        if (unMatched.containsKey(key)) {
            System.out.printf("Matched Wow: " + key);
            FullRecord fullRecord = unMatched.get(key);
            if (fullRecord.getShaparakRecord() != null) {
                throw new DuplicateKeyException("Duplicate Shaparak Record with key= [" + key.toString() + "]");
            }
            unMatched.remove(key);
            fullRecord.setShaparakRecord(shaparakRecord);
            blockedPutInMatchedQueue(fullRecord);
        } else {
            System.out.printf("Not Matched " + key);
            FullRecord fullRecord = new FullRecord();
            fullRecord.setShaparakRecord(shaparakRecord);
            unMatched.put(key, fullRecord);
        }
        log();
    }

    private void log() {
        System.out.println("Size of Matched:" + matched.size() + " Unmatched:" + unMatched.size());
    }

}
