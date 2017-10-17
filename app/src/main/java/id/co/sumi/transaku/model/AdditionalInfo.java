package id.co.sumi.transaku.model;

import java.io.Serializable;

/**
 * Created by gebriani on 30/03/17.
 */

public class AdditionalInfo implements Serializable {
    private String totalCredit;
    private String totalDebit;
    private String labaRugi;
    private String total;

    public AdditionalInfo(String totalCredit, String totalDebit, String labaRugi) {
        this.totalCredit = totalCredit;
        this.totalDebit = totalDebit;
        this.labaRugi = labaRugi;
    }

    public AdditionalInfo(String total) {
        this.total = total;
    }

    public String getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(String totalCredit) {
        this.totalCredit = totalCredit;
    }

    public String getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(String totalDebit) {
        this.totalDebit = totalDebit;
    }

    public String getLabaRugi() {
        return labaRugi;
    }

    public void setLabaRugi(String labaRugi) {
        this.labaRugi = labaRugi;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

