package com.bamzy.insurance.shaparak;

import com.bamzy.insurance.model.ShaparakRecord;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by jalil on 1/25/2015.
 */
public class ShaparakRecordItemProcessor implements ItemProcessor<ShaparakRecord, ShaparakRecord> {

    @Value("${shaparak.intermediate.sheba.number}")
    private String shaparakIntermediateShebaNumber;

    public ShaparakRecord process(ShaparakRecord shaparakRecord) {
        if (!shaparakRecord.getFields().get("IBAN").toString().trim().equals(shaparakIntermediateShebaNumber))
            return null;
        return shaparakRecord;
    }
}
