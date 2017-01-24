package com.smmjocker.db;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Oleg on 20.01.2017.
 */
@Entity
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistant_id")
    private Assistants assistant;

    private Date start;

    @Column(name = "per_id")
    private int perId = 1;

    @Column(name = "per_count")
    private int perCount = 3;

    private boolean go = false;

    private Date nextStart;

    private Date lastEnd;

    private Date lastStart;

    public Tasks() {
    }

    public Tasks(Assistants assistant, Date start, int perId, int perCount, boolean go) {
        this.assistant = assistant;
        this.start = start;
        this.perId = perId;
        this.perCount = perCount;
        this.go = go;
        this.nextStart = start;
        this.lastEnd = start;
        this.lastStart = null;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Assistants getAssistant() {
        return assistant;
    }

    public void setAssistant(Assistants assistant) {
        this.assistant = assistant;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public int getPerId() {
        return perId;
    }

    public void setPerId(int perId) {
        this.perId = perId;
    }

    public int getPerCount() {
        return perCount;
    }

    public void setPerCount(int perCount) {
        this.perCount = perCount;
    }

    public boolean isGo() {
        return go;
    }

    public void setGo(boolean go) {
        this.go = go;
    }

    public Date getNextStart() {
        return nextStart;
    }

    public void setNextStart(Date nextStart) {
        this.nextStart = nextStart;
    }

    public Date getLastEnd() {
        return lastEnd;
    }

    public void setLastEnd(Date lastEnd) {
        this.lastEnd = lastEnd;
    }

    public Date getLastStart() {
        return lastStart;
    }

    public void setLastStart(Date lastStart) {
        this.lastStart = lastStart;
    }
}
