package de.iu.iwmb02_iu_betreuer_app.data;

import android.content.Context;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum StudyProgramEnum {
    computerscience(R.string.studyprogram_computerscience),
    history(R.string.studyprogram_history),
    mathematics(R.string.studyprogram_mathematics);

    private final int stringResId;

    StudyProgramEnum(int stringResId) {
        this.stringResId = stringResId;
    }

    public static String getLocalizedString(Context context, String enumName){
        return context.getString(StudyProgramEnum.valueOf(enumName).getStringResId());
    }

    private int getStringResId(){
        return stringResId;
    }

}
