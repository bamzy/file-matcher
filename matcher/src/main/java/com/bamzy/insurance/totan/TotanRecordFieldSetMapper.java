package com.bamzy.insurance.totan;

import com.bamzy.insurance.model.RecordKey;
import com.bamzy.insurance.model.TotanRecord;
import org.springframework.batch.item.file.transform.FieldSet;

import java.util.UUID;

/**
 * Created by jalil on 1/25/2015.
 */
public class TotanRecordFieldSetMapper implements org.springframework.batch.item.file.mapping.FieldSetMapper<TotanRecord> {
    public TotanRecord mapFieldSet(FieldSet fieldSet) {
        RecordKey key = new RecordKey(fieldSet.readString("rrn"),
                fieldSet.readString("traceCode"),
                fieldSet.readString("localDateTime").split(" ")[0],
                fieldSet.readString("amount"),
                fieldSet.readString("terminalCode"),
                fieldSet.readString("acceptorCode"));

        TotanRecord totanRecord = new TotanRecord(key, fieldSet.readString("accountNumber"),
                fieldSet.readString("traceCode") + "|" +
                        fieldSet.readString("description") + "|" +
                        fieldSet.readString("insuranceNumber") + "|" +
                        fieldSet.readString("uniqueTransactionNumber") + "|" +
                        fieldSet.readString("localDateTime") + "|" +
                        fieldSet.readString("gregorianDateTime") + "|" +
                        fieldSet.readString("rrn") + "|" +
                        fieldSet.readString("acceptorCode") + "|" +
                        fieldSet.readString("terminalCode")
        );
        totanRecord.setFields(fieldSet.getProperties());
        totanRecord.setId(UUID.randomUUID().toString());
        return totanRecord;
    }
}
