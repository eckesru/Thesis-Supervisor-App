package de.iu.iwmb02_iu_betreuer_app.data;

import android.content.Context;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum ThesisStateEnum {
    open(R.string.thesis_state_open),
    consultation(R.string.thesis_state_consultation),
    registered(R.string.thesis_state_registered),
    submitted(R.string.thesis_state_submitted),
    colloquium(R.string.thesis_state_colloquium),
    completed(R.string.thesis_state_completed),
    canceled(R.string.thesis_state_canceled);

    private final int stringResId;

    ThesisStateEnum(int stringResId) {
        this.stringResId = stringResId;
    }

    public static String getLocalizedString(Context context, String enumName){
        return context.getString(ThesisStateEnum.valueOf(enumName).getStringResId());
    }

    private int getStringResId(){
        return stringResId;
    }
}
