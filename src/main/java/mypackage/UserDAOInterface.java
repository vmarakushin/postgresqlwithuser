package mypackage;
import java.util.List;


/**
 * Данный интерфейс {@code UserDAOInterface} указывает на необходимые к реализации в {@link UserDAO} методы
 * @author vmarakushin
 * @version 1.0
 */
public interface UserDAOInterface {

    /**
     * Возвращает список всех пользоваталей.
     * @return список всех пользователей.
     */
    public List<User> getAllUsers();


    /**
     * Возвращает пользователя по указанному id
     * @param id id искомого пользователя
     * @return пользователь или null, если пользователь не найден
     */
    public User getUser(int id);


    /**
     * Обновляет или создает юзера в базе данных
     * @param user обновленный(новый) юзер
     */
    public void updateUser(User user);


    /**
     * Удаляет юзера из базы данных
     * @param user юзер для удаления
     */
    public void deleteUser(User user);

    /**
     * Удаляет юзера по id
     * @param id айди удаляемого юзера
     */
    public void deleteUser(int id);
}
