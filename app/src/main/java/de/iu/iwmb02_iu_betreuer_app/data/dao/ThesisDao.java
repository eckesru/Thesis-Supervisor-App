package de.iu.iwmb02_iu_betreuer_app.data.dao;

import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public interface ThesisDao {
    void getThesis(String thesisId, Callback<Thesis> callback);
    void saveNewThesis(Thesis thesis);
    void updateThesis(String thesisId, Thesis thesis);

}

