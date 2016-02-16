package com.bamzy.insurance.matcher;

import com.bamzy.insurance.model.FullRecord;
import com.samanpr.jalalicalendar.JalaliCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Bamdad
 */
@Component
public class BatchFilePayment {

    @Value("${totan.intermediate.account.number}")
    private String intermediateAccount;
    private Logger log = LoggerFactory.getLogger(BatchFilePayment.class);
    private boolean periodicalFlip = true;

    @Bean(name = "job-listener-channel")
    public MessageChannel jobListenerChannel() {
        return new DirectChannel();
    }

    @Bean(name = "trigger-match-channel")
    public MessageChannel triggerMatchChannel() {
        return new DirectChannel();
    }

    @InboundChannelAdapter(value = "trigger-match-channel", poller = @Poller(cron = "${matcher.periodic.cron}"))
    public Long periodicalMatch() {
        periodicalFlip = !periodicalFlip;
        // it returns every other time, to simulate empty channel after polling
        if (periodicalFlip) {
            return null;
        }
        log.debug("triggered a periodical match");
        return new Date().getTime();
    }

    @Filter(inputChannel = "job-listener-channel", outputChannel = "trigger-match-channel")
    public boolean filterJobChannel(Message<?> message) {
        if (message.getPayload() instanceof StepExecution) {
            return true;
//            return ((StepExecution) message.getPayload()).getExitStatus().equals(ExitStatus.COMPLETED);
        }
        return false;
    }

    String getFormattedTime(Calendar jFromCalendar) {
        int month = jFromCalendar.get(Calendar.MONTH) + 1;
        int day = jFromCalendar.get(Calendar.DAY_OF_MONTH);
        return String.format("%d/%02d/%02d", jFromCalendar.get(Calendar.YEAR), month, day);
    }

    public void toFile(List<FullRecord> items) {
        long paySum = 0;
        // Extra Column
        int total = 1;

        for (FullRecord record : items) {
            System.out.println("match found: " + record.getShaparakRecord().getKey());
            paySum += Long.valueOf(record.getShaparakRecord().getKey().getAmount());
            total++;
        }

        if (total == 1) {
            return;
        }
        Writer writer = null;
        try {
            String outputFileName = getOutputFileName();
            log.debug("writing result to file {}", outputFileName);
            writer = new FileWriter("ibank/" + outputFileName);
            StringBuilder builder = new StringBuilder();
            Calendar calendar = new JalaliCalendar();

            String today = getFormattedTime(calendar);
            writer.write(builder.append(total).append(",")
                    .append(paySum).append(",")
                    .append(today).append(",")
                    .append("شرح سند")
                    .append("\n").toString());
            int i = 1;
            writer.write(new StringBuilder().append(i).append(",")
                    .append(intermediateAccount).append(",")
                    .append("D").append(",")
                    .append(paySum).append(",")
                    .append("برداشت")
                    .append("\n").toString());

            System.out.println("number of matches: " + items.size());
            for (FullRecord item : items) {
                i++;
                writer.write(new StringBuilder().append(i).append(",")
                        .append(item.getTotanRecord().getAccountNumber()).append(",")
                        .append("C").append(",")
                        .append(item.getTotanRecord().getKey().getAmount()).append(",")
                        .append(item.getTotanRecord().getContent())
                        .append("\n").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String getOutputFileName() {
        JalaliCalendar calendar = new JalaliCalendar();
        return String.format("payment-%2d%02d%02d-%02d:%02d:%02d.txt",
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }
}
