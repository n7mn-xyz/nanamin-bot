package xyz.n7mn.dev.Command.vote;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import xyz.n7mn.dev.EventListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VoteSystem {

    public void voteStart(Message message){
        new Thread(()->{
            VoteSystemSub.addMessageCache(message.getId());
            try {
                Connection con = EventListener.getDatabase().getConnect();

                PreparedStatement statement = con.prepareStatement("INSERT INTO `VoteList` (`UUID`, `MessageID`, `Date`) VALUES (?, ?, NOW());");
                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, message.getId());
                statement.execute();
                statement.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public void voteStop(Message message){
        new Thread(()->{

            VoteSystemSub.delMessageCache(message.getId());

            try {
                Connection con = EventListener.getDatabase().getConnect();

                PreparedStatement statement = con.prepareStatement("DELETE FROM `VoteList` WHERE MessageID = ?;");
                statement.setString(1, message.getId());
                statement.execute();
                statement.close();
                PreparedStatement statement2 = con.prepareStatement("DELETE FROM `VoteEmojiList` WHERE MessageID = ?;");
                statement2.setString(1, message.getId());
                statement2.execute();
                statement2.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public static boolean isVote(Message message){

        List<String> list = VoteSystemSub.getMessageCacheList();
        for (String id : list){
            // System.out.println("キャッシュチェック");
            if (id.equals(message.getId())){
                return true;
            }
        }

        //System.out.println("キャッシュなし");

        try {
            Connection con = EventListener.getDatabase().getConnect();

            PreparedStatement statement = con.prepareStatement("SELECT * FROM VoteList WHERE MessageID = ?;");
            statement.setString(1, message.getId());
            ResultSet set = statement.executeQuery();
            if (set.next()){
                set.close();
                statement.close();
                VoteSystemSub.addMessageCache(message.getId());
                return true;
            }
            set.close();
            statement.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static void addReaction(Message message, Member member, String emoji){
        new Thread(()->{
            Connection con = EventListener.getDatabase().getConnect();

            try {
                PreparedStatement statement = con.prepareStatement("INSERT INTO `VoteEmojiList`(`MessageID`, `UserID`, `Emoji`) VALUES (?, ?, ?);");
                statement.setString(1, message.getId());
                statement.setString(2, member.getId());
                statement.setString(3, emoji);
                statement.execute();
                statement.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public static List<VoteData> getVoteDataList(Message message){
        List<VoteData> list = new ArrayList<>();
        try {
            Connection con = EventListener.getDatabase().getConnect();

            PreparedStatement statement1 = con.prepareStatement("SELECT * FROM VoteEmojiList WHERE MessageID = ?;");
            statement1.setString(1, message.getId());
            ResultSet set1 = statement1.executeQuery();
            while (set1.next()){
                list.add(new VoteData(set1.getString("MessageID"), set1.getString("UserID"), set1.getString("Emoji")));
            }
            set1.close();
            statement1.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
}
