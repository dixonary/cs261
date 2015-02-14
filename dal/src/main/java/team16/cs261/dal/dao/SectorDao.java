package team16.cs261.dal.dao;

import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Sector;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class SectorDao extends AbstractDao {

    private static final String INSERT = "INSERT IGNORE INTO Sector (name) VALUES (?);";

    public SectorDao() {

    }

    public void insert(Sector sector) {
        jdbcTemplate.update(INSERT, sector.getName());
    }

}
