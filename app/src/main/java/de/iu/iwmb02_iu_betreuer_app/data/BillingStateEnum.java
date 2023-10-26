package de.iu.iwmb02_iu_betreuer_app.data;

import android.content.Context;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum BillingStateEnum {
    open(R.string.billing_state_open),
    billed(R.string.billing_state_billed),
    settled(R.string.billing_state_settled);

    private final int stringResId;

    BillingStateEnum(int stringResId) {
        this.stringResId = stringResId;
    }

    public static String getLocalizedString(Context context, String enumName){
        return context.getString(BillingStateEnum.valueOf(enumName).getStringResId());
    }

    private int getStringResId(){
        return stringResId;
    }
}
