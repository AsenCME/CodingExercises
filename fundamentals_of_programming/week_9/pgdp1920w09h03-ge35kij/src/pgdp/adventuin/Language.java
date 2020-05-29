package pgdp.adventuin;

public enum Language {
    ENGLISH, GERMAN, BULGARIAN, JAPANESE;

    public String getLocalizedChristmasGreeting(String greeterName) {
        String greeting = "";
        switch (this) {
        case ENGLISH:
        default:
            greeting = String.format("%s wishes you a Merry Christmas!", greeterName);
            break;

        case GERMAN:
            greeting = String.format("Fröhliche Weihnachten wünscht dir %s!", greeterName);
            break;

        case BULGARIAN:
            greeting = String.format("%s ти пожелава Весела Коледа!", greeterName);
            break;

        case JAPANESE:
            greeting = String.format("%sはあなたにメリークリスマスを願っています!", greeterName);
            break;
        }
        return greeting;
    }
}