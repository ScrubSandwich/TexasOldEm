public class Message implements java.io.Serializable {
    private String user;
    private String message;
    private String argument;

    public Message(String u, String m) {
        this.user = u;
        this.message = m;
    }

    public Message(String u, String m, String a) {
        this.user = u;
        this.message = m;
        this.argument = a;
    }

    public String getMessage() {
        return this.message;
    }

    public String getUser() {
        return this.user;
    }
}