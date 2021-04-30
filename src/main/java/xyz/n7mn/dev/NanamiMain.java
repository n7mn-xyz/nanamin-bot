package xyz.n7mn.dev;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NanamiMain {

    private static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public static void main(String[] args) {

        try {
            File file = new File("./setting.json");
            if (!file.exists()){
                if (file.createNewFile()){
                    String json = gson.toJson(new BotSetting());
                    PrintWriter writer = new PrintWriter("./setting.json");
                    writer.print(json);
                    writer.close();
                }
                System.out.println("setting.jsonで設定をしてください。\n必須項目：DiscordToken");
                return;
            }

            StringBuilder jsonText = new StringBuilder();
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                String text;
                while ((text = reader.readLine()) != null) {
                    jsonText.append(text);
                }

            } catch (IOException e) {
                try {
                    if (!new File("./log/").exists()){
                        new File("./log/").mkdir();
                    }

                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    String date = format.format(new Date());

                    PrintWriter writer = new PrintWriter("./log/Error_"+date+".txt");
                    e.printStackTrace(writer);
                    writer.close();
                    System.out.println("エラーが発生しました。 : ./log/"+date+".txt");

                } catch (Exception e1){
                    e1.printStackTrace();
                    return;
                }
            }

            BotSetting json = gson.fromJson(jsonText.toString(), BotSetting.class);
            String token = json.getDiscordToken();
            JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_EMOJIS)
                    .addEventListeners(new BotListener())
                    .enableCache(CacheFlag.VOICE_STATE)
                    .enableCache(CacheFlag.EMOTE)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setActivity(Activity.playing("ななみちゃんbot Ver 2.0-dev"))
                    .build();

        } catch (Exception e) {

            try {
                if (!new File("./log/").exists()){
                    new File("./log/").mkdir();
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String date = format.format(new Date());

                PrintWriter writer = new PrintWriter("./log/Error_"+date+".txt");
                e.printStackTrace(writer);
                writer.close();
                System.out.println("エラーが発生しました。 : ./log/"+date+".txt");

            } catch (Exception e1){
                e1.printStackTrace();
            }
        }

    }

}
