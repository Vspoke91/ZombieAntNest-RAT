package Intro;

public class IntroClassHolder {

    public static Main  main;
    public static ConnectionTypeFrame connectionTypeFrame;
    public static ConnectionConfigFrame connectionConfigFrame;

    //Setters
    public static void setMain(Main mainx){
        main = mainx;
    }
    public static void setConnectionTypeFrame(ConnectionTypeFrame connectionTypeFramex){
       connectionTypeFrame = connectionTypeFramex;
    }
    public static void setConnectionConfigFrame(ConnectionConfigFrame connectionConfigFramex){
        connectionConfigFrame = connectionConfigFramex;
    }

}
