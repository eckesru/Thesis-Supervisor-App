package de.iu.iwmb02_iu_betreuer_app.data;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum ThesisStateEnum {
    open(R.string.thesis_state_open),
    consulation(R.string.thesis_state_open),
    registered(R.string.thesis_state_registered),
    submitted(R.string.thesis_state_submitted),
    colloquium(R.string.thesis_state_colloquium),
    completed(R.string.thesis_state_completed),
    canceled(R.string.thesis_state_canceled);

    private int stringResId;

    ThesisStateEnum(int stringResId) {
        this.stringResId = stringResId;
    }

    public int getStringResId(){
        return stringResId;
    }
}
