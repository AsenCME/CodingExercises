package pgdp.phonebook;

class Entry {
    private String lastName;
    private String firstName;
    private String phoneNumber;

    public Entry(String _firstName, String _lastName, String _phoneNumber) {
        firstName = _firstName;
        lastName = _lastName;
        phoneNumber = _phoneNumber;
    }

    public String getLastName() {
        return lastName;
    };

    public String getFirstName() {
        return firstName;
    };

    public String getPhoneNumber() {
        return phoneNumber;
    };

    public int compareNames(String _firstName, String _lastName) {
        int comp1 = firstName.compareTo(_firstName);
        if (comp1 != 0)
            return comp1;

        int comp2 = lastName.compareTo(_lastName);
        return comp2;
    }
}
