package com.bamzy.insurance.totan;

import com.bamzy.insurance.model.TotanRecord;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by jalil on 1/25/2015.
 */
public class TotanRecordItemProcessor implements ItemProcessor<TotanRecord, TotanRecord> {
    public TotanRecord process(TotanRecord totanRecord) {
        return totanRecord;
    }
}
