package twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Maria
 */
public class Twitter {

    private final ArrayList<User> users;
    public static int nrOfUsers = 0;
    public static boolean tests = false;

    Twitter() {
        this.users = new ArrayList<>();
        loadUsers();
    }

    private void loadUsers() {
        this.users.add(new User("Alice"));
        this.users.add(new User("Bob"));
        this.users.add(new User("Charlie"));
        this.users.add(new User("Mary"));
        Twitter.nrOfUsers = this.users.size();
    }

    public void tweet(String input, long time) throws CannotFindUser, UserNotInTheFollowers {
        if (input.contains("->")) {
            //X->msg = his own post or X->msg to Y = x sends message msg to y
            String[] parts = input.split("->");
            //username=parts[0] ||| message=parts[1]
            parts[0] = StringsManager.removeExtraWhiteSpaces(parts[0]);
            parts[1] = StringsManager.removeExtraWhiteSpaces(parts[1]);
            User user = getUserByName(parts[0]);

            if (parts[1].contains(" to ")) {
                //it's a message from parts[0] to parts2[1]
                String[] parts2 = parts[1].split(" to ");
                //parts2[0]=msg || parts2[1]=user to post
                parts2[0] = StringsManager.removeExtraWhiteSpaces(parts2[0]);
                parts2[1] = StringsManager.removeExtraWhiteSpaces(parts2[1]);

                User userToSendmsg = getUserByName(parts2[1]);

                if (user.followsUser(userToSendmsg.getUsername())) {
                    userToSendmsg.newReceivedMessage(parts2[0], time, user.getUsername());
                } else {
                    throw new UserNotInTheFollowers();
                }

            } else {
                user.newOwnPost(parts[1], time);
            }

        } else if (input.contains(" follows ")) {
            String[] parts = input.split(" follows ");
            //username=parts[0] ||| anotherUsername=parts[1]
            parts[0] = StringsManager.removeExtraWhiteSpaces(parts[0]);
            parts[1] = StringsManager.removeExtraWhiteSpaces(parts[1]);

            User user = getUserByName(parts[0]);
            User followUser = getUserByName(parts[1]);

            user.addUserToFollow(followUser);

            FollowersMatrix fM = FollowersMatrix.getInstance();
            int userIndex = getUserIndex(parts[0]);
            int userToFollowIndex = getUserIndex(parts[1]);
            fM.setFollower(userIndex, userToFollowIndex);

        } else if (input.endsWith(" wall")) {
            String[] parts = input.split(" wall");
            parts[0] = StringsManager.removeExtraWhiteSpaces(parts[0]);

            User user = getUserByName(parts[0]);
            user.showWall(time);

        } else if (input.contains(" unfollow ")) {
            String[] parts = input.split(" unfollow ");
            parts[0] = StringsManager.removeExtraWhiteSpaces(parts[0]);
            parts[1] = StringsManager.removeExtraWhiteSpaces(parts[1]);

            User user = getUserByName(parts[0]);
            User followUser = getUserByName(parts[1]);

            user.removeFollowUser(followUser);
            user.removeMsgsFrom(followUser.getUsername());
            //followUser.removeMsgsFrom(user.getUsername());

            FollowersMatrix fM = FollowersMatrix.getInstance();
            int userIndex = getUserIndex(parts[0]);
            int userToFollowIndex = getUserIndex(parts[1]);
            fM.unsetFollower(userIndex, userToFollowIndex);

        } else if (input.endsWith(" show messages")) {
            String[] parts = input.split(" show messages");
            parts[0] = StringsManager.removeExtraWhiteSpaces(parts[0]);

            User user = getUserByName(parts[0]);
            user.showMessages(time);

        } else if (input.endsWith(" followers' walls")) {
            //X followers' walls
            String parts[] = input.split(" followers' walls");
            parts[0] = StringsManager.removeExtraWhiteSpaces(parts[0]);
            printFollowersWall(parts[0], time);
            //User user = getUserByName(parts[0]);

        } else {
            //just username->reading
            input = StringsManager.removeExtraWhiteSpaces(input);
            User user = getUserByName(input);
            user.showUserPosts(time);
        }

    }

    public User getUserByName(String username) throws CannotFindUser {
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        throw new CannotFindUser();
    }

    public int getUserIndex(String username) throws CannotFindUser {

        for (int i = 0; i < nrOfUsers; i++) {
            if (username.equals(this.users.get(i).getUsername())) {
                return i;
            }
        }
        throw new CannotFindUser();

    }

    public User getUserByIndex(int index) throws CannotFindUser {
        if (this.users.get(index) != null) {
            return this.users.get(index);
        }
        throw new CannotFindUser();
    }

    public void printFollowersWall(String user, long currentTime) throws CannotFindUser {
        int userIndex = getUserIndex(user);
        ArrayList<User> followers = getFollowers(userIndex);

        if (!followers.isEmpty()) {
            for (User fol : followers) {
                ArrayList<Message> folPosts = fol.getOwnPosts();
                ArrayList<Message> folMessages = fol.getMessages();
                ArrayList<Message> msgsAndPosts = new ArrayList<>();
                folMessages = filterFolMessages(folMessages, userIndex);
                if (!folMessages.isEmpty()) {
                    msgsAndPosts.addAll(folMessages);
                }
                if (!folPosts.isEmpty()) {
                    msgsAndPosts.addAll(folPosts);
                }
                if (!msgsAndPosts.isEmpty()) {
                    printFollowerMessages(msgsAndPosts, fol, currentTime);
                }
            }
        } else {
            System.out.println("FOLOWERS EMPTY");
        }
    }

    public ArrayList<User> getFollowers(int userIndex) {
        ArrayList<User> followers = new ArrayList<>();
        FollowersMatrix fM = FollowersMatrix.getInstance();
        for (int i = 0; i < nrOfUsers; i++) {
            if (fM.userFollowsUser2(i, userIndex)) {
                followers.add(this.users.get(i));
            }
        }
        return followers;
    }

    public void printFollowerMessages(ArrayList<Message> msgsAndPosts, User fromUser, long currentTime) {

        if (tests) {
            FilesManager fm = FilesManager.getInstance();
            fm.appendToOutputFile(fromUser.getUsername() + ":");
        }
        System.out.println(fromUser.getUsername() + ":");

        Collections.sort(msgsAndPosts, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return (int) (o2.getTime() - o1.getTime());
            }
        });
        for (Message msg : msgsAndPosts) {
            fromUser.convertTime(currentTime - msg.getTime());
            if (msg.isPost()) {
                fromUser.printUserPosts(msg);
            } else {
                fromUser.printMessages(msg);
            }
        }
    }

    public ArrayList<Message> filterFolMessages(ArrayList<Message> folMessages, int userIndex) throws CannotFindUser {
        FollowersMatrix fM = FollowersMatrix.getInstance();
        ArrayList<Message> filteredMsgs = new ArrayList<>();
        for (Message msg : folMessages) {
            String fromUser = msg.gotMsgFromUsername();
            int fromUserIndex = getUserIndex(fromUser);
            if (fM.userFollowsUser2(fromUserIndex, userIndex)) {
                filteredMsgs.add(msg);
            }

        }
        return filteredMsgs;
    }
}
