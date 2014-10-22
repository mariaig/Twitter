/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Maria
 */
public class User {

    private ArrayList<Message> messages; //key=time, value=message
    private ArrayList<User> follow;
    private String username;

    private long timeLong;
    private String timeStr;
    

    User() {
        messages = new ArrayList<>();
        follow = new ArrayList<>();
        this.timeLong = 0;
        this.timeStr = "";
    }

    User(String username) {
        this();
        this.username = username;
    }

    public void newMessage(String message, long postedTime) {
        this.messages.add(new Message(postedTime, message, this.username));

        Collections.sort(this.messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return (int) (o2.getTime() - o1.getTime());
            }
        });
    }

    public void showUserPosts(long currentTime) {
        
        for (Message msg : this.messages) {
            convertTime(currentTime - msg.getTime());
            printUserPosts(msg);
        }

    }

    public void showWall(long currentTime) {
        ArrayList<Message> allPosts = new ArrayList<>();
        allPosts.addAll(messages);
        for (User fU : follow) {
            allPosts.addAll(fU.getMessages());
        }
        Collections.sort(allPosts, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return (int) (o2.getTime() - o1.getTime());
            }
        });

        for (Message msg : allPosts) {
            convertTime(currentTime - msg.getTime());
            printWall(msg);
        }

    }

    private void printWall(Message msg) {
        String output=msg.getUserName() + " - " + msg.getMessage() + " (" + this.timeLong + " " + this.timeStr + " ago)";
        System.out.println(output);
        FilesManager fm=FilesManager.getInstance();
        fm.appendToOutputFile(output);
    }

    private void printUserPosts(Message msg) {
        String output=msg.getMessage() + " (" + this.timeLong + " " + this.timeStr + " ago)";
        System.out.println(output);
        FilesManager fm=FilesManager.getInstance();
        fm.appendToOutputFile(output);
    }

    public void addUserToFollow(User user) {
        this.follow.add(user);
    }

    public void convertTime(long postedTime) {
        long time = TimeUnit.SECONDS.toDays(postedTime);
        if (time != Long.MIN_VALUE && time != Long.MAX_VALUE && time > 0) {
            this.timeLong = time;
            this.timeStr = "day";
            if (time > 1) {
                timeStr += "s";
            }
            return;
        }

        time = TimeUnit.SECONDS.toHours(postedTime);
        if (time != Long.MIN_VALUE && time != Long.MAX_VALUE && time > 0) {
            this.timeLong = time;
            this.timeStr = "hour";
            if (time > 1) {
                timeStr += "s";
            }
            return;
        }

        time = TimeUnit.SECONDS.toMinutes(postedTime);
        if (time != Long.MIN_VALUE && time != Long.MAX_VALUE && time > 0) {
            this.timeLong = time;
            this.timeStr = "minute";
            if (time > 1) {
                timeStr += "s";
            }
            return;
        }

        this.timeLong = postedTime;
        this.timeStr = "second";
        if (postedTime > 1) {
            timeStr += "s";
        }
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<User> getFollows() {
        return follow;
    }

}
