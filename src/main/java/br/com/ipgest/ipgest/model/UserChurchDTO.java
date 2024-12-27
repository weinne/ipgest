package br.com.ipgest.ipgest.model;

import br.com.ipgest.ipgest.domain.Church;
import br.com.ipgest.ipgest.domain.User;

public class UserChurchDTO {
    private User user;
    private Church church;

    // Getters e Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Church getChurch() {
        return church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }
}