var mapCityRef = new Firebase(FbRef.refD + 'map');
var cityRef = new Firebase(FbRef.refD + 'cities');

/**
 * Diff. of arrays
 * 
 * @param {type} a
 * @returns {Array.prototype@call;filter}
 */
Array.prototype.diff = function(a) {
    return this.filter(function(i) {
        return !(a.indexOf(i) > -1);
    });
};
/*
 * From coords x,y to index
 * 
 * @param {type} x
 * @param {type} y
 * @returns {string} separate x and y value 
 * 
 */
function indexFromXY(x, y) {
    return x + ":" + y;
}
/**
 * coords x, y plus index
 * 
 * @param {type} x
 * @param {type} y
 * @param {type} index
 * @returns {String} index 
 */
function XYPlusIndex(x, y, index) {
    var coords = index.split(":");
    return indexFromXY(x + parseInt(coords[0]), y + parseInt(coords[1]));
}
/**
 * coords x,y from index
 * @param {type} index
 * @returns {XYFromIndex.Anonym$0} object with x,y
 */

function XYFromIndex(index) {
    var coords = index.split(":");
    return {x: parseInt(coords[0]), y: parseInt(coords[1])};
}

function markField(index) {
    var xy = XYFromIndex(index);
    var sheet1 = new sheetengine.Sheet({x: 0, y: 0, z: 0}, {alphaD: 270, betaD: 0, gammaD: 0}, {w: 20, h: 20});
    sheet1.context.fillStyle = '#FF0000';
    sheet1.context.fillRect(0, 0, 20, 20);
    // sheetengine.drawObjectContour = true;
    return  new sheetengine.SheetObject({x: xy.x * sheetengine.scene.tilewidth, y: xy.y * sheetengine.scene.tilewidth, z: 0}, {alphaD: 0, betaD: 0, gammaD: 0}, [sheet1], {w: 30, h: 30, relu: 15, relv: 15});

}
/**
 * is defined
 * 
 * @param {type} game
 * @param {type} index
 * @returns {Boolean}
 */
function existField(game, index) {
    return game.firebaseMap.map[index] !== undefined && game.firebaseMap.map[index].sheet !== undefined;
}
/**
 * Is path on given field
 * 
 * @param {type} game
 * @param {type} index
 * @returns {Boolean}
 */
function isPathOnField(game, index) {
    return game.firebaseMap.map[index] !== undefined && game.firebaseMap.map[index].path !== undefined;
}
/**
 * Is at map field road
 * 
 * @param {type} game
 * @param {type} index
 * @returns {Boolean}
 */
function isRoad(game, index) {
    return (game.firebaseMap.map[index].type.substring(0, 2) === MapTypes.road.direct);
}
/**
 * is station
 * 
 * @param {type} game
 * @param {type} index
 * @returns {Boolean}
 */
function isStation(game, index) {
    return (game.firebaseMap.map[index].type.substring(0, 2) === MapTypes.building.stop.stop);
}
/**
 * is crossroad
 * 
 * @param {type} game
 * @param {type} index
 * @returns {Boolean}
 */
function isCrossRoad(game, index) {
    return game.firebaseMap.map[index].type === MapTypes.road.cross;
}
/**
 * free field
 * 
 * @param {type} game
 * @param {type} index
 * @returns {Boolean}
 */
function isFieldFree(game, index) {
    return (game.firebaseMap.map[index].type === MapTypes.nothing);
}

/**
 * SheetengineField
 * 
 * @param {type} basesheet
 * @param {type} centerp
 * @param {type} type
 * @param {type} path
 * @param {type} building
 * @param {type} owner
 * @returns {SheetengineField}
 */
function SheetengineField(basesheet, centerp, type, path, building, owner) {
//    console.log('SheetengineField instantiated');
    var self = this;
    this.buses = [];
    this.path = path;
    this.type = type;
    this.centerp = centerp;
    this.sheet = basesheet;
    this.building = building;
    this.owner = owner;
}

/**
 * SheetengineFieldFactory
 * 
 * @param {type} sheetengine
 * @returns {SheetengineFieldFactory}
 */
function SheetengineFieldFactory(sheetengine) {
    console.log('SheetengineFieldFactory instantiated');
    var self = this;
    this.color = '#5D7E36';//default field bgcolor
    this.sheetengine = sheetengine;
    this.map = null;
    this.vehicles = null;
    this.busFactory = null;
    this.buildingFactory = null;
    this.loadVehicle = function(pathes) {
        if (pathes !== undefined) {
            for (var path in pathes) {
                if (this.vehicles[path] === undefined) {
                    this.vehicles[path] = 'load';
                    this.busFactory.loadBus(path);
                }
            }
        }
    };
    /*
     * 
     *  @param {type} field
     *  @param {boolean} sheet
     *  @param {boolean} obj
     *  @param {boolean} all
     * 
     * @returns {void}
     */
    this.destroyedFieldService = function(field, sheet, building, all) {
        //alert('Override this function.');
        var index = indexFromXY(field.x, field.y);
        if (this.map[index] !== undefined) {
            if (this.map[index].sheet !== undefined && sheet) {
                this.map[index].sheet.destroy();
                //console.log('map sheet is removed ' + field.x + ":" + field.y);
            }
            if (building && this.map[index].building !== undefined && this.map[index].building.sheet !== undefined) {

                this.map[index].building.sheet.destroy();
                // console.log('map sheet is removed ' + field.x + ":" + field.y);
            }
            if (all) {
                delete this.map[index];
                //console.log('Field is removed ' + field.x + ":" + field.y);
            }


        }
    };

    /**
     * data from callback
     * 
     * @param {type} index
     * @param {type} data
     * @returns {undefined}
     */
    this.loadedFieldService = function(index, data) {
        //alert('Override this function.');
        if (this.map[index] !== undefined) {
            this.map[index].path = data.paths;
            if (this.map[index].type !== data.type) {
                this.setFieldCanvas(data.type, this.map[index].sheet);
                this.destroyedFieldService({x: data.x, y: data.y}, 0, true, 0);
                var building = this.setFieldBuilding(data.type, this.map[index].centerp);
                this.map[index].building = building;
                this.map[index].type = data.type;
                this.sheetengine.dirty = 1;
                console.log("dirty");
            }
            else {
                /*prepare to show people count*/
                if (data.city !== undefined) {
                    this.destroyedFieldService({x: data.x, y: data.y}, 0, true, 0);
                    this.map[index].building = this.buildingFactory.newTownName(this.map[index].centerp, data.city, data.people);
                }
            }
            if (data.paths !== undefined)
                this.loadVehicle(data.paths);
            if (data.owner_id !== undefined)
                this.map[index].owner = data.owner_id;
        }
        else {
            var sheetengineField = this.newField(data);
            this.map[index] = sheetengineField;
            if (data.paths !== undefined)
                this.loadVehicle(data.paths);
            if (data.city !== undefined)
                this.map[index].building = this.buildingFactory.newTownName(this.map[index].centerp, data.city, data.people);
            //        console.log('Field is load ' + data.x + ":" + data.y);
        }

    };
    /**
     * Callback for field
     * 
     * @param {type} snapshot
     * @returns {undefined}
     */
    this.loadFieldCallback = function(snapshot) {
        if (snapshot.val() === null || snapshot.name() === null) {
            console.log('Field can not be loaded.');
        } else {
            var data = snapshot.val();
            var xy = XYFromIndex(snapshot.name());
            data.x = xy.x;
            data.y = xy.y;
            self.loadedFieldService(snapshot.name(), data);
        }
    };
}
SheetengineFieldFactory.prototype = {
    constructor: SheetengineFieldFactory,
    setBusFactory: function(factory)
    {
        this.busFactory = factory;
    },
    setBuildingFactory: function(factory)
    {
        this.buildingFactory = factory;
    },
    setMap: function(map)
    {
        this.map = map;
        this.setBuildingFactory(new BuildingFactory(this.sheetengine, this.map));
    },
    setVehicles: function(vehicles)
    {
        this.vehicles = vehicles;
        this.setBusFactory(new BusFactory(this.sheetengine, this.map, this.vehicles));
    },
    newField: function(data)
    {
        var centerp = {x: (data.x) * this.sheetengine.scene.tilewidth, y: (data.y) * this.sheetengine.scene.tilewidth, z: 0};
        var orientation = {alphaD: 270, betaD: 0, gammaD: 0};
        var size = {w: this.sheetengine.scene.tilewidth + 2, h: this.sheetengine.scene.tilewidth + 2};
        var color = this.color;
        var basesheet = new this.sheetengine.BaseSheet(centerp, orientation, size);
        basesheet.color = color;
        this.setFieldCanvas(data.type, basesheet);
        var building = this.setFieldBuilding(data.type, centerp);
        result = new SheetengineField(basesheet, centerp, data.type, data.paths, building, data.owner_id);
        this.sheetengine.dirty = 1;
        return result;
    },
    setFieldCanvas: function(type, basesheet) {
        if (type === MapTypes.road.left)
            basesheet.img = imgRoad;
        else if (type === MapTypes.road.right)
            basesheet.img = imgRoad2;
        else if (type === MapTypes.road.cross)
            basesheet.img = imgCross;
        else if (type === MapTypes.building.stop.Warehouse || type === MapTypes.building.mainStation)
            basesheet.img = imgDepo;
        else if (type === 'W')
            basesheet.img = imgZas;
        else if (type === MapTypes.building.stop.sources.farm || type === MapTypes.building.stop.sources.wood)
            basesheet.img = imgFarm;
        else if (type === MapTypes.building.field.farm)
            basesheet.img = imgFarmField;
        else if (type === MapTypes.building.field.wood)
            basesheet.img = imgGrass;
        else
            basesheet.img = imgGrass;
    },
    setFieldBuilding: function(type, centerp) {
        if (type === MapTypes.building.cityHall) {
            return this.buildingFactory.newTownCenter(centerp);
        }
        else if (type === MapTypes.building.mainStation) {
            return this.buildingFactory.newDepo(centerp);
        }
        else if (type.slice(0, MapTypes.building.home.home.length) === MapTypes.building.home.home) {
            level = parseInt(type.slice(MapTypes.building.home.home.length, type.length));
            return this.buildingFactory.newHouse(centerp, level);
        }
        else if (type === MapTypes.building.home.skyScraper) {
            return this.buildingFactory.newHouse(centerp, 4);
        }
        else if (type === MapTypes.building.stop.Warehouse) {
            return this.buildingFactory.newWarehouse(centerp);
        }
        else if (type === MapTypes.building.stop.sources.farm) {
            return this.buildingFactory.newFarm(centerp);
        }
        else if (type === MapTypes.building.stop.sources.wood) {
            return this.buildingFactory.newWood(centerp);
        }
        else if (type === MapTypes.building.field.wood) {
            return this.buildingFactory.newTree(centerp);
        }
        else
            return;
    }
};
function FirebaseMap() {
    console.log('FirebaseMap instantiated');
    var self = this;
    this.fieldFactory = null;
    this.cached = 0;
    this.level = 2;
    this.map = [];
    this.vehicles = [];
}

FirebaseMap.prototype = {
    constructor: FirebaseMap,
    init: function(centerField)
    {
        var fieldsToLoad = this.getFieldsAround(centerField);
        this.loadFields(fieldsToLoad);
    },
    reInit: function(centerField)
    {
        var fieldsToLoad = this.getFieldsAround(centerField);
        this.destroyFields(fieldsToLoad);
        var fieldsToLoad = this.getFieldsAround(centerField);
        this.loadFields(fieldsToLoad);
    },
    setFieldFactory: function(factory)
    {
        this.fieldFactory = factory;
        this.fieldFactory.setMap(this.map);
        this.fieldFactory.setVehicles(this.vehicles);
    },
    setLevel: function(level)
    {
        this.level = level;
    },
    /**
     *  Get fields around a point - isometric rectangle 
     *  
     * @param {type {x,y}} centerField
     * @returns {Array of fields}
     */
    getFieldsAround: function(centerField)
    {
        var result = [];
        var minX = centerField.x - this.level;
        var maxX = centerField.x + this.level;
        var x = minX;
        var pom = 0;
        while (x <= maxX) {
            var miniY = centerField.y - pom;
            var maxiY = centerField.y + pom;
            for (u = miniY; u <= maxiY; u++) {
                //result.push({x: x, y: u}); - kvuli diff indexOf
                result.push(x + ":" + u);
            }
            if (x < centerField.x) {
                pom++;
            } else {
                pom--;
            }
            x++;
        }
        return result;
    },
    destroyField: function(field)
    {
        var index = indexFromXY(field.x, field.y);
        if (this.cached === 0) {
            //console.log("REM [x" + field.x + ",y" + field.y + "]");
            mapCityRef.child(index).off('value', this.loadFieldCallback);
            this.fieldFactory.destroyedFieldService(field, true, true, true);
        }
    },
    destroyFields: function(fields)
    {
        var len = fields.length;
        while (len--) {
            var coords = XYFromIndex(fields[len]);
            this.destroyField({x: coords.x, y: coords.y});
        }
    },
    loadField: function(field)
    {
        var index = indexFromXY(field.x, field.y);
        //console.log("INS [x" + field.x + ",y" + field.y + "]");
        if (this.cached === 0)
            mapCityRef.child(index).on('value', this.fieldFactory.loadFieldCallback);
        else if (this.map[index] === undefined)
            mapCityRef.child(index).on('value', this.fieldFactory.loadFieldCallback);
    },
    loadFields: function(fields)
    {
        var len = fields.length;
        while (len--) {
            var coords = XYFromIndex(fields[len]);
            this.loadField({x: coords.x, y: coords.y});
        }
    },
    changePosition: function(oldCenterField, newCenterField)
    {
        var oldF = this.getFieldsAround(oldCenterField);
        var newF = this.getFieldsAround(newCenterField);
        var fieldsToLoad = newF.diff(oldF);
        var fieldsToDestroy = oldF.diff(newF);
        this.loadFields(fieldsToLoad);
        this.destroyFields(fieldsToDestroy);
    }
}
;



