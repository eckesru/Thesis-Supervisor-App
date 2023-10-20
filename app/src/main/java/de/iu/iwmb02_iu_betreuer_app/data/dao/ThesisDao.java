package de.iu.iwmb02_iu_betreuer_app.data.dao;

import de.iu.iwmb02_iu_betreuer_app.model.Thesis;

public interface ThesisDao {
    void getThesis(String thesisId);
    void saveNewThesis(Thesis thesis);
    void updateThesis(String thesisId, Thesis thesis);

}

