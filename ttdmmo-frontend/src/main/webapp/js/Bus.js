var busRef = new Firebase('https://ttdmmo1.firebaseio-demo.com/buses');

function Bus(sheet, direction, nextField) {
    console.log('Bus instantiated');
    var self = this;
    this.direction = direction;
    this.sheet = sheet;
    this.nextField = nextField;
}

function BusFactory(sheetengine, map) {
    console.log('BusFactory instantiated');
    var self = this;
    this.sheetengine = sheetengine;
    this.map = map;
    setInterval(function() {
        //run all buses on map

        for (var index in self.map) {
            if (self.map[index] !== undefined && self.map[index].buses !== undefined) {
                var len = self.map[index].buses.length;
                while (len--) {
                    if (self.map[index].buses[len] !== undefined && self.map[index].buses[len].sheet !== undefined) {
                        obj = self.map[index].buses[len];
                        next = XYFromIndex(obj.nextField);
                        actu = XYFromIndex(index);
                        //    obj.sheet.rotate({x: 0, y: 0, z: 1}, Math.PI / 2 / maxsteps);
                        obj.sheet.move({x: (next.x - actu.x) * 3, y: (next.y - actu.y) * 3, z: 0});
                    }
                }
            }
        }
        sheetengine.calc.calculateChangedSheets();
        sheetengine.drawing.drawScene();
    }, 80);
    /**
     * 
     * @param {snapshot.val()} data 
     * data from fb
     * 
     * @returns {object for map field}
     */
    this.loadedBusService = function(data) {
        var index = indexFromXY(data.x, data.y);
        //alert('Override this function.');
        //  if (this.buses[data.x + ":" + data.y] !== undefined)
        //      this.destroyedFieldService({x: data.x, y: data.y});
        var sheetengineBus = this.newBus(data);
        if (this.map[index] !== undefined) {
            this.map[index].buses.push(sheetengineBus);
        }
        else {
            sheetengineBus.destroy();
        }
        console.log('Bus is load ' + data.x + ":" + data.y);
    };
    this.loadBusCallback = function(snapshot) {
        if (snapshot.val() === null) {
            console.log('Bus cannot be loaded.');
        } else {
            self.loadedBusService(snapshot.val());
        }
    };
}

BusFactory.prototype = {
    constructor: BusFactory,
    loadBus: function(busId)
    {
        console.log("INSBUS [" + busId + "]");
        busRef.child(busId).once('value', this.loadBusCallback);
    },
    newBus: function(data)
    {
        var next = XYFromIndex(data.nextField);
        var direction = (next.y - data.y) * 90 + ((next.x - data.x) * (next.x - data.x) + (next.x - data.x)) * 90;
        /* if (next.x - data.x == 1) {
         direction = 0;
         }
         else if (next.y - data.y == 1) {
         direction = 90;
         }*/

        var obj = this.defineBusObj({x: data.x * this.sheetengine.scene.tilewidth, y: data.y * this.sheetengine.scene.tilewidth, z: 0}, direction);
        var result = new Bus(obj, data.direction, data.nextField);
        return result;
    },
    defineBusObj: function(centerp, direction) {
        // user definition for animation with sheet motion
        var bok = new this.sheetengine.Sheet({x: 0, y: 0, z: 0}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: 30, h: 14});
        var ctx = bok.context;
        // head
        ctx.fillStyle = '#3d1e14';
        ctx.fillRect(0, 0, 30, 14);
        //okna
        ctx.fillStyle = '#0000dd';
        ctx.fillRect(5, 2, 20, 4);

        var bok2 = new this.sheetengine.Sheet({x: 0, y: 10, z: 0}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: 30, h: 14});
        var ctx2 = bok2.context;
        // body
        ctx2.fillStyle = '#3d1e14';
        ctx2.fillRect(0, 0, 30, 14);
        ctx2.fillStyle = '#0000dd';
        ctx2.fillRect(5, 2, 20, 4);

        var top = new this.sheetengine.Sheet({x: -15, y: 5, z: 0}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: 10, h: 14});
        var ctx4 = top.context;
        // head
        ctx4.fillStyle = '#3d1e14';
        ctx4.fillRect(0, 0, 10, 14);
        ctx4.fillStyle = '#0000dd';
        ctx4.fillRect(1, 2, 5, 5);

        var strecha = new this.sheetengine.Sheet({x: 0, y: 5, z: 7}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: 30, h: 10});
        var ctx5 = strecha.context;
        // head
        ctx5.fillStyle = '#3d1e14';
        ctx5.fillRect(0, 0, 30, 14);

        var bottom = new this.sheetengine.Sheet({x: 15, y: 5, z: 0}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: 10, h: 14});
        var ctx3 = bottom.context;
        // head
        ctx3.fillStyle = '#3d1e14';
        ctx3.fillRect(0, 0, 10, 14);
        // define user object
        return new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: direction}, [strecha, bok, bok2, top, bottom], {w: 50, h: 50, relu: 30, relv: 30});
    }

};




