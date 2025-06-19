package hipdogclub.Model;

import java.util.*;

public class UserManager{
    private static Map<String, User> userMap = new HashMap<>();

    public static boolean signUp(String id, String pw) {
        if (id == null || id.trim().isEmpty() || pw == null || pw.trim().isEmpty()) return false;
        if (userMap.containsKey(id)) return false;
        userMap.put(id, new User(id, pw));
        return true;
    }

    public static boolean isDuplicated(String id) {
        return userMap.containsKey(id);
    }

    public static User login(String id, String pw) {
        User user = userMap.get(id);
        if (user != null && user.getPassword().equals(pw)) {
            return user;
        }
        return null;
    }

    public static Map<String, User> getAllUsers() {
        return Collections.unmodifiableMap(userMap);
    }

    public static void setUsers(Map<String, User> map) {
        if (map != null) userMap = map;
    }
}