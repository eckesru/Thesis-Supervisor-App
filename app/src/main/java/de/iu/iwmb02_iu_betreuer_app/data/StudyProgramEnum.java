package de.iu.iwmb02_iu_betreuer_app.data;

import android.content.Context;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum StudyProgramEnum {
    architecture(R.string.studyprogram_architecture),
    computerscience(R.string.studyprogram_computerscience),
    history(R.string.studyprogram_history),
    marketing(R.string.studyprogram_marketing),
    mathematics(R.string.studyprogram_mathematics),
    psychology(R.string.studyprogram_psychology);



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
