package hipdogclub.Model;

import java.io.*;

public class UserDataManager implements Serializable {
    private static final String FILE_PATH = "userdata.ser";

    // 저장
    public static void saveUser(User user) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 불러오기
    public static User loadUser() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (User) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // 파일이 없거나 오류면 null 반환
            return null;
        }
    }
}