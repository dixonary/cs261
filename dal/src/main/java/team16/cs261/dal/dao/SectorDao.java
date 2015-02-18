package team16.cs261.dal.dao;

import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Sector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class SectorDao extends AbstractDao<Integer, Sector> {

    private static final String INSERT = "INSERT IGNORE INTO Sector (name) VALUES (?);";

    public SectorDao() {
        super(Sector.class);
    }

    @Override
    public void insert(Sector sector) {
        jdbcTemplate.update(INSERT, sector.getName());
    }

    @Override
    public void insert(final Iterable<Sector> ents) {
        List<Object[]> args = new ArrayList<>();
        for (Sector ent : ents) {
            args.add(new Object[]{ent.getName()});
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }

}
