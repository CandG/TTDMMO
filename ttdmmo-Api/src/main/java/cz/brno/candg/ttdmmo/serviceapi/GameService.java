package cz.brno.candg.ttdmmo.serviceapi;

/**
 * Game service interface for operations with whole game.
 *
 * @author lastuvka
 */
public interface GameService {

    /**
     * Create game - set constant for new game
     *
     */
    public void createGame(int USER_NUM_OFFSET);

}
