package TucLoA;

/**
 * Created by costi_000 on 5/15/2016.
 */
public class Piece {
    private byte row;
    private byte col;
    private byte color;
    private byte direction;


    public byte getDirection() {
        return direction;
    }

    public void setDirection(byte direction) {
        this.direction = direction;
    }

    public Piece(byte row, byte col, byte color, byte direction) {

        this.row = row;
        this.col = col;
        this.color = color;
        this.direction = direction;
    }

    public byte getRow() {
        return row;
    }

    public void setRow(byte row) {
        this.row = row;
    }

    public byte getCol() {
        return col;
    }

    public void setCol(byte col) {
        this.col = col;
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }
}
