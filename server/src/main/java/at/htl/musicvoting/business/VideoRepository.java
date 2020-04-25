package at.htl.musicvoting.business;

import at.htl.musicvoting.model.Video;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class VideoRepository implements PanacheRepository<Video> {

    public Video save(Video assignment) {
        this.persistAndFlush(assignment);
        return findById(assignment.getId());
    }

    public Video update(Video assignment) {
        this.save(assignment);
        return findById(assignment.getId());
    }

    /**
     * Gibt eine zufölliges Video aus der Liste zurück.
     */
    public Video getRandom() {
        List<Long> ids = streamAll().map(x -> x.getId()).collect(Collectors.toList());
        return this.findById(ids.get(new Random().nextInt(ids.size()-1)));
    }
}
