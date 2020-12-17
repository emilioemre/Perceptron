package Perceptron.src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
/**
 * Class to implement the perceptron based on randomised weight and points
 * output is a line on the frame giving the best fit of the function
 */
public class Core extends JFrame
{
    public static Neuron perceptron ; //create a new Neuron object
    public  Dot [] points; // Create an ArrayList to store the points that will be drawn
    public static JFrame frame;
    public static double START;
    public  Random r;
    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;
    public  double sum_Errors = 0 ;
    private static WindowEvent listen;
    private static WindowAdapter closed;

    public static void main(String[] args)
    {
        START = System.nanoTime();
        perceptronLearningFromRandom();
        System.out.println("time needed :");
        double end = System.nanoTime()-START;
        double convert = MILLISECONDS.convert((long) end, NANOSECONDS);
        System.out.println(new StringBuilder().append(convert).append(" ms").toString());
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
        new Core();
    }
    public static void setup()
    {
        frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);//700
        frame.setMinimumSize(new Dimension(WIDTH,HEIGHT));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //this window listener should give a stable access to the window closing tool,
        // given the program the time to escape the current method
        closed = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                listen = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(listen);
                System.out.println("System closed by user");
                System.exit(0);
            }
        };
        frame.addWindowListener(closed);
        frame.pack();

    }

    public Core ()
    {
        setup();
        r = new Random(System.currentTimeMillis());
        perceptron = new Neuron(3) {
            //method for the assignment
            @Override
            public void train(int[] inputs, int target) {  }
        }; //Initialise the Neuron with random weights

        learnFromRandom();
    }
    public void learnFromRandom()
    {
        //Fill the tests ArrayList with randomly spawned data values
        ArrayList<Dot[]> test= new ArrayList<>();
        while(test.size()<2000)
        {
            points = generate_random_test();
            sum_Errors = 0;
            frame.repaint(); //call to update the graphics
            for (Dot p : points)
            {
                //For each dataset in test
                perceptron.train(p.getInput(), p.getClassifier());
                Thread.onSpinWait();
                //train the Neuron with these values
                //classifier is the third parameter in the point object(the correct answer is assigned here)
                p.setAssignedClassified(perceptron.eval(p.getInput()));
                frame.pack();
                frame.add(p);
                sum_Errors += perceptron.getSum();
                frame.setVisible(true);

            }
            test.add(points);
            System.out.println(sum_Errors);
            if (sum_Errors ==0)
            {
                frame.setVisible(true);
                System.out.println("process finished with weights"+ Arrays.toString(perceptron.weights));
                break;
            }
        }
        //System.exit(0);
    }
    public Dot[] generate_random_test()
    {
        //function that generates random dataset of dataset values
        Dot [] dataset = new Dot [200]; //Create a new ArrayList called dataset
        // adding too much data values in this array could lead to a late response from the graphics, but still possible
        for(int i = 0; i<dataset.length ; i++) {
            //create a new Point
            //x is a random number between 0 and the width of the screen
            //y is a random number between 0 and the height of the screen
            double classifier = 1;      //set the correct answer to be 1
            double x = (r = new Random()).nextDouble();
            if ((r = new Random()).nextDouble() < 0.5) {
                x *= -1;                //randomise if positive or negative as well
            }
            double y = (r = new Random()).nextDouble();
            if ((r = new Random()).nextDouble() < 0.5) {
                y *= -1;
            }
            if (f(x) < y) {              // if the point is below the line
                classifier = -1;         //set the correct answer to -1
            }
            dataset[i] = new Dot(x, y, classifier);
            //add the dataset to the list of tests
        }
        return dataset; //return the list of tests
    }
    double f(double x)
    { //The function that defines the line
        return -0.3*x+0.11; //A given line
    }

    class Dot extends JComponent
    {
        private final double [] inputs; //create an array of input for the point, to show points on the screen
        public double x; //the x coordinate
        public double y; //the y coordinate
        public double classifier; //1 or -1 depending on the output of the Perceptron weights
        public double classified; //the classifier given by the perceptron
        public int iteration;
        public Map value;

        public Dot(double x_, double y_, double o_)
        {
            classified = 0;
            //initialisation function for Point
            this.inputs = new double[3];
            this.inputs[0] = x_; //x is the value specified
            this.x = x_;
            this.inputs[1] = y_; //y is the value specified
            this.y = y_;
            this.inputs[2] = 1;
            this.classifier = o_;   //gets a classifier between 1 and -1, (green or red);
            value = new Map(0 );
        }
        public double setX(double x1)
        {
            return value.map(x1,-1,1,0,700);
        }
        public double setY (double y1)
        {
            return value.map(y1,-1,1,700,0);
        }

        public double getClassifier() { return this.classifier;   }
        public double getClassified(){return this.classified;}
        public void setAssignedClassified(double given) {this.classified = given;   }
        public double [] getInput () { return this.inputs;   }


        public void paintComponent(Graphics g)
        {
            iteration++;
            Graphics2D g2 =(Graphics2D)  g;
            Ellipse2D.Double b ;
            g2.setStroke(new BasicStroke(2.0f));
            Point2D.Double a = new Point2D.Double(setX(-1), setY(f(-1)));
            Point2D.Double a1 = new Point2D.Double(setX(1),setY(f(1)));
            Line2D.Double line = new Line2D.Double(a ,a1);
            double guessYLine = perceptron.guessLineY(-1);
            double guessYLine2 = perceptron.guessLineY(1);
            Line2D.Double guessLine = new Line2D.Double(setX(-1),setY(guessYLine),setX(1),setY(guessYLine2));
            g2.draw(guessLine);
            g2.draw(line);
            double lineX = setX(this.x);
            double lineY = setY(this.y);
            b = new Ellipse2D.Double(lineX,lineY, 10, 10);
            if (this.getClassifier() == 1)    //distinct colours for different classifiers
            {
                g2.setColor(Color.red);
            }
            else if (this.getClassifier() == -1)
            {
                g2.setColor(Color.green);
            }
            g2.fill(b);
            if (this.getClassified() == this.getClassifier())  //once the learning is advanced we can notice the progress
            {
                g2.setColor (Color.black);
                g2.draw(b);
            }

        }
    }
}
