package xyz.n7mn.dev;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.Listener.EarthQuakeListener;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BotListener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        try {
            new EarthQuakeListener(event.getJDA()).run(); // 地震情報


        } catch (Exception e){
            try {
                if (!new File("./log/").exists()){
                    new File("./log/").mkdir();
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

                PrintWriter writer = new PrintWriter("./log/Error_"+format.format(new Date())+".txt");
                e.printStackTrace(writer);
                writer.close();
            } catch (Exception e1){
                e1.printStackTrace();
            }
        }

    }
}
