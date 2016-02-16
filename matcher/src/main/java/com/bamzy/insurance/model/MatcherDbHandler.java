package com.bamzy.insurance.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jalil on 2/1/2015.
 */

@Component
public class MatcherDbHandler {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<TotanData> totanDataRowMapper = new BeanPropertyRowMapper<>(TotanData.class);

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateShaparakToMatched(ShaparakRecord shaparakRecord) {
        jdbcTemplate.update("update shaparakData set status='matched' where id=?", new Object[]{shaparakRecord.getId()});
    }

    public void updateTotanToMatched(TotanRecord totanRecord) {
        System.out.println("toten record Id: " + totanRecord.getId());
        jdbcTemplate.update("update totanData set status='matched' where id=?", new Object[]{totanRecord.getId()});
    }

    public void addToSuccessFull(FullRecord fullRecord) {
        jdbcTemplate.update("insert into successfullData(shaparakId, totanId, paymentId)" +
                        "values (?, ?, ?)",
                new Object[]{fullRecord.getShaparakRecord().getId(),
                        fullRecord.getTotanRecord().getId(),
                        fullRecord.getShaparakRecord().getKey().getRrn()
                });
    }
    public List<TotanData> getUnpaidSuccessfullData(){
        return jdbcTemplate.query("SELECT totanData.* FROM totanData INNER JOIN successfullData ON totanData.`id` = successfullData.`totanId` AND successfullData.`status` = 'unpaid';", totanDataRowMapper);
    }
    public void changeSuccessfullDataToPaid(List<TotanData> totanData){
        List<Object[]> objects = new ArrayList<>(totanData.size());
        for(TotanData data : totanData)
            objects.add(new Object[]{data.getRrn(), data.getId()});

        jdbcTemplate.batchUpdate("UPDATE successfullData SET `status` = 'paid', `paymentId` = ? WHERE `totanId` = ?;", objects);
    }
}
