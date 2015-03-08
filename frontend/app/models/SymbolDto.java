package models;

import com.mysema.query.annotations.QueryProjection;

/**
 * Created by martin on 07/03/15.
 */
public class SymbolDto {

    private int id;
    private String symbol;

    @QueryProjection

    public SymbolDto(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
