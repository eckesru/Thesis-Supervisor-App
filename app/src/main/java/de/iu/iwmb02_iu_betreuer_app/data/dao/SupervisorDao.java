package de.iu.iwmb02_iu_betreuer_app.data.dao;

import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public interface SupervisorDao {
    void getSupervisor(String supervisorId, Callback<Supervisor> callback);
    void saveSupervisor(String supervisorId, Supervisor supervisor);
}

