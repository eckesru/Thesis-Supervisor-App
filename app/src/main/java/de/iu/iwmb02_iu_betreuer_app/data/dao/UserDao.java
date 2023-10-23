package de.iu.iwmb02_iu_betreuer_app.data.dao;

import de.iu.iwmb02_iu_betreuer_app.model.User;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public interface UserDao {
    void getUser(String userId, Callback<User> callback);
}
