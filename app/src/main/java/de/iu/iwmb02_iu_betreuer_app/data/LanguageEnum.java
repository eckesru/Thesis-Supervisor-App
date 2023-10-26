package de.iu.iwmb02_iu_betreuer_app.data;

import de.iu.iwmb02_iu_betreuer_app.R;

public enum LanguageEnum {
    german(R.string.language_german),
    english(R.string.language_english);

    private int stringResId;

    LanguageEnum(int stringResId) {
        this.stringResId = stringResId;
    }

    public int getStringResId(){
        return stringResId;
    }
}

