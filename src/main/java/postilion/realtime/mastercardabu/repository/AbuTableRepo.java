package postilion.realtime.mastercardabu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postilion.realtime.mastercardabu.model.AbuTable;

import java.util.List;

@Repository
public interface AbuTableRepo extends JpaRepository<AbuTable, Long> {

    @Transactional
    @Procedure(name = "AbuTable.abu_populate_pc_cards_abu_I")
    int getTotalRecordsCopied(@Param("numOfRecords") int numOfRecords,@Param("bankBin") String bankBin);

    @Transactional
    @Procedure(name = "AbuTable.abu_populate_pc_cards_abu_N")
    int addNewRecordsForReasonCodeN(@Param("numOfRecords") int numOfRecords, @Param("bankBin") String bankBin);


    @Query(value = "select distinct issuer_nr from pc_card_programs (nolock) where substring(card_prefix,1,6) = :bankBin", nativeQuery = true)
    int getIssuerNumber(@Param("bankBin") String bankBin);


    @Query(value = "select top 2 * from pc_cards_abu (nolock) where hold_rsp_code is null ",nativeQuery = true)
    List<AbuTable> getTop2AbuRecords();


    @Query(value = "SELECT TOP 1 * FROM PC_CARDS_ABU WHERE HOLD_RSP_CODE = 41 AND CLOSED = 1", nativeQuery = true)
    AbuTable getTopClosedRecord();











}
