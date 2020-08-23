package de.tum.in.ase.eist2020;

import java.net.URL;

public class OnlineCourse extends Course {
    public URL livestreamUrl;

    public OnlineCourse() {
        super();
    }

    public OnlineCourse(String _title, URL _url) {
        super(_title);
        description = "An online course";
        livestreamUrl = _url;
    }

    @Override
    public Course join() {
        System.out.println(String.format("Joining an online %s couse with url %s", this.title, this.livestreamUrl));
        return this;
    }

    @Override
    public Course drop() {
        System.out.println(String.format("Dropping an online %s couse with url %s", this.title, this.livestreamUrl));
        return this;
    }

}