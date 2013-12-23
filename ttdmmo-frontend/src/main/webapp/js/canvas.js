var pathImages = 'images/field/';
var imgRoad = new Image();
var imgGrass = new Image();
var imgZas = new Image();
imgZas.src = pathImages + 'imgZas.jpg';

var imgRoad2 = new Image();
imgRoad2.src = pathImages + 'imgRoad2.jpg';

var imgCross = new Image();
imgCross.src = pathImages + 'imgCross.jpg';

var imgDepo = new Image();
imgDepo.src = pathImages + 'imgDepo.jpg';

imgRoad.src = pathImages + 'imgRoad.jpg';
imgGrass.src = pathImages + 'imgGrass.jpg';



demo = {
    maxsteps: 70,
    steps: 0,
    direction: {x: -5, y: 0, z: 0},
    rotate: false,
    objToInsert: '',
    timerInterval: 1000,
    camera: {x: 0, y: 0, z: 0},
    keys: {u: 0, d: 0, l: 0, r: 0}, // pressed keys: up, down, left, right
    boundary: {}, // boundary of user movements without loading new yards
    map: [],
    obj: [],
    load: [],
    zoom: 1,
    init: function() {
        demo.radius = 5;
        demo.max = 10;
        demo.w = 120;
        sheetengine.scene.tilewidth = demo.w;
        /* for (i = -max; i < max; i++) {
         map[i] = [];
         }*/
        geoRef = new Firebase('https://erckre9a2fz.firebaseio-demo.com/geodata'),
                demo.geo = new MyGeoFire(geoRef);
        // initialize the sheetengine
        canvasElement = document.getElementById('mainCanvas');
        canvasElement.onclick = function(event) {
            var puv = {
                u: event.clientX - sheetengine.canvas.offsetLeft + pageXOffset,
                v: event.clientY - sheetengine.canvas.offsetTop + pageYOffset
            };

            var w = sheetengine.canvas.width / 2;
            var h = sheetengine.canvas.height / 2;
            puv.u = (puv.u - w) / demo.zoom + w;
            puv.v = (puv.v - h) / demo.zoom + h;

            var pxy = sheetengine.transforms.inverseTransformPoint({
                u: puv.u + sheetengine.scene.center.u,
                v: puv.v + sheetengine.scene.center.v
            });

            console.debug(pxy);
            var clickpos = sheetengine.scene.getYardFromPos(pxy);
            console.debug(demo.geo.encode([clickpos.relyardx / demo.max, clickpos.relyardy / demo.max]));
            if (demo.objToInsert != '')
                geoRef.child('geoFire/dataByHash/' + demo.geo.encode([clickpos.relyardx / demo.max, clickpos.relyardy / demo.max]) + '/xasf').set({x: clickpos.relyardx / demo.max, y: clickpos.relyardy / demo.max, name: "Tesla", color: "#5D7E36", road: demo.objToInsert});


        };
        sheetengine.scene.init(canvasElement, {w: 2450, h: 2225});
        demo.sceneReady();
    },
    sceneReady: function() {


        /*   for (var x = -10; x <= 10; x++) {
         for (var y = -10; y <= 10; y++) {
         demo.map.push({
         centerp: {x: x * 200, y: y * 200, z: 0},
         orientation: {alphaD: 90, betaD: 0, gammaD: 0},
         size: {w: 200, h: 200},
         init: function() {
         var basesheet = new sheetengine.BaseSheet(this.centerp, this.orientation, this.size);
         basesheet.color = '#5D7E36';
         return basesheet;
         }
         });
         }
         }*/
        demo.obj.push({
            centerp: {x: 0, y: 0, z: 0},
            orientation: {alphaD: 0, betaD: 0, gammaD: 0},
            size: {w: 40, h: 40},
            init: function() {
                var sheet = new sheetengine.Sheet(this.centerp, this.orientation, this.size);
                sheet.context.fillStyle = '#FF0000';
                sheet.context.fillRect(0, 0, 40, 40);
                return sheet;
            }
        });
        // define camera
        demo.camera = {x: 0, y: 0, z: 0};
        // get relative yard coordinates and set initial boundary for visible yards
        var yardpos = sheetengine.scene.getYardFromPos(demo.camera);
        demo.defineUserObj(demo.camera);
        demo.defineBusObj(demo.camera);
        demo.setBoundary(yardpos);
        demo.loadAndRemoveSheets({x: 0, y: 0, z: 0}, yardpos);
        demo.initControls();
        setInterval(demo.timer2, 80);
    },
    setYardCanvas: function(type, basesheet) {
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
    },
    defineUserObj: function(centerp) {
        // user definition for animation with sheet motion
        var body = new sheetengine.Sheet({x: 1, y: 0, z: 13}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: 8, h: 14});
        var ctx = body.context;
        // head
        ctx.fillStyle = '#3d1e14';
        ctx.fillRect(1, 0, 5, 5);
        ctx.fillStyle = '#bfbf00';
        ctx.fillRect(2, 1, 3, 3);
        // body
        ctx.fillStyle = '#459';
        ctx.fillRect(1, 5, 6, 1);
        ctx.fillRect(0, 6, 8, 6);
        // left hand
        ctx.fillStyle = '#bfbf00';
        ctx.fillRect(6, 12, 2, 2);
        // belt
        ctx.fillStyle = '#000';
        ctx.fillRect(0, 12, 6, 2);
        var backhead = new sheetengine.Sheet({x: 1, y: -0.5, z: 17}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: 5, h: 5});
        backhead.context.fillStyle = '#3d1e14';
        backhead.context.fillRect(0, 0, 5, 5);


        // legs
        var leg1 = new sheetengine.Sheet({x: -1.5, y: 0, z: 4}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: 3, h: 8});
        leg1.context.fillStyle = '#3d1e14';
        leg1.context.fillRect(0, 0, 4, 10);
        var leg2 = new sheetengine.Sheet({x: 1.5, y: 0, z: 4}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: 3, h: 8});
        leg2.context.fillStyle = '#3d1e14';
        leg2.context.fillRect(0, 0, 4, 10);
        leg1.angle = 0;
        leg2.angle = 0;

        // define user object
        demo.clovek = new sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [body, backhead, leg1, leg2], {w: 40, h: 40, relu: 20, relv: 30});

    },
    defineBusObj: function(centerp) {
        // user definition for animation with sheet motion
        var bok = new sheetengine.Sheet({x: 0, y: 0, z: 0}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: 30, h: 14});
        var ctx = bok.context;
        // head
        ctx.fillStyle = '#3d1e14';
        ctx.fillRect(0, 0, 30, 14);
        //okna
        ctx.fillStyle = '#0000dd';
        ctx.fillRect(5, 2, 20, 4);

        var bok2 = new sheetengine.Sheet({x: 0, y: 10, z: 0}, {alphaD: 0, betaD: 0, gammaD: 0}, {w: 30, h: 14});
        var ctx2 = bok2.context;
        // body
        ctx2.fillStyle = '#3d1e14';
        ctx2.fillRect(0, 0, 30, 14);
        ctx2.fillStyle = '#0000dd';
        ctx2.fillRect(5, 2, 20, 4);

        var top = new sheetengine.Sheet({x: -15, y: 5, z: 0}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: 10, h: 14});
        var ctx4 = top.context;
        // head
        ctx4.fillStyle = '#3d1e14';
        ctx4.fillRect(0, 0, 10, 14);
        ctx4.fillStyle = '#0000dd';
        ctx4.fillRect(1, 2, 5, 5);

        var strecha = new sheetengine.Sheet({x: 0, y: 5, z: 7}, {alphaD: 90, betaD: 0, gammaD: 0}, {w: 30, h: 10});
        var ctx5 = strecha.context;
        // head
        ctx5.fillStyle = '#3d1e14';
        ctx5.fillRect(0, 0, 30, 14);

        var bottom = new sheetengine.Sheet({x: 15, y: 5, z: 0}, {alphaD: 0, betaD: 0, gammaD: 90}, {w: 10, h: 14});
        var ctx3 = bottom.context;
        // head
        ctx3.fillStyle = '#3d1e14';
        ctx3.fillRect(0, 0, 10, 14);





        // define user object
        demo.bus = new sheetengine.SheetObject({x: centerp.x, y: centerp.y, z: centerp.z}, {alphaD: 0, betaD: 0, gammaD: 0}, [strecha, bok, bok2, top, bottom], {w: 50, h: 50, relu: 30, relv: 30});

    },
    loadAndRemoveSheets: function(oldcentertile, centertile) {
        boundary = demo.boundary;
        map = demo.map;
        // remove sheets that are far
        var len = map.length;
        while (len--) {
            var sheetinfo = map[len];
            if (sheetinfo.centerp.x < boundary.xmin || sheetinfo.centerp.x > boundary.xmax || sheetinfo.centerp.y < boundary.ymin || sheetinfo.centerp.y > boundary.ymax) {
                sheetinfo.sheet.destroy();
                var index = demo.load.indexOf("x" + sheetinfo.centerp.x + "y" + sheetinfo.centerp.y);
                if (index > -1) {
                    demo.load.splice(index, 1);

                }
                console.log("REM x" + sheetinfo.centerp.x + "y" + sheetinfo.centerp.y);
                map.splice(len, 1);
            }

        }



        //console.log(centertile.x);
        //
        // add new sheets
        demo.geo.getPointsNearLoc([centertile.relyardx / demo.max, centertile.relyardy / demo.max], demo.geo.distRect(demo.radius, demo.max),
                function(array) {
                    for (var i = 0; i < array.length; i++) {
                        var centerp = {x: (array[i].x * demo.max) * demo.w, y: (array[i].y * demo.max) * demo.w, z: 0};
                        if (-1 == jQuery.inArray("x" + centerp.x + "y" + centerp.y, demo.load)) {
                            var orientation = {alphaD: 90, betaD: 0, gammaD: 0};
                            var size = {w: demo.w, h: demo.w};
                            var color = array[i].color;
                            var basesheet = new sheetengine.BaseSheet(centerp, orientation, size);
                            basesheet.color = '' + color;
                            demo.setYardCanvas(array[i].road, basesheet);
                            map.push({
                                centerp: centerp,
                                sheet: basesheet
                            });
                            demo.load.push("x" + centerp.x + "y" + centerp.y);
                            console.log("INS x" + centerp.x + "y" + centerp.y);
                        }
                        console.log("A found point = ", array[i]);
                    }

                    callback = function(array) {
                        var len = map.length;
                        while (len--) {
                            var sheetinfo = map[len];
                            sheetinfo.sheet.destroy();
                            map.splice(len, 1);
                        }



                        for (var i = 0; i < array.length; i++) {
                            var centerp = {x: (array[i].x * demo.max) * demo.w, y: (array[i].y * demo.max) * demo.w, z: 0};

                            var orientation = {alphaD: 90, betaD: 0, gammaD: 0};
                            var size = {w: demo.w, h: demo.w};
                            var color = array[i].color;
                            var basesheet = new sheetengine.BaseSheet(centerp, orientation, size);
                            basesheet.color = '' + color;
                            demo.setYardCanvas(array[i].road, basesheet);

                            map.push({
                                centerp: centerp,
                                sheet: basesheet
                            });

                            console.log("REINS x" + centerp.x + "y" + centerp.y);

                            console.log("A found point = ", array[i]);
                        }

                        sheetengine.calc.calculateAllSheets();
                        sheetengine.drawing.drawScene(true);
                    };
                    demo.geo.offPointsNearLoc([oldcentertile.x / demo.max, oldcentertile.y / demo.max], demo.geo.distRect(demo.radius, demo.max), callback);
                    demo.geo.onPointsNearLoc([centertile.relyardx / demo.max, centertile.relyardy / demo.max], demo.geo.distRect(demo.radius, demo.max),
                            callback);
                    /*   console.log("A found point = " + map.length);
                     for (var i = 0; i < map.length; i++) {
                     
                     var sheetinfo = map[i];
                     if (sheetinfo.centerp.x < boundary.xmin || sheetinfo.centerp.x > boundary.xmax || sheetinfo.centerp.y < boundary.ymin || sheetinfo.centerp.y > boundary.ymax)
                     continue;
                     
                     if (!sheetinfo.added) {
                     sheetinfo.sheet = sheetinfo.init();
                     sheetinfo.added = true;
                     console.log("A found point = ");
                     }
                     }
                     */








                    // translate background
                    sheetengine.scene.translateBackground(
                            {x: oldcentertile.x * demo.w, y: oldcentertile.y * demo.w},
                    {x: centertile.relyardx * demo.w, y: centertile.relyardy * demo.w}
                    );
                    sheetengine.calc.calculateAllSheets();
                    sheetengine.drawing.drawScene(true);
                });
        demo.map = map;
    },
    initControls: function() {
        $(window).keydown(demo.onkeydown);
        $(window).keyup(demo.onkeyup);
        // main loop
        setInterval(demo.timer, 30);

    },
    setKeys: function(event, val) {
        var keyProcessed = 0;
        if (event.keyCode == '38' || event.keyCode == '87') {
            demo.keys.u = val;
            keyProcessed = 1;
        }
        if (event.keyCode == '37' || event.keyCode == '65') {
            demo.keys.l = val;
            keyProcessed = 1;
        }
        if (event.keyCode == '39' || event.keyCode == '68') {
            demo.keys.r = val;
            keyProcessed = 1;
        }
        if (event.keyCode == '40' || event.keyCode == '83') {
            demo.keys.d = val;
            keyProcessed = 1;
        }
        if (keyProcessed)
            event.preventDefault();
    },
    onkeydown: function(event) {
        demo.setKeys(event, 1);
    },
    onkeyup: function(event) {
        demo.setKeys(event, 0);
    },
    timer: function() {
        var move = 10;
        x = 0;
        y = 0;
        if (demo.keys.u) {
            x = -move;
        }

        if (demo.keys.d) {
            x = move;
        }

        if (demo.keys.r) {
            y = -move;
        }
        if (demo.keys.l) {
            y = +move;
        }



        if (x || y) {
            var yardpos = sheetengine.scene.getYardFromPos(demo.camera);
            var oldcentertile = {x: yardpos.relyardx, y: yardpos.relyardy, z: yardpos.relyardz};
            // move camera
            var targetp = {
                x: demo.camera.x + x,
                y: demo.camera.y + y,
                z: demo.camera.z};
            demo.camera = targetp;
            // move center
            sheetengine.scene.setCenter({x: demo.camera.x, y: demo.camera.y, z: demo.camera.z});
            var yardpos2 = sheetengine.scene.getYardFromPos(demo.camera);
            //console.debug(oldcentertile);
            //console.debug(yardpos2);
            //console.debug(sheetengine.scene.tilewidth);
            //console.debug(demo.camera);
            if (yardpos.relyardx != yardpos2.relyardx || yardpos.relyardy != yardpos2.relyardy || yardpos.relyardz != yardpos2.relyardz)
            {
                //  set new boundary
                demo.setBoundary(yardpos2);
                demo.loadAndRemoveSheets(oldcentertile, yardpos2);
            }

            sheetengine.calc.calculateAllSheets();
            sheetengine.drawing.drawScene(true);
        }
    },
    setBoundary: function(yardpos) {
        // for boundary we use relative yard coordinates
        var radius = demo.radius;
        demo.boundary = {
            xmin: (yardpos.relyardx - radius) * sheetengine.scene.tilewidth,
            ymin: (yardpos.relyardy - radius) * sheetengine.scene.tilewidth,
            xmax: (yardpos.relyardx + radius) * sheetengine.scene.tilewidth,
            ymax: (yardpos.relyardy + radius) * sheetengine.scene.tilewidth
        };
    },
    getHoveredObject: function(p) {
        for (var i = 0; i < sheetengine.objects.length; i++) {
            var obj = sheetengine.objects[i];
            if (obj.hidden)
                continue;
            if (obj.name == 'Enemy' && obj.health <= 0)
                continue;
            if (obj.name.indexOf('Gate') != -1 && obj.opened)
                continue;

            var selrect = holyyards.selRects[obj.name];
            if (!selrect)
                continue;

            // check is current object is hovered
            var o = sheetengine.drawing.getPointuv(obj.centerp);
            if (o.u - selrect.x > p.u - selrect.w && o.u - selrect.x < p.u + selrect.w && o.v - selrect.y > p.v - selrect.h && o.v - selrect.y < p.v + selrect.h) {
                return obj;
            }
        }
        return null;
    },
    mousemove: function(event) {

        var puv = {
            u: event.clientX - sheetengine.canvas.offsetLeft + pageXOffset,
            v: event.clientY - sheetengine.canvas.offsetTop + pageYOffset
        };

        var w = sheetengine.canvas.width / 2;
        var h = sheetengine.canvas.height / 2;
        puv.u = (puv.u - w) / demo.zoom + w;
        puv.v = (puv.v - h) / demo.zoom + h;
        holyyards.hoveredObj = demo.getHoveredObject(puv);
        if (demo.objToInsert)
            $(sheetengine.canvas).css('cursor', 'default');
        else
            $(sheetengine.canvas).css('cursor', 'crosshair');
    },
    // main timer
    timer2: function() {
        console.log('jedu');
        var startTime = new Date().getTime();

        var sceneChanged = 1;
        obj = demo.bus;


        if (demo.rotate) {
            obj.rotate({x: 0, y: 0, z: 1}, Math.PI / 2 / (demo.maxsteps / 10));
            if (++demo.steps >= demo.maxsteps / 10) {
                demo.steps = 0;
                demo.rotate = false;
            }
        } else {
            obj.move(demo.direction);
            if (++demo.steps >= demo.maxsteps) {
                if (demo.direction.x == 5) {
                    demo.direction.x = 0;
                    demo.direction.y = 5;
                }
                else if (demo.direction.y == 5) {
                    demo.direction.x = -5;
                    demo.direction.y = 0;
                }
                else if (demo.direction.x == -5) {
                    demo.direction.x = 0;
                    demo.direction.y = -5;
                }
                else if (demo.direction.y == -5) {
                    demo.direction.x = 5;
                    demo.direction.y = 0;
                }
                demo.steps = 0;
                demo.rotate = true;
            }
        }


        if (sceneChanged) {
            sheetengine.calc.calculateChangedSheets();
            sheetengine.drawing.drawScene();
        }

        var endTime = new Date().getTime();
        var duration = endTime - startTime;
        var remaining = demo.timerInterval - duration;
    }
}

$(function() {
    demo.init();
});

