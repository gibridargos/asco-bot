package com.saidqosimov.taxinfobot.util;

public interface Constants {
    String START = "/start";
    String[] AUTH_RESPONSE = {"<b>TaxInfo</b> телеграм-ботга хуш келибсиз!\n" + "Тилни танланг", "Добро пожаловать в телеграм-бот <b>TaxInfo</b>!\n" + "Выберите язык"};

    String[] INFO = {"ℹ\uFE0F Солиқлар тўғрисида маълумот", "ℹ\uFE0F Информация о налогах"};
    String[] MY_TAXES = {"\uD83D\uDDC3 Менинг солиқларим", "\uD83D\uDDC3 Мои налоги"};
    String[] CONTACT_US = {"\uD83D\uDC8C Биз билан алоқа", "\uD83D\uDC8C Связаться с нами"};
    String[] SETTINGS = {"⚙\uFE0F Созламалар", "⚙\uFE0F Настройки"};
    String[] TAX_LIST = {"\uD83D\uDDC2 Барча солиқ турлари", "\uD83D\uDDC2 Все виды налогов"};

    String[] STANDARD_AOS = {"\uD83D\uDDC2 Стандарт АОС", "\uD83D\uDDC2 Стандартный налог с оборота"};
    String[] STANDARD_QQS = {"\uD83D\uDDC2 Стандарт ҚҚС", "\uD83D\uDDC2 Стандартный НДС"};
    String LANG_UZ = "\uD83C\uDDFA\uD83C\uDDFF Ўзбекча";
    String LANG_RU = "\uD83C\uDDF7\uD83C\uDDFA Русский";

    String THREE_DAYS_AGO_ON_NOTIFICATION = "three_days_ago_on_notification";
    String THREE_DAYS_AGO_OFF_NOTIFICATION = "three_days_ago_off_notification";
    String TWO_DAYS_AGO_ON_NOTIFICATION = "two_days_ago_on_notification";
    String TWO_DAYS_AGO_OFF_NOTIFICATION = "two_days_ago_off_notification";
    String THE_DAY_BEFORE_ON_NOTIFICATION = "the_day_before_on_notification";
    String THE_DAY_BEFORE_OFF_NOTIFICATION = "the_day_before_off_notification";
    String[] DISABLE = {"\uD83D\uDD15 Ўчириш", "\uD83D\uDD15 отключить"};
    String[] ENABLE = {"\uD83D\uDD14 Ёқиш", "\uD83D\uDD14 включить"};

    String[] LANGUAGE_SUCCESSFUL_CHANGED = {"Тил ўзгартирилди ✅", "Язык был изменен ✅"};
    String[] SELECT_LANGUAGE = {"Керакли тилни танланг \uD83D\uDC47", "Выберите желаемый язык \uD83D\uDC47"};
    String[] CHANGE_LANGUAGE = {"\uD83C\uDF0D Тилни ўзгартириш", "\uD83C\uDF0D Изменить язык"};
    String BACK_BUTTON = "⏪";
    String NEXT_BUTTON = "⏩";
    String[] SAVE = {"✅ Сақлаш", "✅ Сохранять"};
    String[] CLEAR = {"\uD83D\uDEAB Тозалаш", "\uD83D\uDEAB Очистка"};
    String PAGE = "page:";
    String SAVE_CACHE = "save_cache";
    String CLEAR_CACHE = "clear_cache";
    //String DELETE_TAX = "DELETE_TAX";
    String[] DELETE_ALL_TAXES_REQUEST = {"TaxInfo ботига, Сиз томонингиздан киритилган барча маълумотлар ўчирилади.\n" +
            "\n" +
            "Тасдиқлайсизми?", "Вся информация, введенная вами в боте TAXinfo, будет удалена.\n" +
            "\n" +
            "Вы одобряете?"};
    String[] ALLOW = {"✅ Тасдиқлаш", "✅ Подтверждение"};
    String[] DENY = {"❌ Бекор қилиш", "❌ Отмена"};
    String DELETE_SELECTED_TAXES = "delete_selected_taxes";
    String[] DELETE_SELECTED_TAXES_BUTTON = {"\uD83D\uDDD1 Танланганларни ўчириш", "\uD83D\uDDD1 Удалить выбранные"};
    String[] CLEAR_ALL_TAXES_BUTTON = {"⛔\uFE0F Барча маълумотларни ўчириш", "⛔\uFE0F Удалить все данные"};
    String CLEAR_ALL_TAXES = "clear_all_taxes";


    String[] CONTACT = {"Ассалому алайкум, Бот бўйича таклиф ва эътирозларингиз бўлса хабар юборинг.\nhttps://t.me/TaxInfoChat_bot", "Здравствуйте, если у вас есть предложения или возражения по поводу бота, отправьте сообщение.\nhttps://t.me/TaxInfoChat_bot"};
    String[] MY_TAXES_CAPTION = {"Созламалар / Солиқларни қўшиш панели орқали солиқ туралини қўшишингиз мумкин", "Вы можете добавить тип налога через панель «Настройки» / «Добавить налоги»."};

    String[] BACK_TO_MAIN_MENU = {"\uD83D\uDD19 Асосий менюга қайтиш", "\uD83D\uDD19 Вернуться в главное меню"};

    String[] EDIT_NOTIFICATION_DATE = {"\uD83D\uDCC6 Хабарнома кунини ўзгартириш", "\uD83D\uDCC6 Изменение даты уведомления"};
    String[] USER_MANUAL = {"\uD83D\uDCC4 Фойдаланиш қўлланмаси", "\uD83D\uDCC4 Инструкция пользователя"};
    String[] SELECT_TAX_HELP = {"Эслатма (маълумот) келиши учун солиқ турларини танланг ва сақлаш тугмасини босинг \uD83D\uDC47", "Выберите виды налогов и нажмите кнопку «Сохранить», чтобы получить напоминание (информацию) \uD83D\uDC47"};

    String[] ADD_TAXES_BUTTON = {"\uD83D\uDDC2 Солиқларни қўшиш", "\uD83D\uDDC2 Добавить налоги"};
    String[] MAIN_MENU = {"🏠 Асосий мену", "🏠 Главное меню"};
    String[] SUCCESS_DELETE = {"Муваффақиятли ўчирилди", "Удален успешно"};
    String[] DATA_SAVED = {"Маълумотлар сақланди✅", "Данные сохранены✅"};
    String[] DATA_CLEARED = {"Маълумотлар тозаланди✅", "Данные удалены✅"};
    String[] NEED_PAY_FOR_SELECTED_TAX = {"Ушбу ойда тўланиши керак бўлган солиқлар ва топширилиши керак бўлган ҳисоботлар\n", "Налоги подлежащие к оплате в текущем месяце и отчеты подлежащие к сдачи\n"};
    String[] DATA_NOT_ENTERED = {"⭕\uFE0F Маълумот танланмаган", "⭕\uFE0F Информация не выбрана"};
    String DENY_ACCESS = "deny_access";

    String[] NOTIFICATION_DATE_REQUEST = {"\uD83D\uDCC6 Хабарнома (эслатма) келиш муддатини танланг", "\uD83D\uDCC6 Выберите дату прибытия уведомления (напоминания)"};
    String[] THE_DAY_BEFORE = {"1 кун аввал ⏰", "1 день назад ⏰"};
    String[] TWO_DAYS_BEFORE = {"2 кун аввал ⏰", "2 дня назад ⏰"};
    String[] THREE_DAYS_BEFORE = {"3 кун аввал ⏰", "3 дня назад ⏰"};
    String[] DENY_DELETE = {"Бекор қилинди", "Отменено"};

    String[] NOTIFICATION = {"Э С Л А Т М А ❗\uFE0F❗\uFE0F❗\uFE0F  : ", "Н А П О М И Н А Н И Е ❗\uFE0F❗\uFE0F❗\uFE0F"};

    String[] THREE_DAY_CHANGE_NOTIFICATION_TO_ON = {"3 кун олдин огоҳлантириш фаоллаштирилди \uD83D\uDD14", "Оповещение активировано 3 дня назад \uD83D\uDD14"};
    String[] TWO_DAY_CHANGE_NOTIFICATION_TO_ON = {"2 кун олдин огоҳлантириш фаоллаштирилди \uD83D\uDD14", "Оповещение активировано 2 дня назад \uD83D\uDD14"};
    String[] THE_DAY_CHANGE_NOTIFICATION_TO_ON = {"1 кун олдин огоҳлантириш фаоллаштирилди \uD83D\uDD14", "Оповещение активировано 1 день назад \uD83D\uDD14"};
    String[] THREE_DAY_CHANGE_NOTIFICATION_TO_OFF = {"3 кун олдин огоҳлантириш фаолсизлантирилди \uD83D\uDD15", "Оповещение отключено 3 дня назад \uD83D\uDD15"};
    String[] TWO_DAY_CHANGE_NOTIFICATION_TO_OFF = {"2 кун олдин огоҳлантириш фаолсизлантирилди \uD83D\uDD15", "Оповещение отключено 2 дня назад \uD83D\uDD15"};
    String[] THE_DAY_CHANGE_NOTIFICATION_TO_OFF = {"1 кун олдин огоҳлантириш фаолсизлантирилди \uD83D\uDD15", "Оповещение отключено 1 день назад \uD83D\uDD15"};


    String[] MONTHLY_PHOTO = {
            "https://instagram.fhan5-11.fna.fbcdn.net/v/t51.29350-15/454405879_355447300947480_288364587250816226_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-11.fna.fbcdn.net&_nc_cat=100&_nc_ohc=QUaOq8VspUcQ7kNvgGeMZL0&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYALd8lzg9zM5mFoKKRKEH--Yz1ez5RqU9MRqpajs6dtcw&oe=66BBDCA9&_nc_sid=10d13b",
            "https://instagram.fhan5-11.fna.fbcdn.net/v/t51.29350-15/454617118_890835896415948_250522346155072732_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-11.fna.fbcdn.net&_nc_cat=100&_nc_ohc=eGEM_VAI8yEQ7kNvgGkTf4D&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYASLt9LQeB6jxkiFAmdtxqQNf-W3BUbN9rj3G7prh66Jg&oe=66BBFA3D&_nc_sid=10d13b",
            "https://instagram.fhan5-2.fna.fbcdn.net/v/t51.29350-15/454610816_1149894806078848_5261212359983998469_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-2.fna.fbcdn.net&_nc_cat=104&_nc_ohc=14Sapxzt4DwQ7kNvgGDTcv1&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYCLPwrsSYn5PfbItmpAcenyKCjUdHkgcDtseZ2oYW_Oqg&oe=66BBE4A7&_nc_sid=10d13b",
            "https://instagram.fhan5-8.fna.fbcdn.net/v/t51.29350-15/454496031_525192453293564_571054997652807330_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-8.fna.fbcdn.net&_nc_cat=108&_nc_ohc=LgGchM-FJaEQ7kNvgEIrHbI&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYBkVULPBqgUIqvpYHo_7FuQDgbqxqOXLxagJcPad-v79Q&oe=66BBEADA&_nc_sid=10d13b",
            "https://instagram.fhan5-9.fna.fbcdn.net/v/t51.29350-15/454160098_1209302393446870_22584886997520392_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-9.fna.fbcdn.net&_nc_cat=110&_nc_ohc=fHaciLf6pEEQ7kNvgEHvqLf&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYDTvNOhsuSaX9Qs8waN-4aXUUacRz9B7QikOAPptZSlaA&oe=66BC0F2B&_nc_sid=10d13b",
            "https://instagram.fhan5-2.fna.fbcdn.net/v/t51.29350-15/454367675_1866604433850184_5447320895798675134_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-2.fna.fbcdn.net&_nc_cat=104&_nc_ohc=Zt8I-gInErQQ7kNvgFpuKq3&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYBN862ZC5sV_1FYrGMMICPGLGCzjfeH3VYrjqhzAav1Zg&oe=66BBDE32&_nc_sid=10d13b",
            "https://instagram.fhan5-10.fna.fbcdn.net/v/t51.29350-15/454513737_1009046551005553_7211128918932416919_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-10.fna.fbcdn.net&_nc_cat=101&_nc_ohc=ettY76jtNO4Q7kNvgG8BCrV&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYDxH4rqdwCRIW2Lb_HCJ1RoHXwDM0vAF6dCkY89vJW9SQ&oe=66BBE585&_nc_sid=10d13b",
            "https://instagram.fhan5-8.fna.fbcdn.net/v/t51.29350-15/454553281_886970076597954_779421764232672839_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-8.fna.fbcdn.net&_nc_cat=106&_nc_ohc=tK2KlycbTZ0Q7kNvgEC0QQU&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYDUi95wwXxA7uwyqTRJUHsu1AAPkUaRiBDf82HrVpOxjw&oe=66BBF32C&_nc_sid=10d13b",
            "https://instagram.fhan5-8.fna.fbcdn.net/v/t51.29350-15/454457857_2278024069256672_4825515989938881118_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-8.fna.fbcdn.net&_nc_cat=108&_nc_ohc=TCXv6UL2x78Q7kNvgFbRq22&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYDgOwaXddOo6GrMhP7gALPYrSlmA2nZzBxD0lZ8ht6lXQ&oe=66BBFD51&_nc_sid=10d13b",
            "https://instagram.fhan5-10.fna.fbcdn.net/v/t51.29350-15/454905912_489924110457923_4832927736379825970_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-10.fna.fbcdn.net&_nc_cat=111&_nc_ohc=2bUHKpHJr8cQ7kNvgE1C6q0&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYB-a-EIC4ZIoeH5Nvwxw-VEAx8AXpn6cROHtHidKgAOrw&oe=66BBF5D4&_nc_sid=10d13b",
            "https://instagram.fhan5-8.fna.fbcdn.net/v/t51.29350-15/454655813_929874102231570_6416810586705151909_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-8.fna.fbcdn.net&_nc_cat=106&_nc_ohc=OxagG-BePLQQ7kNvgG-P2KD&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYCc3atwxmmqE-4pAkHbdFVYQEpdnhCL1UOd7yKMqaSR9Q&oe=66BC0A86&_nc_sid=10d13b",
            "https://instagram.fhan5-10.fna.fbcdn.net/v/t51.29350-15/454604658_1697174974452851_1740977403853010397_n.webp?stp=dst-jpg_e35&_nc_ht=instagram.fhan5-10.fna.fbcdn.net&_nc_cat=111&_nc_ohc=iT6H3cxD1iYQ7kNvgGyHU38&edm=APs17CUBAAAA&ccb=7-5&oh=00_AYAkhu1EtyrRnS8569xr5LmZ6CL7G4bJzcsbwJKRh3plwA&oe=66BC0CEF&_nc_sid=10d13b"};
    String[] CAPTION = {"\uD83D\uDCC4 Фойдаланиш қўлланмаси", "\uD83D\uDCC4 Инструкция пользователя"};

    String[] REPORTS_DEADLINE = {"\uD83D\uDCE3 Ҳисоботларни тақдим этиш:", "\uD83D\uDCE3 Сдача отчетов:"};
    String[] TAX_PAY_DEADLINE = {"\uD83D\uDCE3 Солиқларни тўлаш:", "\uD83D\uDCE3 Оплата налогов:"};

    String[] QQS_AOS_HELP = {"Ушбу солиқ турлари бўйича эслатма (маълумот) келиши учун сақлаш тугмасини босинг", "Нажмите кнопку «Сохранить», чтобы получить напоминание (информацию) об этих видах налогов."};
    String[] SUCCESS_DELETE_ALL_TAXES = {"Барча маълумотларингиз маваффақиятли ўчирилди. Илтимос ушбу чат тарихини ўчириб ташланг ёки /start буйруғини беринг.", "Все ваши данные были успешно удалены. Пожалуйста, удалите эту историю чата или введите команду /start."};
}
