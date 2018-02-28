package holauser.lea.holauser.language;

/**
 * Created by leandro on 28/2/18.
 */

public class FrenchLanguage extends LanguageStrategy {
    
    public FrenchLanguage() {
        populateValues();
    }

    @Override
    protected void populateValues() {
        values.put("donate_title_choose", "Merci de faire un don à Reiki Sound");
        values.put("donate_subtitle_choose", "Combien voulez-vous donner?");
        values.put("donate_title", "Merci d\'utiliser Reiki Sound :)");
        values.put("donate_subtitle", "Reiki Sound est gratuit et sans publicité!");
        values.put("donate_body", "C\'est très utile pour notre équipe qui peut collaborer pour continuer à maintenir et améliorer le service pour tous les Reikistas.");
        values.put("donate_button", "Faire un don");
        values.put("donate_thanks", "Merci beaucoup pour votre aide!");
        values.put("donate_button_not", "Non merci");
        values.put("select_audio_title", "Sélectionnez la musique");
        values.put("select_audio_body", "Choisissez une chanson de votre appareil ou utilisez la musique par défaut de Reiki Sound?");
        values.put("select_audio_select", "Sélectionnez");
        values.put("select_audio_default", "Utiliser par défaut");
        values.put("selected_audio", "Audio sélectionné");
        values.put("selected_audio_body", "Nous sommes désolés mais l\'élément sélectionné ne peut pas être lu, essayez un autre son ou changez le format.");
        values.put("music_selected", "Musique sélectionnée");
        values.put("service_subtitle", "Chronomètre en cours ...");
        values.put("remaining", "Temps restant:");
        values.put("time_title", "Temps (minutes):");
        values.put("enable_music", "Activer la musique");
        values.put("tv_volume", "Volume: ");
    }
}