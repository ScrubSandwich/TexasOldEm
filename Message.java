public class Message implements java.io.Serializable {
    private String user;
    private String message;

    public Message(String u, String m) {
        this.user = u;
        this.message = m;
    }

    public String getMessage() {
        return this.message;
    }

    public String getUser() {
        return this.user;
    }
}