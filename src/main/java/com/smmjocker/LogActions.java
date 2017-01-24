package com.smmjocker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Oleg on 19.01.2017.
 */
public class LogActions {
    private String fName;
    private boolean append;
    private PrintStream ps;

    public LogActions(String fName) {
        this.fName = fName;


        File f = new File(fName);
        f.getParentFile().mkdirs();

        try {
            ps = new PrintStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void info(String s) {
        ps.println("[INFO " + getPrefix() + "] " + s);
        ps.flush();
    }

    public void error(String s) {
        ps.println("[ERROR " + getPrefix() + "] " + s);
        ps.flush();
    }

    public void warn(String s) {
        ps.println("[WARN " + getPrefix() + "] " + s);
        ps.flush();
    }

    public void debug(String s) {
        ps.println("[DEBUG " + getPrefix() + "] " + s);
        ps.flush();
    }

    public void close() {
        ps.flush();
        ps.close();
    }

    private String getPrefix() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date()) + " " + Thread.currentThread().getName();
    }

    public static String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
        return dateFormat.format(new Date());
    }
}
