package mypackage;
import java.util.*;
/**
 * Класс {@code UserIdService} представляет собой систему управления id пользователей.
 * Содержит очередь свободных и сет занятых айди.
 * Предоставляет методы для выдачи id пользователю и освобождения id.
 * Масштабируется автоматически
 * Класс утилитарный, нельзя создавать его экземпляр.
 * @author vmarakushin
 * @version 1.0
 */

public final class UserIdService {

    /** Количество создаваемых по дефолту Id, система не ужмется ниже этого значения. */
    private final static int DEFAULT_IDS = 1000;


    /** Максимальный процент использования id для масштабирования */
    private final static float MAX_LOAD_FACTOR = 0.75f;


    /** Множитель масштабирования на увеличение */
    private final static float SCALING_FACTOR = 2.0f;


    /** Минимальный процент использования id для масштабирования */
    private final static float MIN_LOAD_FACTOR = 0.3f;


    /** Множитель масштабирования на уменьшение */
    private final static float SHRINKING_FACTOR = 0.5f;


    /** Очередь свободных id */
    private static final NavigableSet<Integer> free = new TreeSet<>();


    /** Сет занятых id */
    private static final Set<Integer> used = new HashSet<>();


    /** Заполняет свободное дерево ID в количестве MAXID */
     static {
         for (int i = 1; i <= DEFAULT_IDS; i++) {free.add(i);}
         System.out.println("USER_ID_SERVICE: Сгенерировано " + DEFAULT_IDS + " уникальных ID.");
    }


    /**
     * Выдает наименьший свободный айди по запросу.
     * @return наименьший свободный Id.
     */
    public static int getId() {
        int id = free.pollFirst();
        free.remove(id);
        used.add(id);
        System.out.println("USER_ID_SERVICE: Id " + id + " выдан");
        manageIdPoolCapacity();
        return id;

    }


    /**
     * Освобождает указанный Id.
     */
    public static void freeId(int id) {
        if (!used.contains(id)) {
            System.out.println("USER_ID_SERVICE: Попытка освободить незанятый ID " + id);
            return;
        }
        used.remove(id);
        free.add(id);
        System.out.println("USER_ID_SERVICE: Id " + id + " освобожден");
        manageIdPoolCapacity();
    }


    /**
     * Регулирует количество id
     *
     * При загрузке, превышающей MAX_LOAD_FACTOR
     * Добавляет ID в соответствии с SCALING_FACTOR
     * Номер Id перед добавлением проверяется на уникальность
     *
     * При загрузке, ниже MIN_LOAD_FACTOR
     * Удаляет наибольшие свободные Id в соответствии с SHRINKING_FACTOR
     */
    private static void manageIdPoolCapacity() {
        int totalIds = used.size() + free.size();
        float currentLoadFactor =  (float) used.size() / (float) totalIds;

        /** Сценарий переполнения */
        if (currentLoadFactor > MAX_LOAD_FACTOR) {
            int newCapacity = (int)(totalIds * SCALING_FACTOR);
            int offer = 1;
            while (totalIds < newCapacity) {
                if (used.contains(offer) || free.contains(offer)) {
                    offer++;
                }
                else{
                    free.add(offer);
                    offer++;
                    totalIds++;
                }
            }
            System.out.println("USER_ID_SERVICE: Количество ID увеличено до " + newCapacity);
        }

        /** Сценарий недобора */
        if (currentLoadFactor < MIN_LOAD_FACTOR) {
            int newCapacity = (int)(totalIds * SHRINKING_FACTOR);
            if (newCapacity <= DEFAULT_IDS) {return;}
            while (totalIds > newCapacity) {
                int maxId = free.last();
                free.remove(maxId);
                totalIds--;
            }
            System.out.println("USER_ID_SERVICE: Количество ID уменьшено до " + newCapacity);
        }
    }

}
