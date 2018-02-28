package holauser.lea.holauser.language;

/**
 * Created by leandro on 28/2/18.
 */

public class ItalianLanguage extends LanguageStrategy {

    public ItalianLanguage() {
        populateValues();
    }

    @Override
    protected void populateValues() {
        values.put("donate_title_choose", "Grazie per aver donato a Reiki Sound");
        values.put("donate_subtitle_choose", "Quanto vuoi donare?");
        values.put("donate_title", "Grazie per aver usato il suono del Reiki :)");
        values.put("donate_subtitle", "Reiki Sound è gratuito e privo di pubblicità!");
        values.put("donate_body", "È molto utile per il nostro team che può collaborare per continuare a mantenere e migliorare il servizio per tutti i Reikistan.");
        values.put("donate_button", "Donare");
        values.put("donate_thanks", "Grazie mille per il tuo aiuto!");
        values.put("donate_button_not", "No, grazie");
        values.put("select_audio_title", "Seleziona la musica");
        values.put("select_audio_body", "Scegli una canzone dal tuo dispositivo o usa la musica predefinita di Reiki Sound?");
        values.put("select_audio_select", "Selezionare");
        values.put("select_audio_default", "Usa predefinito");
        values.put("selected_audio", "Audio selezionato");
        values.put("selected_audio_body", "Siamo spiacenti ma l\'elemento selezionato non può essere riprodotto, provare un altro audio o modificare il formato.");
        values.put("music_selected", "Musica selezionata");
        values.put("service_subtitle", "Cronometro in esecuzione ...");
        values.put("remaining", "Tempo rimanente:");
        values.put("time_title", "Tempo (minuti):");
        values.put("enable_music", "Attiva la musica");
        values.put("tv_volume", "Volume: ");
    }
}