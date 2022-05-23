package postilion.realtime.mastercardabu.model;


import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Slf4j
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@NamedStoredProcedureQuery(name = "AbuTable.abu_populate_pc_cards_abu_I",
procedureName = "abu_populate_pc_cards_abu_I", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "bankBin", type= String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "numOfRecords", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "recordsCount", type = Integer.class)

})
@NamedStoredProcedureQuery(name = "AbuTable.abu_populate_pc_cards_abu_N",
        procedureName = "abu_populate_pc_cards_abu_N", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "bankBin", type= String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "numOfRecords", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "recordsCount", type = Integer.class)

})
@Table(name = "pc_cards_abu", indexes = {@Index(name = "ix_pc_cards_abu", columnList = "pan,seqNr,expiryDate", unique = true),
                                         @Index(name = "ix_pc_cards_abu_2", columnList = "cardStatus,holdRspCode,accountHierarchy," +
                                                 "expiryDate")})
public class AbuTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long a_id;

    @Column(name = "issuerNr", nullable = false)
    private Integer aa_issuerNr;


    @Column(name= "pan", nullable = false, length = 66)
    private String b_pan;

    @Column(name= "seqNr", nullable = false, columnDefinition = "char(3) default 'NIL'")
    private String c_seqNr;


    @Column(name= "cardProgram", nullable = false, length = 20)
    private String d_cardProgram;

    @Column(name= "defaultAccountType", nullable = false, length = 2)
    private String e_defaultAccountType;

    @Column(name= "cardStatus", nullable = false)
    private Integer f_cardStatus;

    @Column(name= "cardCustomState")
    private Integer g_cardCustomState;


    @Column(name= "expiryDate", nullable = false, columnDefinition = "varchar(4) default 'NIL'")
    private String h_expiryDate;

    @Column(name= "holdRspCode", columnDefinition = "char(2)")
    private String i_holdRspCode;


    @Column(name= "track2_value", length = 20)
    private String j_track2Value;

    @Column(name = "track2_value_offset")
    private Integer k_track2ValueOffset;

    @Column(name = "pvkiOrPinLength")
    private Integer l_pvkiOrPinLength;


    @Column(name="pvvOrPinOffset", length = 12)
    private String  m_pvvOrPinOffset;


    @Column(name="pvv2_or_pin2_offset", length = 12)
    private String  n_pvv2OrPinOffset;


    @Column(name = "validationDataQuestion", length = 50)
    private String o_validationDataQuestion;

    @Column(name = "validationData", length = 50)
    private String p_validationData;


    @Column(name = "cardholderRspInfo", length = 50)
    private String q_cardholderRspInfo;

    @Column(name = "mailerDestination", nullable = false)
    private Integer r_mailerDestination;


    @Column(name = "discretionaryData", length = 19)
    private String s_discretionaryData;

    @Column(name = "dateIssued",columnDefinition="datetime")
    private  java.sql.Timestamp t_dateIssued;

    @Column(name = "dateActivated",columnDefinition="datetime")
    private java.sql.Timestamp u_dateActivated;


    @Column(name = "issuerReference", length = 20)
    private String v_issuerReference;


    @Column(name = "branchCode", length = 10)
    private String w_branchCode;

    @Column(name = "lastUpdatedDate" , nullable = false,columnDefinition="datetime")
    private java.sql.Timestamp x_lastUpdatedDate;


    @Column(name = "lastUpdatedUser", nullable = false, length = 20)
    private String y_lastUpdatedUser;


    @Column(name = "customerId", length = 25)
    private String z_customerId;

    @Column(name = "batchNr")
    private Integer za_batchNr;

    @Column(name = "companyCard")
    private Integer zb_companyCard;

    @Column(name = "dateDeleted",columnDefinition="datetime")
    private java.sql.Timestamp zc_dateDeleted;

    @Column(name = "pvki2_or_pin2_length")
    private Integer zd_pvki2OrPin2Length;

    @Size()
    @Column(name = "extendedFields")
    private String ze_extendedFields;

    @Column(name = "expiryDay", length = 2)
    private String zf_expiryDay;

    @Column(name ="fromDate", length =4)
    private String zg_fromDate;

    @Column(name = "fromDay", length = 2)
    private String zh_fromDay;

    @Column(name = " contactlessDiscData", length = 19)
    private String zi_contactlessDiscData;

    @Column(name = "dcvvKeyIndex")
    private Integer zj_dcvvKeyIndex;

    @Column(name = "panEncrypted", length = 70)
    private String zk_panEncrypted;


    @Column(name = "expiryDateTime",columnDefinition="datetime" )
    private java.sql.Timestamp zl_expiryDateTime;

    @Column(name = "uploadStatus",nullable = false, columnDefinition="bit default 0")
    private boolean zm_uploadStatus;

    @Column( name = "accountChain", length = 66)
    private String zn_accountChain;

    @Column(name = "accountHierarchy")
    private Integer zo_accountHierarchy;

    @Column(name = "closed",nullable = false, columnDefinition="bit default 0")
    private boolean zp_closed;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AbuTable abuTable = (AbuTable) o;

        return Objects.equals(a_id, abuTable.a_id);
    }

    @Override
    public int hashCode() {
        return 1747101152;
    }
}
