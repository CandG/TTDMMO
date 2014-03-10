var mapRef = new Firebase(FbRef.ref + 'map');
var cityRef = new Firebase(FbRef.ref + 'cities');
var mapCityRef = Firebase.util.join(
        {
            ref: mapRef,
            keyMap: {x: 'x', y: 'y', obj: 'obj', type: 'type'}
        },
{
    ref: cityRef,
    keyMap: {name: 'city', people: 'people'}
}
);


Array.prototype.diff = function(a) {
    return this.filter(function(i) {
        return !(a.indexOf(i) > -1);
    });
};
/*
 * 
 * @param {type} x
 * @param {type} y
 * @returns {string} separate x and y value 
 * 
 */
function indexFromXY(x, y) {
    return x + ":" + y;
}
function XYPlusIndex(x, y, index) {
    var coords = index.split(":");
    return indexFromXY(x + parseInt(coords[0]), y + parseInt(coords[1]));
}
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
    /*  var a = 120 / 12;
     var b = 120 / 4;
     var h = 12;
     var windowColor = '#0000dd';
     var busColor = '#B8EFF7';
     
     // user definition for animation with sheet motion
     var bok = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
     var ctx = bok.context;
     // head
     ctx.fillStyle = '#3d1e14';
     ctx.fillRect(0, 0, 30, 14);
     //okna
     ctx.fillStyle = '#0000dd';
     ctx.fillRect(5, 2, 20, 4);
     
     var bok2 = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: 30, h: h});
     var ctx2 = bok2.context;
     // body
     ctx2.fillStyle = '#3d1e14';
     ctx2.fillRect(0, 0, 30, 14);
     ctx2.fillStyle = '#0000dd';
     ctx2.fillRect(5, 2, 20, 4);
     
     var top = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
     var ctx4 = top.context;
     // head
     ctx4.fillStyle = '#3d1e14';
     ctx4.fillRect(0, 0, 10, 14);
     ctx4.fillStyle = '#0000dd';
     ctx4.fillRect(1, 2, 5, 5);
     
     var strecha = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
     var ctx5 = strecha.context;
     // head
     ctx5.fillStyle = '#3d1e14';
     ctx5.fillRect(0, 0, 30, 14);
     
     var bottom = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
     var ctx3 = bottom.context;
     // head
     ctx3.fillStyle = '#3d1e14';
     ctx3.fillRect(0, 0, 10, 14);
     // define user object
     sheetengine.drawObjectContour = true;
     return new sheetengine.SheetObject({x: xy.x * sheetengine.scene.tilewidth, y: xy.y * sheetengine.scene.tilewidth, z: 0}, {alphaD: 0, betaD: 0, gammaD: 0}, [strecha, bok, bok2, top, bottom], {w: 60, h: 60, relu: 30, relv: 30});
     */
}
function existField(game, index) {
    return game.firebaseMap.map[index] !== undefined && game.firebaseMap.map[index].sheet !== undefined;
}
function isRoad(game, index) {
    return (game.firebaseMap.map[index].type === 'A' || game.firebaseMap.map[index].type === 'A2');
}
function isCrossRoad(game, index) {
    return game.firebaseMap.map[index].type === 'A3';
}
function isFieldFree(game, index) {
    return (game.firebaseMap.map[index].type === 'B');
}
/**
 * 
 * @param {int} x 
 * number of x points (-x,x)
 * @param {int} y
 * number of y points (-y,y)
 * 
 * @returns {void}
 */
function generateMap(x, y) {
    for (i = -x; i <= x; i++) {
        for (z = -y; z <= y; z++) {
            if (z === 0)
                pom = 'A';
            else
                pom = 'B';
            mapRef.child(indexFromXY(i, z)).set({x: (i), y: (z), type: pom});
        }
    }
}

function createCity(x, y) {
    //  for (i = -x; i <= x; i=) {
    //    for (z = -y; z <= y; z++) {

    q = x + 0;
    w = y + 0;
    mapRef.child(indexFromXY(q, w)).set({x: (q), y: (w), type: 'A3'});

    q = x - 1;
    w = y + 0;
    mapRef.child(indexFromXY(q, w)).set({x: (q), y: (w), type: 'A'});
    q = x + 1;
    w = y + 0;
    mapRef.child(indexFromXY(q, w)).set({x: (q), y: (w), type: 'A'});

    q = x + 0;
    w = y + 1;
    mapRef.child(indexFromXY(q, w)).set({x: (q), y: (w), type: 'B1'});
    q = x + 0;
    w = y - 1;
    mapRef.child(indexFromXY(q, w)).set({x: (q), y: (w), type: 'B3'});
    q = x - 1;
    w = y - 1;
    mapRef.child(indexFromXY(q, w)).set({x: (q), y: (w), type: 'B0'});
    q = x + 3;
    w = y + 2;
    mapRef.child(indexFromXY(q, w)).set({x: (q), y: (w), type: 'BF'});
    q = x - 1;
    w = y + 1;
    mapRef.child(indexFromXY(q, w)).set({x: (q), y: (w), type: 'B2'});

}
function createCities(n) {
    fields = 10;
    city = [["Kvikalkov", "Mordor", "Rohlenka", "Kraj", "Gondor"],
        ["Brno", "Praha", "London", "Zagreb", "Tvarozna Lhota"],
        ["Madrid", "Berlin", "Bern", "Sochi", "Vancouver"],
        ["Centuria", "Sandwich", "Machackala", "Lexibook", "Patagonie"],
        ["Reckovice", "Ceska", "Sydney", "Koln", "Sanghai"]];
    padding = fields / n;
    margin = padding / 2;
    for (i = 0; i < n; i++) {
        for (z = 0; z < n; z++) {
            x = (margin + padding * i) - fields / 2;
            y = (margin + padding * z) - fields / 2;
            //createCity(x, y);
            cityRef.child(indexFromXY(x, y)).set({people: 200, xy: indexFromXY(x, y), name: city[i][z]});
        }
    }
}


function SheetengineField(basesheet, centerp, type, obj, building) {
    console.log('SheetengineField instantiated');
    var self = this;
    this.buses = [];
    this.obj = obj;
    this.type = type;
    this.centerp = centerp;
    this.sheet = basesheet;
    this.building = building;
}


function SheetengineFieldFactory(sheetengine) {
    console.log('SheetengineFieldFactory instantiated');
    var self = this;
    this.color = '#5D7E36';
    this.sheetengine = sheetengine;
    this.map = null;
    this.busFactory = null;
    this.buildingFactory = null;

    this.loadObjects = function(obj) {
        if (obj.buses !== undefined) {
            console.debug(obj.buses);
            for (var bus in obj.buses) {
                //console.debug(bus);
                this.busFactory.loadBus(bus);
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
    this.destroyedFieldService = function(field, sheet, obj, all) {
        //alert('Override this function.');
        var index = indexFromXY(field.x, field.y);
        if (this.map[index] !== undefined) {
            if (this.map[index].sheet !== undefined && sheet) {
                this.map[index].sheet.destroy();
                console.log('map sheet is removed ' + field.x + ":" + field.y);
            }
            if (obj) {
                var len = this.map[index].buses.length;
                while (len--) {
                    this.map[index].buses[len].sheet.destroy();
                    this.map[index].buses.splice(len, 1)
                }
                console.log('map obj is removed ' + field.x + ":" + field.y);
            }
            if (all) {
                delete this.map[index];
                console.log('Field is removed ' + field.x + ":" + field.y);
            }


        }
    };
    /**
     * 
     * @param {snapshot.val()} data 
     * data from fb
     * 
     * @returns {void}
     */
    this.loadedFieldService = function(data) {
        //alert('Override this function.');
        var index = indexFromXY(data.x, data.y);
        if (this.map[index] !== undefined) {
            if (this.map[index].type !== data.type) {
                this.setFieldCanvas(data.type, this.map[index].sheet);
                this.map[index].type = data.type;
                this.sheetengine.dirty = 1;
                console.log("dirty");
            }
            else {
                this.destroyedFieldService({x: data.x, y: data.y}, 0, true, 0);
                if (data.obj !== undefined)
                    this.loadObjects(data.obj);
                if (data.city !== undefined)
                    this.map[index].building = this.buildingFactory.newTownName(this.map[index].centerp, data.city, data.people);
            }
        }
        else {
            var sheetengineField = this.newField(data);
            this.map[index] = sheetengineField;
            if (data.obj !== undefined)
                this.loadObjects(data.obj);
            if (data.city !== undefined)
                this.map[index].building = this.buildingFactory.newTownName(this.map[index].centerp, data.city, data.people);

            console.log('Field is load ' + data.x + ":" + data.y);
        }

    };
    this.loadFieldCallback = function(snapshot) {
        if (snapshot.val() === null) {
            console.log('Field can be loaded.');
        } else {
            self.loadedFieldService(snapshot.val());
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
        this.setBusFactory(new BusFactory(this.sheetengine, this.map));
        this.setBuildingFactory(new BuildingFactory(this.sheetengine, this.map));
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
        result = new SheetengineField(basesheet, centerp, data.type, data.obj, building);
        this.sheetengine.dirty = 1;
        return result;
    },
    setFieldCanvas: function(type, basesheet) {
        if (type == 'A')
            basesheet.img = imgRoad;
        else if (type == 'A2')
            basesheet.img = imgRoad2;
        else if (type == 'A3')
            basesheet.img = imgCross;
        else if (type == 'B1' || type == 'B3')
            basesheet.img = imgDepo;
        else if (type == 'W')
            basesheet.img = imgZas;
        else if (type == 'BF')
            basesheet.img = imgFarm;
        else
            basesheet.img = imgGrass;
    },
    setFieldBuilding: function(type, centerp) {
        if (type == 'B0') {
            return this.buildingFactory.newTownCenter(centerp);
        }
        else if (type == 'B1') {
            return this.buildingFactory.newDepo(centerp);
        }
        else if (type == 'B2') {
            return this.buildingFactory.newHouse(centerp);
        }
        else if (type == 'B3') {
            return this.buildingFactory.newWarehouse(centerp);
        }
        else if (type == 'BF') {
            return this.buildingFactory.newFarm(centerp);
        }
        else
            return null;
    }
};


function FirebaseMap() {
    console.log('FirebaseMap instantiated');
    var self = this;
    this.fieldFactory = null;
    this.cached = 0;
    this.level = 2;
    this.map = [];
}

FirebaseMap.prototype = {
    constructor: FirebaseMap,
    init: function(centerField)
    {
        var fieldsToLoad = this.getFieldsAround(centerField);
        this.loadFields(fieldsToLoad);

    },
    setFieldFactory: function(factory)
    {
        this.fieldFactory = factory;
        this.fieldFactory.setMap(this.map);
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
            console.log("REM [x" + field.x + ",y" + field.y + "]");
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
        console.log("INS [x" + field.x + ",y" + field.y + "]");
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



