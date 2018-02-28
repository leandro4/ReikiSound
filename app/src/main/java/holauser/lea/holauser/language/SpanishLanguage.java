package holauser.lea.holauser.language;

/**
 * Created by leandro on 28/2/18.
 */

public class SpanishLanguage extends LanguageStrategy {
    
    public SpanishLanguage() {
        populateValues();
    }

    @Override
    protected void populateValues() {
        values.put("donate_title_choose", "Gracias por donar a Reiki Sound");
        values.put("donate_subtitle_choose", "Cuánto desea donar?");
        values.put("donate_title", "Gracias por usar Reiki Sound :)");
        values.put("donate_subtitle", "Reiki Sound es gratis y libre de publicidades!");
        values.put("donate_body", "Es muy útil para nuestro equipo que pueda colaborar para seguir manteniendo y mejorando el servicio para todos los reikistas.");
        values.put("donate_button", "Donar");
        values.put("donate_thanks", "Muchas gracias por su ayuda!");
        values.put("donate_button_not", "No, gracias");
        values.put("select_audio_title", "Seleccionar música");
        values.put("select_audio_body", "Elegir una canción de su dispositivo o usar la música por defecto de Reiki Sound?");
        values.put("select_audio_select", "Seleccionar");
        values.put("select_audio_default", "Usar default");
        values.put("selected_audio", "Audio seleccionado");
        values.put("selected_audio_body", "Lo sentimos pero no se puede reproducir el elemento seleccionado, intente con otro audio o bien cambie el formato.");
        values.put("music_selected", "Música seleccionada");
        values.put("service_subtitle", "Cronómetro corriendo...");
        values.put("time_title", "Tiempo (minutos):");
        values.put("enable_music", "Activar música");
        values.put("remaining", "Tiempo restante:");
        values.put("tv_volume", "Volumen: ");
    }
}