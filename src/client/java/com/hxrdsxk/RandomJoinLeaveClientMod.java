package com.hxrdsxk;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RandomJoinLeaveClientMod implements ClientModInitializer {

    private static final String[] NAMES = {
            "player123", "NoobMaster", "KillerXD", "maz0lb", "treep", "Fivfiv1337", "SosiskaKiller", "kapycta123", "artik221"
    };

    private static final String[] MESSAGES = {
            "подключился к игре",
            "вышел из игры",
            "зашел на сервер",
            "вышел с сервера"
    };

    private final Random random = new Random();
    private final Timer timer = new Timer();

    @Override
    public void onInitializeClient() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null && client.world != null) {
                    String name = NAMES[random.nextInt(NAMES.length)];
                    String action = MESSAGES[random.nextInt(MESSAGES.length)];
                    String message = "§7[" + name + "] " + action;
                    client.execute(() -> {
                        client.inGameHud.getChatHud().addMessage(Text.literal(message));
                    });
                }
            }
        }, 0, 1 * 60 * 1000); // каждую 1 минуту
    }
}
