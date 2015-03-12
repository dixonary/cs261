package team16.cs261.common.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.common.entity.Comm;
import team16.cs261.common.entity.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class CommDao extends AbstractDao<Integer, Comm> {

    private static final String INSERT = "CALL InsertComm(?, ?, ?, ?, ?)";
    private static final String SELECT = "SELECT * FROM Comm";

    public CommDao() {
        super(Comm.class);
    }

    private static Object[] getArgs(Comm ent) {
        return new Object[]{
                ent.getTime(),
                ent.getTimestamp(),
                ent.getTick(),
                ent.getSender(),
                ent.getRecipient(),
                ent.getSenderId(),
                ent.getRecipientId()

        };
    }

    public void insert(Comm ent) {
        jdbcTemplate.update(INSERT,
                ent.getTime(), ent.getTick(), ent.getTimestamp(),
                ent.getSender(), ent.getRecipient()
        );
    }

/*    public void insert(final Iterable<Comm> ents) {
        List<Object[]> args = new ArrayList<>();
        for (Comm ent : ents) {
            args.add(new Object[]{
                    ent.getTime(), ent.getTick(), ent.getTimestamp(),
                    ent.getSender(), ent.getRecipient()
            });
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }*/

    int columns = 7;
    String columnsStmt = "(time, timestamp, tick, sender, recipient, senderId, recipientId)";
    public void insert(List<Comm> ents) {
        if (ents.size() == 0) return;
        StringBuilder sql = new StringBuilder("INSERT INTO Comm "+columnsStmt+" VALUES ");



        String values = genValuesStmt(columns);

        Object[] args = new Object[ents.size() * columns];

        for (int i = 0; i < ents.size(); i++) {
            sql.append(values);
            if (i < ents.size() - 1) {
                sql.append(", ");
            }

            Comm ent = ents.get(i);

            Object[] entArgs = getArgs(ent);

            for (int c = 0; c < columns; c++) {
                args[i * columns + c] = entArgs[c];
            }

            //args[i * 3] = ents.get(i).getType().name();
            //args[i * 3 + 1] = ents.get(i).getTime();
            //args[i * 3 + 2] = ents.get(i).getRaw();


        }

        jdbcTemplate.update(sql.toString(), args);
    }

    @Override
    public List<Comm> selectAll() {
        return jdbcTemplate.query(
                SELECT, new Object[0],
                new BeanPropertyRowMapper<>(Comm.class));
    }

}
