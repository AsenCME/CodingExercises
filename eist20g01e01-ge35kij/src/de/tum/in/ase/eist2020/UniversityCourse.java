package de.tum.in.ase.eist2020;

public class UniversityCourse extends Course {
    public String roomName;

    public UniversityCourse() {
        super();
    }

    public UniversityCourse(String _title, String _room) {
        super(_title);
        description = "A university course";
        roomName = _room;
    }

    @Override
    public Course join() {
        System.out.println(String.format("Joining a university %s couse in room %s", this.title, this.roomName));
        return this;
    }

    @Override
    public Course drop() {
        System.out.println(String.format("Dropping a university %s couse in room %s", this.title, this.roomName));
        return this;
    }

}