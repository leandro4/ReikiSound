package holauser.lea.holauser.language;

/**
 * Created by leandro on 28/2/18.
 */

public class PortugeseLanguage extends LanguageStrategy {

    public PortugeseLanguage() {
        populateValues();
    }

    @Override
    protected void populateValues() {
    values.put("donate_title_choose", "Obrigado por doar para Reiki Sound");
    values.put("donate_subtitle_choose", "Quanto você quer doar?");
    values.put("donate_title", "Obrigado por usar o Reiki Sound :)");
    values.put("donate_subtitle", "O Reiki Sound é gratuito e sem propaganda!");
    values.put("donate_body", "É muito útil para o nosso time que possa colaborar para continuar a manter e melhorar o serviço para todos os Reikistas.");
    values.put("donate_button", "Doar");
    values.put("donate_thanks", "Muito obrigado pela vossa ajuda!");
    values.put("donate_button_not", "Não, obrigado");
    values.put("select_audio_title", "Selecione música");
    values.put("select_audio_body", "Escolha uma música do seu dispositivo ou use a música padrão do Reiki Sound?");
    values.put("select_audio_select", "Selecione");
    values.put("select_audio_default", "Use padrão");
    values.put("selected_audio", "Áudio selecionado");
    values.put("selected_audio_body", "Lamentamos, mas o item selecionado não pode ser jogado, tente outro áudio ou altere o formato.");
    values.put("music_selected", "Música selecionada");
    values.put("service_subtitle", "Cronômetro executando ...");
    values.put("remaining", "Tempo restante:");
    values.put("time_title", "Tempo (minutos):");
    values.put("enable_music", "Ativar música");
        values.put("tv_volume", "Volume: ");
    }
}