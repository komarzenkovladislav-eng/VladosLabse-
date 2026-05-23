package com.example.ui

object Localization {
    private val ruStrings = mapOf(
        "app_title" to "Расписание",
        "homework" to "ДЗ",
        "settings" to "Настройки",
        "welcome" to "Привет, %s!",
        "next_lesson" to "Следующий урок",
        "no_next_lesson" to "Нет уроков на сегодня",
        "starts_in" to "До начала %d мин",
        "ends_in" to "До конца %d мин",
        "lesson_active" to "Урок идет (%d мин осталось)",
        "onboarding_title" to "Добро пожаловать!",
        "onboarding_desc" to "Создайте ваш профиль, чтобы настроить индивидуальное расписание.",
        "first_name" to "Имя",
        "last_name" to "Фамилия",
        "save" to "Сохранить",
        "edit_profile" to "Редактировать профиль",
        "day_monday" to "ПН",
        "day_tuesday" to "ВТ",
        "day_wednesday" to "СР",
        "day_thursday" to "ЧТ",
        "day_friday" to "ПТ",
        "add_lesson_title" to "Добавить урок",
        "day_of_week" to "День недели",
        "lesson_number" to "Номер урока",
        "subject_name" to "Название предмета",
        "start_time" to "Время начала",
        "end_time" to "Время окончания",
        "empty_schedule" to "Нет уроков в этот день",
        "empty_homework" to "Нет домашних заданий на этот день",
        "add_lesson_btn" to "Добавить",
        "cancel" to "Отмена",
        "assigned_today" to "Задали на сегодня",
        "dues_tomorrow" to "Домашка на завтра",
        "theme" to "Тема",
        "theme_dark" to "Чёрная (Dark AI mode)",
        "theme_light" to "Белая (Light AI mode)",
        "language" to "Язык",
        "edit_avatar" to "Выбрать аватар",
        "placeholder_assign" to "Нажмите, чтобы записать ДЗ...",
        "profile_block" to "Данные профиля",
        "interface_block" to "Интерфейс",
        "choose_day" to "Выберите день",
        "choose_num" to "Номер урока (1-8)",
        "invalid_fields" to "Пожалуйста, заполните все поля корректно",
        "saved" to "Сохранено!",
        "lesson_label" to "Урок %d",
        "empty_subject" to "Свободный слот",
        "hint_click_neon" to "Нажмите на урок для переливающейся неоновой подсветки!"
    )

    private val uaStrings = mapOf(
        "app_title" to "Розклад",
        "homework" to "ДЗ",
        "settings" to "Налаштування",
        "welcome" to "Привіт, %s!",
        "next_lesson" to "Наступний урок",
        "no_next_lesson" to "Немає уроків на сьогодні",
        "starts_in" to "До початку %d хв",
        "ends_in" to "До кінця %d хв",
        "lesson_active" to "Урок іде (%d хв залишилось)",
        "onboarding_title" to "Ласкаво просимо!",
        "onboarding_desc" to "Створіть свій профіль, щоб налаштувати індивідуальний розклад.",
        "first_name" to "Ім'я",
        "last_name" to "Прізвище",
        "save" to "Зберегти",
        "edit_profile" to "Редагувати профіль",
        "day_monday" to "ПН",
        "day_tuesday" to "ВТ",
        "day_wednesday" to "СР",
        "day_thursday" to "ЧТ",
        "day_friday" to "ПТ",
        "add_lesson_title" to "Додати урок",
        "day_of_week" to "День тижня",
        "lesson_number" to "Номер уроку",
        "subject_name" to "Назва предмета",
        "start_time" to "Час початку",
        "end_time" to "Час закінчення",
        "empty_schedule" to "Немає уроків у цей день",
        "empty_homework" to "Немає домашніх завдань на цей день",
        "add_lesson_btn" to "Додати",
        "cancel" to "Скасувати",
        "assigned_today" to "Задали на сьогодні",
        "dues_tomorrow" to "Домашка на завтра",
        "theme" to "Тема",
        "theme_dark" to "Чорна (Dark AI mode)",
        "theme_light" to "Біла (Light AI mode)",
        "language" to "Мова",
        "edit_avatar" to "Обрати аватар",
        "placeholder_assign" to "Натисніть, щоб записати ДЗ...",
        "profile_block" to "Дані профілю",
        "interface_block" to "Інтерфейс",
        "choose_day" to "Оберіть день",
        "choose_num" to "Номер уроку (1-8)",
        "invalid_fields" to "Будь ласка, заповніть усі поля правильно",
        "saved" to "Збережено!",
        "lesson_label" to "Урок %d",
        "empty_subject" to "Вільний слот",
        "hint_click_neon" to "Натисніть на урок для переливного неонового підсвічування!"
    )

    fun get(key: String, lang: String): String {
        val dictionary = if (lang == "UA") uaStrings else ruStrings
        return dictionary[key] ?: key
    }

    fun getFormatted(key: String, lang: String, vararg args: Any): String {
        val raw = get(key, lang)
        return try {
            String.format(raw, *args)
        } catch (e: Exception) {
            raw
        }
    }
}
