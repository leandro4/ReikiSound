package holauser.lea.holauser.language;

/**
 * Created by leandro on 28/2/18.
 */

public class RussianLanguage extends LanguageStrategy {

    public RussianLanguage() {
        populateValues();
    }

    @Override
    protected void populateValues() {
        values.put("donate_title", "Благодарю Вас за использование Reiki Sound!");
        values.put("donate_subtitle", "Reiki Sound бесплатна и не содержит рекламы!");
        values.put("donate_body", "Будет очень полезна для нас, если Вы сможете помочь нашей команде в обслуживании и улучшении обслуживания всех членов сообщества Рэйки.");
        values.put("donate_button", "Пожертвовать");
        values.put("donate_thanks", "Большое спасибо Вам за Вашу помощь!");
        values.put("donate_button_not", "Нет, спасибо");
        values.put("donate_title_choose", "Спасибо за пожертвование на Reiki Sound");
        values.put("donate_subtitle_choose", "Какую сумму Вы желаете пожертвовать?");
        values.put("selected_audio", "Выбрать Аудио");
        values.put("selected_audio_body", "Извините, но ваш аудио файл не воспроизводится. Пожалуйста, выберите другой.");
        values.put("select_audio_title", "Выбрать музыку");
        values.put("select_audio_body", "Выбрать музыку с устройства или использовать встроенную музыку Reiki Sound?");
        values.put("select_audio_select", "Выбрать");
        values.put("select_audio_default", "По умолчанию");
        values.put("music_selected", "Музыка выбрана");
        values.put("service_subtitle", "Таймер работает...");
        values.put("remaining", "Осталось времени:");
        values.put("time_title", "Время (минуты):");
        values.put("enable_music", "Включить музыку");
        values.put("tv_volume", "Громкость: ");
    }
}