/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

/**
 *
 * @author Maria
 */
public class FollowersMatrix {
    
    private static final FollowersMatrix instance = new FollowersMatrix();
    private final byte folowersMatrix[][];

    private FollowersMatrix() {
        folowersMatrix=new byte[Twitter.nrOfUsers][Twitter.nrOfUsers];
    }
    
     public static FollowersMatrix getInstance() {
        return instance;
    }
    public void setFollower(int user,int followedUser){
        folowersMatrix[user][followedUser]=1;
    }
    public void unsetFollower(int user,int unfollowedUser){
        folowersMatrix[user][unfollowedUser]=0;
    }
    public boolean userFollowsUser2(int user,int followedUser){
        return (folowersMatrix[user][followedUser]==1);
    }
}
