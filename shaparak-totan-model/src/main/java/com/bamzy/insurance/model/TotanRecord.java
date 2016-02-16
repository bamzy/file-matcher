package com.bamzy.insurance.model;

import java.util.Map;

/**
 * @author Bamdad
 */
public class TotanRecord {
    private final String accountNumber;
    private final String content;
    private RecordKey recordKey;
    private String id;
    private Map<Object, Object> fields;

    public TotanRecord(RecordKey key, String accountNumber, String content) {
        this.recordKey = key;
        this.accountNumber = accountNumber;
        this.content = content;
    }

    public Map<Object, Object> getFields() {
        return fields;
    }

    public void setFields(Map<Object, Object> fields) {
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public RecordKey getKey() {
        return recordKey;
    }

    public void setRecordKey(RecordKey recordKey) {
        this.recordKey = recordKey;
    }
}
