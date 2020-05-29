package pgdp.zoo;

class Zoo {
    private Vivarium[] vivaria;

    public Zoo(Vivarium[] _vivaria) {
        vivaria = _vivaria;
    }

    public int getCosts() {
        int all = 0;
        for (Vivarium vivarium : vivaria)
            all += vivarium.getCosts();
        return all;
    }

    public String toString() {
        String base = "{";
        for (int i = 0; i < vivaria.length; i++) {
            base += vivaria[i].toString();
            if (i != vivaria.length - 1)
                base += ", ";
        }
        return base + "}";
    }
}