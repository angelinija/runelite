package net.runelite.client.plugins.roomrememberer;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.chat.ChatColor;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.config.ChatColorConfig;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.*;
import java.util.Locale;

@Slf4j
@PluginDescriptor(
        name = "RoomRememberer"
)

public class RoomRemembererPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
//    private RoomRemembererOverlay overlay;
    private RoomRemembererConfig config;

    private String lastNpcDialogueText = null;

    @Override
    protected void startUp() throws Exception
    {
        log.info("RoomRemembererOverlay started!");
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.info("RoomRemembererOverlay stopped!");
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged)
    {
        if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
        {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "RoomRemembererOverlay says " + config.greeting(), null);
        }
    }

    @Provides
    RoomRemembererConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(RoomRemembererConfig.class);
    }


    @Subscribe
    public void onGameTick(GameTick tick) {
        Widget npcDialogueTextWidget = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);

        if (npcDialogueTextWidget != null && npcDialogueTextWidget.getText() != lastNpcDialogueText) {
            String npcText = npcDialogueTextWidget.getText();
            lastNpcDialogueText = npcText;

            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Try " + String.valueOf(npcText.toLowerCase(Locale.ROOT).contains("Try")), null);
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "the " +String.valueOf(npcText.toLowerCase(Locale.ROOT).contains("the")), null);
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Try the " + String.valueOf(npcText.toLowerCase(Locale.ROOT).contains("Try the")), null);
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "try the " + String.valueOf(npcText.toLowerCase(Locale.ROOT).contains("try the")), null);
            // Try the top floor, north-western bay.
            if (npcText.toLowerCase(Locale.ROOT).contains("try the")) {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Contains try the", null);
//                int floorIndex = npcText.indexOf("floor,");
//                int bayIndex = npcText.indexOf("bay.");
//                String[] strArr = npcText.split(" ");
//                String floor = strArr[floorIndex - 1];
//                String bay = strArr[bayIndex - 1];
//                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Floor: " + floor, null);
//                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Bay: " + bay, null);

                for (ChatMessageType cmt : ChatMessageType.values()) {
                    String type = cmt.toString();
                    client.addChatMessage(cmt, "", type + ": " + npcText, null);
                }

                String response = new ChatMessageBuilder()
                        .append(ChatColorType.HIGHLIGHT)
                        .append(npcText)
                        .build();
                ChatMessage chatMessage = new ChatMessage();

                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", response, null);


//                for (ChatColorType cct : ChatColorType.values()) {
//
////                    ChatColor cc = new ChatColor(cct, Color.red, false);
//
//
//
//
//                    String type = cct.toString();
//                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", type + ": " + npcText, null);
//                }
            }
        }
    }
}
