package BmiCalculatorServer;

import java.io.*;

public class BmiCalcIHandler implements IHandler {

    private volatile boolean doWork = true;

    @Override
    public void resetMembers() {
        this.doWork = true;
    }

    // func isBetween //
    private static boolean isBetween(double x, double lower, double upper) {
        return lower <= x && x <= upper;
    }
    private static double calculateSlimness(String bodyType) {
        double slimness;
        switch (bodyType) {
            case "Small":
                slimness = 0.9;
                break;
            case "Medium":
                slimness = 1;
                break;
            case "Large":
                slimness = 1.1;
                break;
            default:
                slimness = 0;
                break;
        }
        return slimness;
    }

    @Override
    public void handle(InputStream fromClient, OutputStream toClient) throws IOException, ClassNotFoundException {

        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);

        boolean doWork = true;
        // handle client's tasks
        while (doWork) {

            switch (objectInputStream.readObject().toString()) {

                // calcBMI func //
            case "calcBMI": {
            	double resultbmi; // result of bmi
            	double bmi;
                double[] tempArray = (double[]) objectInputStream.readObject();
                System.out.println("Server: Got height " + tempArray[0] + " and weight " + tempArray[1]);
                tempArray[1] = tempArray[1] / 100;
                bmi = tempArray[0] / (tempArray[1]*tempArray[1]);
                int leftDot = (int) bmi;
                int rightDot = ((int) ((bmi * 100) % 100));
                double rightRounded = Math.round((double) rightDot / 10); // for example 69=>70 (6.9=>7.0)
                resultbmi = (double) leftDot + rightRounded / 10.0;
                objectOutputStream.writeObject(resultbmi);
                System.out.println("Server: bmi " + resultbmi);
                break;
            }
                   
         // getWeightStatus func //
            case "getWeightStatus": {
                String status = "";
                double bmi = (double) objectInputStream.readObject();
                System.out.println("Server: Got bmi " + bmi);
                if (bmi < 15) {
                    status = "Anorexic";
                } else if (isBetween(bmi, 15, 18.5)) {
                    status = "Underweight";
                } else if (isBetween(bmi, 18.5, 24.9)) {
                    status = "Normal";
                } else if (isBetween(bmi, 25, 29.9)) {
                    status = "Overweight";
                } else if (isBetween(bmi, 30, 35)) {
                    status = "Obese";
                } else if (bmi > 35) {
                    status = "Extreme Obese";
                }
                objectOutputStream.writeObject(status);
                System.out.println("Status: Got bmi " + status.toString());
                break;
            }
                // getIdealWeight func //
                case "getIdealWeight": {
                    double height =  (double) objectInputStream.readObject();
                    System.out.println("Server: Got height " + height);
                    int age = (int) objectInputStream.readObject();
                    System.out.println("Server: Got age " + age);
                    String bodyType = (String) objectInputStream.readObject();
                    System.out.println("Server: Got bodyType " + bodyType);
                    double slimness = calculateSlimness(bodyType);
                    double idealWeight = (height - 100 + ((double) (age / 10))) * 0.9 * slimness;
                    int leftDot = (int) idealWeight;
                    int rightDot = ((int) ((idealWeight * 100) % 100)); // two numbers after dot
                    double rightRounded = Math.round((double) rightDot / 10); // for example 69=>70 (6.9=>7.0)
                    double resultIdel = (double) leftDot + rightRounded / 10.0;
                    System.out.println("Ideal is : " + resultIdel);
                    objectOutputStream.writeObject(resultIdel);
                    break;
                }
                case "stop":{
                    doWork = false;
                    break;
                }
            }
        }
    }
}