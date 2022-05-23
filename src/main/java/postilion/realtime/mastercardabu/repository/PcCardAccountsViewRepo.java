package postilion.realtime.mastercardabu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import postilion.realtime.mastercardabu.model.PcCardAccountsView;

@Repository
public interface PcCardAccountsViewRepo extends JpaRepository<PcCardAccountsView, String> {

    PcCardAccountsView findByBin(String bin);
}
