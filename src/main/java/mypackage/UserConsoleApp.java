package mypackage;


import java.util.List;
import java.util.Scanner;

public class UserConsoleApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Меню пользователя ===");
            System.out.println("1. Создать нового пользователя");
            System.out.println("2. Показать всех пользователей");
            System.out.println("3. Найти пользователя по ID");
            System.out.println("4. Обновить пользователя");
            System.out.println("5. Удалить пользователя");
            System.out.println("0. Выход");
            System.out.print("Выбор: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> createUser();
                case 2 -> showAllUsers();
                case 3 -> findUserById();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 0 -> {
                    System.out.println("До встречи!");
                    return;
                }
                default -> System.out.println("Неверный ввод. Попробуй снова.");
            }
        }
    }

    private static void createUser() {
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Фамилия: ");
        String surname = scanner.nextLine();
        System.out.print("Возраст: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Телефон: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        User user = new User(name, surname, age, phone, email);
        userDAO.saveUser(user);
        System.out.println("✅ Пользователь создан!");
    }

    private static void showAllUsers() {
        List<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей.");
        } else {
            users.forEach(User::show);
        }
    }

    private static void findUserById() {
        System.out.print("ID пользователя: ");
        int id = Integer.parseInt(scanner.nextLine());
        User user = userDAO.getUserById(id);
        if (user != null) {
            user.show();
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private static void updateUser() {
        System.out.print("ID пользователя для обновления: ");
        int id = Integer.parseInt(scanner.nextLine());
        User user = userDAO.getUserById(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.print("Новое имя (" + user.getName() + "): ");
        user.setName(scanner.nextLine());

        System.out.print("Новая фамилия (" + user.getSurname() + "): ");
        user.setSurname(scanner.nextLine());

        System.out.print("Новый возраст (" + user.getAge() + "): ");
        user.setAge(Integer.parseInt(scanner.nextLine()));

        System.out.print("Новый телефон (" + user.getPhone() + "): ");
        user.setPhone(scanner.nextLine());

        System.out.print("Новый email (" + user.getEmail() + "): ");
        user.setEmail(scanner.nextLine());

        userDAO.updateUser(user);
        System.out.println("✅ Данные обновлены.");
    }

    private static void deleteUser() {
        System.out.print("ID пользователя для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());
        userDAO.deleteUser(id);
        System.out.println("✅ Пользователь удалён.");
    }
}