package Controller.Java;

public class Target {

    public String ip;
    public String internet;
    public String deviceType;
    public boolean flashlightState;

    public Target(String name){

        ip = name;

    }

    public void addTargetInfo(String field, String state){

        switch (field){

            case "int": //internet
                internet = state;
                break;

            case "fls": //flashlightState
                flashlightState = Boolean.valueOf(state);
                ControllerCommandFrame.me.setFlashLightButton(flashlightState);
                break;

            case "dt": //Device Type
                deviceType = state;
                break;

        }

    }

}
