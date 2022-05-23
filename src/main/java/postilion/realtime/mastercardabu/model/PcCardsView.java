package postilion.realtime.mastercardabu.model;


import lombok.Data;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Immutable
@Subselect(
        "select distinct substring (cp.card_prefix,1,6) as  bin, cp.issuer_nr    from pc_cards as pc" +
        " inner join pc_card_programs as cp " +
        " on pc.card_program = cp.card_program"

        )
public class PcCardsView {
    @Id
    private String bin;

    private String issuerNr;
}
