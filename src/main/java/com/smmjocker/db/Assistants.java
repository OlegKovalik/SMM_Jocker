package com.smmjocker.db;


import javax.persistence.*;

/**
 * Created by Oleg on 20.01.2017.
 */

@Entity
@Table(name = "assistants")
public class Assistants {
    @Id
    @GeneratedValue
    private long id;
    private String login;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Pages page;

    @OneToOne(mappedBy = "assistant", cascade = CascadeType.REFRESH)
    private Tasks task;

    public Assistants() {
    }

    public Assistants(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Pages getPage() {
        return page;
    }

    public void setPage(Pages page) {
        this.page = page;
    }


    public void setTask(Tasks task) {
        this.task = task;
    }

    public Tasks getTask() {
        return task;
    }
}
