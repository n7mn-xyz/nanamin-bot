package xyz.n7mn.dev;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import xyz.n7mn.dev.Command.money.MoneySystem;

public class NanamiMain {

    public static void main(String[] args) {

        try {

            String json = NanamiFunction.fileRead("./token.json");

            String token = new Gson().fromJson(json, String.class);

            JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_EMOJIS)
                    .addEventListeners(new EventListener())
                    .enableCache(CacheFlag.VOICE_STATE)
                    .enableCache(CacheFlag.EMOTE)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setActivity(Activity.playing("ななみちゃんbot Ver " + NanamiFunction.getVersion()))
                    .build();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
