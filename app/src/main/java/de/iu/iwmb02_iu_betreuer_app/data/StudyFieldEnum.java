package de.iu.iwmb02_iu_betreuer_app.data;

import android.content.Context;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum StudyFieldEnum {
    engineeringsciences(R.string.studyfield_engineeringsciences),
    planning_controlling(R.string.studyfield_planning_controlling),
    datascience_ai(R.string.studyfield_datascience_ai),
    design_media(R.string.studyfield_design_media),
    business_management(R.string.studyfield_business_management),
    personell_law(R.string.studyfield_personell_law);

    private final int stringResId;

    StudyFieldEnum(int stringResId) {
        this.stringResId = stringResId;
    }

    public static String getLocalizedString(Context context, String enumName){
        return context.getString(StudyFieldEnum.valueOf(enumName).getStringResId());
    }

    private int getStringResId(){
        return stringResId;
    }

}
