UtilityUIX {
	var fxs;
	var loadPreset, savePreset;
	var /*presetListView,*/ presetFile, presetFiles, presetPath;
	var loadTrackPreset, saveTrackPreset;
	var presetTrackFile, presetTrackFiles, presetTrackPath;
	var handler;
	var orbits;
	var orbitT;
	var defaultParentEvent;

	*new { | initHandler, initOrbits, initPresetPath, initDefaultParentEvent|
		^super.new.init(initHandler, initOrbits, initPresetPath, initDefaultParentEvent)
	}

	init { |initHandler, initOrbits, initPresetPath, initDefaultParentEvent|
		handler = initHandler;
		orbits = initOrbits;
		orbitT = initOrbits[0 /*~tidalTrackPresetNumber*/ /*~tidalCurrentOrbit*/]; // 0 since ~tidalTrackPresetNumber is not initialized
		defaultParentEvent = initDefaultParentEvent;
		presetPath = initPresetPath;
		presetFile = 'Default.json';
		presetTrackPath = ~tidalPresetTrackXPath;
		// presetTrackPath = "../presetsTr/" /*initPresetPath.postln*/;
		presetTrackFile = 'DefaultT.json'; // necessary ?

		// ~presetTrackPath = presetTrackPath.resolveRelative; // Necessary to put into a variable to get the path
		// ~presetTrackPath = (PathName(thisProcess.nowExecutingPath).pathOnly++"Xpresets/");
		// To get ~tidalTrackPresetListView.items.size before the initialzation of the GUI
		~tidalPresetTrackSize = PathName(~tidalPresetTrackXPath).files.size;
		~tidalPresetTrackXPath2 = ~tidalPresetXPath ++ ~tidalPresetTrackXFileName;

		// Création du fichier s'il n'existe pas
		if (File.exists(~tidalPresetTrackXPath2), { }, {File.open(~tidalPresetTrackXPath2, "w")});
		// Mode w -> Creates an empty file for writing. If a file with the same name already exists its content is erased and thefile is treated as a new empty file.

		// > 21 car indication de DEvent dans le fichier quand tous les presets ont été retiré avec Del donc obligé de réinitialiser la liste des presets avec le Default
		if (File.fileSize(~tidalPresetTrackXPath2) <= 21, {
			~tidalAllPresetsData = IdentityDictionary.new;
			// ~tidalAllPresetsData = ();
		} , {
			~tidalAllPresetsDataFileW = ZArchive.read(~tidalPresetTrackXPath2);
			~tidalAllPresetsData = ~tidalAllPresetsDataFileW.readItem;
			~tidalAllPresetsDataFileW.close;
		});

		~tidalPresetLineBegin = 1;
		~tidalPresetLineEnd = ~tidalPresetTrackSize;

		if (handler.isNil.not, {
			handler.subscribe(this, \extendDefaultParentEvent);
		});

		if (presetFile.isNil.not, {

			this.prLoadPresetFiles;

			~tidalPresetListView /*presetListView*/ = ListView(nil,Rect(10,10,30,30)).maxHeight_(60).selectedStringColor_(Color.white)
			.items_(presetFiles)
			.action_({ arg sbs;
				presetFile = /*presetListView*/ ~tidalPresetListView.items[sbs.value]; // .value returns the integer
			}).maxWidth_(100 /*150*/)/*.maxHeight_(200)*/;

			/*presetListView*/ ~tidalPresetListView.value = nil;

			this.addTidalvstPresetListener;
			this.addLoadPresetListener;
		});

		// Implementation of a local Track Preset on some parameters except Volume / Mute / Solo & Reverb
		this.prLoadTrackPresetFiles;

		~tidalAllPresetListView = ListView(nil,Rect(10,10,10,0)).minHeight_(200).selectedStringColor_(Color.white)
		// PopUpMenu.allowsReselection_(true)
		.action_({ arg sbs; var size, t, d;
			~tidalAllPresetTextField.string = sbs.item;

			size = ~tidalAllPresetsData[~tidalAllPresetTextField.string.asSymbol].size-1;
			t = ~tidalAllPresetsData[~tidalAllPresetTextField.string.asSymbol];
			d = ~tidalTrackPresetListView.items.collect { |i| i.split($ )[1] };
			// d.postln;
			(size..0).do { |i|
					/*t[i][0].postln;
					t[i][0].class.postln;*/
				if  (~tidalAllPresetSwitchNT == 0, { // Load value of the preset
					~tidalGuiElements[i][\preset][\value].valueAction_(t[i][0].asInteger);
				},{ // Load text of the preset
					~tidalGuiElements[i][\preset][\value].valueAction_(d.indexOfEqual(t[i][1])+1);
				});
			}

		}).maxWidth_(112 /*150*/)/*.maxHeight_(200)*/
		// .items_(~tidalAllPresetsData.keys.array);
		.items_(~tidalAllPresetsData.keys.asArray.sort);

		~tidalTrackPresetListView = ListView(nil,Rect(10,10,80,30)).minWidth_(80).maxHeight_(200).selectedStringColor_(Color.white)
		.items_(presetTrackFiles)
		.action_({ arg sbs;
			~tidalAllPresetNb = ~tidalTrackPresetListView.items[sbs.value]; // .value returns the integer
			~tidalTrackPresetTextField.string = sbs.item.split($ )[1]; // ~tidalTrackPreset.split($.)[1];
			// Qu'est ce que fait ~tidalTrackPreset ?
		}).maxWidth_(112 /*150*/)/*.maxHeight_(200)*/;

		~tidalRecTrack = 0;
		~tidalRecTemp = 0 ! 3; // To record 3 temporary states / presets - TO DO
		~tidalRecSeqline = 0 ! ~tidalNbOfOrbits ! 3;
		~tidalRecRytS = 0 ! ~tidalNbOfOrbits ! 3;
		~tidalRecRyt = 0 ! ~tidalNbOfOrbits ! 3;
		~tidalRecLegS = 1 ! ~tidalNbOfOrbits ! 3;
		~tidalRecLeg = 1 ! ~tidalNbOfOrbits ! 3;
		~tidalRecBufS = 0 ! ~tidalNbOfOrbits ! 3;
		~tidalRecBuf = 0 ! ~tidalNbOfOrbits ! 3;
		~tidalRecBuf2 = 0 ! ~tidalNbOfOrbits ! 3;

	}

	handleEvent { |eventName, eventData|
		if (eventName == \extendDefaultParentEvent, {
			eventData.pairsDo { |key, val|
				defaultParentEvent.put(key, val)
			};
		});
	}

	prLoadPresetFiles { |path|
		var filePaths;
		path = path ?? { presetPath.resolveRelative };
		filePaths = pathMatch(standardizePath(path +/+ "*"));

		presetFiles = filePaths.collect { |filepath|
			PathName.new(filepath).fileName;
		};
	}

	prLoadTrackPresetFiles { |path|
		var fileTrackPaths;
		path = path ?? { presetTrackPath.resolveRelative };
		fileTrackPaths = pathMatch(standardizePath(path +/+ "*"));

		presetTrackFiles = fileTrackPaths.collect { |fileTrackpath, i|
			(i+1).asString ++ "." + (PathName.new(fileTrackpath).fileNameWithoutExtension) /*fileName*/;
		};
	}

	/* PRESET MANAGEMENT */
	loadPreset {
		var defaultEvents = JSONlib.convertToSC(File.readAllString((presetPath ++ presetFile).resolveRelative, "r"));

		// To select only orbits available
		defaultEvents = defaultEvents[0..(~tidalNbOfOrbits-1)];

		defaultEvents.do({
			arg defaultEvent, index;

			defaultParentEvent.keysValuesDo({|key, val|
				if (defaultEvent.keys.includes(key) == false, {defaultEvent.put(key, val)})
			});

			orbits[index].set(*defaultEvent.asPairs);
		});

		handler.emitEvent(\presetLoaded, defaultEvents);

	}

	savePreset {
		var orbitPresets = Array.new(orbits.size);
		var file = File((presetPath ++ presetFile).resolveRelative, "w");

		orbits.do({
			arg orbit;
			var presetEvent = ();
			// orbit.postln;

			defaultParentEvent.keys.do({|eventKey| presetEvent.put(eventKey, orbit.get(eventKey)) });

			orbitPresets.add(presetEvent);
		});

		file.write(*JSONlib.convertToJSON(orbitPresets));
		file.close;
	}

	loadTrackPreset {
		var defaultTrackEvents = JSONlib.convertToSC(File.readAllString((presetTrackPath ++ ~tidalTrackPresetListView.item.split($ )[1] ++".json" /*presetTrackFile*/).resolveRelative, "r"));

		defaultTrackEvents.do({
			arg defaultEvent /*, index*/;
			// defaultEvent.postln;

			/*defaultParentEvent.keysValuesDo*/
			[\seqLine, \ryt, \rytS, \leg, \legS, \bufS, \buf, \buf2, \buf2R, \fxs, \fxs2, \fxx, \fxx2, \fxt, \fxt2, \fxp, \fxp2, \fxv, \fxv2, \fus, \fus2, \fux, \fux2, \fut, \fut2, \fup, \fup2, \fuv, \fuv2, \pan /*, \masterGain, \mute, reverbVariableName.asSymbol*/
			].do({|key, val| // key.postln;
				if (defaultEvent.keys.includes(key) == false, {defaultEvent.put(key, val)})
			});

			orbits[~tidalTrackPresetNumber /*~tidalCurrentOrbit*/].set(*defaultEvent.asPairs);
		});

		handler.emitEvent(\presetLoaded, defaultTrackEvents);

	}

	saveTrackPreset {
		var presetTrack = Array.new(/*orbits.size*/);
		var orbitT = /*initOrbits*/ orbits[~tidalTrackPresetNumber];
		var file = File((presetTrackPath ++ ~tidalTrackPresetTextField.value ++ ".json" /*presetTrackFile*/).resolveRelative, "w");

		/*orbits.do({ arg orbit;*/
		var presetTrackEvent = ();

		/*defaultParentEvent.keys*/
		[\seqLine, \ryt, \rytS, \leg, \legS, \bufS, \buf, \buf2, \buf2R, \fxs, \fxs2, \fxx, \fxx2, \fxt, \fxt2, \fxp, \fxp2, \fxv, \fxv2, \fus, \fus2, \fux, \fux2, \fut, \fut2, \fup, \fup2, \fuv, \fuv2, \pan /*, \masterGain, \mute, reverbVariableName.asSymbol*/
		].do({ |eventKey| presetTrackEvent.put(eventKey, orbitT.get(eventKey)) });
		presetTrack.add(presetTrackEvent);
		/*});*/
		// orbitT.get(\seqLine).postln;

		// Test to delete
		// ~tidalPresetsData[~tidalTrackPresetNumber] = presetTrack.copy /*defaultTrackEvents*/;
		// ~tidalPresetsData[~tidalTrackPresetNumber].postln;

		file.write(*JSONlib.convertToJSON(presetTrack.asArray));
		file.close;

		this.prLoadTrackPresetFiles;
		~tidalTrackPresetListView.items_(presetTrackFiles);
		~tidalTrackPresetListView.value_(~tidalTrackPresetListView.items.indexOfEqual(~tidalTrackPresetTextField.value ++ ".json"));
		~tidalPresetTrackSize = ~tidalTrackPresetListView.items.size;
		~tidalPresetLine = (1 .. ~tidalPresetTrackSize /*~tidalTrackPresetListView.items.size*/); // to update data of controller XoneK2
		~tidalNbOfOrbits.do { |i|
			~tidalGuiElements[i][\preset][\value].clipHi_(~tidalTrackPresetListView.items.size)
			// ~tidalGuiElements[0][\preset][\element]
		};
	}

	/* DEFINE PRESET UI */
	createUI {
		| container |
		container.layout = VLayout(
			StaticText.new.string_("Global Preset").fixedHeight_(15).maxWidth_(100).align_(\center), // maxWidth added
			/*Button.new.states_([["Mute All", Color.black, Color.white], ["Unmute All", Color.white, Color.blue]])
			.action_({
			|view|
			if(view.value == 0) { this.tidalNetAddr.sendMsg("/unmuteAll") };
			if(view.value == 1) { this.tidalNetAddr.sendMsg("/muteAll") };
			}),*/
			~tidalPresetListView /*presetListView*/,
			~tidalLoadPresetButtonView = Button.new/*.string_("Load Gl Preset")*/.states_([["Load Gl Preset", Color.blue, Color.white]]).maxWidth_(100) // maxWidth added
			.action_({
				|view|
				this.loadPreset;
				handler.emitEvent(\updateUI);
			}),
			Button.new/*.string_("Save Gl Preset")*/.states_([["Save Gl Preset", Color.black, Color.green]]).maxWidth_(100) // maxWidth added
			.action_({
				handler.emitEvent(\updateActiveOrbit);
				this.savePreset;
			}),


			HLayout(StaticText.new.string_("---------------").fixedHeight_(15).maxWidth_(100).align_(\center)),

			HLayout(StaticText.new.string_("TraX Presets").fixedHeight_(15).maxWidth_(100).align_(\center)),

			HLayout(~tidalAllPresetTextField = TextField.new.maxWidth_(100).background_(Color.black).stringColor_(Color.white).string = "XPresetsName"),

			HLayout(~tidalAllPresetListView),

			~tidalAllPresetSwitchNTButton = Button.new.maxWidth_(50).states_([["Nb", Color.white, Color.gray],["Text", Color.white, Color.gray]])
			.action_({ |v| ~tidalAllPresetSwitchNT = v.value;
			}).valueAction_(1),

			Button.new/*.string_("Load Gl Preset")*/.states_([["Load X Presets", Color.blue, Color.white]]).maxWidth_(110) // maxWidth added
			.action_({
				|view| var size, t, d;
				~tidalAllPresetsDataFileW = ZArchive.read(~tidalPresetTrackXPath2);
				~tidalAllPresetsData = ~tidalAllPresetsDataFileW.readItem;
				~tidalAllPresetsDataFileW.close;
				size = ~tidalAllPresetsData[~tidalAllPresetTextField.string.asSymbol].size-1;
				t = ~tidalAllPresetsData[~tidalAllPresetTextField.string.asSymbol];
				d = ~tidalTrackPresetListView.items.collect { |i| i.split($ )[1] };
				// d.postln;
				(size..0).do { |i|
					/*t[i][0].postln;
					t[i][0].class.postln;*/
					if  (~tidalAllPresetSwitchNT == 0, { // Load value of the preset
						~tidalGuiElements[i][\preset][\value].valueAction_(t[i][0].asInteger);
					},{ // Load text of the preset
						~tidalGuiElements[i][\preset][\value].valueAction_(d.indexOfEqual(t[i][1])+1);
					});
				}
			}),
			Button.new/*.string_("Save Gl Preset")*/.states_([["Save X Presets", Color.black, Color.green]]).maxWidth_(110) // maxWidth added
			.action_({
				~tidalAllPresetsData.put(~tidalAllPresetTextField.string.asSymbol, ~tidalNbOfOrbits.collect { |i| [ ~tidalTrackPresetListView.items[~tidalPresetNb[i]-1].split($.)[0], ~tidalTrackPresetListView.items[~tidalPresetNb[i]-1].split($.)[1].stripWhiteSpace ] } );

				~tidalPresetsFile = ZArchive.write(~tidalPresetTrackXPath2);
				~tidalPresetsFile.writeItem(~tidalAllPresetsData);
				~tidalPresetsFile.writeClose;

				~tidalAllPresetListView.items_(~tidalAllPresetsData.keys.asArray.sort);
			}),
			Button.new/*.string_("Delete Tr Preset")*/.states_([["Delete X Presets", Color.red, Color.white]]).maxWidth_(110) // maxWidth added
			.action_({
				~tidalAllPresetsData.removeAt(~tidalAllPresetTextField.string.asSymbol);

				~tidalPresetsFile = ZArchive.write(~tidalPresetTrackXPath2);
				~tidalPresetsFile.writeItem(~tidalAllPresetsData);
				~tidalPresetsFile.writeClose;

				~tidalAllPresetListView.items_(~tidalAllPresetsData.keys.asArray.sort);
			}),


			HLayout(StaticText.new.string_("---------------").fixedHeight_(15).maxWidth_(100).align_(\center)),


			HLayout(
				~tidalLegSwitchButton = Button.new.maxWidth_(50).states_([["Dur S", Color.black, Color.green],["Dur S", Color.white, Color.red]])
				.action_({ |v| ~tidalLegSwitch = v.value;
				}).valueAction_(1),
				~tidalFolSwitchButton = Button.new.maxWidth_(50).states_([["Fol S", Color.black, Color.green], ["Fol S", Color.white, Color.red]])
				.action_({ |v| ~tidalFolSwitch = v.value;
				}).valueAction_(0),
			),

			10,

			HLayout(
				~tidalRecTempButton1 = Button.new.maxWidth_(30).states_([["R1", Color.white, Color.black]])
				.action_({ |v| // v.value.postln;
					// ~tidalRecTrack = 0;
					if (v. value == 0, {~tidalRecTrack = 0; ~tidalRecTemp[~tidalRecTrack] = 1} );
					v.states_([["R1", Color.white, Color.red]]);
					~tidalTrigTempButton1.states_([["T1", Color.white, Color.blue]]);
					if (~tidalRecTemp[1] == 1, {
						~tidalRecTempButton2.states_([["R2", Color.black, Color.yellow]]);
						~tidalTrigTempButton2.states_([["T2", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton2.states_([["R2", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[2] == 1, {
						~tidalRecTempButton3.states_([["R3", Color.black, Color.yellow]]);
						~tidalTrigTempButton3.states_([["T3", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton3.states_([["R3", Color.white, Color.black]]);
					});
					// ~tidalRecTempButton1.states_([["R1", Color.white, Color.red]]);
					// ~tidalRecTempButton1.states_([["R1", Color.black, Color.yellow]]);
					~tidalNbOfOrbits.do { |i|
						~tidalRecSeqline[~tidalRecTrack][i] = ~tidalGuiElements[i][\seqLine][\value].value;
						~tidalRecRytS[~tidalRecTrack][i] = ~tidalGuiElements[i][\rytS][\value].value;
						~tidalRecRyt[~tidalRecTrack][i] = ~tidalGuiElements[i][\ryt][\value].value;
						~tidalRecLegS[~tidalRecTrack][i] = ~tidalGuiElements[i][\legS][\value].value;
						~tidalRecLeg[~tidalRecTrack][i] = ~tidalGuiElements[i][\leg][\value].value;
						~tidalRecBufS[~tidalRecTrack][i] = ~tidalGuiElements[i][\bufS][\value].value;
						~tidalRecBuf[~tidalRecTrack][i] = ~tidalGuiElements[i][\buf][\value].value;
						~tidalRecBuf2[~tidalRecTrack][i] = ~tidalGuiElements[i][\buf2][\value].value;
					};
				}),
				~tidalRecTempButton2 = Button.new.maxWidth_(30).states_([["R2", Color.white, Color.black]])
				.action_({ |v|
					if (v. value == 0, {~tidalRecTrack = 1; ~tidalRecTemp[~tidalRecTrack] = 1} );
					v.states_([["R2", Color.white, Color.red]]);
					~tidalTrigTempButton2.states_([["T2", Color.white, Color.blue]]);
					if (~tidalRecTemp[0] == 1, {
						~tidalRecTempButton1.states_([["R1", Color.black, Color.yellow]]);
						~tidalTrigTempButton1.states_([["T1", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton1.states_([["R1", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[2] == 1, {
						~tidalRecTempButton3.states_([["R3", Color.black, Color.yellow]]);
						~tidalTrigTempButton3.states_([["T3", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton3.states_([["R3", Color.white, Color.black]]);
					});
					~tidalRecTrack = 1;
					~tidalNbOfOrbits.do { |i|
						~tidalRecSeqline[~tidalRecTrack][i] = ~tidalGuiElements[i][\seqLine][\value].value;
						~tidalRecRytS[~tidalRecTrack][i] = ~tidalGuiElements[i][\rytS][\value].value;
						~tidalRecRyt[~tidalRecTrack][i] = ~tidalGuiElements[i][\ryt][\value].value;
						~tidalRecLegS[~tidalRecTrack][i] = ~tidalGuiElements[i][\legS][\value].value;
						~tidalRecLeg[~tidalRecTrack][i] = ~tidalGuiElements[i][\leg][\value].value;
						~tidalRecBufS[~tidalRecTrack][i] = ~tidalGuiElements[i][\bufS][\value].value;
						~tidalRecBuf[~tidalRecTrack][i] = ~tidalGuiElements[i][\buf][\value].value;
						~tidalRecBuf2[~tidalRecTrack][i] = ~tidalGuiElements[i][\buf2][\value].value;
					};
				}),
				~tidalRecTempButton3 = Button.new.maxWidth_(30).states_([["R3", Color.white, Color.black]])
				.action_({ |v|
					if (v. value == 0, {~tidalRecTrack = 2; ~tidalRecTemp[~tidalRecTrack] = 1} );
					v.states_([["R3", Color.white, Color.red]]);
					~tidalTrigTempButton3.states_([["T3", Color.white, Color.blue]]);
					if (~tidalRecTemp[0] == 1, {
						~tidalRecTempButton1.states_([["R1", Color.black, Color.yellow]]);
						~tidalTrigTempButton1.states_([["T1", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton1.states_([["R1", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[1] == 1, {
						~tidalRecTempButton2.states_([["R2", Color.black, Color.yellow]]);
						~tidalTrigTempButton2.states_([["T2", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton2.states_([["R2", Color.white, Color.black]]);
					});
					~tidalNbOfOrbits.do { |i|
						~tidalRecSeqline[~tidalRecTrack][i] = ~tidalGuiElements[i][\seqLine][\value].value;
						~tidalRecRytS[~tidalRecTrack][i] = ~tidalGuiElements[i][\rytS][\value].value;
						~tidalRecRyt[~tidalRecTrack][i] = ~tidalGuiElements[i][\ryt][\value].value;
						~tidalRecLegS[~tidalRecTrack][i] = ~tidalGuiElements[i][\legS][\value].value;
						~tidalRecLeg[~tidalRecTrack][i] = ~tidalGuiElements[i][\leg][\value].value;
						~tidalRecBufS[~tidalRecTrack][i] = ~tidalGuiElements[i][\bufS][\value].value;
						~tidalRecBuf[~tidalRecTrack][i] = ~tidalGuiElements[i][\buf][\value].value;
						~tidalRecBuf2[~tidalRecTrack][i] = ~tidalGuiElements[i][\buf2][\value].value;
					};
				}),
			),

			HLayout(
				~tidalTrigTempButton1 = Button.new.maxWidth_(30).states_([["T1", Color.white, Color.grey]])
				.action_({ |v| ~tidalRecTrack = 0;
					v.states_([["T1", Color.white, Color.blue]]);
					~tidalTrigTempButton2.states_([["T2", Color.white, Color.grey]]);
					~tidalTrigTempButton3.states_([["T3", Color.white, Color.grey]]);
					~tidalNbOfOrbits.do { |i|
						~tidalGuiElements[i][\seqLine][\value].valueAction_(~tidalRecSeqline[~tidalRecTrack][i]);
						~tidalGuiElements[i][\rytS][\value].valueAction_(~tidalRecRytS[~tidalRecTrack][i]);
						~tidalGuiElements[i][\ryt][\value].valueAction_(~tidalRecRyt[~tidalRecTrack][i]);
						~tidalGuiElements[i][\legS][\value].valueAction_(~tidalRecLegS[~tidalRecTrack][i]);
						~tidalGuiElements[i][\leg][\value].valueAction_(~tidalRecLeg[~tidalRecTrack][i]);
						~tidalGuiElements[i][\bufS][\value].valueAction_(~tidalRecBufS[~tidalRecTrack][i]);
						~tidalGuiElements[i][\buf][\value].valueAction_(~tidalRecBuf[~tidalRecTrack][i]);
						~tidalGuiElements[i][\buf2][\value].valueAction_(~tidalRecBuf2[~tidalRecTrack][i]);
					};
				}),
				~tidalTrigTempButton2 = Button.new.maxWidth_(30).states_([["T2", Color.white, Color.grey]])
				.action_({ |v| ~tidalRecTrack = 1;
					v.states_([["T2", Color.white, Color.blue]]);
					~tidalTrigTempButton1.states_([["T1", Color.white, Color.grey]]);
					~tidalTrigTempButton3.states_([["T3", Color.white, Color.grey]]);
					~tidalNbOfOrbits.do { |i|
						~tidalGuiElements[i][\seqLine][\value].valueAction_(~tidalRecSeqline[~tidalRecTrack][i]);
						~tidalGuiElements[i][\rytS][\value].valueAction_(~tidalRecRytS[~tidalRecTrack][i]);
						~tidalGuiElements[i][\ryt][\value].valueAction_(~tidalRecRyt[~tidalRecTrack][i]);
						~tidalGuiElements[i][\legS][\value].valueAction_(~tidalRecLegS[~tidalRecTrack][i]);
						~tidalGuiElements[i][\leg][\value].valueAction_(~tidalRecLeg[~tidalRecTrack][i]);
						~tidalGuiElements[i][\bufS][\value].valueAction_(~tidalRecBufS[~tidalRecTrack][i]);
						~tidalGuiElements[i][\buf][\value].valueAction_(~tidalRecBuf[~tidalRecTrack][i]);
						~tidalGuiElements[i][\buf2][\value].valueAction_(~tidalRecBuf2[~tidalRecTrack][i]);
					};
				}),
				~tidalTrigTempButton3 = Button.new.maxWidth_(30).states_([["T3", Color.white, Color.grey]])
				.action_({ |v| ~tidalRecTrack = 2;
					v.states_([["T3", Color.white, Color.blue]]);
					~tidalTrigTempButton1.states_([["T1", Color.white, Color.grey]]);
					~tidalTrigTempButton2.states_([["T2", Color.white, Color.grey]]);
					~tidalNbOfOrbits.do { |i|
						~tidalGuiElements[i][\seqLine][\value].valueAction_(~tidalRecSeqline[~tidalRecTrack][i]);
						~tidalGuiElements[i][\rytS][\value].valueAction_(~tidalRecRytS[~tidalRecTrack][i]);
						~tidalGuiElements[i][\ryt][\value].valueAction_(~tidalRecRyt[~tidalRecTrack][i]);
						~tidalGuiElements[i][\legS][\value].valueAction_(~tidalRecLegS[~tidalRecTrack][i]);
						~tidalGuiElements[i][\leg][\value].valueAction_(~tidalRecLeg[~tidalRecTrack][i]);
						~tidalGuiElements[i][\bufS][\value].valueAction_(~tidalRecBufS[~tidalRecTrack][i]);
						~tidalGuiElements[i][\buf][\value].valueAction_(~tidalRecBuf[~tidalRecTrack][i]);
						~tidalGuiElements[i][\buf2][\value].valueAction_(~tidalRecBuf2[~tidalRecTrack][i]);
					};
				}),
			),

			10,

			HLayout(
				StaticText.new.string_("TraK Preset").fixedHeight_(15).maxWidth_(70).align_(\center),
				~tidalTrackPresetNumBox = NumberBox.new.maxWidth_(24).background_(Color.black).stringColor_(Color.white).normalColor_(Color.white).clipLo_(1).clipHi_(8).align_(\center)
				.action_({ |v|
					~tidalTrackPresetNumber = v.value.asInteger - 1;
				}).valueAction_(1);
			),

			~tidalTrackPresetTextField = TextField.new.maxWidth_(100).background_(Color.black).stringColor_(Color.white).string = "TPresetName",
			~tidalTrackPresetListView,
			Button.new/*.string_("Load Tr Preset")*/.states_([["Load Tr Preset", Color.blue, Color.white]]).maxWidth_(110) // maxWidth added
			.action_({ |view|
				this.loadTrackPreset;
				handler.emitEvent(\updateUI);
			}),
			Button.new/*.string_("Save Tr Preset")*/.states_([["Save Tr Preset", Color.black, Color.green]]).maxWidth_(110) // maxWidth added
			.action_({
				handler.emitEvent(\updateActiveOrbit);
				this.saveTrackPreset;
			}),
			10,
			Button.new/*.string_("Delete Tr Preset")*/.states_([["Delete Tr Preset", Color.red, Color.white]]).maxWidth_(110) // maxWidth added
			.action_({
				var pos = ~tidalTrackPresetListView.items.indexOfEqual(~tidalTrackPresetTextField.value ++ ".json");
				// ~tidalPresetTrackXPath.postln; // Put into a variable
				// presetTrackPath.resolveRelative.postln; // since this line triggers : ERROR: can't resolve relative to an unsaved file.
				// (~tidalPresetTrackXPath ++ ~tidalTrackPresetTextField.value ++ ".json").postln;
				// .parentLevelPath(2) // from atk-sc3 to go back within the structure
				File.delete(~tidalPresetTrackXPath ++ ~tidalTrackPresetTextField.value ++ ".json");
				this.prLoadTrackPresetFiles;
				~tidalTrackPresetListView.items_(presetTrackFiles)/*.valueAction_(0)*/ ;
				~tidalTrackPresetListView.valueAction_((pos-1).max(0));
				~tidalPresetTrackSize = ~tidalTrackPresetListView.items.size;
				~tidalPresetLine = (1 .. ~tidalPresetTrackSize /*~tidalTrackPresetListView.items.size*/);
			}),
			10,
			~tidalResetAllButtonView = Button.new/*.string_("Reset All")*/.states_([["Reset All", Color.white, Color.black]]).maxWidth_(110) // maxWidth added
			.action_({
				|view|
				handler.emitEvent(\resetAll);
				~tidalMuteAllButton.valueAction_(0);
				~tidalMute2AllButton.valueAction_(0);
			})
		)
	}


	addLoadPresetListener {
		OSCFunc ({|msg|
			{
				var receivedPresetFile = msg[1];
				var presetFilesAsSymbol = presetFiles.collect({|item| item.asSymbol});
				presetFile = receivedPresetFile;

				this.loadPreset;
				/*presetListView*/ ~tidalpresetListView.value = presetFilesAsSymbol.indexOf(receivedPresetFile.asSymbol);

				handler.emitEvent(\updateUI);
			}.defer;
		}, ("/SuperDirtMixer/loadPreset"), recvPort: 57120).fix;
	}


	addTidalvstPresetListener { OSCFunc ({|msg|
		{
			var fxName = msg[1];
			var preset = msg[2];

			var combinedDictionary = Dictionary.new;
			var keys;

			combinedDictionary.putPairs(~tidalvst.instruments);
			combinedDictionary.putPairs(~tidalvst.fxs);

			keys = combinedDictionary.keys();

			if (keys.includes(fxName), {
				~tidalvst.loadPreset(fxName, preset);
			});
		}.defer;
	}, ("/SuperDirtMixer/tidalvstPreset"), recvPort: 57120).fix;
	}

}