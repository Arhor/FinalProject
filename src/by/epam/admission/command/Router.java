package by.epam.admission.command;

public class Router {

    private String page;
    private Type type;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        FORWARD,
        REDIRECT
    }

}
