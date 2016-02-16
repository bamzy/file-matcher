package com.bamzy.insurance.model;

/**
 * @author Bamdad
 */
public class RecordKey {
    private String rrn;
    private String traceCode;
    private String localDateTime;
    private String amount;
    private String terminalCode;
    private String acceptorCode;

    RecordKey() {

    }
    public RecordKey(String rrn, String trace, String localDateTime, String amount, String terminalCode, String acceptorCode) {
        this.rrn = rrn;
        this.traceCode = trace;
        this.localDateTime = localDateTime;
        this.amount = amount;
        this.terminalCode = terminalCode;
        this.acceptorCode = acceptorCode;
    }

    public String getAcceptorCode() {
        return acceptorCode;
    }

    public void setAcceptorCode(String acceptorCode) {
        this.acceptorCode = acceptorCode;
    }

    public String getTraceCode() {
        return traceCode;
    }

    public void setTraceCode(String traceCode) {
        this.traceCode = traceCode;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @Override
    public String toString() {
        return "RecordKey{" +
                "rrn='" + rrn + '\'' +
                ", traceCode='" + traceCode + '\'' +
                ", localDateTime='" + localDateTime + '\'' +
                ", amount='" + amount + '\'' +
                ", terminalCode='" + terminalCode + '\'' +
                ", acceptorCode='" + acceptorCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordKey recordKey = (RecordKey) o;

        if (acceptorCode != null ? !acceptorCode.equals(recordKey.acceptorCode) : recordKey.acceptorCode != null)
            return false;
        if (amount != null ? !amount.equals(recordKey.amount) : recordKey.amount != null) return false;
        if (localDateTime != null ? !localDateTime.equals(recordKey.localDateTime) : recordKey.localDateTime != null)
            return false;
        if (rrn != null ? !rrn.equals(recordKey.rrn) : recordKey.rrn != null) return false;
        if (terminalCode != null ? !terminalCode.equals(recordKey.terminalCode) : recordKey.terminalCode != null)
            return false;
        if (traceCode != null ? !traceCode.equals(recordKey.traceCode) : recordKey.traceCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rrn != null ? rrn.hashCode() : 0;
        result = 31 * result + (traceCode != null ? traceCode.hashCode() : 0);
        result = 31 * result + (localDateTime != null ? localDateTime.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (terminalCode != null ? terminalCode.hashCode() : 0);
        result = 31 * result + (acceptorCode != null ? acceptorCode.hashCode() : 0);
        return result;
    }
}
