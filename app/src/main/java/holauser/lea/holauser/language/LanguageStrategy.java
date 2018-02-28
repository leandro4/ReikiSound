package holauser.lea.holauser.language;

import java.util.HashMap;

/**
 * Created by leandro on 28/2/18.
 */

public abstract class LanguageStrategy {

    protected HashMap<String, String> values = new HashMap<>();

    public String getString(String key) {
        return values.get(key);
    }

    protected abstract void populateValues();
}