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

    public void tweet(String input, long time) throws CannotFindUser {

        if (input.contains("->")) {
            String[] parts = input.split("->");
            //username=parts[0] ||| message=parts[1]
            parts[0] = StringsManager.removeExtraWhiteSpaces(parts[0]);
            parts[1] = StringsManager.removeExtraWhiteSpaces(parts[1]);

            User user = getUserByName(parts[0]);
            user.newMessage(parts[1], time);
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
        } else {
            //just username->reading
            input=StringsManager.removeExtraWhiteSpaces(input);
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
