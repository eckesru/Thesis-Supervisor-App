package de.iu.iwmb02_iu_betreuer_app.data;

import android.content.Context;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum LanguageEnum {
    german(R.string.language_german),
    english(R.string.language_english);

    private final int stringResId;

    LanguageEnum(int stringResId) {
        this.stringResId = stringResId;
    }

    public static String getLocalizedString(Context context, String enumName){
        return context.getString(LanguageEnum.valueOf(enumName).getStringResId());
    }

    private int getStringResId(){
        return stringResId;
    }

}

