var mapRef = new Firebase('https://ttdmmo1.firebaseio-demo.com/map');

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
function XYFromIndex(index) {
    var coords = index.split(":");
    return {x: coords[0], y: coords[1]};
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

function SheetengineField(basesheet, centerp, type, obj) {
    console.log('SheetengineField instantiated');
    var self = this;
    this.buses = [];
    this.obj = obj;
    this.type = type;
    this.centerp = centerp;
    this.sheet = basesheet;
}


function SheetengineFieldFactory(sheetengine) {
    console.log('SheetengineFieldFactory instantiated');
    var self = this;
    this.color = '#5D7E36';
    this.sheetengine = sheetengine;
    this.map = null;
    this.busFactory = null;

    this.loadObjects = function(obj) {
        if (obj.buses !== undefined) {
            for (var bus in obj.buses) {
                //var attr = object[index];
                console.log('calback bus' + bus);
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
            }
            else {
                this.destroyedFieldService({x: data.x, y: data.y}, 0, true, 0);
                if (data.obj !== undefined)
                    this.loadObjects(data.obj);
            }
        }
        else {
            var sheetengineField = this.newField(data);
            this.map[index] = sheetengineField;
            if (data.obj !== undefined)
                this.loadObjects(data.obj);
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
    setMap: function(map)
    {
        this.map = map;
        this.setBusFactory(new BusFactory(this.sheetengine, this.map))
    },
    newField: function(data)
    {
        var centerp = {x: (data.x) * this.sheetengine.scene.tilewidth, y: (data.y) * this.sheetengine.scene.tilewidth, z: 0};
        var orientation = {alphaD: 90, betaD: 0, gammaD: 0};
        var size = {w: this.sheetengine.scene.tilewidth, h: this.sheetengine.scene.tilewidth};
        var color = this.color;
        var basesheet = new this.sheetengine.BaseSheet(centerp, orientation, size);
        basesheet.color = color;
        this.setFieldCanvas(data.type, basesheet);
        result = new SheetengineField(basesheet, centerp, data.type, data.obj);
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
        else if (type == 'Q')
            basesheet.img = imgDepo;
        else if (type == 'W')
            basesheet.img = imgZas;
        else
            basesheet.img = imgGrass;
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
            mapRef.child(index).off('value', this.loadFieldCallback);
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
            mapRef.child(index).on('value', this.fieldFactory.loadFieldCallback);
        else if (this.map[index] === undefined)
            mapRef.child(index).on('value', this.fieldFactory.loadFieldCallback);
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



