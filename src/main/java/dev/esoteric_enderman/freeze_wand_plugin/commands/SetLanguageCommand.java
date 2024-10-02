package dev.esoteric_enderman.freeze_wand_plugin.commands;

import dev.esoteric_enderman.freeze_wand_plugin.FreezeWandPlugin;
import dev.esoteric_enderman.freeze_wand_plugin.language.LanguageManager;
import dev.esoteric_enderman.freeze_wand_plugin.language.Message;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.Set;

public class SetLanguageCommand extends CommandAPICommand {

    public SetLanguageCommand(FreezeWandPlugin plugin) {
        super("set-language");

        LanguageManager languageManager = plugin.getLanguageManager();
        Set<String> languages = languageManager.getLanguages();

        String languageArgumentNodeName = "language";

        Argument<String> languageArgument = new CustomArgument<>(
                new GreedyStringArgument(languageArgumentNodeName),
                info -> {
                    String selectedLanguage = info.currentInput();

                    Bukkit.getLogger().info(selectedLanguage);

                    if (!languages.contains(selectedLanguage)) {
                        Component errorMessage = languageManager.getMessage(Message.UNKNOWN_LANGUAGE, info.sender(), true,
                                selectedLanguage);
                        throw CustomArgumentException.fromAdventureComponent(errorMessage);
                    }

                    return selectedLanguage;
                }).includeSuggestions(ArgumentSuggestions.strings(languageManager.getLanguages().toArray(String[]::new)));

        withArguments(languageArgument);

        executesPlayer((player, arguments) -> {
            String selectedLanguage = (String) arguments.get(languageArgumentNodeName);
            languageManager.setLanguage(player, selectedLanguage);

            player.sendMessage(languageManager.getMessage(Message.SET_LANGUAGE_SUCCESSFULLY, player, selectedLanguage));
        });

        register(plugin);
    }
}