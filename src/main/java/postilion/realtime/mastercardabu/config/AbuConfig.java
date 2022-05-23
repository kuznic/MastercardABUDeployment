package postilion.realtime.mastercardabu.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "abu.deploy")
public class AbuConfig {
    private String abuSolutionZippedFilePath;
    private String icaBins;
    private String  deployFolder;
    private boolean dbEncrypted;
    private String url;
    private String userName;
    private String password;
}
