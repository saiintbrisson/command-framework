package me.saiintbrisson.minecraft.command.message;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author SaiintBrisson
 */

@Getter
public class MessageHolder {

    private final EnumMap<MessageType, String> messageMap = new EnumMap<>(MessageType.class);

    public MessageHolder() {
        for (MessageType value : MessageType.values()) {
            messageMap.put(value, value.getDefMessage());
        }
    }

    public String getMessage(MessageType type) {
        return messageMap.get(type);
    }

    public String getReplacing(MessageType type, String message) {
        return getMessage(type).replace(type.getPlaceHolder(), message);
    }

    public void setMessage(MessageType type, String message) {
        messageMap.put(type, message);
    }

    public void loadFromResources(String baseName, Locale locale) {
        final ResourceBundle bundle = PropertyResourceBundle.getBundle(baseName, locale);
        loadFromResources(bundle);
    }

    public void loadFromResources(String baseName) {
        final ResourceBundle bundle = PropertyResourceBundle.getBundle(baseName);
        loadFromResources(bundle);
    }

    public void loadFromResources(ResourceBundle bundle) {
        for (String s : bundle.keySet()) {
            try {
                final MessageType type = MessageType.valueOf(s);
                setMessage(type, bundle.getString(s).replace("&", "§"));
            } catch (IllegalArgumentException ignore) {}
        }
    }

}
