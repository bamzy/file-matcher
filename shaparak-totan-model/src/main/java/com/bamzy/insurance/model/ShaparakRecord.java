package com.bamzy.insurance.model;

import java.util.Map;

/**
 * @author Bamdad
 */
public class ShaparakRecord {

    private final String pspCode;
    private RecordKey key;
    private String id;
    private Map<Object, Object> fields;

    public ShaparakRecord(RecordKey key, String pspCode) {

        this.key = key;
        this.pspCode = pspCode;
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

    public String getPspCode() {
        return pspCode;
    }

    public RecordKey getKey() {
        return key;
    }

}
