var cityRef = new Firebase(FbRef.refD + 'cities');


function SheetengineCity(basesheet, centerp, building, owner) {
//    console.log('SheetengineField instantiated');
    var self = this;
    this.buses = [];
    this.centerp = centerp;
    this.sheet = basesheet;
    this.building = building;
    this.owner = owner;
}


function SheetengineCityFactory(sheetengine, size) {
    console.log('SheetengineCityFactory instantiated');
    var self = this;
    this.color = '#5D7E36';
    this.sheetengine = sheetengine;
    this.map = null;
    this.size = size;
    this.buildingFactory = null;
    this.minePosition = null;
    /*
     * 
     *  @param {type} field
     *  @param {boolean} sheet
     *  @param {boolean} obj
     *  @param {boolean} all
     * 
     * @returns {void}
     */
    this.destroyedFieldService = function(field, sheet, all) {
        //alert('Override this function.');
        var index = indexFromXY(field.x, field.y);
        if (this.map[index] !== undefined) {
            if (this.map[index].sheet !== undefined && sheet) {
                this.map[index].sheet.destroy();
                console.log('map sheet is removed ' + field.x + ":" + field.y);
            }
            if (this.map[index].building !== undefined && this.map[index].building.sheet !== undefined) {

                this.map[index].building.sheet.destroy();
                console.log('map sheet is removed ' + field.x + ":" + field.y);
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
    this.loadedFieldService = function(index, data) {
        //alert('Override this function.');
        if (this.map[index] === undefined) {
            var sheetengineField = this.newField(data);
            this.map[index] = sheetengineField;
            this.map[index].building = this.buildingFactory.newCityName(this.map[index].centerp, data.name, data.people, this.minePosition.x === data.x && this.minePosition.y === data.y);
            console.debug(data);
        }

    };
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
SheetengineCityFactory.prototype = {
    constructor: SheetengineCityFactory,
    setMap: function(map)
    {
        this.map = map;
        this.setBuildingFactory(new BuildingFactory(this.sheetengine, this.map));
    },
    setBuildingFactory: function(factory)
    {
        this.buildingFactory = factory;
    },
    setMinePosition: function(minePosition)
    {
        this.minePosition = minePosition;
    },
    newField: function(data)
    {
        var centerp = {x: (data.x) * this.size, y: (data.y) * this.size, z: 0};
        var orientation = {alphaD: 270, betaD: 0, gammaD: 0};
        var size = {w: this.sheetengine.scene.tilewidth + 2, h: this.sheetengine.scene.tilewidth + 2};
        var color = this.color;
        var basesheet = new this.sheetengine.BaseSheet(centerp, orientation, size);
        basesheet.color = color;
        basesheet.img = imgCity;
        result = new SheetengineField(basesheet, centerp, null, data.owner_id);
        this.sheetengine.dirty = 1;
        return result;
    }
};


function CityMap() {
    console.log('CityMap instantiated');
    var self = this;
    this.fieldFactory = null;
    this.cached = 0;
    this.padding = 20;
    this.level = 2;
    this.map = [];
}

CityMap.prototype = {
    constructor: CityMap,
    init: function(centerField)
    {
        var fieldsToLoad = this.getFieldsAround(centerField);
        this.fieldFactory.setMinePosition(centerField);
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
    },
    setLevel: function(level)
    {
        this.level = level;
    },
    setPadding: function(padding)
    {
        this.padding = padding;
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
        var minX = centerField.x / this.padding - this.level;
        var maxX = centerField.x / this.padding + this.level;
        var x = minX;
        var pom = 0;
        while (x <= maxX) {
            var miniY = centerField.y / this.padding - pom;
            var maxiY = centerField.y / this.padding + pom;
            for (u = miniY; u <= maxiY; u++) {
                //result.push({x: x, y: u}); - kvuli diff indexOf
                result.push((x * this.padding) + ":" + (u * this.padding));
            }
            if (x < centerField.x / this.padding) {
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
            console.log("REMCity [x" + field.x + ",y" + field.y + "]");
            //cityRef.child(index).off('value', this.loadFieldCallback);
            this.fieldFactory.destroyedFieldService(field, true, true);
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
        console.log("INSCity [x" + field.x + ",y" + field.y + "]");
        if (this.cached === 0)
            cityRef.child(index).once('value', this.fieldFactory.loadFieldCallback);
        else if (this.map[index] === undefined)
            cityRef.child(index).once('value', this.fieldFactory.loadFieldCallback);
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



