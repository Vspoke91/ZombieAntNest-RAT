package Utilities.Child;

public abstract class Child {

    public abstract void setInfo(String[] message);

    public enum Type{
        CONTROLLER,
        TARGET
    }

    public String ip;
    public Type type;
    public String os;

}
