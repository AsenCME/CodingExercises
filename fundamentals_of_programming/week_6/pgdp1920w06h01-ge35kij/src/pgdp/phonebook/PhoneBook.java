package pgdp.phonebook;

class PhoneBook {
    private Entry[] entries;

    public PhoneBook(Entry[] _entries) {
        entries = _entries;
    }

    public String find(String firstName, String lastName) {
        int begin = 0;
        int end = entries.length - 1;

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Entry entry = entries[mid];
            int comparison = entry.compareNames(firstName, lastName);

            if (comparison < 0)
                begin = mid + 1;

            else if (comparison > 0)
                end = mid - 1;

            else
                return entry.getPhoneNumber();
        }

        return null;
    }
}