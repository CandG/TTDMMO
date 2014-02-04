
function Building(sheet) {
    console.log('Building instantiated');
    var self = this;
    this.sheet = sheet;
}

function BuildingFactory(sheetengine, map) {
    console.log('BuildingFactory instantiated');
    var self = this;
    this.sheetengine = sheetengine;
    this.map = map;
    this.h = sheetengine.scene.tilewidth / 6;
    this.w = sheetengine.scene.tilewidth;
}

BuildingFactory.prototype = {
    constructor: BuildingFactory,
    newDepo: function(centerp)
    {
        var a = this.w / 1.2;
        var b = this.w / 3.2;
        var h = this.h;
        var windowColor = '#0000dd';
        var buildingColor = '#B8EFF7';

        var sideL = new sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx = sideL.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideR = new sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        ctx = sideR.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideB = new sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideB.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

        var sideF = new sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideF.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);


        var roof = new sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        ctx = roof.context;
        ctx.fillStyle = '#7F1717';
        ctx.fillRect(0, 0, b, a);

        obj = new this.sheetengine.SheetObject({x: centerp.x - sheetengine.scene.tilewidth / 2 + b / 2, y: centerp.y + sheetengine.scene.tilewidth / 2 - a / 2, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: 2 * (b + a), h: 4 * h, relu: (b + a), relv: (b + a)});

        var result = new Building(obj);
        return result;
    },
    newWarehouse: function(centerp)
    {
        var a = this.w / 1;
        var b = this.w / 1;
        var h = this.h;
        var windowColor = '#0000dd';
        var buildingColor = '#B8EFF7';

        var sideL = new sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx = sideL.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);


        var sideR = new sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        ctx = sideR.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //doors
        ctx.clearRect(b / 4.8, h / 6, b / 1.6, h);

        var sideB = new sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideB.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);


        var sideF = new sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideF.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);



        var roof = new sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        ctx = roof.context;
        ctx.fillStyle = '#414141';
        ctx.fillRect(0, 0, b, a);

        obj = new this.sheetengine.SheetObject({x: centerp.x - sheetengine.scene.tilewidth / 2 + b / 2, y: centerp.y + sheetengine.scene.tilewidth / 2 - a / 2, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: 2 * (b + a), h: 4 * h, relu: (b + a), relv: (b + a)});

        var result = new Building(obj);
        return result;
    },
    newFarm: function(centerp)
    {
        var a = this.w / 3.2;
        var b = this.w / 2.2;
        var h = this.h;
        var windowColor = '#0000dd';
        var buildingColor = '#994C00';

        var sideL = new sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx = sideL.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideR = new sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        ctx = sideR.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideB = new sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideB.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

        var sideF = new sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideF.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);


        var roof = new sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        ctx = roof.context;
        ctx.fillStyle = '#331900';
        ctx.fillRect(0, 0, b, a);

        obj = new this.sheetengine.SheetObject({x: centerp.x - sheetengine.scene.tilewidth / 2 + b / 2, y: centerp.y + sheetengine.scene.tilewidth / 2 - a / 2, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: 2 * (b + a), h: 4 * h, relu: (b + a), relv: (b + a)});

        var result = new Building(obj);
        return result;
    },
    newHouse: function(centerp, type)
    {
        var a = this.w / 3;
        var b = this.w / 3;
        var h = this.h;
        var windowColor = '#0000dd';
        var buildingColor = '#ffffff';

        var sideL = new sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx = sideL.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideR = new sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        ctx = sideR.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideB = new sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideB.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

        var sideF = new sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideF.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);


        var roof = new sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        ctx = roof.context;
        ctx.fillStyle = '#7F1717';
        ctx.fillRect(0, 0, b, a);

        obj = new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: 2 * (b + a), h: 4 * h, relu: (b + a), relv: (b + a)});

        var result = new Building(obj);
        return result;
    },
    newTownCenter: function(centerp, type)
    {
        var w = this.w / 2;
        var h = this.h;
        var bok = new sheetengine.Sheet({x: 0, y: -w / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: w, h: h});
        var ctx = bok.context;
        // head
        ctx.fillStyle = '#FFFFCC';
        ctx.fillRect(0, 0, w, h);
        //okna
        ctx.fillStyle = '#0000dd';
        ctx.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx.fillStyle = '#0000dd';
        ctx.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        var bok2 = new sheetengine.Sheet({x: 0, y: w / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: w, h: h});
        var ctx2 = bok2.context;
        // body
        ctx2.fillStyle = '#FFFFCC';
        ctx2.fillRect(0, 0, w, h);
        ctx2.fillStyle = '#0000dd';
        ctx2.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx2.fillStyle = '#0000dd';
        ctx2.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        var top = new sheetengine.Sheet({x: -w / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: w, h: h});
        var ctx4 = top.context;
        // head
        ctx4.fillStyle = '#FFFFCC';
        ctx4.fillRect(0, 0, w, h);

        var strecha = new sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: w, h: w});
        var ctx5 = strecha.context;
        // head
        ctx5.fillStyle = '#7F1717';
        ctx5.fillRect(0, 0, w, w);

        var bottom = new sheetengine.Sheet({x: w / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: w, h: h});
        var ctx3 = bottom.context;
        // head
        ctx3.fillStyle = '#FFFFCC';
        ctx3.fillRect(0, 0, w, h);
        ctx3.fillStyle = '#0000dd';
        ctx3.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx3.fillStyle = '#0000dd';
        ctx3.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        var h2 = h;
        w = this.w / 6;
        h = this.h * 1.5;
        var side = new sheetengine.Sheet({x: 0, y: -w / 2, z: h / 2 + h2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: w, h: h});
        var ctx = side.context;
        // head
        ctx.fillStyle = '#FFFFCC';
        ctx.fillRect(0, 0, w, h);
        //okna
        ctx.fillStyle = '#0000dd';
        ctx.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx.fillStyle = '#0000dd';
        ctx.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        var side2 = new sheetengine.Sheet({x: 0, y: w / 2, z: h / 2 + h2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: w, h: h});
        var ctx2 = side2.context;
        // body
        ctx2.fillStyle = '#FFFFCC';
        ctx2.fillRect(0, 0, w, h);
        ctx2.fillStyle = '#0000dd';
        ctx2.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx2.fillStyle = '#0000dd';
        ctx2.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        var side3 = new sheetengine.Sheet({x: -w / 2, y: 0, z: h / 2 + h2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: w, h: h});
        var ctx4 = side3.context;
        // head
        ctx4.fillStyle = '#FFFFCC';
        ctx4.fillRect(0, 0, w, h);

        var roof = new sheetengine.Sheet({x: 0, y: 0, z: h + h2}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: w, h: w});
        var ctx5 = roof.context;
        // head
        ctx5.fillStyle = '#7F1717';
        ctx5.fillRect(0, 0, w, w);

        var side4 = new sheetengine.Sheet({x: w / 2, y: 0, z: h / 2 + h2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: w, h: h});
        var ctx3 = side4.context;
        // head
        ctx3.fillStyle = '#FFFFCC';
        ctx3.fillRect(0, 0, w, h);
        ctx3.fillStyle = '#0000dd';
        ctx3.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx3.fillStyle = '#0000dd';
        ctx3.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        obj = new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [strecha, roof, side, side2, side3, side4, bok, bok2, top, bottom], {w: 2 * w, h: 4 * h, relu: w, relv: w});

        var result = new Building(obj);
        return result;
    }

};




