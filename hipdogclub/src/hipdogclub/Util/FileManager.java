package hipdogclub.Util;

import hipdogclub.Model.UserManager;

import java.io.*;
import java.util.Map;

public class FileManager {
    private static final String FILE_PATH = "Data/users.dat";

    public static void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(UserManager.getAllUsers());
            System.out.println("✔ 데이터 저장 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj = in.readObject();
            if (obj instanceof Map<?, ?>) {
                UserManager.setUsers((Map<String, hipdogclub.Model.User>) obj);
                System.out.println("✔ 데이터 불러오기 완료");
            }
        } catch (Exception e) {
            System.out.println("초기 실행 - 저장된 데이터 없음");
        }
    }
}