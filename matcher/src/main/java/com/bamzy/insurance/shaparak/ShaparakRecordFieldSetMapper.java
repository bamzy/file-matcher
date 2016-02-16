package com.bamzy.insurance.shaparak;

import com.bamzy.insurance.model.RecordKey;
import com.bamzy.insurance.model.ShaparakRecord;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.util.UUID;

/**
 * Created by jalil on 1/24/2015.
 */
public class ShaparakRecordFieldSetMapper implements FieldSetMapper<ShaparakRecord> {
    public ShaparakRecord mapFieldSet(FieldSet fieldSet) {
        RecordKey key = new RecordKey(fieldSet.readString("rrn"),
                fieldSet.readString("traceCode"),
                fieldSet.readString("localDate"),
                Integer.toString(Integer.parseInt(fieldSet.readString("amount"))),
                fieldSet.readString("terminalCode"),
                fieldSet.readString("acceptorCode"));
        ShaparakRecord shaparakRecord = new ShaparakRecord(key, fieldSet.readString("pspCode"));
        shaparakRecord.setFields(fieldSet.getProperties());
        shaparakRecord.setId(UUID.randomUUID().toString());
        return shaparakRecord;
    }
}
