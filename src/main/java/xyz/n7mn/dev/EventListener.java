package xyz.n7mn.dev;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.Command.CommandSystem;
import xyz.n7mn.dev.Command.Help;
import xyz.n7mn.dev.Command.money.Money;
import xyz.n7mn.dev.Command.money.MoneySystem;
import xyz.n7mn.dev.Command.music.GuildMusicManager;
import xyz.n7mn.dev.Command.music.PlayerManager;
import xyz.n7mn.dev.Command.vote.VoteData;
import xyz.n7mn.dev.Command.vote.VoteSystem;
import xyz.n7mn.dev.Listener.ServerLogListener;
import xyz.n7mn.dev.adminCommand.CheckServer;
import xyz.n7mn.dev.api.Earthquake;
import xyz.n7mn.dev.i.SystemRun;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventListener extends ListenerAdapter {
    private static Database database = null;
    private Earthquake earthquake = new Earthquake();

    @Override
    public void onGenericGuildVoice(@NotNull GenericGuildVoiceEvent event) {
        // super.onGenericGuildVoice(event);
        AudioManager audioManager = event.getGuild().getAudioManager();
        if (audioManager.isConnected()){
            VoiceChannel channel = audioManager.getConnectedChannel();
            if (channel.getMembers().size() <= 1){
                PlayerManager Playermanager = PlayerManager.getINSTANCE();
                GuildMusicManager guildMusicManager = Playermanager.getGuildMusicManager(event.getGuild());
                guildMusicManager.player.stopTrack();
                audioManager.closeAudioConnection();
            }
        }
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        new ServerLogListener().join(event);
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        new ServerLogListener().leave(event);
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot()){
            return;
        }

        Message message = event.retrieveMessage().complete();

        if (VoteSystem.isVote(message)){

            String[] string = NanamiFunction.getRegionalList();
            boolean isFlag = false;
            for (String s : string){
                if (event.getReactionEmote().getEmoji().equals(s)){
                    isFlag = true;
                    break;
                }
            }

            if (isFlag){
                message.removeReaction(event.getReaction().getReactionEmote().getEmoji(), event.getUser()).queue();
                PrivateChannel privateChannel = event.getUser().openPrivateChannel().complete();
                EmbedBuilder builder = new EmbedBuilder();

                List<VoteData> list = VoteSystem.getVoteDataList(message);
                for (VoteData data : list){
                    if (data.getUserId().equals(event.getUser().getId()) && data.getEmoji().equals(event.getReaction().getReactionEmote().getEmoji())){
                        builder.setTitle("えらー",message.getJumpUrl());
                        builder.setDescription("投票済みの選択肢ですっ！");
                        builder.setColor(Color.RED);
                        privateChannel.sendMessage(builder.build()).queue();
                        return;
                    }
                }
                builder.setTitle("投票完了っ",message.getJumpUrl());
                builder.setDescription(event.getReactionEmote().getEmoji() + "に投票しました！");
                builder.setColor(Color.GREEN);
                privateChannel.sendMessage(builder.build()).queue();

                VoteSystem.addReaction(message, event.getMember(), event.getReaction().getReactionEmote().getEmoji());
            }

            return;
        }

        if (event.getReaction().getReactionEmote().isEmoji()){
            if (event.getReactionEmote().getEmoji().equals("\u274C")){
                Message message1 = event.retrieveMessage().complete();

                if (message1.getEmbeds().size() > 0 && message1.getEmbeds().get(0) != null && message1.getEmbeds().get(0).getTitle() != null && message1.getEmbeds().get(0).getTitle().endsWith("さんの情報")){
                    if (message1.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())){
                        message1.delete().queue();
                    }
                }
            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isWebhookMessage()){
            return;
        }

        if (event.getAuthor().isBot()){
            return;
        }

        if (event.getMessage().getContentRaw().startsWith("<@!781323086624456735>")){
            if (event.isFromType(ChannelType.PRIVATE)){
                event.getPrivateChannel().sendMessage("？").queue();
            } else {
                event.getMessage().reply("？").queue();
            }

            return;
        }

        if (event.isFromType(ChannelType.PRIVATE)){

            SystemRun system = null;
            User nana = event.getJDA().getUserById("529463370089234466");
            EmbedBuilder builder = new EmbedBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            builder.setTitle("実行ログ").setFooter(sdf.format(new Date())).setColor(Color.PINK);
            builder.addField("実行者", event.getAuthor().getAsTag()+"\n("+event.getAuthor().getId()+")", false);
            builder.addField("内容", event.getMessage().getContentRaw(), false);
            builder.addField("メッセージリンクURL", event.getMessage().getJumpUrl(), false);

            nana.openPrivateChannel().complete().sendMessage(builder.build()).queue();

            Message message = event.getMessage();

            if (message.getContentRaw().startsWith("n.help")){
                Help.run(event.getPrivateChannel(), message.getContentRaw());
                return;
            }

            if (message.getContentRaw().startsWith("ぱんつ何色") || message.getContentRaw().startsWith("パンツ何色")){
                message.getPrivateChannel().sendMessage("へんたいっ！").queue();
                return;
            }

            if (message.getContentRaw().startsWith("にゃーん")){
                message.getPrivateChannel().sendMessage("にゃーん").queue(message1 -> {
                    message1.addReaction("\uD83D\uDC31").queue();
                });
                return;
            }

            if (message.getContentRaw().startsWith("パンツ食べますか") || message.getContentRaw().startsWith("ぱんつ食べますか")){
                message.getPrivateChannel().sendMessage(":thinking:").queue();
                return;
            }


            if (message.getContentRaw().toLowerCase().startsWith("n.checkserver") && event.getAuthor().getId().equals("529463370089234466")){
                system = new CheckServer(message);
            }

            if (system != null){
                system.run();
                return;
            }

            message.getPrivateChannel().sendMessage(
                    "ふぬ？なにもおきないですよ？\n" +
                            "\n" +
                            "ヘルプを見るには「n.help」を入力してください\n" +
                            "このbotを入れるには：https://discord.com/api/oauth2/authorize?client_id=781323086624456735&permissions=8&scope=bot\n" +
                            "botについてバグ報告、テスト、要望が出したい！： https://discord.gg/FnjCMzP7d4").queue((message1 -> {
                message1.suppressEmbeds(true).queue();
            }));
            return;
        }

        new Thread(()->{
            Money data = MoneySystem.getData(event.getAuthor().getId());
            if (data == null){
                MoneySystem.createData(event.getAuthor().getId());
                data = MoneySystem.getDefaultData(event.getAuthor().getId());
            }
            long tempInt = data.getMoney() + 1L;
            if (tempInt > Integer.MAX_VALUE){
                MoneySystem.updateData(new Money(data.getUserID(), data.getMoney()));
            } else {
                MoneySystem.updateData(new Money(data.getUserID(), data.getMoney() + 1));
            }
        }).start();

        CommandSystem.run(event.getTextChannel(), event.getMessage());

    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        database = new Database();
        new EarthquakeListener(event.getJDA(), earthquake);
        new VoteCheck(event.getJDA(), database);
    }

    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {
        //super.onGuildMessageDelete(event);
    }

    public static Database getDatabase(){
        return database;
    }
}
