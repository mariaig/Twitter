
package twitter;

/**
 *
 * @author Maria
 */
public class Message {
    private String userName;
    private long time;
    private String message;
    
    Message(long time,String message,String user){
        this.time=time;
        this.message=message;
        this.userName=user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    
    
            
}
