package postilion.realtime.mastercardabu.implementation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class DeployChoiceProcessor {

    @Autowired
    ABUServiceImpl ABUServiceImpl;

    public void processDeployChoice(int deployChoice,
                                    List<String> abuBinList) throws Exception {
        switch (deployChoice) {
            case 1: {
                ABUServiceImpl.createAbuFolderAndCopyConfigFiles();
                ABUServiceImpl.checkForPcCards(abuBinList);
                ABUServiceImpl.checkForPcCardAccounts(abuBinList);
                ABUServiceImpl.createTriggersOnIcaIssuerTables(abuBinList);
                ABUServiceImpl.copyInitialRecordsToAbuTable(abuBinList);
                ABUServiceImpl.createProcedureToAutoPopulateAbuTable(abuBinList);
                break;
            }
            case 2: {
                ABUServiceImpl.copyNewRecordsToAbuTable(abuBinList);
                break;
            }
            case 3:
            {
                ABUServiceImpl.updateRecordsForReasonCodeC();
                break;

            }
            case 4:
            {
                ABUServiceImpl.insertNewRecordForReasonCodeR();
                break;
            }
        }


    }
}
