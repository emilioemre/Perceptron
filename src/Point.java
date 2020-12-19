package Perceptron.src;
import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Point  extends JComponent{

    private static Neuron perceptron= DoubleCore.passNeuron();
    private static Neuron perceptron2= DoubleCore.passNeuron2();
    private double[] inputs; //create an array of input for the point, to show points on the screen
    private double x; //the x coordinate
    private double y; //the y coordinate
    private double classifier; //1 or -1 depending on the output of the Perceptron weights
    private double classifier2; //here the second classifier
    private double classified; //the classifier given by the perceptron
    private double classified2; //the classifier given by the perceptron
    private int iteration;
    private Map value;

    public Point(double x_, double y_, double o_, double p_)
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
        this.classifier2 = p_;
        value = new Map(0 );
    }

    public double setX(double x1)
    {
        return value.map(x1,0,700);
    }
    public double setY (double y1)
    {
        return value.map(y1,700,0);
    }
    public double getClassifier() { return this.classifier;   }
    public double getClassifier2(){return this.classifier2 ;   }
    public double getClassified(){return this.classified;}
    public void setAssignedClassified(double given) {this.classified = given;   }
    public void setAssignedClassified2(double given){this.classified2 = given;}
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

        Point2D.Double A = new Point2D.Double(setX(-1), setY(g(-1)));
        Point2D.Double A2 = new Point2D.Double(setX(1),setY(g(1)));
        Line2D.Double line2 = new Line2D.Double(A ,A2);
        double guessYLineTwo = perceptron2.guessLineY(-1);
        double guessYLineTwo2 = perceptron2.guessLineY(1);
        Line2D.Double guessLineTwo2 = new Line2D.Double(setX(-1),setY(guessYLineTwo),setX(1),setY(guessYLineTwo2));
        g2.draw(guessLineTwo2);
        g2.draw(line2);

        double lineX = setX(this.x);
        double lineY = setY(this.y);
        b = new Ellipse2D.Double(lineX,lineY, 10, 10);
        if (this.getClassifier() == 1 && this.getClassifier2()== 1 )    //distinct colours for different classifiers
        {
            g2.setColor(Color.red);
        }
        if (this.getClassifier() == -1 && this.getClassifier2() == 1)
        {
            g2.setColor(Color.green);
        }
        if (this.getClassifier2()== -1&& this.getClassifier()== -1){
            g2.setColor(Color.blue);
        }
        if (this.getClassifier()== 1&& this.getClassifier2()== -1){
            g2.setColor(Color.orange);
        }
        g2.fill(b);
    }

    private double f(double x) { return DoubleCore.f(x) ; }
    private double g (double x ){return DoubleCore.g(x);}
}