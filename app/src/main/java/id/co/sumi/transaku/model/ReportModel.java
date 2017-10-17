package id.co.sumi.transaku.model;

/**
 * Created by gebriani on 30/03/17.
 */

public class ReportModel {
    private String trxType;
    private double credit;
    private double debit;
    private String trxDate;
    private String note;

    public ReportModel(String trxType, double credit, double debit, String trxDate, String note) {
        this.trxType = trxType;
        this.credit = credit;
        this.debit = debit;
        this.trxDate = trxDate;
        this.note = note;
    }

    public String getTrxType() {
        return trxType;
    }

    public void setTrxType(String trxType) {
        this.trxType = trxType;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public String getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(String trxDate) {
        this.trxDate = trxDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
