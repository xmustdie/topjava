package ru.javawebinar.topjava.util.comparators;

import ru.javawebinar.topjava.model.User;

import java.util.Comparator;

public class UsersListSortByNameAndEmailComparator implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        int result = o1.getName().compareTo(o2.getName());
        if (result == 0) {
            result = o1.getEmail().compareTo(o2.getEmail());
        }
        return result;
    }
}
