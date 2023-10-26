package de.iu.iwmb02_iu_betreuer_app.data;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum BillingStateEnum {
    open(R.string.billing_state_open),
    billed(R.string.billing_state_billed),
    settled(R.string.billing_state_settled);

    private int stringResId;

    BillingStateEnum(int stringResId) {
        this.stringResId = stringResId;
    }

    public int getStringResId(){
        return stringResId;
    }
}
