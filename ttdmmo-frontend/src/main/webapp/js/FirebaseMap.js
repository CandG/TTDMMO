var geoRef = new Firebase('https://ttdmmo.firebaseio-demo.com/map');

Array.prototype.diff = function(a) {
    return this.filter(function(i) {
        return !(a.indexOf(i) > -1);
    });
};


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
            geoRef.child(i).child(z).set({x: (i), y: (z), type: pom});
        }
    }
}

function SheetengineFieldFactory(sheetengine) {
    console.log('SheetengineFieldFactory instantiated');
    var self = this;
    this.tileWidth = 100;
    this.color = '#5D7E36';
    this.sheetengine = sheetengine;
}
SheetengineFieldFactory.prototype = {
    constructor: SheetengineFieldFactory,
    setTileWidth: function(tileWidth)
    {
        this.tileWidth = tileWidth;
    },
    newField: function(data)
    {
        var centerp = {x: (data.x) * this.tileWidth,
            y: (data.y) * this.tileWidth, z: 0};

        var orientation = {alphaD: 90, betaD: 0, gammaD: 0};
        var size = {w: this.tileWidth, h: this.tileWidth};
        var color = this.color;
        var basesheet = new this.sheetengine.BaseSheet(centerp, orientation, size);
        basesheet.color = color;
        this.setFieldCanvas(data.type, basesheet);
        result = {
            centerp: centerp,
            sheet: basesheet
        };
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
    this.level = 2;
    this.map = [];

    this.destroyedFieldService = function(field) {
        //alert('Override this function.');
        if (this.map[field.x + ":" + field.y] !== undefined) {
            this.map[field.x + ":" + field.y].sheet.destroy();
            delete this.map[field.x + ":" + field.y];
            console.log('Field is removed ' + field.x + ":" + field.y);
        }
    };
    /**
     * 
     * @param {snapshot.val()} data 
     * data from fb
     * 
     * @returns {object for map field}
     */
    this.loadedFieldService = function(data) {
        //alert('Override this function.');
        if (this.map[data.x + ":" + data.y] !== undefined)
            this.destroyedFieldService({x: data.x, y: data.y});
        var sheetengineField = this.fieldFactory.newField(data);
        this.map[data.x + ":" + data.y] = sheetengineField;
        console.log('Field is load ' + data.x + ":" + data.y);
    };
    this.loadFieldCallback = function(snapshot) {
        if (snapshot.val() === null) {
            console.log('Field can be loaded.');
        } else {
            self.loadedFieldService(snapshot.val());
        }
    };
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
    },
    setLevel: function(level)
    {
        this.level = level;
    },
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
        console.log("REM [x" + field.x + ",y" + field.y + "]");
        geoRef.child(field.x).child(field.y).off('value', this.loadFieldCallback);
        this.destroyedFieldService(field);
    },
    destroyFields: function(fields)
    {
        var len = fields.length;
        while (len--) {
            var coords = fields[len].split(":");
            this.destroyField({x: coords[0], y: coords[1]});
        }
    },
    loadField: function(field)
    {
        console.log("INS [x" + field.x + ",y" + field.y + "]");
        geoRef.child(field.x).child(field.y).on('value', this.loadFieldCallback);
    },
    loadFields: function(fields)
    {
        var len = fields.length;
        while (len--) {
            var coords = fields[len].split(":");
            this.loadField({x: coords[0], y: coords[1]});
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
};



