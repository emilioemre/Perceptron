package Perceptron.src;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DoubleCore
{
    private static Neuron perceptron ; //create a new Neuron object
    private static Neuron perceptron2 ; //create a new Neuron object
    private Point[] points; // Create an ArrayList to store the points that will be drawn
    public static JFrame frame;
    private static double START;
    private static final int WIDTH = 780;
    private static final int HEIGHT = 780;
    private double sum_Errors = 0 ;
    private double sum_Errors2 = 0;
    private static WindowEvent listen;
    private boolean one = false;
    private boolean two = false;

    public static void main(String[] args)
    {
        START = System.nanoTime();
        perceptronLearningFromRandom();
        double end = System.nanoTime()-START;
        double convert = SECONDS.convert((long) end, NANOSECONDS);
        System.out.println(String.format("Time needed > %s  s", convert));
    }
    public static void perceptronLearningFromRandom()
    {
        /**
         * Running the perceptron with the graphics and (expected) mapping on the frame
         * Random weights
         * Random points
         * lk = 0.001;
         *
         */
        new DoubleCore();
    }
    public static void setup()
    {
        frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);//700
        frame.setMinimumSize(new Dimension(WIDTH,HEIGHT));
        frame.getContentPane();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //this window listener should give a stable access to the window closing tool,
        // given the program the time to escape the current method
        WindowAdapter closed = new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                DoubleCore.listen = new WindowEvent(DoubleCore.frame, 201);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(DoubleCore.listen);
                System.out.println("System closed by user");
                System.exit(0);
            }
        };
        frame.addWindowListener(closed);
        frame.setVisible(true);
        frame.pack();

    }
    public DoubleCore ()
    {
        setup();
        new Random(System.currentTimeMillis());
        perceptron = Neuron.passNeuron(3);
        perceptron2 = Neuron.passNeuron(3);
        learnFromRandom();
    }
    public void learnFromRandom()
    {
        //Fill the tests ArrayList with randomly spawned data values
        ArrayList<Point[]> test= new ArrayList<>();
        double [] weights1 = new double[3];
        double [] weights2 = new double [3];
        //points = generate_random_test();
        points = generate_random_test();
        while(test.size()<2000)
        {
            this.sum_Errors = 0;
            this.sum_Errors2= 0;
            frame.repaint();
            evaluate(points);
            test.add(points);
            if (!one)
                System.out.println("Errors > 1  -> [  "+(int)(sum_Errors/2)+" ]");
            if (!two)
                System.out.println("Errors > 2  -> [  "+(int)(sum_Errors2/2)+" ]");
            if (this.sum_Errors == 0 && !one)
            {
                one = true;
                weights1 = perceptron.get_weights();
            }
            if (this.sum_Errors2 == 0 && !two)
            {
                two = true;
                weights2 = perceptron2.get_weights();
            }
            if (one && two)
            {
                System.out.println("process finished with weights :");
                System.out.println(Arrays.toString(weights1));
                System.out.println(Arrays.toString(weights2));
                frame.setVisible(true);
                break;
            }
        }
        //System.exit(0);
    }
    public void evaluate (Point [] points)
    {
        for (Point p : points)
        {
            //For each dataset in test
            if (!one)
            {
                this.perceptron.train(p.getInput(), p.getClassifier());
                p.setAssignedClassified(this.perceptron.activation(p.getInput()));
                this.sum_Errors += this.perceptron.getSum();
            }
            if (!two)
            {
                this.perceptron2.train(p.getInput(), p.getClassifier2());
                p.setAssignedClassified2(this.perceptron2.activation(p.getInput()));
                this.sum_Errors2 += this.perceptron2.getSum();
            }
            // train the Neuron with these values
            // classifier is the third parameter in the point object(the correct answer is assigned here)
            if (p.getClassifier()== p.getClassified()&& p.getClassifier2()== p.getClassified2())
                p.wasMissed(false);
            else
                p.wasMissed(true);
            frame.pack();
            frame.repaint();
            frame.add(p);
            frame.setVisible(true);
        }
    }
    public Point [] generate_random_test()
    {
        //function that generates random dataset of dataset values
        Point[] dataset = new Point [500]; //Create a new ArrayList called dataset
        // adding too much data values in this array could lead to a late response from the graphics, but still possible
        for(int i = 0; i<dataset.length ; i++)
        {
            //create a new Point
            //x is a random number between 0 and 1
            //y is a random number between 0 and 1
            double classifier = 1;      //set the correct answer to be 1
            double classifier2 = 1;
            double x = new Random().nextDouble();
            if (new Random().nextDouble() < 0.5)
            {
                x *= -1;                //randomise if positive or negative as well
            }
            double y = new Random().nextDouble();
            if (new Random().nextDouble() < 0.5)
            {
                y *= -1;
            }
            if (f(x) < y)
            {              // if the point is below the line
                classifier = -1;         //set the correct answer to -1
            }
            if (g(x)< y)
            {
                classifier2 = -1;
            }
            dataset[i] = new Point(x, y, classifier, classifier2);
            dataset[i].setValue1(perceptron);
            dataset[i].setValue2(perceptron2);
            //add the dataset to the list of tests
        }
        return dataset; //return the list of tests
    }
    //The function that defines the line
    public static double f(double x) { return 2*x+1; }
    public static double g(double x ){return -0.3*x+0.26;}
}
