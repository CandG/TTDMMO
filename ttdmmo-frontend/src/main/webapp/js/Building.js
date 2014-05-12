
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

        var sideL = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx = sideL.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideR = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        ctx = sideR.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideB = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideB.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

        var sideF = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideF.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);


        var roof = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        ctx = roof.context;
        ctx.fillStyle = '#7F1717';
        ctx.fillRect(0, 0, b, a);

        obj = new this.sheetengine.SheetObject({x: centerp.x - this.sheetengine.scene.tilewidth / 2 + b / 2, y: centerp.y + this.sheetengine.scene.tilewidth / 2 - a / 2, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: (b + a), h: (b + a), relu: (b + a) / 2, relv: (b + a) / 2});

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

        var sideL = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx = sideL.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);


        var sideR = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        ctx = sideR.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //doors
        ctx.clearRect(b / 4.8, h / 6, b / 1.6, h);

        var sideB = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideB.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);


        var sideF = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideF.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);



        var roof = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        ctx = roof.context;
        ctx.fillStyle = '#414141';
        ctx.fillRect(0, 0, b, a);

        obj = new this.sheetengine.SheetObject({x: centerp.x - this.sheetengine.scene.tilewidth / 2 + b / 2, y: centerp.y + this.sheetengine.scene.tilewidth / 2 - a / 2, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: (b + a), h: (b + a), relu: (b + a) / 2, relv: (b + a) / 2});

        var result = new Building(obj);
        return result;
    },
    newFarm: function(centerp)
    {
        var a = this.w / 1;
        var b = this.w / 4.2;
        var h = this.h;
        var windowColor = '#0000dd';
        var buildingColor = '#994C00';

        var sideL = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx = sideL.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideR = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        ctx = sideR.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideB = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideB.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

        var sideF = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideF.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);


        var roof = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        ctx = roof.context;
        ctx.fillStyle = '#331900';
        ctx.fillRect(0, 0, b, a);

        obj = new this.sheetengine.SheetObject({x: centerp.x - this.sheetengine.scene.tilewidth / 2 + b / 2, y: centerp.y - this.sheetengine.scene.tilewidth / 2 + a / 2, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: (b + a), h: (b + a), relu: (b + a) / 2, relv: (b + a) / 2});

        var result = new Building(obj);
        return result;
    },
    newWood: function(centerp)
    {
        var a = this.w / 2.5;
        var b = this.w / 4.2;
        var h = this.h;
        var windowColor = '#0000dd';
        var buildingColor = '#867F72';

        var sideL = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        var ctx = sideL.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideR = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
        ctx = sideR.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, b, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

        var sideB = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideB.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

        var sideF = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
        ctx = sideF.context;
        ctx.fillStyle = buildingColor;
        ctx.fillRect(0, 0, a, h);
        //windows
        ctx.fillStyle = windowColor;
        ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
        ctx.fillStyle = windowColor;
        ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);


        var roof = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
        ctx = roof.context;
        ctx.fillStyle = '#331900';
        ctx.fillRect(0, 0, b, a);

        obj = new this.sheetengine.SheetObject({x: centerp.x - this.sheetengine.scene.tilewidth / 2 + b / 2, y: centerp.y - this.sheetengine.scene.tilewidth / 2 + a / 2, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: (b + a), h: (b + a), relu: (b + a) / 2, relv: (b + a) / 2});

        var result = new Building(obj);
        return result;
    },
    newTree: function(centerp)
    {
        var a = this.w / 1;
        var b = this.w / 1;
        var h = this.h * 2;
        var color = '#BDFF70';
        var color2 = '#BDaF77';

        function drawPineTexture(context, si, km, color) {
            context.fillStyle = color;
            context.beginPath();
            context.moveTo(si * 4 / 7, 0);
            context.lineTo(si * 6 / 7, km / 2);
            context.lineTo(si * 5 / 7, km / 2);
            context.lineTo(si, km);
            context.lineTo(si / 7, km);
            context.lineTo(si * 3 / 7, km / 2);
            context.lineTo(si * 2 / 7, km / 2);
            context.fill();
            context.fillStyle = '#725538';
            context.fillRect(si / 2, km, 10, 20);
        }

        var r = Math.floor((Math.random() * 10) + 1);
        h = this.h * 2 + r;
        var sheet4 = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: h * 2, h: h * 2});
        var sheet5 = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: h * 2, h: h * 2});
        drawPineTexture(sheet4.context, h * 2 - h / 8, h * 2 - 20, color);
        drawPineTexture(sheet5.context, h * 2 - h / 8, h * 2 - 20, color);

        r = Math.floor((Math.random() * 10) + 1);
        h = this.h + r;
        var sheet6 = new this.sheetengine.Sheet({x: 0, y: 40, z: h}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: h * 2, h: h * 2});
        var sheet7 = new this.sheetengine.Sheet({x: 0, y: 40, z: h}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: h * 2, h: h * 2});
        drawPineTexture(sheet6.context, h * 2 - h / 8, h * 2 - 20, color2);
        drawPineTexture(sheet7.context, h * 2 - h / 8, h * 2 - 20, color2);

        r = Math.floor((Math.random() * 10) + 1);
        h = this.h * 1.5 + r;
        var sheet8 = new this.sheetengine.Sheet({x: 20, y: 0, z: h}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: h * 2, h: h * 2});
        var sheet9 = new this.sheetengine.Sheet({x: 20, y: 0, z: h}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: h * 2, h: h * 2});
        drawPineTexture(sheet8.context, h * 2 - h / 8, h * 2 - 20, color);
        drawPineTexture(sheet9.context, h * 2 - h / 8, h * 2 - 20, color);
        obj = new this.sheetengine.SheetObject({x: centerp.x - this.sheetengine.scene.tilewidth / 2 + b / 2, y: centerp.y - this.sheetengine.scene.tilewidth / 2 + a / 2, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [sheet4, sheet5, sheet6, sheet7, sheet8, sheet9], {w: (b + a), h: (b + a), relu: (b + a) / 2, relv: (b + a) / 2});

        var result = new Building(obj);
        return result;
    },
    newHouse: function(centerp, level)
    {
        if (level === 0) {
            var a = this.w / (4);
            var b = this.w / (4);
            var h = this.h;
            var windowColor = '#0000dd';
            var buildingColor = '#DBDBDB';

            var sideL = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            var ctx = sideL.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideR = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            ctx = sideR.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideB = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideB.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

            var sideF = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideF.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);


            var roof = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
            ctx = roof.context;
            ctx.fillStyle = '#7F1717';
            ctx.fillRect(0, 0, b, a);

            obj = new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: (b + a), h: (b + a), relu: (b + a) / 2, relv: (b + a) / 2});
        }
        else if (level === 1) {
            var a = this.w / (3.6);
            var b = this.w / (2.7);
            var h = this.h;
            var windowColor = '#0000dd';
            var buildingColor = '#FBFBFB';

            var sideL = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            var ctx = sideL.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideR = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            ctx = sideR.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideB = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideB.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

            var sideF = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideF.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);


            var roof = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
            ctx = roof.context;
            ctx.fillStyle = '#7F1717';
            ctx.fillRect(0, 0, b, a);

            obj = new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 45}, [roof, sideR, sideL, sideB, sideF], {w: (b + a), h: (b + a), relu: (b + a) / 2, relv: (b + a) / 2});
        } else if (level === 2) {
            var a = this.w / (2);
            var b = this.w / (3);
            var h = this.h;
            var windowColor = '#0000dd';
            var buildingColor = '#FFAD5C';

            var sideL = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            var ctx = sideL.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideR = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            ctx = sideR.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideB = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideB.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

            var sideF = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideF.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);


            var roof = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
            ctx = roof.context;
            ctx.fillStyle = '#7F1717';
            ctx.fillRect(0, 0, b, a);

            obj = new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: (b + a), h: (b + a), relu: (b + a) / 2, relv: (b + a) / 2});
        } else if (level === 3) {
            var a = this.w / (1.6);
            var b = this.w / (2);
            var h = this.h;
            var windowColor = '#0000dd';
            var buildingColor = '#FFFF99';

            var sideL = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            var ctx = sideL.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideR = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            ctx = sideR.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideB = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideB.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

            var sideF = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideF.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);


            var roof = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
            ctx = roof.context;
            ctx.fillStyle = '#7F1717';
            ctx.fillRect(0, 0, b, a);

            obj = new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF], {w: (b + a), h: (b + a), relu: (b + a) / 2, relv: (b + a) / 2});
        } else {
            var a = this.w / (1.4);
            var b = this.w / (1.4);
            var h = this.h;
            var windowColor = '#0000dd';
            var buildingColor = '#B2B2B2';

            var sideL = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            var ctx = sideL.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideR = new this.sheetengine.Sheet({x: 0, y: a / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            ctx = sideR.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideB = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideB.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

            var sideF = new this.sheetengine.Sheet({x: b / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideF.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);



            var sideL2 = new this.sheetengine.Sheet({x: 0, y: -a / 2, z: 3 * h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            var ctx = sideL2.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideR2 = new this.sheetengine.Sheet({x: 0, y: a / 2, z: 3 * h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: b, h: h});
            ctx = sideR2.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, b, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(b / 5, h / 5, b / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * b / 5, h / 5, b / 5, h / 2);

            var sideB2 = new this.sheetengine.Sheet({x: -b / 2, y: 0, z: 3 * h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideB2.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

            var sideF2 = new this.sheetengine.Sheet({x: b / 2, y: 0, z: 3 * h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: a, h: h});
            ctx = sideF2.context;
            ctx.fillStyle = buildingColor;
            ctx.fillRect(0, 0, a, h);
            //windows
            ctx.fillStyle = windowColor;
            ctx.fillRect(a / 5, h / 5, a / 5, h / 2);
            ctx.fillStyle = windowColor;
            ctx.fillRect(3 * a / 5, h / 5, a / 5, h / 2);

            var roof = new this.sheetengine.Sheet({x: 0, y: 0, z: 2 * h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: b, h: a});
            ctx = roof.context;
            ctx.fillStyle = '#7F1717';
            ctx.fillRect(0, 0, b, a);

            obj = new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [roof, sideR, sideL, sideB, sideF, sideR2, sideL2, sideB2, sideF2], {w: (b + a), h: (b + a), relu: (b + a) / 2, relv: (b + a) / 2});
        }
        var result = new Building(obj);
        return result;
    },
    newTownCenter: function(centerp, type)
    {
        var w = this.w / 2;
        var h = this.h;
        var bok = new this.sheetengine.Sheet({x: 0, y: -w / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: w, h: h});
        var ctx = bok.context;
        // head
        ctx.fillStyle = '#FFFFCC';
        ctx.fillRect(0, 0, w, h);
        //okna
        ctx.fillStyle = '#0000dd';
        ctx.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx.fillStyle = '#0000dd';
        ctx.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        var bok2 = new this.sheetengine.Sheet({x: 0, y: w / 2, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: w, h: h});
        var ctx2 = bok2.context;
        // body
        ctx2.fillStyle = '#FFFFCC';
        ctx2.fillRect(0, 0, w, h);
        ctx2.fillStyle = '#0000dd';
        ctx2.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx2.fillStyle = '#0000dd';
        ctx2.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        var top = new this.sheetengine.Sheet({x: -w / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: w, h: h});
        var ctx4 = top.context;
        // head
        ctx4.fillStyle = '#FFFFCC';
        ctx4.fillRect(0, 0, w, h);

        var strecha = new this.sheetengine.Sheet({x: 0, y: 0, z: h}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: w, h: w});
        var ctx5 = strecha.context;
        // head
        ctx5.fillStyle = '#7F1717';
        ctx5.fillRect(0, 0, w, w);

        var bottom = new this.sheetengine.Sheet({x: w / 2, y: 0, z: h / 2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: w, h: h});
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
        var side = new this.sheetengine.Sheet({x: 0, y: -w / 2, z: h / 2 + h2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: w, h: h});
        var ctx = side.context;
        // head
        ctx.fillStyle = '#FFFFCC';
        ctx.fillRect(0, 0, w, h);
        //okna
        ctx.fillStyle = '#0000dd';
        ctx.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx.fillStyle = '#0000dd';
        ctx.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        var side2 = new this.sheetengine.Sheet({x: 0, y: w / 2, z: h / 2 + h2}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: w, h: h});
        var ctx2 = side2.context;
        // body
        ctx2.fillStyle = '#FFFFCC';
        ctx2.fillRect(0, 0, w, h);
        ctx2.fillStyle = '#0000dd';
        ctx2.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx2.fillStyle = '#0000dd';
        ctx2.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        var side3 = new this.sheetengine.Sheet({x: -w / 2, y: 0, z: h / 2 + h2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: w, h: h});
        var ctx4 = side3.context;
        // head
        ctx4.fillStyle = '#FFFFCC';
        ctx4.fillRect(0, 0, w, h);

        var roof = new this.sheetengine.Sheet({x: 0, y: 0, z: h + h2}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: w, h: w});
        var ctx5 = roof.context;
        // head
        ctx5.fillStyle = '#7F1717';
        ctx5.fillRect(0, 0, w, w);

        var side4 = new this.sheetengine.Sheet({x: w / 2, y: 0, z: h / 2 + h2}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: w, h: h});
        var ctx3 = side4.context;
        // head
        ctx3.fillStyle = '#FFFFCC';
        ctx3.fillRect(0, 0, w, h);
        ctx3.fillStyle = '#0000dd';
        ctx3.fillRect(w / 5, h / 5, w / 5, h / 2);
        ctx3.fillStyle = '#0000dd';
        ctx3.fillRect(3 * w / 5, h / 5, w / 5, h / 2);

        obj = new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [strecha, roof, side, side2, side3, side4, bok, bok2, top, bottom], {w: this.w, h: this.w, relu: this.w / 2, relv: this.w / 2});

        var result = new Building(obj);
        return result;
    },
    newTownName: function(centerp, name, people)
    {
        var w = this.w * 2;
        var h = this.h * 2;

        var sheetText = new this.sheetengine.Sheet(
                {x: 0, y: 0, z: 0},
        {alphaD: 0, betaD: 0, gammaD: 45},
        {w: w, h: h}
        );
        //sheetText.context.fillStyle = '#FFFFff';
        //sheetText.context.fillRect(0, 0, 200, 40);
        sheetText.context.font = "26px sans-serif";
        sheetText.context.fillStyle = "#ffffff";
        sheetText.context.textAlign = "center";
        sheetText.context.fillText(name, w / 2, h - 10);

        obj = new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z + 175}, {alphaD: 0, betaD: 0, gammaD: 0}, [sheetText], {w: w, h: h, relu: w / 2, relv: h / 2});

        var result = new Building(obj);
        return result;
    }
    ,
    newCityName: function(centerp, name, people, isMine)
    {
        var w = this.w * 2;
        var h = this.h * 2;

        var sheetText = new this.sheetengine.Sheet(
                {x: 0, y: 0, z: 0},
        {alphaD: 0, betaD: 0, gammaD: 45},
        {w: w, h: h}
        );
        sheetText.context.font = "13px sans-serif";
        if (isMine)
            sheetText.context.fillStyle = "#ff0000";
        else
            sheetText.context.fillStyle = "#ffffff";
        sheetText.context.textAlign = "center";
        sheetText.context.fillText(name, w / 2, h - 10);

        obj = new this.sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z + 20}, {alphaD: 0, betaD: 0, gammaD: 0}, [sheetText], {w: w, h: h, relu: w / 2, relv: h / 2});

        var result = new Building(obj);
        return result;
    }
};




