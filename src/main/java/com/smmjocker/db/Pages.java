package com.smmjocker.db;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg on 20.01.2017.
 */
@Entity
@Table(name = "pages")
public class Pages {
    @Id
    @GeneratedValue
    private long id;
    private String page;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;


    @OneToMany(mappedBy = "page", cascade = CascadeType.REFRESH)
    private List<Assistants> assistants = new ArrayList<>();

    public Pages() {
    }

    public Pages(String page, Users user) {
        this.page = page;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Assistants> getAssistants() {
        return assistants;
    }

    public void setAssistants(List<Assistants> assistants) {
        this.assistants = assistants;
    }
}
