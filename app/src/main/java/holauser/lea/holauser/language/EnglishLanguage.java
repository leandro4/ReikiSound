package holauser.lea.holauser.language;

/**
 * Created by leandro on 28/2/18.
 */

public class EnglishLanguage extends LanguageStrategy {

    public EnglishLanguage() {
        populateValues();
    }

    @Override
    protected void populateValues() {
        values.put("donate_title", "Thanks for use Reiki Sound!");
        values.put("donate_subtitle", "Reiki Sound is free and has no advertisements!");
        values.put("donate_body", "It is very usefull for us if you could help the team to mantenaince and improving the service for all reiki comunity.");
        values.put("donate_button", "Donate");
        values.put("donate_thanks", "Thank you very much for your help!");
        values.put("donate_button_not", "No, thanks");
        values.put("donate_title_choose", "Thanks for donate to Reiki Sound");
        values.put("donate_subtitle_choose", "How much money would you like to donate?");
        values.put("selected_audio", "Selected Audio");
        values.put("selected_audio_body", "Sorry but your audio file coul not be played. Please choose another one.");
        values.put("select_audio_title", "Select music");
        values.put("select_audio_body", "Choose a song from my device or use Reiki Sound default music?");
        values.put("select_audio_select", "Select");
        values.put("select_audio_default", "Use default");
        values.put("music_selected", "Music selected");
        values.put("service_subtitle", "Timer running...");
        values.put("remaining", "Time left:");
        values.put("time_title", "Time (minutes):");
        values.put("enable_music", "Enable music");
        values.put("tv_volume", "Volume: ");
    }
}