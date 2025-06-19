package hipdogclub.Util;

import java.io.*;
import hipdogclub.Model.User;

public class FileManager {

    private static final String FILE_NAME = "user.dat";

    // User 객체를 파일에 저장
    public static void saveUser(User user) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에서 User 객체 불러오기 (없으면 새 User 반환)
    public static User loadUser() {
        User user = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            user = (User) in.readObject();
        } catch (FileNotFoundException e) {
            // 파일이 없으면 새 User("me") 생성
            user = new User("me");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            user = new User("me");
        }
        return user;
    }
}