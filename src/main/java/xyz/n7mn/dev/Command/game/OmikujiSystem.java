package xyz.n7mn.dev.Command.game;

import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class OmikujiSystem {

    public static List<Omikuji> getOmikujiData(Guild guild){

        List<Omikuji> omikuji = new ArrayList<>();

        if (guild.getId().equals("517669763556704258")){

            omikuji.add(new Omikuji("変態","変態は死刑ですっ！",-10));
            omikuji.add(new Omikuji("凶","ざんねーん！",0));
            omikuji.add(new Omikuji("吉","すごい微妙？",10));
            omikuji.add(new Omikuji("小吉","ちょっと微妙？",20));
            omikuji.add(new Omikuji("中吉","ふつうだね！",30));
            omikuji.add(new Omikuji("大吉","やったね！",40));
            omikuji.add(new Omikuji("遅刻","遅刻はダメですよ...",50));
            omikuji.add(new Omikuji("ななみちゃん","ボーナスですっ！",100));

            omikuji.add(new Omikuji("ゆるり","昼夜逆転には気をつけよう！",10));
            omikuji.add(new Omikuji("砂",":Sand:",20));
            omikuji.add(new Omikuji("パンマスター","/pan",25));
            omikuji.add(new Omikuji("虫特攻",":thinking:",30));
            omikuji.add(new Omikuji("金装備","はてな",35));
            omikuji.add(new Omikuji("ふーぷれす",":thinking:",75));


        } else {

            omikuji.add(new Omikuji("変態","変態は死刑ですっ！",-5));
            omikuji.add(new Omikuji("凶","ざんねーん！",0));
            omikuji.add(new Omikuji("吉","すごい微妙？",10));
            omikuji.add(new Omikuji("小吉","ちょっと微妙？",20));
            omikuji.add(new Omikuji("中吉","ふつうだね！",30));
            omikuji.add(new Omikuji("大吉","やったね！",40));
            omikuji.add(new Omikuji("遅刻","遅刻はダメですよ...",40));
            omikuji.add(new Omikuji("ななみちゃん","ボーナスですっ！",100));

        }

        return omikuji;
    }

}
