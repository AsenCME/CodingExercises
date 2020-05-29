package pgdp.zoo;

class Vivarium {
    private int area;
    private int constructionYear;
    private Animal[] inhabitants;

    public Vivarium(Animal[] _inhabitants, int _area, int _constructionYear) {
        inhabitants = _inhabitants;
        area = _area;
        constructionYear = _constructionYear;
    }

    public int getCosts() {
        int animalCosts = 0;
        for (Animal animal : inhabitants)
            animalCosts += animal.getFoodCosts();

        return 900 + area * 100 + area * (2019 - constructionYear) * 5 + animalCosts;
    }

    public String toString() {
        String animalsString = "";
        for (int i = 0; i < inhabitants.length; i++) {
            animalsString += inhabitants[i].toString();
            if (i != inhabitants.length - 1)
                animalsString += ", ";
        }
        return String.format("[area: %d, constructionYear: %d, animals: %s]", area, constructionYear, animalsString);
    }
}