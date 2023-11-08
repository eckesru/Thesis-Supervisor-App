package de.iu.iwmb02_iu_betreuer_app.data;

import android.content.Context;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum StudyFieldEnum {
    architecture(R.string.studyfield_architecture),
    business_management(R.string.studyfield_business_management),
    computersciences_softwaredevelopment(R.string.studyfield_computersciences_softwaredevelopment),
    datascience_ai(R.string.studyfield_datascience_ai),
    design_media(R.string.studyfield_design_media),
    engineeringsciences(R.string.studyfield_engineeringsciences),
    marketing_communication(R.string.studyfield_marketing_communication),
    personell_law(R.string.studyfield_personell_law),
    planning_controlling(R.string.studyfield_planning_controlling),
    projectmanagement(R.string.studyfield_projectmanagement);

    private final int stringResId;

    StudyFieldEnum(int stringResId) {
        this.stringResId = stringResId;
    }

    public static String getLocalizedString(Context context, String enumName){
        return context.getString(StudyFieldEnum.valueOf(enumName).getStringResId());
    }

    public int getStringResId(){
        return stringResId;
    }

}
