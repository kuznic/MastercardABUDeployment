package postilion.realtime.mastercardabu.implementation;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ABUDeployer {

    @Autowired
    ABUServiceImpl ABUServiceImpl;

    @Autowired
    DeployChoiceProcessor deployChoiceProcessor;

    public void runAbu() throws Exception {
        var  deployChoice = ABUServiceImpl.deployChoice();
        var binList = ABUServiceImpl.setAbuBinList();

        deployChoiceProcessor.processDeployChoice(deployChoice,binList);




    }

}
