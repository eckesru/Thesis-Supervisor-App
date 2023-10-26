package de.iu.iwmb02_iu_betreuer_app.data;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum StudyProgramEnum {
    computerscience(R.string.studyprogram_computerscience),
    history(R.string.studyprogram_history),
    mathematics(R.string.studyprogram_mathematics);

    private int stringResId;

    StudyProgramEnum(int stringResId) {
        this.stringResId = stringResId;
    }

    public int getStringResId(){
        return stringResId;
    }
}
