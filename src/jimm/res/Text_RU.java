// #sijapp cond.if lang_RU is "true" #

package jimm.res;

import jimm.util.ResourceBundle;

public class Text_RU extends ResourceBundle {


    public Text_RU() {

        // Labels
        // MOTOROLA formatted buttons
        // #sijapp cond.if target is "MOTOROLA"#
        resources.put("backlight_timeout", "Время подсветки (сек)");
        resources.put("backlight_manual", "Ручная подсветка (*)");
        resources.put("select", "Выбор     ");
        resources.put("reset", "      Сброс");
        resources.put("cancel", "    Отмена");
        resources.put("back", "     Назад");
        resources.put("exit_button", "     Выход");
        resources.put("menu_button", "      Меню");
        resources.put("save", "Сохранить ");
        resources.put("ok", "OK         ");
        resources.put("reply", "Ответ      ");
        resources.put("close", "   Закрыть");
        resources.put("search_user", "Найти      ");
        resources.put("send", "Отправить");
        resources.put("next", "Следующий");
        // #sijapp cond.else#

        resources.put("reply", "Ответ");
        resources.put("close", "Закрыть");
        resources.put("select", "Выбрать");
        resources.put("reset", "Сброс");
        resources.put("cancel", "Отмена");
        resources.put("back", "Назад");
        resources.put("save", "Сохр.");
        resources.put("ok", "OK");
        resources.put("search_user", "Найти контакт");
        resources.put("send", "Отправить");
        resources.put("next", "Следующий");
        // #sijapp cond.end#

        resources.put("monday", "Понедельник");
        resources.put("tuesday", "Вторник");
        resources.put("wednesday", "Среда");
        resources.put("thursday", "Четверг");
        resources.put("friday", "Пятница");
        resources.put("saturday", "Суббота");
        resources.put("sunday", "Воскресенье");

        resources.put(".", ".");
        resources.put("about", "О программе");
        resources.put("about_info", "KICQ - аналог ICQ на старом протоколе\n\nKICQ - это аналог ICQ, основанный на старом протоколе OSCAR.\nВ основе - проект с открытым исходным кодом IserverD.\nТо есть это та самая аська (ну почти), что была у тебя в телефоне в 2008.\nАдрес сервера 195.66.114.37\n\nВерсия 0.4.4\nУстройство: ###TARGET###\nМодули: ###MODULES###\n\nhttp://narodweb.ru/kicq.html");
        resources.put("account", "Учетная запись");
        resources.put("add_group", "Добавить группу");
        resources.put("add_user", "Добавить контакт");
        resources.put("add_to_list", "Добавить");
        resources.put("addr", "Адрес");
        resources.put("age", "Возраст");
        resources.put("async", "Асинхронная передача");
        resources.put("attention", "Внимание!");
        resources.put("auth", "Авторизация");
        resources.put("auto_connect", "Подключаться автоматически");
        resources.put("beep", "Гудок");
        resources.put("birth_day", "День рождения");
        resources.put("black_on_white", "Чёрная на белом");
        resources.put("byte", "Байт");
        resources.put("cell_phone", "Сотовый");
        resources.put("chat", "Настройки чата");
        resources.put("chat_small_font", "Мелкий шрифт");
        resources.put("city", "Город");
        resources.put("clear", "Стереть");
        resources.put("cancel_ticker", "Отменить тикер");
        resources.put("color_scheme", "Цветовая схема");
        resources.put("conn_type", "Тип подключения");
        resources.put("connect", "Подключиться");
        resources.put("connecting", "Подключение");
        resources.put("copy_text", "Копировать текст");
        resources.put("cost", "Стоимость");
        resources.put("contact_list", "Список");
        resources.put("cp1251", "Исправлять ошибку CP1251?");
        resources.put("cpd", "Стоимость в день");
        resources.put("cpp", "Стоимость пакета");
        resources.put("currency", "Валюта");
        resources.put("dc_info", "DC Info");
        resources.put("del_group", "Удалить группу");
        resources.put("delete_chat", "Удалить чат");
        resources.put("deny", "Отклонить авторизацию");
        resources.put("denyedby", "Ваш запрос на авторизацию отклонил: ");
        resources.put("description", "Описание");
        resources.put("name_desc", "Имя файла и описание");
        resources.put("user_menu", "Меню контакта");
        resources.put("depart", "Отдел");
        resources.put("disconnect", "Отключиться");
        resources.put("disconnecting", "Отключение");
        resources.put("display_date", "Дата на заставке");
        resources.put("email", "E-mail");
        resources.put("emotions", "Смайлы");
        resources.put("error", "Ошибка");
        resources.put("exit", "Выход");
        resources.put("fax", "Факс");
        resources.put("female", "Ж");
        resources.put("female_male", "М/Ж");
        resources.put("filename", "Имя файла");
        resources.put("filetransfer", "Передача файла");
        resources.put("filepath", "Путь к файлу");
        resources.put("find", "Поиск");
        resources.put("firstname", "Имя");
        resources.put("free_heap", "Свободная память");
        resources.put("ft_name", "Отправить файл");
        resources.put("ft_cam", "Отправить фото с камеры");
        resources.put("gender", "Пол");
        resources.put("grant", "Авторизировать");
        resources.put("grantedby", "Вас авторизировал: ");
        resources.put("group_name", "Имя группы");
        resources.put("group_is_not_empty", "Выбранная группа не пустая!\nПереместите все контакты в другие группы!");
        resources.put("have_unread_mess", "У Вас остались непрочитанные сообщения. Выйти из программы?");
        resources.put("hide_offline", "Скрывать отключившихся");
        resources.put("home_page", "Д-я страничка");
        resources.put("home_info", "Дом");
        resources.put("info", "Информация");
        resources.put("init_ft", "Инициализация...");
        resources.put("insert_emotion", "Смайлики");
        resources.put("interests", "Интересы");
        resources.put("invisible_check", "Невидимый?");
        resources.put("kb", " КБ");
        resources.put("kbs", "КБ/сек");
        resources.put("keep_chat", "Хранить историю чата?");
        resources.put("keep_conn_alive", "Поддерживать подключение?");
        resources.put("keylock", "Блокировка");
        resources.put("keylock_message", "Для разблокировки нажмите и удерживайте клавишу #");
        resources.put("keylock_enable", "Блокировка клавиатуры");
        resources.put("keylock_enabled", "Блокировка включена");
        resources.put("keyword", "Ключевое слово");
        resources.put("language", "Язык интерфейса");
        resources.put("lang_BG", "Болгарский");
        resources.put("lang_BR", "Португальский (Бразилия)");
        resources.put("lang_CZ", "Чешский");
        resources.put("lang_DE", "Немецкий");
        resources.put("lang_EN", "Английский");
        resources.put("lang_ES", "Испанский");
        resources.put("lang_HE", "Еврейский");
        resources.put("lang_IT", "Итальянский");
        resources.put("lang_LT", "Литовский");
        resources.put("lang_PL", "Польский");
        resources.put("lang_RU", "Русский");
        resources.put("lang_SE", "Шведский");
        resources.put("lang_SR", "Српски");
        resources.put("lang_UA", "Украинский");
        resources.put("lastname", "Фамилия");
        resources.put("latest_ver", "Самая последняя доступная версия");
        resources.put("loading", "Загрузка");
        resources.put("main_info", "О пользователе");
        resources.put("male", "M");
        resources.put("me", "Я");
        resources.put("menu", "Меню");
        resources.put("message_notification", "Уведомление о сообщениях");
        resources.put("msg_sound_file_name", "Файл звука сообщения");
        resources.put("message", "Сообщение");
        resources.put("message_from", "Сообщение от");
        resources.put("minimize", "Фоновый режим");
        resources.put("name", "Имя");
        resources.put("nick", "Ник");
        resources.put("no", "Нет");
        resources.put("not", "без");
        resources.put("no_results", "Нет результатов");
        resources.put("no_not_empty_gr", "Удаление не пустых групп не поддерживается");
        resources.put("not_implemented", "Функция не поддерживается");
        resources.put("noreason", "Причина не дана");
        resources.put("notes", "Заметки");
        resources.put("notice", "Уведомление");
        resources.put("nr", "Номер");
        resources.put("once_a_session", "Один раз за сессию");
        resources.put("onl_notification", "Информировать о подключившемся контакте");
        resources.put("onl_sound_file_name", "Файл звука подключения");
        resources.put("only_online", "Показывать только онлайн контакты");
        resources.put("options", "Настройки");
        resources.put("options_account", "Учетная запись");
        resources.put("options_cost", "Стоимость");
        resources.put("options_effect", "Вам необходимо переподключиться для применения изменений");
        resources.put("options_interface", "Интерфейс");
        resources.put("options_network", "Сеть");
        resources.put("options_other", "Разное");
        resources.put("options_signaling", "Звук");
        resources.put("password", "Пароль");
        resources.put("phone", "Телефон");
        resources.put("plength", "Длина оплачиваемого пакета в КБ");
        resources.put("plsauthme", "Привет! Пожалуйста, авторизируй меня и разреши добавить тебя в контакт-лист");
        resources.put("position", "Должность");
        resources.put("prev", "Предыдущий");
        resources.put("quote", "Цитировать");
        resources.put("reason", "Причина");
        resources.put("remove", "Удалить из списка");
        resources.put("remove_me", "Удалиться из его КЛ");
        resources.put("remove_me_from", "Удалиться из контакт листа ");
        resources.put("remove_group", "Удалить группу");
        resources.put("remove_user", "Удалить контакт");
        resources.put("rename", "Переименовать");
        resources.put("requauth", "Запрос авторизации");
        resources.put("reqstatmsg", "Статус контакта");
        resources.put("requ", "Требуется");
        resources.put("requno", "Не требуется");
        resources.put("res", "Разрешение");
        resources.put("results", "Результаты");
        resources.put("send_img", "Отправить фото");
        resources.put("send_message", "Новое сообщение");
        resources.put("send_url", "Новый URL");
        resources.put("server", "Сервер");
        resources.put("server_host", "Имя сервера");
        resources.put("server_port", "Порт");
        resources.put("session", "Сессия");
        resources.put("set_status", "Статус");
        resources.put("shadow_con", "Доп. подключение");
        resources.put("show_user_groups", "Группировать контакты");
        resources.put("since", "Начиная с");
        resources.put("size", "Размер");
        resources.put("sound", "Звуковой файл");
        resources.put("sort_by", "Сортировать контакт-лист");
        resources.put("sort_by_name", "По имени");
        resources.put("sort_by_status", "По статусу");
        resources.put("speed", "Скорость");
        resources.put("state", "Респ-ка/район/округ");
        resources.put("status", "Статус");
        resources.put("status_message", "Статусное сообщение");
        resources.put("status_away", "Отсутствую");
        resources.put("status_chat", "Свободен для беседы");
        resources.put("status_dnd", "Не беспокоить");
        resources.put("status_invisible", "Невидимый");
        resources.put("status_na", "Недоступен");
        resources.put("status_occupied", "Занят");
        resources.put("status_offline", "Оффлайн");
        resources.put("status_online", "Онлайн");
        resources.put("successful", "успешной");
        resources.put("sysnotice", "Системное уведомление");
        resources.put("text_too_long", "Текст слишком большой для вставки в редактор");
        resources.put("timeout_interv", "Таймаут пинга");
        resources.put("title", "Название");
        resources.put("traffic", "Трафик");
        resources.put("uin", "UIN");
        resources.put("url", "URL");
        resources.put("use_history", "Хранить историю");
        resources.put("use_smiles", "Смайлики");
        resources.put("user_add", "Добавить контакт");
        resources.put("user_search", "Найти контакт");
        resources.put("vibration", "Вибрация");
        resources.put("viewfinder", "Видоискатель");
        resources.put("volume", "Громкость");
        resources.put("wait", "Пожалуйста, ждите ...");
        resources.put("when_locked", "Если заблокирован");
        resources.put("warning", "Предупреждение");
        resources.put("wantsyourauth", " хочет авторизироваться. Причина: ");
        resources.put("was", "была");
        resources.put("whichgroup", "В какой группе?");
        resources.put("white_on_black", "Белая на чёрном");
        resources.put("white_on_blue", "Белая на синем");
        resources.put("work_info", "Работа");
        resources.put("yes", "Да");
        resources.put("youwereadded", "Вас добавил: ");

        resources.put("proxy", "Прокси");
        resources.put("proxy_type", "Тип прокси");
        resources.put("proxy_do_not_use", "Не использовать");
        resources.put("proxy_socks4", "Socks4");
        resources.put("proxy_socks5", "Socks5");
        resources.put("proxy_guess", "Авто");
        resources.put("proxy_server_host", "Прокси сервер:");
        resources.put("proxy_server_port", "Порт:");

        resources.put("proxy_server_login", "Логин прокси:");
        resources.put("proxy_server_pass", "Пароль прокси:");

        resources.put("auto_retry_count", "Попыток подключения:");

        //#sijapp cond.if modules_HISTORY is "true" #
        resources.put("add_to_history", "В историю");
        resources.put("text_to_find", "Текст");
        resources.put("find_backwards", "В обратном направлении");
        resources.put("find_case_sensitiv", "С учётом регистра");
        resources.put("history_info", "Информация");
        resources.put("hist_cur", "Число сообщений контакта");
        resources.put("hist_rc", "Число сообщений в истории");
        resources.put("hist_size", "Использовано (Кб)");
        resources.put("hist_avail", "Всего места (Кб)");
        resources.put("history", "История сообщений");
        resources.put("not_found", "не найден");
        resources.put("clear_all", "Стереть все записи");
        resources.put("clear_all2", "Стереть все записи для всех контактов?");
        resources.put("clear_history", "Очищать историю");
        resources.put("ch_never", "Никогда");
        resources.put("ch_day", "Каждый день");
        resources.put("ch_week", "Каждую неделю");
        resources.put("ch_month", "Каждый месяц");
        //#sijapp cond.end#

        // Generic errors
        resources.put("error_100", "Неизвестная ошибка (#100.EXT)");

        // Login specific errors
        resources.put("error_110", "Множественный вход с тем же UIN (#110.EXT)");
        resources.put("error_111", "Неверный пароль (#111.EXT)");
        resources.put("error_112", "Несуществующий UIN (#112.EXT)");
        resources.put("error_113", "Слишком много клиентов с одного IP (#113.EXT)");
        resources.put("error_114", "Попытки исчерпаны (#114.EXT)");
        resources.put("error_115", "Контакт-лист не может быть обработан (#115.EXT)");
        resources.put("error_116", "Оффлайн сообщение не может быть обработано (#116.EXT)");
        resources.put("error_117", "Нет UIN и/или пароля (#117.EXT)");
        resources.put("error_118", "Сервер не отвечает (#118.EXT)");

        // Network communication specific exceptions
        // Connection to server
        resources.put("error_120", "Произошла ошибка ввода-вывода (#120.EXT)");
        resources.put("error_121", "Требуемое TCP соединение неосуществимо (#121.EXT)");
        resources.put("error_122", "Указанный сервер и/или порт некорректны (#122.EXT)");
        resources.put("error_123", "Соединение не может быть установлено (#123.EXT)");
        resources.put("error_124", "Входной поток не синхронизован (#124.EXT)");


        // Parsing specific errors
        resources.put("error_130", "FLAP заголовок не обработан (#130.EXT)");
        resources.put("error_131", "Неизвестный канал (#131.EXT)");
        resources.put("error_132", "Пакет подсоединенного канала не обработан (#132.EXT)");
        resources.put("error_133", "SNAC заголовок не обработан (#133.EXT)");
        resources.put("error_134", "Ошибка пакета канала не обработана (#134.EXT)");
        resources.put("error_135", "Пакет отсоединенного канала не обработан (#135.EXT)");
        resources.put("error_136", "Пакет канала ping не обработан (#136.EXT)");
        resources.put("error_137", "Заголовок старого протокола ICQ не обработан (#137.EXT)");

        // Action errors
        resources.put("error_140", "Требуемое действие не может быть выполнено в данное время (#140.EXT)");

        // Specific action errors
        resources.put("error_150", "Полученное сообщение не обработано (#150.EXT)");
        resources.put("error_151", "Полученное сообщение 1 типа не обработано (#151.EXT)");
        resources.put("error_152", "Полученное сообщение 2 типа не обработано (#152.EXT)");
        resources.put("error_153", "Полученное сообщение 4 типа не обработано (#153.EXT)");
        resources.put("error_154", "Ошибка обновления списка пользователей (#154.EXT)");
        resources.put("error_155", "Объект уже находится в списке на сервере (#155.EXT)");
        resources.put("error_156", "Ошибка добавления. Попробуйте снова (#156.EXT)");
        resources.put("error_157", "Разрешено не больше количество элементов этого типа (#157.EXT)");
        resources.put("error_158", "Вы совершили попытку добавить пользователя ICQ в спиок AIM (#158.EXT)");
        resources.put("error_159", "Сервер не отвечает на запрос поиска. Попробуйте снова (#159.EXT)");
        resources.put("error_160", "Ошибка поиска (#160.EXT)");
        resources.put("error_161", "У Вас нет ни одной группы в списке. Пожалуйста, создайте новую группу (#161.EXT)");

        // Other errors
        resources.put("error_170", "Возможно, недостаточно памяти (#160.EXT)");
        resources.put("error_171", "Невозможно получить мета информацию (#161.EXT)");

        // Camera errors
        resources.put("error_180", "Ошибка создания объекта 'VideoControl' (#180.EXT)");
        resources.put("error_181", "Ошибка инициализации видеоискателя (#181.EXT)");
        resources.put("error_182", "Ошибка запуска видеоискателя (#182.EXT)");
        resources.put("error_183", "Ошибка во время получения снимка (#183.EXT)");
        resources.put("error_185", "Фотосъёмка не поддерживается (#185.EXT)");

        // File transfer errors
        resources.put("error_190", "Передача файлов не поддерживается для клиентов ICQ ниже 8-ой версии (#190.EXT)");
        resources.put("error_191", "Ошибка чтения файла. Возможно, данная функция не поддерживается (#191.EXT)");
        resources.put("error_192", "Некорректный путь к файлу. Возможно, данная функция не поддерживается (#192.EXT)");

    }
}

// #sijapp cond.end #
