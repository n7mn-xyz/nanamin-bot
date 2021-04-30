package xyz.n7mn.dev;

class BotSetting {

    private String DiscordToken;
    private String MySQLServerIP;
    private int MySQLPort;
    private String MySQLDatabase;
    private String MySQLOption;
    private String MySQLUsername;
    private String MySQLPassword;

    public BotSetting(){
        this.DiscordToken = "";
        this.MySQLServerIP = "";
        this.MySQLPort = 3306;
        this.MySQLDatabase = "";
        this.MySQLOption = "?allowPublicKeyRetrieval=true&useSSL=false";
        this.MySQLUsername = "";
        this.MySQLPassword = "";
    }

    public String getDiscordToken() {
        return DiscordToken;
    }

    public String getMySQLServerIP() {
        return MySQLServerIP;
    }

    public int getMySQLPort() {
        return MySQLPort;
    }

    public String getMySQLDatabase() {
        return MySQLDatabase;
    }

    public String getMySQLOption() {
        return MySQLOption;
    }

    public String getMySQLUsername() {
        return MySQLUsername;
    }

    public String getMySQLPassword() {
        return MySQLPassword;
    }
}
