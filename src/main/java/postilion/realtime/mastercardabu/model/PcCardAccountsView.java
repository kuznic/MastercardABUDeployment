package postilion.realtime.mastercardabu.model;


import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Subselect(
        "select distinct substring (cp.card_prefix,1,6) as  bin  from pc_card_accounts as ca" +
                " inner join pc_card_programs as cp " +
                " on cp.issuer_nr = ca.issuer_nr "

)
public class PcCardAccountsView {
    @Id
    private String bin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PcCardAccountsView that = (PcCardAccountsView) o;

        return Objects.equals(bin, that.bin);
    }

    @Override
    public int hashCode() {
        return 1419138893;
    }
}
