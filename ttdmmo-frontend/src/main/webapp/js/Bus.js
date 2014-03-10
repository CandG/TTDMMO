var busRef = new Firebase(FbRef.ref + 'buses');

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
    this.h = sheetengine.scene.tilewidth / 10;
    this.w = sheetengine.scene.tilewidth;

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
                        obj.sheet.move({x: (next.x - actu.x) * 2, y: (next.y - actu.y) * 2, z: 0});
                    }
                }
            }
        }
        sheetengine.calc.calculateChangedSheets();
        sheetengine.drawing.drawScene();
    }, 100);
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
        var posun = 20;
        var obj = this.defineVehicleObj({x: (next.y - data.y) * (-posun) + data.x * this.sheetengine.scene.tilewidth, y: (next.x - data.x) * posun + data.y * this.sheetengine.scene.tilewidth, z: 0}, direction, data.color, data.cargo);
        var result = new Bus(obj, data.direction, data.nextField);
        return result;
    },
    defineBusObj: function(centerp, direction, color) {
        var a = this.w / 12;//10
        var b = this.w / 4;//30
        var h = this.h;//12
        var windowColor = '#0000dd';
        var busColor = color;

        // user definition for animation with sheet motion
        var bok = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx = bok.context;
        // head
        ctx.fillStyle = busColor;
        ctx.fillRect(0, 0, b, h);
        //okna
        ctx.fillStyle = windowColor;
        ctx.fillRect(5, 2, 20, 4);

        var bok2 = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx2 = bok2.context;
        // body
        ctx2.fillStyle = busColor;
        ctx2.fillRect(0, 0, b, h);
        ctx2.fillStyle = windowColor;
        ctx2.fillRect(5, 2, 20, 4);

        var top = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        var ctx4 = top.context;
        // head
        ctx4.fillStyle = busColor;
        ctx4.fillRect(0, 0, a, h);
        ctx4.fillStyle = windowColor;
        ctx4.fillRect(1, 2, 8, 6);

        var strecha = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        var ctx5 = strecha.context;
        // head
        ctx5.fillStyle = busColor;
        ctx5.fillRect(0, 0, b, h);

        var bottom = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        var ctx3 = bottom.context;
        // head
        ctx3.fillStyle = busColor;
        ctx3.fillRect(0, 0, a, h);
        // define user object
        return new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: direction}, [strecha, bok, bok2, top, bottom], {w: 60, h: 60, relu: 30, relv: 30});
    },
    defineVehicleObj: function(centerp, direction, color, cargo) {
        var a = this.w / 12;//10
        var b = this.w / 4;//30
        var h = this.h;//12
        var windowColor = '#0000dd';
        var busColor = color;
        var cabinColor = "#ffffff";
        var cargoColor = "#603311";
        var tyreColor = "#010101";

        // user definition for animation with sheet motion
        var bok = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx = bok.context;
        // head
        ctx.fillStyle = busColor;
        ctx.fillRect(b / 3, h / 2, 2 * b / 3, h / 2);
        ctx.fillStyle = cargoColor;
        ctx.fillRect(b / 3, 0, 2 * b / 3, h / 2);
        ctx.fillStyle = tyreColor;
        ctx.fillRect(2 * b / 3 - 1, h - 3, 3, 3);
        ctx.fillStyle = cabinColor;
        ctx.fillRect(0, 0, b / 3, h);
        ctx.fillStyle = tyreColor;
        ctx.fillRect(b / 6 - 1, h - 3, 3, 3);
        //okna
        ctx.fillStyle = windowColor;
        ctx.fillRect(2, 2, 5, 4);
        if (cargo === 0)
            ctx.clearRect(b / 3, 0, 2 * b / 3, h / 2);

        var bok2 = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx2 = bok2.context;
        // head
        ctx2.fillStyle = busColor;
        ctx2.fillRect(b / 3, h / 2, 2 * b / 3, h);
        ctx2.fillStyle = cargoColor;
        ctx2.fillRect(b / 3, 0, 2 * b / 3, h / 2);
        ctx2.fillStyle = tyreColor;
        ctx2.fillRect(2 * b / 3 - 1, h - 3, 3, 3);
        ctx2.fillStyle = cabinColor;
        ctx2.fillRect(0, 0, b / 3, h);
        ctx2.fillStyle = tyreColor;
        ctx2.fillRect(b / 6 - 1, h - 3, 3, 3);

        ctx2.fillStyle = windowColor;
        ctx2.fillRect(2, 2, 5, 4);
        if (cargo === 0)
            ctx2.clearRect(b / 3, 0, 2 * b / 3, h / 2);

        var top = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        var ctx4 = top.context;
        // head
        ctx4.fillStyle = cabinColor;
        ctx4.fillRect(0, 0, a, h);
        ctx4.fillStyle = windowColor;
        ctx4.fillRect(1, 2, 8, 6);

        var top2 = new this.sheetengine.Sheet({x: -b / 2 + b / 3, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx4 = top2.context;
        // head
        ctx4.fillStyle = cabinColor;
        ctx4.fillRect(0, 0, a, h);

        var strecha = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        var ctx5 = strecha.context;
        // head
        ctx5.fillStyle = cabinColor;
        ctx5.fillRect(0, 0, b / 3, a);
        ctx5.fillStyle = cargoColor;
        ctx5.fillRect(b / 3, 0, 2 * b / 3, a);
        if (cargo === 0)
            ctx5.clearRect(b / 3, 0, 2 * b / 3, a);

        var back = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        var ctx3 = back.context;
        // head
        ctx3.fillStyle = busColor;
        ctx3.fillRect(0, h / 2, a, h / 2);

        ctx3.fillStyle = cargoColor;
        ctx3.fillRect(0, 0, a, h / 2);

        if (cargo === 0)
            ctx3.clearRect(0, 0, a, h / 2);

        var bottom = new this.sheetengine.Sheet({x: 0, y: 0, z: 0}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        ctx3 = bottom.context;
        // head
        ctx3.fillStyle = busColor;
        ctx3.fillRect(0, 0, b, a);
        // define user object
        return new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: direction}, [back, strecha, bok, bok2, top, top2, bottom], {w: 60, h: 60, relu: 30, relv: 30});
    }
};




