//Model class for ObservableList in Main.java
package sample;

public class Cat {
    private String name;
    private int year;
    private double weight;
    private boolean vaccinated;

    public Cat() {
    }

    public Cat(String name, int year, double weight, boolean vaccinated) {
        this.name = name;
        this.year = year;
        this.weight = weight;
        this.vaccinated = vaccinated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", weight=" + weight +
                ", vaccinated=" + vaccinated +
                '}';
    }
}
