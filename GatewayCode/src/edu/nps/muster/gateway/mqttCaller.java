package edu.nps.muster.gateway;

/**
 * Created by Micah on 8/22/16.
 */
public interface mqttCaller {
    public void messageReceived(String topic, String message);
}
