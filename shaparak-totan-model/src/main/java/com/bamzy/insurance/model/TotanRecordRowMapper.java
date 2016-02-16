package com.bamzy.insurance.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by jalil on 5/6/2015.
 */
public class TotanRecordRowMapper implements RowMapper<TotanRecord> {

    @Override
    public TotanRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        RecordKey key = new RecordKey(rs.getString("rrn"),
                rs.getString("traceCode"),
                rs.getString("localDateTime"),
                rs.getString("amount"),
                rs.getString("terminalCode"),
                rs.getString("acceptorCode"));
        TotanRecord totanRecord = new TotanRecord(key, rs.getString("accountNumber"), rs.getString("content"));
        totanRecord.setId(rs.getString("id"));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            totanRecord.setFields(objectMapper.readValue(rs.getString("jsonData"), Map.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totanRecord;
    }
}
