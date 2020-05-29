package pgdp.collections;

public class Penguin {
    private final int birthYear;
    private final String name;
    private final Gender gender;
    private Fish favoriteFish;

    public Penguin(int birthYear, String name, Gender gender, Fish favoriteFish) {
        this.birthYear = birthYear;
        this.name = name;
        this.gender = gender;
        this.favoriteFish = favoriteFish;
    }

    public boolean equals(Object other) {
        if (other == null)
            return false;

        if (other instanceof Penguin) {
            Penguin otherObj = (Penguin) other;
            if (otherObj.name == null || otherObj.gender == null)
                return false;
            else {
                return otherObj.birthYear == this.birthYear && otherObj.name == this.name
                        && otherObj.gender == this.gender;
            }
        } else
            return false;
    }

    public int hashCode() {
        int sum = 0;
        for (char c : name.toCharArray())
            sum += (int) c;
        sum += birthYear;
        return sum + gender.hashCode();
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public Fish getFavoriteFish() {
        return favoriteFish;
    }

    public void setFavoriteFish(Fish favoriteFish) {
        this.favoriteFish = favoriteFish;
    }

}
