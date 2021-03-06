package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.util.List;

public class Msg extends Chat {

    public Msg(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {
        // https://discord.com/channels/517669763556704258/543092075336433681/788207229018308678
        String[] split = getMessageText().split("/",-1);

        if (split.length != 7){
            return;
        }

        if (!split[4].equals("@me")){
            Guild guild = getGuild().getJDA().getGuildById(split[4]);
            if (guild == null){
                getMessage().reply("見つからないよぉ").queue();
                return;
            }

            TextChannel textChannelById = guild.getTextChannelById(split[5]);
            if (textChannelById == null){
                getMessage().reply("見つからないよぉ").queue();
                return;
            }

            try {
                textChannelById.retrieveMessageById(split[6]).queue(message1 ->{
                    try {
                        String contentRaw = message1.getContentRaw();
                        // System.out.println(contentRaw);
                        boolean edited = message1.isEdited();
                        User author = message1.getAuthor();

                        EmbedBuilder builder = new EmbedBuilder();
                        builder.addField("内容", contentRaw, true);
                        MessageEmbed build;

                        List<Message.Attachment> attachments = message1.getAttachments();
                        for (Message.Attachment attachment : attachments){
                            builder.addField("添付ファイル", attachment.getUrl(), false);
                        }

                        if (contentRaw.length() > 0){
                            build = builder.build();
                        } else {
                            build = message1.getEmbeds().get(0);
                        }

                        getMessage().reply(
                                "---- メッセージの情報 ----\n" +
                                        "投稿したチャンネル : " + message1.getChannel().getName() + "\n" +
                                        "文字数 : " + contentRaw.length() + "\n" +
                                        "編集済みかどうか : " + edited + "\n" +
                                        "投稿者 : " + author.getAsTag() + "\n" +
                                        "添付ファイルの数 : " + attachments.size() + "\n"
                        ).embed(build).queue();
                    } catch (Exception e){
                        getMessage().reply("メッセージが存在しませんっ！").queue();
                    }

                });
                return;
            } catch (Exception e){
                getMessage().reply("メッセージが存在しませんっ！").queue();
                return;
            }
        }

        JDA jda = getGuild().getJDA();
        PrivateChannel privateChannel = jda.getPrivateChannelById(split[5]);
        if (privateChannel == null){
            getMessage().reply("見つからないよぉ").queue();
            return;
        }

        privateChannel.retrieveMessageById(split[6]).queue(message -> {
            String contentRaw = message.getContentRaw();
            // System.out.println(contentRaw);
            boolean edited = message.isEdited();
            User author = message.getAuthor();

            EmbedBuilder builder = new EmbedBuilder();
            builder.addField("内容", contentRaw, true);

            List<Message.Attachment> attachments = message.getAttachments();
            for (Message.Attachment attachment : attachments){
                builder.addField("添付ファイル", attachment.getUrl(), false);
            }

            MessageEmbed build = builder.build();

            getMessage().reply(
                    "---- メッセージの情報 ----\n" +
                            "投稿したチャンネル : (DM)\n" +
                            "文字数 : " + contentRaw.length() + "\n" +
                            "編集済みかどうか : " + edited + "\n" +
                            "投稿者 : " + author.getAsTag() + "\n" +
                            "添付ファイルの数 : " + attachments.size() + "\n"
            ).embed(build).queue();
        });

    }
}
