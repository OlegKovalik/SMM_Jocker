package com.smmjocker.model;


import com.smmjocker.LogActions;
import com.smmjocker.db.Tasks;
import com.smmjocker.exceptions.*;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Oleg on 14.01.2017.
 */
public class FBInviter implements Runnable {
    protected final static String PHANTOMJS_PATH = "C:/1/phantomjs/bin/phantomjs.exe";
    protected final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:46.0) Gecko/20100101 Firefox/46.0";
    protected final static int MAX_POSTS = 200;

    @Autowired
    private LogActions appLog;


    LogActions invLog;

    private String login;
    private String pass;
    private Date dateStart;
    private Date dateEnd;
    private String page;
    private WebDriver driver;
    private JavascriptExecutor jse;
    private int inv = 0;

    private Long task_id;
    private SessionFactory sessionFact;


    public FBInviter(String login, String pass, Date dateStart, Date dateEnd, String page) {
        this.login = login;
        this.pass = pass;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.page = page;


    }

    public FBInviter(Long task_id, SessionFactory sessionFact) {
        this.task_id = task_id;
        this.sessionFact = sessionFact;
    }

    @Override
    public void run() {

        Session session = sessionFact.openSession();
        session.getTransaction().begin();
        Tasks task = (Tasks) session.get(Tasks.class, task_id);
        task.setLastEnd(null);
        task.setLastStart(new Date());
        session.save(task);
        session.getTransaction().commit();

        login = task.getAssistant().getLogin();
        pass = task.getAssistant().getPassword();
        dateStart = null;
        dateEnd = null;
        page = task.getAssistant().getPage().getPage();

        session.close();


        Thread.currentThread().setName(page);
        init();

        try {
            login();
            stepByStep();
        } catch (InviteLimitException e) {
            System.out.println("Invite Limit");

        } catch (InviteBlockException e) {
            System.out.println("Invite Block");
        } catch (InfiniteLoopException e) {
            System.out.println("Infinite Loop");
        } catch (PostLimitException e) {
            e.printStackTrace();
        } catch (NoLikersException e) {
            e.printStackTrace();
        } finally {
            saveHtmlAndScreenshot();
            stop();
        }

        session = sessionFact.openSession();
        session.getTransaction().begin();
        task = (Tasks) session.get(Tasks.class, task_id);


        task.setLastEnd(new Date());

        Calendar cal = Calendar.getInstance();
        cal.setTime(task.getLastStart());
        cal.add((task.getPerId() == 1) ? Calendar.HOUR : Calendar.DATE, task.getPerCount());
        task.setNextStart(cal.getTime());

        session.save(task);
        session.getTransaction().commit();
        session.close();


    }

//    public void logInit() {
//        invLog = org.apache.log4j.Logger.getLogger(FBInviter.class);
//
//        SimpleLayout layout = new SimpleLayout();
//        FileAppender appender = null;
//        try {
//            appender = new FileAppender(layout, getCurrentTime() + "-" + login + "-" + page + ".log", false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        invLog.addAppender(appender);
//
//    }

//    public void logInit() {
//
//        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//        FileAppender fileAppender = new FileAppender();
//        fileAppender.setContext(loggerContext);
//        fileAppender.setName("timestamp");
//        // set the file name
//        fileAppender.setFile("log/" + getCurrentTime() + "-" + login + "-" + page + ".log");
//
//        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
//        encoder.setContext(loggerContext);
//        encoder.setPattern("%.-19date: %r %thread %level - %msg%n");
//        encoder.start();
//
//        fileAppender.setEncoder(encoder);
//        fileAppender.start();
//
//        // attach the rolling file appender to the logger of your choice
//        invLog = loggerContext.getLogger("Inviter");
//        invLog.addAppender(fileAppender);
//        invLog.info("Log started");
//    }

    public synchronized void init() {
//        logInit();

        invLog = new LogActions("log/" + page + "/" + LogActions.getCurrentTime() + "-" + login + "-" + page + ".log");
        invLog.info("Log started");


        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("takesScreenshot", true);
        caps.setCapability("browserName", "firefox");
        caps.setCapability("useragent", USER_AGENT);
        caps.setBrowserName("firefox");
        //caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", USER_AGENT);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", false);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "browserName", "firefox");
        //caps.setCapability("browserName", BrowserType.FIREFOX);
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PHANTOMJS_PATH);

        driver = new PhantomJSDriver(caps);
        driver.manage().deleteAllCookies();


        invLog.info("Driver started");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        invLog.info("Driver load fb page");
        driver.get("https://www.facebook.com/");

    }

    public void login() throws NoSuchElementException {
        try {
            driver.findElement(By.id("email")).sendKeys(login);
            driver.findElement(By.id("pass")).sendKeys(pass);
            driver.findElement(By.xpath("//label[@id='loginbutton']/input")).submit();
            invLog.info("Logined by " + login);
        } catch (NoSuchElementException e) {
            invLog.error("Error at Login Page");
            saveHtmlAndScreenshot();
            throw new NoSuchElementException("Error at Login Page");
        }


    }

    public void stepByStep() throws NoSuchElementException, InviteLimitException, InviteBlockException, InfiniteLoopException, PostLimitException, NoLikersException {


        invLog.info("Load page " + page);
        driver.get("https://www.facebook.com/" + page + "/posts/");


        WebElement mainContainer = driver.findElement(By.xpath("//div[@id='mainContainer']"));

        int i = 1;
        int noLikers = 0;

        jse = (JavascriptExecutor) driver;
        boolean infiniteLoop = false;

        while (true) {

            if (i > MAX_POSTS) {
                throw new PostLimitException();
            }

            WebElement postTime = null;
            List<WebElement> postLikes = null;
            WebElement postContainer = null;


            try {


                postContainer = driver.findElement(By.xpath("(//div[@class='_1xnd']//div[@class='_4-u2 _4-u8'])[" + i + "]"));
                postTime = postContainer.findElement(By.xpath(".//div[@class='clearfix _5va3']//span[@class='timestampContent']/../.."));
                postLikes = postContainer.findElements(By.xpath(".//div[@class='userContentWrapper _5pcr']//div/a/span[2]/span"));
                infiniteLoop = false;

//                postTime = driver.findElement(By.xpath("(//div[@class='_1xnd']//div[@class='_4-u2 _4-u8'])[" + i + "]//div[@class='clearfix _5va3']//span[@class='timestampContent']/../.."));
//                postLikes = driver.findElements(By.xpath("(//div[@class='_1xnd']//div[@class='_4-u2 _4-u8'])[" + i + "]//div[@class='userContentWrapper _5pcr']//div/a/span[2]/span"));

            } catch (NoSuchElementException e) {
                if (infiniteLoop) {
                    invLog.error("Infinite Loop, last post=" + i);
                    saveHtmlAndScreenshot();
                    throw new InfiniteLoopException();
                }
                infiniteLoop = true;
                invLog.info("Load next posts");
                //saveHtmlAndScreenshot();
                WebElement buttonNext = driver.findElement(By.xpath("//a[@class='pam uiBoxLightblue uiMorePagerPrimary']"));
                jse.executeScript("arguments[0].click();", buttonNext);
                jse.executeScript("arguments[0].scrollIntoView()", buttonNext);
                waitForPageLoad(3000L);
                continue;
            }

            Date postDate = null;

            try {
                String postTimeStr = driver.findElement(By.xpath("(//div[@class='_1xnd']//div[@class='_4-u2 _4-u8'])[" + i + "]//div[@class='clearfix _5va3']//span[@class='timestampContent']/..")).getAttribute("data-utime");
                Long dateUnixPost = Long.parseLong(postTimeStr);
                postDate = new Date(dateUnixPost * 1000L);
            } catch (NumberFormatException | NoSuchElementException e) {
                System.out.println(e);
                continue;
            }


            System.out.print(postDate + " ");

            if ((dateStart != null) && (postDate.after(dateStart))) {
                if (postLikes.size() > 0) {
                    jse.executeScript("arguments[0].scrollIntoView()", postLikes);
                }
                i++;
                invLog.info(postDate + " - skip post");

                waitForPageLoad(1000L);
                continue;
            }

            if ((dateEnd != null) && (postDate.before(dateEnd))) {
//                    if (l1.getAttribute("href").equals("https://www.facebook.com/sribnyk/photos/a.827716040603798.1073741829.824082987633770/1001699953205405/?type=3")) {
//                        System.out.println("Skip Tet-a-Tet");
//                        i++;
//                        continue;
//                    }
                invLog.info("Stop");
                break;
            }

            invLog.info(i + " " + postTime.getText());
            invLog.info(postTime.getAttribute("href"));


            if (postLikes.size() > 0) {
                invite(postLikes.get(0));
                noLikers = 0;
                jse.executeScript("arguments[0].scrollIntoView()", postLikes.get(0));
            } else {
                saveHtmlAndScreenshot();
                invLog.info("No likers for this post");
                invLog.info("Total invited: 0/" + inv);
                jse.executeScript("arguments[0].scrollIntoView()", postTime);
                noLikers++;
                if (noLikers >= 10) {
                    throw new NoLikersException();
                }

            }


            waitForPageLoad(1000L);

            i++;


        }


    }

    public void invite(WebElement postLikes) throws InviteLimitException, InviteBlockException, NoSuchElementException {

        invLog.info("Start inviting");
        jse.executeScript("arguments[0].click();", postLikes);

        int i = 0;

        while (true) {

            //List<WebElement> wellcomeButton = driver.findElements(By.xpath("//a[contains(text(),'Пригласить')]"));
            WebElement wellcomeButton = null;
            try {
                wellcomeButton = driver.findElement(By.xpath("//a[@class='_42ft _4jy0 _4jy3 _517h _51sy']"));
            } catch (NoSuchElementException e) {
                List<WebElement> nextPage = driver.findElements(By.xpath("//div[@id='reaction_profile_pager']/div/a"));
                if (nextPage.size() > 0) {
                    invLog.info("Try to find more");
                    jse.executeScript("arguments[0].scrollIntoView()", nextPage.get(0));
                    jse.executeScript("arguments[0].click();", nextPage.get(0));
                    continue;
                } else {
                    invLog.info("No more likers for inviting");
                    invLog.info("Total invited: " + i + "/" + (inv + i));
                    driver.findElement(By.linkText("Закрыть")).click();
                    inv = inv + i;
                    return;
                }
            }


            jse.executeScript("arguments[0].scrollIntoView()", wellcomeButton);
            jse.executeScript("arguments[0].click();", wellcomeButton);


            //check facebooklimitpage
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            List<WebElement> fbLimit = driver.findElements(By.xpath("//h3[contains(text(),'Лимит приглашений исчерпан')]"));
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            if ((inv + i >= 500) || (fbLimit.size() > 0)) {
                if ((inv + i) == 0) {
                    saveHtmlAndScreenshot();
                    invLog.warn("Account blocked for Inviting. Process stoped");
                    invLog.info("Total invited: " + i + "/" + (inv + i));
                    throw new InviteBlockException();
                }
                saveHtmlAndScreenshot();
                invLog.warn("Stoped by Facebook limits");
                invLog.info("Total invited: " + i + "/" + (inv + i));

                throw new InviteLimitException();
            }

            i++;
        }

    }

    public void stop() {
        driver.close();
        driver.quit();
        invLog.info("Driver Stoped");
        invLog.close();
    }

    private void waitForPageLoad(Long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveHtmlAndScreenshot() {

        System.out.println("Taking screenshot now");
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try (PrintWriter out = new PrintWriter(new FileWriter("log/" + page + "/" + LogActions.getCurrentTime() + ".html"))) {
            out.println(driver.getPageSource());
            FileUtils.copyFile(srcFile, new File("log/" + page + "/" + LogActions.getCurrentTime() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}