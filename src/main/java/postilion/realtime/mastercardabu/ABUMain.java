package postilion.realtime.mastercardabu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import postilion.realtime.mastercardabu.implementation.ABUDeployer;

import java.util.Arrays;

/**
 * @project MasterCard ABU Deployment Tool
 * @author Vin-Anuonye Chukwuemeka
 * Created 04/07/2021
 */



@Slf4j
@SpringBootApplication
public class ABUMain implements CommandLineRunner {

    @Autowired
    ABUDeployer testABUDeployer;

    public static void main(String[] args) {


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("        **************************************************************************");
        System.out.println("\t*  THIS IS A MASTERCARD ABU UTILITY PROGRAM THAT HELPS WITH THE          *");
        System.out.println("\t*\t             FOLLOWING ANCILLARY PROCESSES                       *");
        System.out.println("\t*\t1. CREATE THE MASTERCARD ABU WORKING FOLDER                      *");
        System.out.println("\t*\t2. CREATE THE PC_CARDS_ABU TABLE                                 *");
        System.out.println("\t*\t3. CREATE THE REQUIRED STORED PROCEDURES                         *");
        System.out.println("\t*\t4. CHECKS THAT PC_CARDS AND PC_CARD_ACCOUNTS VIEWS EXIST         *");
        System.out.println("\t*\t5. CREATE TRIGGERS TO UPDATE RECORDS IN THE PC_CARDS_ABU TABLE   *");
        System.out.println("\t*\t6. COPY RECORDS FROM PC_CARDS TO THE PC_CARDS_ABU TABLE          *");
        System.out.println("\t*\t                                                                 *");
        System.out.println("\t*\t   PLEASE NOTE, IF THIS IS NOT THE FIRST DEPLOYMENT              *");
        System.out.println("\t*\t   SELECT OPTION 6 and 5 TO DEPLOY FOR ANOTHER BANK              *");
        System.out.println("        **************************************************************************");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();


            SpringApplication.run(ABUMain.class, args);


    }


    @Override
    public void run(String... args) throws Exception {
        log.info("Application started with command-line arguments: {}.To kill this application, press Ctrl + C.", Arrays.toString(args));
        testABUDeployer.runAbu();

    }

}
