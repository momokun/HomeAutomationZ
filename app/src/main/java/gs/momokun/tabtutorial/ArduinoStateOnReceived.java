package gs.momokun.tabtutorial;

/**
 * Created by ElmoTan on 12/5/2016.
 */

public class ArduinoStateOnReceived {

    private int getState;

    public ArduinoStateOnReceived(int state){
        this.getState = state;
    }

    public int getStateArduino(){
        return getState;
    }

}
