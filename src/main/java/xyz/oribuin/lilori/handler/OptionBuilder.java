package xyz.oribuin.lilori.handler;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OptionBuilder {

    private final OptionType type; // The type of option
    private final String name; // The name of the option
    private final String description; // The description of the option
    private boolean required; // Whether the option is required or not
    private boolean autoComplete; // Whether the option should autocomplete or not
    private Map<String, Command.Choice> choices; // The choices for the option
    private List<ChannelType> channelTypes; // The channel types for the option
    private int maxLength; // The max length of the option
    private int minLength; // The min value of the option
    private int maxValue; // The max value of the option
    private int minValue; // The min value of the option

    public OptionBuilder(OptionType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.required = false;
        this.autoComplete = false;
        this.choices = new HashMap<>();
        this.channelTypes = new ArrayList<>();
        this.maxLength = 0;
        this.minLength = 0;
        this.maxValue = 0;
        this.minValue = 0;
    }

    /**
     * Build the option data
     *
     * @return The  OptionData
     */
    public OptionData build() {
        OptionData optionData = new OptionData(this.type, this.name, this.description);
        optionData.setRequired(this.required);
        optionData.setAutoComplete(this.autoComplete);

        if (this.type.canSupportChoices()) optionData.addChoices(this.choices.values());

        if (this.type == OptionType.CHANNEL) optionData.setChannelTypes(this.channelTypes);

        if (this.type == OptionType.INTEGER) {
            optionData.setMaxValue(this.maxValue);
            optionData.setMinValue(this.minValue);
        }

        if (this.type == OptionType.STRING) {
            optionData.setMaxLength(this.maxLength);
            optionData.setMinLength(this.minLength);
        }

        return optionData;
    }

    public void addChoice(String name, String value) {
        this.choices.put(name, new Command.Choice(name, value));
    }

    public OptionType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isAutoComplete() {
        return autoComplete;
    }

    public void setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
    }

    public Map<String, Command.Choice> getChoices() {
        return choices;
    }

    public void setChoices(Map<String, Command.Choice> choices) {
        this.choices = choices;
    }

    public void setChannelTypes(List<ChannelType> channelTypes) {
        this.channelTypes = channelTypes;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

}
