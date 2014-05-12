var busRef = new Firebase(FbRef.refD + 'vehicles');
var pathRef = new Firebase(FbRef.refD + 'paths');
var busPathRef = Firebase.util.join(
        {
            ref: busRef,
            keyMap: {startTime: 'startTime', direction: 'direction', type: 'type', p_size: 'p_size', cargo: 'cargo', color: 'color', name: 'name', speed: 'speed'}
        },
{
    ref: pathRef,
    keyMap: {path: 'path'}
}
);

function Bus(sheet, steps, maxsteps, direction, data, stop) {
    console.log('Bus instantiated');
    var self = this;
    this.sheet = sheet;
    this.steps = steps;
    this.maxsteps = maxsteps;
    this.direction = direction;
    this.data = data;
    this.stop = stop;
}

function BusFactory(sheetengine, map, vehicles) {
    console.log('BusFactory instantiated');
    var self = this;
    this.sheetengine = sheetengine;
    this.map = map;
    this.vehicles = vehicles;
    this.h = sheetengine.scene.tilewidth / 10;
    this.w = sheetengine.scene.tilewidth;
    this.timeInterval = 100;

    setInterval(function() {
        //run all buses on map

        for (var index in self.vehicles) {
            if (self.vehicles[index] !== undefined && self.vehicles[index].stop !== true && self.vehicles[index].sheet !== undefined && self.vehicles[index] !== 'load') {
                var vehicle = self.vehicles[index];
                var obj = vehicle.sheet;
                if (vehicle.rotate) {
                    obj.rotate({x: 0, y: 0, z: 1}, Math.PI / 2 / (vehicle.maxsteps / 10));
                    vehicle.steps += self.timeInterval;
                    if (vehicle.steps >= vehicle.maxsteps / 10) {
                        vehicle.steps = 0;
                        vehicle.rotate = false;
                    }
                } else {
                    obj.move(vehicle.direction);
                    vehicle.steps += self.timeInterval;
                    if (vehicle.steps >= vehicle.maxsteps) {
                        self.positionOfBus(vehicle.data, vehicle);
                        //vehicle.rotate = true;
                    }

                }
            }
        }
        //console.log("redraw");
        sheetengine.calc.calculateChangedSheets();
        sheetengine.drawing.drawScene();
        sheetengine2.calc.calculateChangedSheets();
        sheetengine2.drawing.drawScene();
    }, self.timeInterval);
    /**
     * 
     * @param {snapshot.val()} data 
     * data from fb
     * 
     * @returns {object for map field}
     */
    this.loadedBusService = function(index, data) {
        var sheetengineBus = this.newBus(data);
        if (self.vehicles[index] !== undefined && self.vehicles[index].sheet !== undefined && self.vehicles[index] !== 'load') {
            self.vehicles[index].sheet.destroy();
        }
        this.vehicles[index] = sheetengineBus;
    };
    this.loadBusCallback = function(snapshot) {
        if (snapshot.val() === null || snapshot.val().p_size === 0) {
            console.log('Bus cannot be loaded.');
            if (self.vehicles[snapshot.name()] !== undefined && self.vehicles[snapshot.name()].sheet !== undefined && self.vehicles[snapshot.name()] !== 'load') {
                self.vehicles[snapshot.name()].sheet.destroy();
            }
        } else {
            self.loadedBusService(snapshot.name(), snapshot.val());
        }
    };
}

BusFactory.prototype = {
    constructor: BusFactory,
    loadBus: function(busId)
    {
        console.log("INSBUS [" + busId + "]");
        busPathRef.child(busId).on('value', this.loadBusCallback);
    },
    positionOfBus: function(data, vehicle)
    {
        var obj = vehicle.sheet;
        var startTime = data.startTime;
        var pathTime = data.p_size * data.speed;
        var estimatedServerTimeMs = new Date().getTime() + offsetFirebase;
        var duration = estimatedServerTimeMs - startTime;//now - starttime 
        if (duration >= pathTime - data.speed) {
            //console.log("DOJEL BUS");
            vehicle.stop = true;
        }
        else {
            //console.log("Start");
            /*if (duration >= pathTime - data.speed) {
             console.log("DOJEL BUS");
             return null;
             }*/
            var field = Math.floor(duration / data.speed);
            var nextFld = 1;
            var steps = duration - field * data.speed;
            if (data.direction === -1)
            {
                field = data.p_size - field - 1;
                nextFld = -1;
            }

            var fieldXY = XYFromIndex(data.path[field]);
            var next = XYFromIndex(data.path[field + nextFld]);
            // console.log(startTime + "," + pathTime + "," + duration + "," + field + "," + steps);
            var direction = (next.y - fieldXY.y) * 90 + ((next.x - fieldXY.x) * (next.x - fieldXY.x) + (next.x - fieldXY.x)) * 90;

            var timeToPixels = this.sheetengine.scene.tilewidth / data.speed;
            var pixels = this.timeInterval * timeToPixels;
            var directionToMove = {x: (next.x - fieldXY.x) * pixels, y: (next.y - fieldXY.y) * pixels, z: 0};

            var rightSideOfRoad = 20;
            var positionOnField = steps * timeToPixels;

            obj.setPosition({x: (next.x - fieldXY.x) * positionOnField + (next.y - fieldXY.y) * (-rightSideOfRoad) + fieldXY.x * this.sheetengine.scene.tilewidth, y: (next.y - fieldXY.y) * positionOnField + (next.x - fieldXY.x) * rightSideOfRoad + fieldXY.y * this.sheetengine.scene.tilewidth, z: 0});
            obj.setOrientation({alphaD: 0, betaD: 0, gammaD: direction});
            vehicle.steps = steps;
            vehicle.direction = directionToMove;
        }
    },
    newBus: function(data)
    {

        var startTime = data.startTime;
        var pathTime = data.p_size * data.speed;
        var estimatedServerTimeMs = new Date().getTime() + offsetFirebase;
        //console.debug(offsetFirebase);
        // console.debug(estimatedServerTimeMs);
        //console.debug(estimatedServerTimeMs - startTime);
        var duration = estimatedServerTimeMs - startTime;//now - starttime 
        if (duration >= pathTime - data.speed) {
            //console.log("DOJEL BUS");
            return "load";
        }
        else {
            var field = Math.floor(duration / data.speed);
            var nextFld = 1;
            var steps = duration - field * data.speed;//time on field
            if (data.direction === -1)
            {
                field = data.p_size - field - 1;
                nextFld = -1;
            }
            var fieldXY = XYFromIndex(data.path[field]);
            var next = XYFromIndex(data.path[field + nextFld]);
            //console.log(startTime + "," + pathTime + "," + duration + "," + field + "," + steps);
            var direction = (next.y - fieldXY.y) * 90 + ((next.x - fieldXY.x) * (next.x - fieldXY.x) + (next.x - fieldXY.x)) * 90;

            var timeToPixels = this.sheetengine.scene.tilewidth / data.speed;
            var pixels = this.timeInterval * timeToPixels;
            var directionToMove = {x: (next.x - fieldXY.x) * pixels, y: (next.y - fieldXY.y) * pixels, z: 0};

            var rightSideOfRoad = 20;
            var positionOnField = steps * timeToPixels;
            var obj = this.defineVehicleObj({x: (next.x - fieldXY.x) * positionOnField + (next.y - fieldXY.y) * (-rightSideOfRoad) + fieldXY.x * this.sheetengine.scene.tilewidth, y: (next.y - fieldXY.y) * positionOnField + (next.x - fieldXY.x) * rightSideOfRoad + fieldXY.y * this.sheetengine.scene.tilewidth, z: 0}, direction, data.color, data.cargo);

            var result = new Bus(obj, steps, data.speed, directionToMove, data, false);
            return result;
        }
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




