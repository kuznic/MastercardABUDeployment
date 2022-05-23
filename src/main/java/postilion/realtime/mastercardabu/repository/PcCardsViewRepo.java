package postilion.realtime.mastercardabu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import postilion.realtime.mastercardabu.model.PcCardsView;

@Repository
public interface PcCardsViewRepo extends JpaRepository<PcCardsView, String> {

    PcCardsView findByBin(String bin);
}
