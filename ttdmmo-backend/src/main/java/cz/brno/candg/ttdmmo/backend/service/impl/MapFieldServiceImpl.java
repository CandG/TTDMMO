package cz.brno.candg.ttdmmo.backend.service.impl;

import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.CityDao;
import cz.brno.candg.ttdmmo.backend.dao.HousePlacesDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.dao.MessageDao;
import cz.brno.candg.ttdmmo.backend.dao.RankDao;
import cz.brno.candg.ttdmmo.backend.firebase.FbConstants;
import cz.brno.candg.ttdmmo.backend.firebase.FbServerTime;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.constants.MapTypes;
import cz.brno.candg.ttdmmo.dto.HousePlacesDTO;
import cz.brno.candg.ttdmmo.dto.MapFieldDTO;
import cz.brno.candg.ttdmmo.model.City;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.model.Message;
import cz.brno.candg.ttdmmo.model.Rank;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Impl. service for map
 *
 * @author lastuvka
 */
public class MapFieldServiceImpl implements MapFieldService {

    final static Logger log = LoggerFactory.getLogger(MapFieldServiceImpl.class);

    @Inject
    FbServerTime serverTime;

    @Inject
    FbConstants fbConstants;

    @Inject
    private MapFieldDao mapFieldDao;

    @Inject
    private CityDao cityDao;

    @Inject
    private RankDao rankDao;

    @Inject
    private MessageDao messageDao;

    @Inject
    private HousePlacesDao housePlacesDao;

    @Inject
    private AuthUserDao authUserDao;

    @Override
    public void build(int money, MapField mapField, MapFieldDTO firebaseReq) {
        if (money > 0 && (mapField.getType().equals(MapTypes.nothing) || (mapField.getOwner_id().equals(firebaseReq.getUser_id()) && isRoad(mapField.getType()) && firebaseReq.getType().equals(MapTypes.nothing) && mapField.getPaths().isEmpty()))) {
            authUserDao.updateMoney(firebaseReq.getUser_id(), -100);
            mapField.setType(firebaseReq.getType());
            mapField.setOwner_id(firebaseReq.getUser_id());
            mapFieldDao.create(mapField);
        } else {
            Message message = new Message();
            message.setText("The operation can not be done!");
            message.setUser_id(firebaseReq.getUser_id());
            message.setXy(firebaseReq.getXy());
            messageDao.create(message);
        }
    }

    public boolean isRoad(String type) {
        return type.startsWith(MapTypes.road);
    }

    @Override
    public Map<String, Integer> findPosition(int num) {
        int x = 0;
        int y = 0;
        int padding = (FbRef.CITY_PADDING);

        int w = num - fbConstants.getUSER_NUM_OFFSET();
        int level = 1;
        int countInLevel = 2;
        while (w >= countInLevel * 4) {
            w -= countInLevel * 4;
            countInLevel += 2;
            level++;
        }
        int direction = w / countInLevel;
        int numInLevel = w % countInLevel;
        if (direction == 1) {
            x = level * padding;
            y = -1 * level * padding + numInLevel * padding;
        } else if (direction == 3) {
            x = -1 * level * padding;
            y = level * padding - numInLevel * padding;
        } else if (direction == 2) {
            y = level * padding;
            x = level * padding - numInLevel * padding;
        } else if (direction == 0) {
            y = -1 * level * padding;
            x = -1 * level * padding + numInLevel * padding;
        }
        Map<String, Integer> pos = new HashMap<String, Integer>();
        pos.put("x", x);
        pos.put("y", y);
        return pos;
    }

    @Override
    public void generateCity(String name, int x, int y) {
        int padding = (FbRef.CITY_PADDING);
        int margin = padding / 2;

        City city = new City();
        city.setName(name);
        city.setPeople(FbRef.START_PEOPLE);
        city.setXy(MapField.indexFromXY(x, y));
        cityDao.create(city);

        Rank rank = new Rank();
        rank.setName(name);
        rank.setPeople(FbRef.START_PEOPLE);
        rank.setXy(MapField.indexFromXY(x, y));
        rankDao.create(rank);

        int left = x - margin;
        int right = x + margin;
        int top = y - margin;
        int bottom = y + margin;
        for (int col = left; col < right; col++) {
            for (int row = top; row < bottom; row++) {
                MapField mapField = new MapField();
                if (col == x && row == y) {
                    mapField.setCity(name);
                    mapField.setType(MapTypes.cross);
                } else if (col == x - 1 && row == y) {
                    mapField.setType(MapTypes.left);
                } else if (col == x + 1 && row == y) {
                    mapField.setType(MapTypes.left);
                } else if (col == x && row == y + 1) {
                    mapField.setType(MapTypes.mainStation);
                } else if (col == x && row == y - 1) {
                    mapField.setType(MapTypes.Warehouse);
                } else if (col == x - 1 && row == y - 1) {
                    mapField.setType(MapTypes.cityHall);
                } else if (col == x + 3 && row == y + 2) {
                    mapField.setType(MapTypes.farm);
                } else if (col == x + 3 && row == y + 3) {
                    mapField.setType(MapTypes.farmField);
                } else if (col == x + 2 && row == y + 2) {
                    mapField.setType(MapTypes.farmField);
                } else if (col == x + 4 && row == y + 2) {
                    mapField.setType(MapTypes.farmField);
                } else if (col == x + 1 && row == y - 6) {
                    mapField.setType(MapTypes.wood);
                } else if (col == x + 0 && row == y - 6) {
                    mapField.setType(MapTypes.woodField);
                } else if (col == x + 1 && row == y - 5) {
                    mapField.setType(MapTypes.woodField);
                } else if (col == x + 2 && row == y - 6) {
                    mapField.setType(MapTypes.woodField);
                } else if (col == x - 1 && row == y + 1) {
                    mapField.setType(MapTypes.home + '0');
                } else {
                    mapField.setType(MapTypes.nothing);
                }
                mapField.setX(col);
                mapField.setY(row);
                mapFieldDao.create(mapField);
            }
        }
    }

    @Override
    public void generateHousePlaces(int x, int y) {
        String city_id = MapField.indexFromXY(x, y);
        housePlacesDao.addPlace(city_id, MapField.indexFromXY(x - 1, y + 1));
        housePlacesDao.addPlace(city_id, MapField.indexFromXY(x - 2, y - 1));
        housePlacesDao.addPlace(city_id, MapField.indexFromXY(x - 2, y + 1));
        housePlacesDao.addPlace(city_id, MapField.indexFromXY(x + 1, y - 1));
        housePlacesDao.addPlace(city_id, MapField.indexFromXY(x + 1, y + 1));
        housePlacesDao.addPlace(city_id, MapField.indexFromXY(x + 2, y - 1));
        housePlacesDao.addPlace(city_id, MapField.indexFromXY(x + 2, y + 1));
    }

    @Override
    public void spreadCity(List<MapField> places, HousePlacesDTO req) {
        while (places.size() > 0) {
            Random rand = new Random();
            int n = rand.nextInt(places.size());
            MapField mapField = places.get(n);
            log.info("Rest fields: " + places.size() + ", choosen field:" + mapField);
            if (places.size() < 14) {
                housePlacesDao.addPlace(req.getCity_id(), MapField.indexFromXY(mapField.getX(), mapField.getY() - 1));
                housePlacesDao.addPlace(req.getCity_id(), MapField.indexFromXY(mapField.getX(), mapField.getY() + 1));
                housePlacesDao.addPlace(req.getCity_id(), MapField.indexFromXY(mapField.getX() - 1, mapField.getY()));
                housePlacesDao.addPlace(req.getCity_id(), MapField.indexFromXY(mapField.getX() + 1, mapField.getY()));
                housePlacesDao.addPlace(req.getCity_id(), MapField.indexFromXY(mapField.getX() + 1, mapField.getY() + 1));
                housePlacesDao.addPlace(req.getCity_id(), MapField.indexFromXY(mapField.getX() - 1, mapField.getY() - 1));
                housePlacesDao.addPlace(req.getCity_id(), MapField.indexFromXY(mapField.getX() + 1, mapField.getY() - 1));
                housePlacesDao.addPlace(req.getCity_id(), MapField.indexFromXY(mapField.getX() - 1, mapField.getY() + 1));
            }
            if (mapField.getType().equals(MapTypes.nothing) || mapField.getType().startsWith(MapTypes.home)) {
                if (mapField.getType().startsWith(MapTypes.home)) {

                    String num = mapField.getType().substring(MapTypes.home.length(), mapField.getType().length());
                    if (!num.equals("")) {
                        int number = Integer.parseInt(num) + 1;
                        mapField.setType(MapTypes.home + number);
                        if (number == 4) {
                            mapField.setType(MapTypes.skyScraper);
                            housePlacesDao.removePlace(req.getCity_id(), MapField.indexFromXY(mapField.getX(), mapField.getY()));
                        }
                    } else {
                        return;
                    }
                } else {
                    mapField.setType(MapTypes.home + '0');
                }
                mapFieldDao.create(mapField);
                return;
            } else {
                housePlacesDao.removePlace(req.getCity_id(), MapField.indexFromXY(mapField.getX(), mapField.getY()));
                places.remove(n);
            }
        }
    }

    @Override
    public void generateMiddle(int x, int y) {
        int padding = (FbRef.CITY_PADDING);
        int margin = padding / 2;

        int left = x - margin;
        int right = x + margin;
        int top = y - margin;
        int bottom = y + margin;
        for (int col = left; col < right; col++) {
            for (int row = top; row < bottom; row++) {
                MapField mapField = new MapField();
                mapField.setType(MapTypes.nothing);
                mapField.setX(col);
                mapField.setY(row);
                mapFieldDao.create(mapField);
            }
        }
    }
}
