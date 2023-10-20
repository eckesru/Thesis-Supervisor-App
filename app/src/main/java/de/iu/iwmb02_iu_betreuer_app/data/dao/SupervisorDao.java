package de.iu.iwmb02_iu_betreuer_app.data.dao;

import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;

public interface SupervisorDao {
    void getSupervisor(String supervisorId);
    void saveSupervisor(Supervisor supervisor);
    void updateSupervisor(String supervisorId, Supervisor supervisor);
}
