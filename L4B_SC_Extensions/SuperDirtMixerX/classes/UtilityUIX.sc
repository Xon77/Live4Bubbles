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
		// ~tidalPresetTrackXPath2 = ~tidalPresetXPath ++ ~tidalPresetTrackXFileName; // ???
		~tidalPresetTrackSize = PathName(~tidalPresetTrackXPath).files.size;

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

		~tidalPresetLineBegin = 1 ! ~tidalNbOfOrbits;
		~tidalPresetLineEnd = ~tidalPresetTrackSize ! ~tidalNbOfOrbits;

		if (handler.isNil.not, {
			handler.subscribe(this, \extendDefaultParentEvent);
		});

		if (presetFile.isNil.not, {

			this.prLoadPresetFiles;

			~tidalPresetListView /*presetListView*/ = ListView(nil,Rect(10,10,30,30)).maxWidth_(100 /*150*/).maxHeight_(46).selectedStringColor_(Color.white)
			.items_(presetFiles)
			.action_({ arg sbs;
				presetFile = /*presetListView*/ ~tidalPresetListView.items[sbs.value]; // .value returns the integer
			});

			/*presetListView*/ ~tidalPresetListView.value = nil;

			this.addTidalvstPresetListener;
			this.addLoadPresetListener;
		});

		// Implementation of a local Track Preset on some parameters except Volume / Mute / Solo & Reverb
		this.prLoadTrackPresetFiles;

		~tidalAllPresetListView = ListView(nil,Rect(10,10,10,0)).minHeight_(200).selectedStringColor_(Color.white)/*.allowsReselection_(1)*/
		// PopUpMenu.allowsReselection_(true)
		.action_({ arg sbs; var size, t, d;
			~tidalAllPresetTextField.string = sbs.item;

			size = ~tidalAllPresetsData[~tidalAllPresetTextField.string.asSymbol][0].size-1;
			t = ~tidalAllPresetsData[~tidalAllPresetTextField.string.asSymbol];
			d = ~tidalTrackPresetListView.items.collect { |i| i.split($ )[1] };
			// d.postln;
			(size..0).do { |i|
					/*t[i][0].postln;
					t[i][0].class.postln;*/
				if  (~tidalAllPresetSwitchNT == 0, { // Load value of the preset
					~tidalGuiElements[i][\preset][\value].valueAction_(t[0][i][0].asInteger);
				},{ // Load text of the preset
					~tidalGuiElements[i][\preset][\value].valueAction_(d.indexOfEqual(t[0][i][1])+1);
				});

				if (~tidalPresetSeqLineMinMax == 1, {
					~tidalGuiElements[i][\presetMin][\value].valueAction_(t[1][i]);
					~tidalGuiElements[i][\presetMax][\value].valueAction_(t[2][i]);
					~tidalGuiElements[i][\seqLineMin][\value].valueAction_(t[3][i]);
					~tidalGuiElements[i][\seqLineMax][\value].valueAction_(t[4][i]);
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
			if (~tidalLoadSwitch == 0, {
				~tidalGuiElements[~tidalTrackPresetNumber][\preset][\value].valueAction_(sbs.value+1);
			});
		}).maxWidth_(112 /*150*/)/*.maxHeight_(200)*/;

		~tidalRecTrack = 0;
		~tidalRecTemp = 0 ! 6; // To record 3 temporary states / presets - TO DO
		~tidalRecSeqline = 0 ! ~tidalNbOfOrbits ! 6;
		~tidalRecRytS = 0 ! ~tidalNbOfOrbits ! 6;
		~tidalRecRyt = 0 ! ~tidalNbOfOrbits ! 6;
		~tidalRecLegS = 1 ! ~tidalNbOfOrbits ! 6;
		~tidalRecLeg = 1 ! ~tidalNbOfOrbits ! 6;
		~tidalRecBufS = 0 ! ~tidalNbOfOrbits ! 6;
		~tidalRecBuf = 0 ! ~tidalNbOfOrbits ! 6;
		~tidalRecBuf2 = 0 ! ~tidalNbOfOrbits ! 6;

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
		var defaultTrackEvents = JSONlib.convertToSC(File.readAllString((presetTrackPath ++ ~tidalTrackPresetListView.item.split($ )[1] ++ ".json" /*presetTrackFile*/).resolveRelative, "r"));

		defaultTrackEvents.do({
			arg defaultEvent /*, index*/;
			// defaultEvent.postln;

			/*defaultParentEvent.keysValuesDo*/
			[\seqLine, \ryt, \rytS, \leg, \legS, \bufS, \bufS2, \buf, \bufR, \buf2, \buf2R, \rat, \ran, \fxs, \fxs2, \fxx, \fxx2, \fxt, \fxt2, \fxp, \fxp2, \fxv, \fxv2, \fus, \fus2, \fux, \fux2, \fut, \fut2, \fup, \fup2, \fuv, \fuv2, \pan, \spa /*, \masterGain, \mute, reverbVariableName.asSymbol*/
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
		[\seqLine, \ryt, \rytS, \leg, \legS, \bufS, \bufS2, \buf, \bufR, \buf2, \buf2R, \rat, \ran, \fxs, \fxs2, \fxx, \fxx2, \fxt, \fxt2, \fxp, \fxp2, \fxv, \fxv2, \fus, \fus2, \fux, \fux2, \fut, \fut2, \fup, \fup2, \fuv, \fuv2, \pan, \spa/*, \masterGain, \mute, reverbVariableName.asSymbol*/
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
		~tidalPresetTrackSize = ~tidalTrackPresetListView.items.size;
		~tidalNbOfOrbits.do { |i|
			~tidalGuiElements[i][\presetMax][\value].clipHi_(~tidalPresetTrackSize);
			~tidalGuiElements[i][\presetMax][\value].value_(~tidalPresetTrackSize);
			~tidalGuiElements[i][\preset][\value].clipHi_(~tidalPresetTrackSize);
			~tidalPresetLine[i] = (1 .. ~tidalPresetTrackSize); // to update data of controller XoneK2
			// ~tidalGuiElements[0][\preset][\element]
		};
		~tidalPresetLineEndNumBox.clipHi_(~tidalPresetLine[0].last);
		~tidalPresetLineEndNumBox.value_(~tidalPresetLine[0].last);
		~tidalGuiElements[~tidalTrackPresetNumber][\preset][\value].value_(~tidalTrackPresetListView.items.collect {|i| i. split($ )[1]}.indexOfEqual(~tidalTrackPresetTextField.value)+1);
		~tidalTrackPresetListView.value_(/*(~tidalTrackPresetListView.items*/ ~tidalTrackPresetListView.items.collect {|i| i. split($ )[1]}.indexOfEqual(~tidalTrackPresetTextField.value));
	}

	/* DEFINE PRESET UI */
	createUI {
		| container |
		container.layout = VLayout(
			HLayout(StaticText.new.string_("Global Preset").fixedHeight_(15).maxWidth_(100).align_(\center)), // maxWidth added
			/*Button.new.states_([["Mute All", Color.black, Color.white], ["Unmute All", Color.white, Color.blue]])
			.action_({
			|view|
			if(view.value == 0) { this.tidalNetAddr.sendMsg("/unmuteAll") };
			if(view.value == 1) { this.tidalNetAddr.sendMsg("/muteAll") };
			}),*/
			HLayout(~tidalPresetListView) /*presetListView*/,
			HLayout(~tidalLoadPresetButtonView = Button.new/*.string_("Load Gl Preset")*/.states_([["Load Gl Preset", Color.blue, Color.white]]).maxWidth_(100) // maxWidth added
				.action_({
					|view|
					this.loadPreset;
					handler.emitEvent(\updateUI);
			})),
			HLayout(Button.new/*.string_("Save Gl Preset")*/.states_([["Save Gl Preset", Color.black, Color.green]]).maxWidth_(100) // maxWidth added
				.action_({
					handler.emitEvent(\updateActiveOrbit);
					this.savePreset;
			})),


			HLayout(StaticText.new.string_("---------------").fixedHeight_(15).maxWidth_(100).align_(\center)),

			HLayout(StaticText.new.string_("X Presets").fixedHeight_(15).maxWidth_(100).align_(\center)),

			HLayout(~tidalAllPresetTextField = TextField.new.maxWidth_(110).background_(Color.black).stringColor_(Color.white).string = "XPresetsName"),

			HLayout(~tidalAllPresetListView),

			HLayout(
				~tidalAllPresetSwitchNTButton = Button.new.maxWidth_(50).states_([["Nb", Color.white, Color.gray],["Text", Color.white, Color.gray]])
				.action_({ |v| ~tidalAllPresetSwitchNT = v.value;
				}).valueAction_(1),

				~tidalLoad0PresetButton = Button.new.maxWidth_(30).states_([["0", Color.white, Color.black]])
				.action_({ |v| ~tidalAllPresetListView.valueAction_(0);
				})
			),

			HLayout(Button.new/*.string_("Load Gl Preset")*/.states_([["Load X Presets", Color.blue, Color.white]]).maxWidth_(110) // maxWidth added
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
							~tidalGuiElements[i][\preset][\value].valueAction_(t[0][i][0].asInteger);
						},{ // Load text of the preset
							~tidalGuiElements[i][\preset][\value].valueAction_(d.indexOfEqual(t[0][i][1])+1);
						});

						~tidalGuiElements[i][\presetMin][\value].valueAction_(t[1][i]);
						~tidalGuiElements[i][\presetMax][\value].valueAction_(t[2][i]);
						~tidalGuiElements[i][\seqLineMin][\value].valueAction_(t[3][i]);
						~tidalGuiElements[i][\seqLineMax][\value].valueAction_(t[4][i]);
					}
			})),
			HLayout(Button.new/*.string_("Save Gl Preset")*/.states_([["Save X Presets", Color.black, Color.green]]).maxWidth_(110) // maxWidth added
				.action_({
					~tidalAllPresetsData.put(~tidalAllPresetTextField.string.asSymbol, [~tidalNbOfOrbits.collect { |i| [ ~tidalTrackPresetListView.items[~tidalPresetNb[i]-1].split($.)[0], ~tidalTrackPresetListView.items[~tidalPresetNb[i]-1].split($.)[1].stripWhiteSpace ] }, ~tidalPresetLineBegin.copy, ~tidalPresetLineEnd.copy, ~tidalSeqLineBegin.copy, ~tidalSeqLineEnd.copy] );

					~tidalPresetsFile = ZArchive.write(~tidalPresetTrackXPath2);
					~tidalPresetsFile.writeItem(~tidalAllPresetsData);
					~tidalPresetsFile.writeClose;

					~tidalAllPresetListView.items_(~tidalAllPresetsData.keys.asArray.sort);
			})),
			HLayout(Button.new/*.string_("Delete Tr Preset")*/.states_([["Delete X Presets", Color.red, Color.white]]).maxWidth_(110) // maxWidth added
				.action_({
					~tidalAllPresetsData.removeAt(~tidalAllPresetTextField.string.asSymbol);

					~tidalPresetsFile = ZArchive.write(~tidalPresetTrackXPath2);
					~tidalPresetsFile.writeItem(~tidalAllPresetsData);
					~tidalPresetsFile.writeClose;

					~tidalAllPresetListView.items_(~tidalAllPresetsData.keys.asArray.sort);
			})),


			HLayout(StaticText.new.string_("---------------").fixedHeight_(15).maxWidth_(100).align_(\center)),


			HLayout(
				~tidalRytSwitchButton = Button.new.maxWidth_(26).states_([["Ryt", Color.black, Color.green], ["Ryt", Color.white, Color.red]])
				.action_({ |v| ~tidalRytSwitch = v.value;
				}).valueAction_(1),
				~tidalLegSwitchButton = Button.new.maxWidth_(26).states_([["Dur", Color.black, Color.green],["Dur", Color.white, Color.red]])
				.action_({ |v| ~tidalLegSwitch = v.value;
				}).valueAction_(1),
				~tidalFolSwitchButton = Button.new.maxWidth_(26).states_([["Fol", Color.black, Color.green], ["Fol", Color.white, Color.red]])
				.action_({ |v| ~tidalFolSwitch = v.value;
				}).valueAction_(0),
				~tidalSpaSwitchButton = Button.new.maxWidth_(26).states_([["Spa", Color.black, Color.green], ["Spa", Color.white, Color.red]])
				.action_({ |v| ~tidalSpaSwitch = v.value;
				}).valueAction_(0),
			),

			5,

			HLayout(
				~tidalRecTempButton1 = Button.new.maxWidth_(30).states_([["R 1", Color.white, Color.black]])
				.action_({ |v| // v.value.postln;
					// ~tidalRecTrack = 0;
					if (v. value == 0, {~tidalRecTrack = 0; ~tidalRecTemp[~tidalRecTrack] = 1} );
					v.states_([["R1", Color.white, Color.red]]);
					~tidalTrigTempButton1.states_([["T 1", Color.white, Color.blue]]);
					if (~tidalRecTemp[1] == 1, {
						~tidalRecTempButton2.states_([["R 2", Color.black, Color.yellow]]);
						~tidalTrigTempButton2.states_([["T 2", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton2.states_([["R 2", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[2] == 1, {
						~tidalRecTempButton3.states_([["R 3", Color.black, Color.yellow]]);
						~tidalTrigTempButton3.states_([["T 3", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton3.states_([["R 3", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[3] == 1, {
						~tidalRecTempButton4.states_([["R 4", Color.black, Color.yellow]]);
						~tidalTrigTempButton4.states_([["T 4", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton4.states_([["R 4", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[4] == 1, {
						~tidalRecTempButton5.states_([["R 5", Color.black, Color.yellow]]);
						~tidalTrigTempButton5.states_([["T 5", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton5.states_([["R 5", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[5] == 1, {
						~tidalRecTempButton6.states_([["R 6", Color.black, Color.yellow]]);
						~tidalTrigTempButton6.states_([["T 6", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton6.states_([["R 6", Color.white, Color.black]]);
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
				~tidalRecTempButton2 = Button.new.maxWidth_(30).states_([["R 2", Color.white, Color.black]])
				.action_({ |v|
					if (v. value == 0, {~tidalRecTrack = 1; ~tidalRecTemp[~tidalRecTrack] = 1} );
					v.states_([["R2", Color.white, Color.red]]);
					~tidalTrigTempButton2.states_([["T 2", Color.white, Color.blue]]);
					if (~tidalRecTemp[0] == 1, {
						~tidalRecTempButton1.states_([["R 1", Color.black, Color.yellow]]);
						~tidalTrigTempButton1.states_([["T 1", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton1.states_([["R 1", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[2] == 1, {
						~tidalRecTempButton3.states_([["R 3", Color.black, Color.yellow]]);
						~tidalTrigTempButton3.states_([["T 3", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton3.states_([["R 3", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[3] == 1, {
						~tidalRecTempButton4.states_([["R 4", Color.black, Color.yellow]]);
						~tidalTrigTempButton4.states_([["T 4", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton4.states_([["R 4", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[4] == 1, {
						~tidalRecTempButton5.states_([["R 5", Color.black, Color.yellow]]);
						~tidalTrigTempButton5.states_([["T 5", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton5.states_([["R 5", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[5] == 1, {
						~tidalRecTempButton6.states_([["R 6", Color.black, Color.yellow]]);
						~tidalTrigTempButton6.states_([["T 6", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton6.states_([["R 6", Color.white, Color.black]]);
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
				~tidalRecTempButton3 = Button.new.maxWidth_(30).states_([["R 3", Color.white, Color.black]])
				.action_({ |v|
					if (v. value == 0, {~tidalRecTrack = 2; ~tidalRecTemp[~tidalRecTrack] = 1} );
					v.states_([["R3", Color.white, Color.red]]);
					~tidalTrigTempButton3.states_([["T 3", Color.white, Color.blue]]);
					if (~tidalRecTemp[0] == 1, {
						~tidalRecTempButton1.states_([["R 1", Color.black, Color.yellow]]);
						~tidalTrigTempButton1.states_([["T 1", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton1.states_([["R 1", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[1] == 1, {
						~tidalRecTempButton2.states_([["R 2", Color.black, Color.yellow]]);
						~tidalTrigTempButton2.states_([["T 2", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton2.states_([["R 2", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[3] == 1, {
						~tidalRecTempButton4.states_([["R 4", Color.black, Color.yellow]]);
						~tidalTrigTempButton4.states_([["T 4", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton4.states_([["R 4", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[4] == 1, {
						~tidalRecTempButton5.states_([["R 5", Color.black, Color.yellow]]);
						~tidalTrigTempButton5.states_([["T 5", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton5.states_([["R 5", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[5] == 1, {
						~tidalRecTempButton6.states_([["R 6", Color.black, Color.yellow]]);
						~tidalTrigTempButton6.states_([["T 6", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton6.states_([["R 6", Color.white, Color.black]]);
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
				})
			),
			HLayout(
				~tidalRecTempButton4 = Button.new.maxWidth_(30).states_([["R 4", Color.white, Color.black]])
				.action_({ |v|
					if (v. value == 0, {~tidalRecTrack = 3; ~tidalRecTemp[~tidalRecTrack] = 1} );
					v.states_([["R4", Color.white, Color.red]]);
					~tidalTrigTempButton4.states_([["T 4", Color.white, Color.blue]]);
					if (~tidalRecTemp[0] == 1, {
						~tidalRecTempButton1.states_([["R 1", Color.black, Color.yellow]]);
						~tidalTrigTempButton1.states_([["T 1", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton1.states_([["R 1", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[1] == 1, {
						~tidalRecTempButton2.states_([["R 2", Color.black, Color.yellow]]);
						~tidalTrigTempButton2.states_([["T 2", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton2.states_([["R 2", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[2] == 1, {
						~tidalRecTempButton3.states_([["R 3", Color.black, Color.yellow]]);
						~tidalTrigTempButton3.states_([["T 3", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton3.states_([["R 3", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[4] == 1, {
						~tidalRecTempButton5.states_([["R 5", Color.black, Color.yellow]]);
						~tidalTrigTempButton5.states_([["T 5", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton5.states_([["R 5", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[5] == 1, {
						~tidalRecTempButton6.states_([["R 6", Color.black, Color.yellow]]);
						~tidalTrigTempButton6.states_([["T 6", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton6.states_([["R 6", Color.white, Color.black]]);
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
				~tidalRecTempButton5 = Button.new.maxWidth_(30).states_([["R 5", Color.white, Color.black]])
				.action_({ |v|
					if (v. value == 0, {~tidalRecTrack = 4; ~tidalRecTemp[~tidalRecTrack] = 1} );
					v.states_([["R5", Color.white, Color.red]]);
					~tidalTrigTempButton5.states_([["T 5", Color.white, Color.blue]]);
					if (~tidalRecTemp[0] == 1, {
						~tidalRecTempButton1.states_([["R 1", Color.black, Color.yellow]]);
						~tidalTrigTempButton1.states_([["T 1", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton1.states_([["R 1", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[1] == 1, {
						~tidalRecTempButton2.states_([["R 2", Color.black, Color.yellow]]);
						~tidalTrigTempButton2.states_([["T 2", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton2.states_([["R 2", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[2] == 1, {
						~tidalRecTempButton3.states_([["R 3", Color.black, Color.yellow]]);
						~tidalTrigTempButton3.states_([["T 3", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton3.states_([["R 3", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[3] == 1, {
						~tidalRecTempButton4.states_([["R 4", Color.black, Color.yellow]]);
						~tidalTrigTempButton4.states_([["T 4", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton4.states_([["R 4", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[5] == 1, {
						~tidalRecTempButton6.states_([["R 6", Color.black, Color.yellow]]);
						~tidalTrigTempButton6.states_([["T 6", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton6.states_([["R 6", Color.white, Color.black]]);
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
				~tidalRecTempButton6 = Button.new.maxWidth_(30).states_([["R 6", Color.white, Color.black]])
				.action_({ |v|
					if (v. value == 0, {~tidalRecTrack = 5; ~tidalRecTemp[~tidalRecTrack] = 1} );
					v.states_([["R6", Color.white, Color.red]]);
					~tidalTrigTempButton6.states_([["T 6", Color.white, Color.blue]]);
					if (~tidalRecTemp[0] == 1, {
						~tidalRecTempButton1.states_([["R 1", Color.black, Color.yellow]]);
						~tidalTrigTempButton1.states_([["T 1", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton1.states_([["R 1", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[1] == 1, {
						~tidalRecTempButton2.states_([["R 2", Color.black, Color.yellow]]);
						~tidalTrigTempButton2.states_([["T 2", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton2.states_([["R 2", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[2] == 1, {
						~tidalRecTempButton3.states_([["R 3", Color.black, Color.yellow]]);
						~tidalTrigTempButton3.states_([["T 3", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton3.states_([["R 3", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[3] == 1, {
						~tidalRecTempButton4.states_([["R 4", Color.black, Color.yellow]]);
						~tidalTrigTempButton4.states_([["T 4", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton4.states_([["R 4", Color.white, Color.black]]);
					});
					if (~tidalRecTemp[4] == 1, {
						~tidalRecTempButton5.states_([["R 5", Color.black, Color.yellow]]);
						~tidalTrigTempButton5.states_([["T 5", Color.white, Color.gray]]);
					},{
						~tidalRecTempButton5.states_([["R 5", Color.white, Color.black]]);
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
				})
			),

			HLayout(
				~tidalTrigTempButton1 = Button.new.maxWidth_(30).states_([["T 1", Color.white, Color.grey]])
				.action_({ |v| ~tidalRecTrack = 0;
					v.states_([["T 1", Color.white, Color.blue]]);
					~tidalTrigTempButton2.states_([["T 2", Color.white, Color.grey]]);
					~tidalTrigTempButton3.states_([["T 3", Color.white, Color.grey]]);
					~tidalTrigTempButton4.states_([["T 4", Color.white, Color.grey]]);
					~tidalTrigTempButton5.states_([["T 5", Color.white, Color.grey]]);
					~tidalTrigTempButton6.states_([["T 6", Color.white, Color.grey]]);
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
				~tidalTrigTempButton2 = Button.new.maxWidth_(30).states_([["T 2", Color.white, Color.grey]])
				.action_({ |v| ~tidalRecTrack = 1;
					v.states_([["T 2", Color.white, Color.blue]]);
					~tidalTrigTempButton1.states_([["T 1", Color.white, Color.grey]]);
					~tidalTrigTempButton3.states_([["T 3", Color.white, Color.grey]]);
					~tidalTrigTempButton4.states_([["T 4", Color.white, Color.grey]]);
					~tidalTrigTempButton5.states_([["T 5", Color.white, Color.grey]]);
					~tidalTrigTempButton6.states_([["T 6", Color.white, Color.grey]]);
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
				~tidalTrigTempButton3 = Button.new.maxWidth_(30).states_([["T 3", Color.white, Color.grey]])
				.action_({ |v| ~tidalRecTrack = 2;
					v.states_([["T 3", Color.white, Color.blue]]);
					~tidalTrigTempButton1.states_([["T 1", Color.white, Color.grey]]);
					~tidalTrigTempButton2.states_([["T 2", Color.white, Color.grey]]);
					~tidalTrigTempButton4.states_([["T 4", Color.white, Color.grey]]);
					~tidalTrigTempButton5.states_([["T 5", Color.white, Color.grey]]);
					~tidalTrigTempButton6.states_([["T 6", Color.white, Color.grey]]);
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

			HLayout(
				~tidalTrigTempButton4 = Button.new.maxWidth_(30).states_([["T 4", Color.white, Color.grey]])
				.action_({ |v| ~tidalRecTrack = 3;
					v.states_([["T 4", Color.white, Color.blue]]);
					~tidalTrigTempButton2.states_([["T 2", Color.white, Color.grey]]);
					~tidalTrigTempButton3.states_([["T 3", Color.white, Color.grey]]);
					~tidalTrigTempButton1.states_([["T 1", Color.white, Color.grey]]);
					~tidalTrigTempButton5.states_([["T 5", Color.white, Color.grey]]);
					~tidalTrigTempButton6.states_([["T 6", Color.white, Color.grey]]);
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
				~tidalTrigTempButton5 = Button.new.maxWidth_(30).states_([["T 5", Color.white, Color.grey]])
				.action_({ |v| ~tidalRecTrack = 4;
					v.states_([["T 5", Color.white, Color.blue]]);
					~tidalTrigTempButton2.states_([["T 2", Color.white, Color.grey]]);
					~tidalTrigTempButton3.states_([["T 3", Color.white, Color.grey]]);
					~tidalTrigTempButton4.states_([["T 4", Color.white, Color.grey]]);
					~tidalTrigTempButton1.states_([["T 1", Color.white, Color.grey]]);
					~tidalTrigTempButton6.states_([["T 6", Color.white, Color.grey]]);
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
				~tidalTrigTempButton6 = Button.new.maxWidth_(30).states_([["T 6", Color.white, Color.grey]])
				.action_({ |v| ~tidalRecTrack = 5;
					v.states_([["T 6", Color.white, Color.blue]]);
					~tidalTrigTempButton1.states_([["T 1", Color.white, Color.grey]]);
					~tidalTrigTempButton2.states_([["T 2", Color.white, Color.grey]]);
					~tidalTrigTempButton3.states_([["T 3", Color.white, Color.grey]]);
					~tidalTrigTempButton4.states_([["T 4", Color.white, Color.grey]]);
					~tidalTrigTempButton5.states_([["T 5", Color.white, Color.grey]]);
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
				})
			),

			5,

			HLayout(
				~tidalXFadeANumBox = NumberBox.new.maxWidth_(35).background_(Color.blue(0.6)).stringColor_(Color.yellow).normalColor_(Color.yellow).clipLo_(0).clipHi_(12).step_(1).scroll_step_(1).align_(\center)
				.action_({ |v|

					~tidalXFadeA = v.value.asInteger + ~tidalMainControlInterpolationA /* - 1*/;

					// ~tidalNetAddr.sendMsg("/ctrl", "xFadeA", ~tidalXFadeA)
					if (~tidalEvalTabPaneSwitch == 1, {
						~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'pane', ~tidalMainControlPane, 'tab', ~tidalMainControlTab, \row, ~tidalXFadeA, \column, 1);

					},{
						~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', ~tidalMainControlTab, \row, ~tidalXFadeA, \column, 1);
					});

				}).valueAction_(0),

				~tidalXFadeBPMButton = Button.new.maxWidth_(25).states_([["0", Color.white, Color.grey]])
				.action_({ |v|
					~tidalXFadeANumBox.valueAction_(0);
					~tidalBPMANumBox.valueAction_(0);
				}),

				~tidalBPMANumBox = NumberBox.new.maxWidth_(35).background_(Color.magenta).stringColor_(Color.yellow).normalColor_(Color.yellow).clipLo_(0).clipHi_(~tidalMainControlBPMADur).step_(1).align_(\center)
				.action_({ |v|
					~tidalBPMA = v.value.asInteger + ~tidalMainControlBPMA /* - 1*/;

					if (~tidalBPMA != ~tidalBPMAPrev, {

						// ~tidalNetAddr.sendMsg("/ctrl", "xFadeA", ~tidalBPMA)
						if (~tidalEvalTabPaneSwitch == 1, {
							~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'pane', ~tidalMainControlPane, 'tab', ~tidalMainControlTab, \row, ~tidalBPMA, \column, 1);

						},{
							~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', ~tidalMainControlTab, \row, ~tidalBPMA, \column, 1);
						});

						// ~tidalBPMA.postln;

					});

					~tidalBPMAPrev = ~tidalBPMA;

				}).valueAction_(0)
			),

			5,

			HLayout(
				StaticText.new.string_("Track").fixedHeight_(15).maxWidth_(30).align_(\center),
				~tidalTrackPresetNumBox = NumberBox.new.maxWidth_(24).background_(Color.black).stringColor_(Color.white).normalColor_(Color.white).clipLo_(1).clipHi_(8).step_(1).align_(\center)
				.action_({ |v|
					~tidalTrackPresetNumber = v.value.asInteger - 1;
				}).valueAction_(1),
				~tidalLoadSwitchButton = Button.new.maxWidth_(33).states_([["Load", Color.black, Color.green], ["Load", Color.white, Color.red]])
				.action_({ |v| ~tidalLoadSwitch = v.value;
				}).valueAction_(0)
			),

			HLayout(~tidalTrackPresetTextField = TextField.new.maxWidth_(110).background_(Color.black).stringColor_(Color.white).string = "TPresetName"),
			HLayout(~tidalTrackPresetListView),
			HLayout(Button.new/*.string_("Load Tr Preset")*/.states_([["Load Tr Preset", Color.blue, Color.white]]).maxWidth_(110) // maxWidth added
				.action_({ |view|
					this.loadTrackPreset;
					handler.emitEvent(\updateUI);
			})),
			HLayout(Button.new/*.string_("Save Tr Preset")*/.states_([["Save Tr Preset", Color.black, Color.green]]).maxWidth_(110) // maxWidth added
				.action_({
					this.saveTrackPreset;
					handler.emitEvent(\updateActiveOrbit);
			})),
			HLayout(Button.new/*.string_("Delete Tr Preset")*/.states_([["Delete Tr Preset", Color.red, Color.white]]).maxWidth_(110) // maxWidth added
				.action_({
					var pos = ~tidalTrackPresetListView.items.collect {|i| i. split($ )[1]}.indexOfEqual(~tidalTrackPresetTextField.value);
					// ~tidalPresetTrackXPath.postln; // Put into a variable
					// presetTrackPath.resolveRelative.postln; // since this line triggers : ERROR: can't resolve relative to an unsaved file.
					// (~tidalPresetTrackXPath ++ ~tidalTrackPresetTextField.value ++ ".json").postln;
					// .parentLevelPath(2) // from atk-sc3 to go back within the structure
					File.delete(~tidalPresetTrackXPath ++ ~tidalTrackPresetTextField.value ++ ".json");
					this.prLoadTrackPresetFiles;
				~tidalTrackPresetListView.items_(presetTrackFiles)/*.valueAction_(0)*/ ;
					~tidalPresetTrackSize = ~tidalTrackPresetListView.items.size;
					~tidalNbOfOrbits.do { |i|
						~tidalGuiElements[i][\presetMax][\value].clipHi_(~tidalPresetTrackSize);
						~tidalGuiElements[i][\presetMax][\value].value_(~tidalPresetTrackSize);
						~tidalGuiElements[i][\preset][\value].clipHi_(~tidalPresetTrackSize);
						~tidalPresetLine[i] = (1 .. (~tidalPresetTrackSize));
					};
					~tidalPresetLineEndNumBox.clipHi_(~tidalPresetLine[0].last);
					~tidalPresetLineEndNumBox.value_(~tidalPresetLine[0].last);
					~tidalTrackPresetListView.valueAction_((pos-1).max(0));
			})),
			10,
			HLayout(~tidalResetAllButtonView = Button.new/*.string_("Reset All")*/.states_([["Reset All", Color.white, Color.black]]).maxWidth_(110) // maxWidth added
				.action_({
					|view|
					handler.emitEvent(\resetAll);
					~tidalMuteAllButton.valueAction_(0);
					~tidalMute2AllButton.valueAction_(0);
			}))
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