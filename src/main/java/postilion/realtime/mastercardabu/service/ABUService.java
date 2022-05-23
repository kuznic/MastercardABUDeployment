package postilion.realtime.mastercardabu.service;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ABUService {

    void checkForPcCards(List<String> bins);
    void checkForPcCardAccounts(List<String> bins) throws Exception;
    void copyInitialRecordsToAbuTable( List<String> abuBins) throws Exception;
    void copyNewRecordsToAbuTable(List<String> abuBins) throws Exception;
    void updateRecordsForReasonCodeC() throws Exception;
    void createTriggersOnIcaIssuerTables(List<String> bins) throws Exception;
    void insertNewRecordForReasonCodeR() throws Exception;
    void createProcedureToAutoPopulateAbuTable(List<String> abuBins) throws Exception;
}
