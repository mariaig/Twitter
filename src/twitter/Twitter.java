package twitter;

import java.util.ArrayList;

/**
 *
 * @author Maria
 */
public class Twitter {

    ArrayList<User> users;

    Twitter() {
        this.users = new ArrayList<>();
        loadUsers();
    }

    private void loadUsers() {
        this.users.add(new User("Alice"));
        this.users.add(new User("Bob"));
        this.users.add(new User("Charlie"));
        this.users.add(new User("Mary"));
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
                String[] parts2 = parts[1].split(" to ");
                //parts2[0]=msg || parts2[1]=user to post
                parts2[0] = StringsManager.removeExtraWhiteSpaces(parts2[0]);
                parts2[1] = StringsManager.removeExtraWhiteSpaces(parts2[1]);
                User userToSendmsg = getUserByName(parts2[1]);
                if (user.followsUser(userToSendmsg.getUsername())) {
                    userToSendmsg.newReceivedMessage(parts2[0], time, user.getUsername());
                }else{
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

        }else if(input.endsWith(" show messages")){
            String[] parts=input.split(" show messages");
            parts[0]=StringsManager.removeExtraWhiteSpaces(parts[0]);
            User user=getUserByName(parts[0]);
            user.showMessages(time);
        }
        else {
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

}
