package BmiCalculatorServer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket =new Socket("127.0.0.1",8010);
        System.out.println("client: Created Socket");

        ObjectOutputStream toServer=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream fromServer=new ObjectInputStream(socket.getInputStream());

        double[] source = {50.0, 1.60};
        toServer.writeObject("calcBMI");
        toServer.writeObject(source);
        double calculatedBmi = (double) fromServer.readObject();
        System.out.println(" my bmi" + calculatedBmi);
        
        double resultbmi = calculatedBmi;
        toServer.writeObject("getWeightStatus");
        toServer.writeObject(resultbmi);
        String status = (String) fromServer.readObject();
        System.out.println(" my status is " + status.toString());
        
        double height = 1.80;
        int age = 33;
        String bodyType = "Large";
        toServer.writeObject("getIdealWeight");
        toServer.writeObject(height);
        toServer.writeObject(age);
        toServer.writeObject(bodyType);
        double IdealWeight = (double) fromServer.readObject();
        System.out.println( " my ideal " + IdealWeight);
        
        toServer.writeObject("stop");
        System.out.println("client: Close all streams");
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("client: Closed operational socket");
    }
}
