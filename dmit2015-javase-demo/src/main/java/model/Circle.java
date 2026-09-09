package model;

public class Circle {
    private double radius;

    // this is our get method. it's the same as a get() in c#
    public double getRadius() {
        return radius;
    }

    //our set method.
    public void setRadius(double radius) {
        this.radius=radius;
    }

    //our constructor. typically you can tell if something is a constructor if it's named the same as the class
    public Circle() {
        radius = 1;
    }

    public double area(){
        return Math.PI * Math.pow(radius,2);
    }

    public static void main(String[] args){
        Circle currentCircle= new Circle();
        currentCircle.setRadius(5);

        System.out.printf("radius is: %.2f, area is %.2f\n",currentCircle.getRadius(), currentCircle.area());
    }
}
