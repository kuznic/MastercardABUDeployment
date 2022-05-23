package postilion.realtime.mastercardabu.implementation;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postilion.realtime.mastercardabu.config.AbuConfig;
import postilion.realtime.mastercardabu.model.AbuTable;
import postilion.realtime.mastercardabu.repository.AbuTableRepo;
import postilion.realtime.mastercardabu.repository.PcCardAccountsViewRepo;
import postilion.realtime.mastercardabu.repository.PcCardsViewRepo;
import postilion.realtime.mastercardabu.service.ABUService;

import javax.persistence.EntityManager;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ABUServiceImpl implements ABUService {


    private final FileUnZipper fileUnZipper;
    private final PcCardsViewRepo pcCardsViewRepo;
    private final PcCardAccountsViewRepo pcCardAccountsViewRepo;
    private final AbuTableRepo abuTableRepo;
    private final EntityManager entityManager;
    private final AbuConfig abuConfig;
    private final Gson gson;




    @Autowired
    public ABUServiceImpl(FileUnZipper fileUnZipper,
                          PcCardsViewRepo pcCardsViewRepo,
                          PcCardAccountsViewRepo pcCardAccountsViewRepo,
                          AbuTableRepo abuTableRepo,
                          EntityManager entityManager,
                          AbuConfig abuConfig, Gson gson) {
        this.fileUnZipper = fileUnZipper;
        this.pcCardsViewRepo = pcCardsViewRepo;
        this.pcCardAccountsViewRepo = pcCardAccountsViewRepo;
        this.abuTableRepo = abuTableRepo;
        this.entityManager = entityManager;
        this.abuConfig = abuConfig;
        this.gson = gson;
    }





    private String setPostcardCardTableSuffix(){
        String postcardDataTableSuffix;

        if(abuConfig.isDbEncrypted()){
            postcardDataTableSuffix = "_B";
        }else{
            postcardDataTableSuffix = "_A";
        }

        return postcardDataTableSuffix;
    }



    public int deployChoice() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("===================================================");
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. DEPLOY MASTERCARD ABU:");
        System.out.println("2. POPULATE PC_CARDS_ABU TABLE WITH NEW RECORDS FOR REASON CODE N");
        System.out.println("3. MODIFY PC_CARDS_ABU RECORDS FOR REASON CODE C");
        System.out.println("4. ADD NEW RECORD FOR REASON CODE R");
        System.out.println("7. EXIT");
        System.out.println();
        System.out.println("Options 2,3 and 4 should be used only on the test environment");
        System.out.println();



        return scanner.nextInt();

    }




    public void createAbuFolderAndCopyConfigFiles() throws IOException {
        System.out.println();
        System.out.println();
        System.out.println("Unzipping zipped folder in " + abuConfig.getAbuSolutionZippedFilePath());
        fileUnZipper.unZipFile(abuConfig.getAbuSolutionZippedFilePath(), abuConfig.getDeployFolder());
    }

    public List<String> setAbuBinList() throws Exception {
        Exception invalidInput = new Exception("Invalid Input found");

        //set to hold bins passed as parameters
        String[] ica_bins = abuConfig.getIcaBins().split(",");

        //Check that valid input have been provided
        if (ica_bins.length == 0) {
            log.error("No BIN found");
            throw invalidInput;

        }
        for (String s : ica_bins) {
            if ((!s.matches("[0-9]+")) || (s.toCharArray().length < 5)) {
               log.error("There is something wrong with this value {} please review",s);
                System.out.println();
                throw invalidInput;
            }
        }

        return Arrays.asList(ica_bins);//convert bin array to hashed set
    }


    @Override
    public void checkForPcCards(List<String> abuBinToBeCheckedList) {
        System.out.println("Checking if pc_cards view exists..........");

        try {
            for (String bin : abuBinToBeCheckedList) {
                var rs = pcCardsViewRepo.findByBin(bin);
                if (rs == null) {
                    log.error("The issuer for BIN {} is missing from the pc_cards view", bin);
                    log.info("Update the view to include the issuer for BIN " + bin);
                    System.exit(1);
                }
            }


        } catch (Exception e) {
            String errorType = e.getClass().getName();
            if (errorType.contains("org.springframework.dao.InvalidDataAccessResourceUsageException")) {
                log.error("pc_cards view does not exist, please create the pc_cards view");
                System.exit(1);
            }

        }


    }

    @Override
    public void checkForPcCardAccounts(List<String> abuBinToBeCheckedList) {
        System.out.println("Checking if pc_card_accounts view exists..........");
        try {
            for (String bin : abuBinToBeCheckedList) {
                var rs = pcCardAccountsViewRepo.findByBin(bin);
                if (rs == null) {
                    System.out.println("The issuer for BIN " + bin + " is missing from the pc_card_accounts view");
                    System.exit(1);
                }
            }


        } catch (Exception e) {
            String errorType = e.getClass().getName();
            if (errorType.contains("org.springframework.dao.InvalidDataAccessResourceUsageException")) {
                log.error("pc_card_accounts view does not exist, please create the pc_card_accounts view");
                System.exit(1);
            }

        }
    }

    @Override
    public void copyInitialRecordsToAbuTable(List<String> abuBins) throws Exception {

        //percentage parameter
        int numberOfRecords;

        Exception invalidInputException = new Exception("Invalid Input provided");

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("IS THIS A TEST OR PRODUCTION ENVIRONMENT? TEST/PROD?");
        String typeOfEnvironment = scanner.nextLine().toUpperCase();
        System.out.println();
        if (typeOfEnvironment.equalsIgnoreCase("TEST")
                || typeOfEnvironment.equalsIgnoreCase("T")) {
            System.out.println("Copying data from pc_cards to pc_cards_abu table...............");

            numberOfRecords = 1;
            for (String bin : abuBins) {
                System.out.println(abuTableRepo.getTotalRecordsCopied(numberOfRecords, bin) + " records copied! for BIN " +bin);

            }


        } else if (typeOfEnvironment.equalsIgnoreCase("PROD")
                || typeOfEnvironment.equalsIgnoreCase("P")) {
            System.out.println("Copying data from pc_cards to pc_cards_abu table...............");

            numberOfRecords = 100;
            for (String bin : abuBins) {
                System.out.println(abuTableRepo.getTotalRecordsCopied(numberOfRecords, bin) + " records copied! for BIN " +bin);

            }

        }

        else{
            log.error("Invalid input " + typeOfEnvironment +  " provided..");
            throw invalidInputException;
        }

    }

    @Override
    public void copyNewRecordsToAbuTable(List<String> abuBins) {
        int numberOfRecords = 50;
        for (String bin : abuBins)
        {
            System.out.println("Copying records for BIN " + bin + " from pc_cards to pc_cards_abu............");
            System.out.println(abuTableRepo.addNewRecordsForReasonCodeN(numberOfRecords, bin) + " records copied! for BIN " + bin);
        }


    }

    @Transactional
    @Modifying
    @Override
    public void updateRecordsForReasonCodeC() {
        System.out.println("Updating hold response code to 41...........");


        for (AbuTable abuTable : abuTableRepo.getTop2AbuRecords()) {
            System.out.println(" Pan retrieved is " + abuTable.getB_pan());

            String query = "update pc_cards_" + abuTable.getAa_issuerNr() +
                    "_A     set hold_rsp_code = 41 where seq_nr = " +
                    "'" +
                    abuTable.getC_seqNr() +
                    "'" + "and pan = "
                    + "'" + abuTable.getB_pan() + "'";
            int rs = entityManager.createNativeQuery(query).executeUpdate();
            System.out.println(rs + " record updated");

        }

    }

    @Transactional
    @Modifying
    @Override
    public void createTriggersOnIcaIssuerTables(List<String> abuBins) {
        String postcardDataTableSuffix = setPostcardCardTableSuffix();


        System.out.println();
        System.out.println("Creating Triggers.......................");
        System.out.println();
        HashSet<Integer> issuerNumberSet = new HashSet<>();
        for (String bin : abuBins) {
            issuerNumberSet.add(abuTableRepo.getIssuerNumber(bin));

        }

        for (Integer issuerNr : issuerNumberSet) {
            String dropTrigger = "IF EXISTS (select * from sys.objects where object_id = OBJECT_ID(N'[dbo].[tr_at_update_mastercard_abu_"
                    + issuerNr + postcardDataTableSuffix +  "]"
                    + "')  AND type in (N'TR'))"
                    + "  BEGIN"
                    + "  DROP TRIGGER [dbo].[tr_at_update_mastercard_abu_"
                    + issuerNr
                    + postcardDataTableSuffix
                    + "]"
                    + " END"
                    + "  ELSE"
                    + "  RETURN ";

            entityManager.createNativeQuery(dropTrigger).executeUpdate();


            System.out.printf("Creating Trigger on Issuer %s", issuerNr);
            System.out.println();

            String createTrigger = "CREATE TRIGGER [dbo].[tr_at_update_mastercard_abu_"
                    + issuerNr
                    + postcardDataTableSuffix
                    + "] ON [dbo].[pc_cards_"
                    + issuerNr
                    + postcardDataTableSuffix
                    + "]"
                    + " after update AS "
                    + "  BEGIN set nocount on;"
                    + "  UPDATE pc_cards_abu SET card_status = i.card_status from pc_cards_abu as pcb "
                    + "  inner join inserted i  on pcb.pan= i.pan and pcb.seq_nr = i.seq_nr;"

                    + "  update pc_cards_abu set hold_rsp_code = i.hold_rsp_code from pc_cards_abu as pcb"
                    + "  inner join inserted i  on pcb.pan= i.pan and pcb.seq_nr = i.seq_nr; "

                    + "  update pc_cards_abu set last_updated_date = i.last_updated_date from pc_cards_abu as pcb"
                    + "  inner join inserted i  on pcb.pan= i.pan and pcb.seq_nr = i.seq_nr;"
                    + "  end";


            entityManager.createNativeQuery(createTrigger).executeUpdate();


        }


    }


    @Transactional
    @Modifying
    @Override
    public void createProcedureToAutoPopulateAbuTable(List<String> abuBins) {

        String dropProcedure = "IF EXISTS (SELECT * FROM [dbo].sysobjects WHERE id = object_id (N'[dbo].[abu_mastercard_insert_new_records]') " +
                "  AND OBJECTPROPERTY(id, N'IsProcedure') = 1)  DROP PROCEDURE [dbo].[abu_mastercard_insert_new_records];";

        entityManager.createNativeQuery(dropProcedure).executeUpdate();



        StringBuilder binBuffer = new StringBuilder();
        binBuffer.append("'");
        for (int i = 0; i < abuBins.size(); i++) {
            if (i == (abuBins.size() - 1)) {
                binBuffer.append(abuBins.get(i));
            } else {
                binBuffer.append(abuBins.get(i)).append(",");
            }
        }
        binBuffer.append("'");


        String createProcedure = String.format(constants.SP_INSERT_NEW_RECORDS_IN_ABU_TABLE,binBuffer);
        System.out.println("Creating stored proc abu_mastercard_insert_new_records......");
        entityManager.createNativeQuery(createProcedure).executeUpdate();
        System.out.println("Done creating stored procedure abu_mastercard_insert_new_records.....");

    }

    @Transactional
    @Modifying
    @Override
    public void insertNewRecordForReasonCodeR() {

        Scanner scanner = new Scanner(System.in);
        var abuTableRecord = abuTableRepo.getTopClosedRecord();
        String postcardDataTableSuffix = setPostcardCardTableSuffix();

        System.out.println(abuTableRecord);

        //String seqNr = "005";//could use this in the future to allow input of the seq_nr

        System.out.println("Provide new pan that will serve as replacement for the following closed record with PAN: " + abuTableRecord.getB_pan());
        String replacementPan = scanner.nextLine();
        String encPan = "";
        String newCardRecord;
        String newCardAccountRecord;

        if (abuConfig.isDbEncrypted()) {
            HashMap<String, String> panToEncryptMap = new HashMap<>();
            panToEncryptMap.put(replacementPan, replacementPan);
            HashMap<String, String> encryptedHashedPanMap = encryptPanOrAccount(panToEncryptMap);

            encPan = encryptedHashedPanMap.get(replacementPan + "_enc");
            replacementPan = encryptedHashedPanMap.get(replacementPan + "_hash");

        }

        newCardRecord = " insert into pc_cards_" + abuTableRecord.getAa_issuerNr() + postcardDataTableSuffix
                + "(issuer_nr,pan,seq_nr,card_program,default_account_type,card_status,expiry_date,hold_rsp_code,"
                + "pvki_or_pin_length,pvv_or_pin_offset,mailer_destination,discretionary_data, date_issued,date_activated,"
                + "branch_code,last_updated_date,last_updated_user,customer_id,company_card,pvki2_or_pin2_length,pan_encrypted)"

                + "  select issuer_nr," + "'" + replacementPan + "'" + ",'005',card_program,default_account_type,card_status,N'2312',"
                + "null,pvki_or_pin_length,pvv_or_pin_offset,mailer_destination,discretionary_data,getdate(),GETDATE(),"
                + "branch_code,GETDATE(),last_updated_user,customer_id,company_card,pvki2_or_pin2_length," + "'" + encPan + "'"
                + "  from pc_cards_abu where pan =  " + "'" + abuTableRecord.getB_pan() + "'";

        newCardAccountRecord = " insert into pc_card_accounts_" + abuTableRecord.getAa_issuerNr() + postcardDataTableSuffix
                + "(issuer_nr,pan,seq_nr,account_id,account_type_nominated,account_type_qualifier,last_updated_date,"
                + "last_updated_user,account_type,date_deleted)"

                + " select issuer_nr," + "'" + replacementPan + "'" + ",'005',account_id,account_type_nominated,account_type_qualifier,"
                + "getdate(),last_updated_user,account_type,date_deleted"
                + " from pc_card_accounts where pan =  " +   "'" + abuTableRecord.getB_pan() + "'";


            entityManager.createNativeQuery(newCardRecord).executeUpdate();

            entityManager.createNativeQuery(newCardAccountRecord).executeUpdate();


            String newAbuRecord = "insert into pc_cards_abu (issuer_nr,pan,seq_nr,card_program,default_account_type,card_status,"
                    + "expiry_date,hold_rsp_code,pvki_or_pin_length,pvv_or_pin_offset,mailer_destination,discretionary_data,"
                    + "date_issued,date_activated,branch_code,last_updated_date,last_updated_user,customer_id,company_card,"
                    + "pvki2_or_pin2_length,pan_encrypted,upload_status,account_chain,account_hierarchy,closed)"

                    + " select issuer_nr,pan,seq_nr,card_program,default_account_type,card_status,'2312',null,pvki_or_pin_length,"
                    + "pvv_or_pin_offset,mailer_destination,discretionary_data,date_issued,date_activated,branch_code,"
                    + "last_updated_date,last_updated_user,customer_id,company_card,pvki2_or_pin2_length," + "'" + encPan + "'" + ",0 ,null,null,0"
                    + " from pc_cards where pan = "  + "'" + replacementPan + "'"  + "and seq_nr = '005'";

            entityManager.createNativeQuery(newAbuRecord).executeUpdate();


        }
    


    public HashMap encryptPanOrAccount(HashMap<String, String> panAccountMap){
        String url = abuConfig.getUrl();
        String userName = abuConfig.getUserName();
        String password = abuConfig.getPassword();
        String payload = gson.toJson(panAccountMap);

        Client client = Client.create();
        WebResource resource = client.resource(url);

        String authStr = userName + ":" + password;
        String encoded = Base64.getEncoder().withoutPadding().encodeToString(authStr.getBytes());


        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .header("Content-type", "application/json")
                .header("Authorization", "Basic " + encoded)
                .post(ClientResponse.class, payload);

        String jsonResponse = response.getEntity(String.class);
        //System.out.println("Json Response " + jsonResponse);
        HashMap encryptedMap = gson.fromJson(jsonResponse, HashMap.class);
        return encryptedMap;

    }


}
