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

    private ArrayList<Message> ownPosts; //key=time, value=message
    private ArrayList<Message> messages; //messages from users who are following this user
    private ArrayList<User> follow;
    private String username;

    private long timeLong;
    private String timeStr;

    User() {
        ownPosts = new ArrayList<>();
        follow = new ArrayList<>();
        messages = new ArrayList<>();
        this.timeLong = 0;
        this.timeStr = "";
    }

    User(String username) {
        this();
        this.username = username;
    }

    public void newOwnPost(String message, long postedTime) {
        this.ownPosts.add(new Message(postedTime, message, this.username));
    }

    public void newReceivedMessage(String message, long postedTime, String fromUser) {
        String msg = fromUser + " says:" + message;
        this.messages.add(new Message(postedTime, msg, this.username, fromUser));
    }

    public void showUserPosts(long currentTime) {
        ArrayList<Message> postsAndMessages = new ArrayList<>();
        if (!this.messages.isEmpty()) {
            postsAndMessages.addAll(messages);
        }
        if (!this.ownPosts.isEmpty()) {
            postsAndMessages.addAll(ownPosts);
        }
        if (!postsAndMessages.isEmpty()) {
            Collections.sort(postsAndMessages, new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    return (int) (o2.getTime() - o1.getTime());
                }
            });
            for (Message msg : postsAndMessages) {
                convertTime(currentTime - msg.getTime());
                printUserPosts(msg);
            }
        }
    }

    public void showWall(long currentTime) {
        ArrayList<Message> allPosts = new ArrayList<>();

        if (!this.ownPosts.isEmpty()) {
            allPosts.addAll(this.ownPosts);
        }

        if (!this.follow.isEmpty()) {
            for (User fU : this.follow) {
                if (!fU.getOwnPosts().isEmpty()) {
                    allPosts.addAll(fU.getOwnPosts());
                }
            }
        }

        if (!allPosts.isEmpty()) {
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

    }

    public void showMessages(long currentTime) {
        if (!this.messages.isEmpty()) {
            Collections.sort(this.messages, new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    return (int) (o2.getTime() - o1.getTime());
                }
            });
            for (Message msg : this.messages) {
                convertTime(currentTime - msg.getTime());
                printMessages(msg);
            }
        }
    }

    private void printMessages(Message msg) {
        String output = msg.getMessage() + " " + " (" + this.timeLong + " " + this.timeStr + " ago)";
        System.out.println(output);
        FilesManager fm = FilesManager.getInstance();
        fm.appendToOutputFile(output);
    }

    private void printWall(Message msg) {
        String output = msg.getUserName() + " - " + msg.getMessage() + " (" + this.timeLong + " " + this.timeStr + " ago)";
        System.out.println(output);
        FilesManager fm = FilesManager.getInstance();
        fm.appendToOutputFile(output);
    }

    private void printUserPosts(Message msg) {
        String output = msg.getMessage() + " (" + this.timeLong + " " + this.timeStr + " ago)";
        System.out.println(output);
        FilesManager fm = FilesManager.getInstance();
        fm.appendToOutputFile(output);
    }

    public void addUserToFollow(User user) {
        this.follow.add(user);
    }

    public void removeFollowUser(User user) throws UserNotInTheFollowers {

        if (!this.follow.isEmpty() && this.followsUser(user.getUsername())) {
            if (!this.messages.isEmpty()) {
                //delete all messages form this user
                int i = 0;
                while (i < this.messages.size()) {
                    String msgFrom = this.messages.get(i).getMsgFromUsername();
                    if (msgFrom.equals(user.getUsername())) {
                        this.messages.remove(i);
                    } else {
                        i++;
                    }
                }

            }
            this.follow.remove(user);
        } else {
            throw new UserNotInTheFollowers();
        }
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

    public boolean followsUser(String user) {
        if (!this.follow.isEmpty()) {
            for (User u : this.follow) {
                if (u.getUsername().equals(user)) {
                    return true;
                }
            }
        }
        return false;
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

    public ArrayList<Message> getOwnPosts() {
        return ownPosts;
    }

    public ArrayList<User> getFollows() {
        return follow;
    }

}
