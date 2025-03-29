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
		presetTrackPath = "../presetsTr/" /*initPresetPath.postln*/;
		presetTrackFile = 'DefaultT.json'; // necessary ?

		~presetTrackPath = presetTrackPath.resolveRelative; // Necessary to put into a variable to get the path

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

		~tidalAllPresetListView = ListView(nil,Rect(10,10,30,30)).minHeight_(200).selectedStringColor_(Color.white)
		// PopUpMenu.allowsReselection_(true)
		.action_({ arg sbs;
		}).maxWidth_(112 /*150*/)/*.maxHeight_(200)*/;

		~tidalTrackPresetListView = ListView(nil,Rect(10,10,80,30)).minWidth_(80).maxHeight_(100).selectedStringColor_(Color.white)
		.items_(presetTrackFiles)
		.action_({ arg sbs;
			~tidalAllPresetNb = ~tidalTrackPresetListView.items[sbs.value]; // .value returns the integer
			~tidalTrackPresetTextField.string = ~tidalTrackPreset.split($.)[1];
		}).maxWidth_(112 /*150*/)/*.maxHeight_(200)*/;

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
			orbit.postln;

			defaultParentEvent.keys.do({|eventKey| presetEvent.put(eventKey, orbit.get(eventKey)) });

			orbitPresets.add(presetEvent);
		});

		file.write(*JSONlib.convertToJSON(orbitPresets));
		file.close;
	}

	loadTrackPreset {
		var defaultTrackEvents = JSONlib.convertToSC(File.readAllString((presetTrackPath ++ ~tidalTrackPresetListView.item /*presetTrackFile*/).resolveRelative, "r"));

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
			Button.new/*.string_("Save Gl Preset")*/.states_([["Save Gl Preset", Color.black, Color.green]]).maxWidth_(100) // maxWidth added
			.action_({
				handler.emitEvent(\updateActiveOrbit);
				this.savePreset;
			}),
			~tidalLoadPresetButtonView = Button.new/*.string_("Load Gl Preset")*/.states_([["Load Gl Preset", Color.blue, Color.white]]).maxWidth_(100) // maxWidth added
			.action_({
				|view|
				this.loadPreset;
				handler.emitEvent(\updateUI);
			}),

			10,

			// HLayout(StaticText.new.string_("---------------").fixedHeight_(15).maxWidth_(100).align_(\center)),

			HLayout(StaticText.new.string_("Presets").fixedHeight_(15).maxWidth_(70).align_(\center)),
			~tidalAllPresetListView,

			// HLayout(StaticText.new.string_("---------------").fixedHeight_(15).maxWidth_(100).align_(\center)),

			10,

			HLayout(
				~tidalRecTempButton1 = Button.new.maxWidth_(24).states_([["1", Color.white, Color.black]])
				.action_({ |v|
					/*~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\ryt][\value].valueAction_(0);
						~tidalGuiElements[i][\legS][\value].valueAction_(0);
						~tidalGuiElements[i][\leg][\value].valueAction_(1.0);
						~tidalGuiElements[i][\buf][\value].valueAction_(0);
						~tidalGuiElements[i][\buf2][\value].valueAction_(0);
					};*/
				}),
				~tidalRecTempButton2 = Button.new.maxWidth_(24).states_([["2", Color.white, Color.black]])
				.action_({ |v|
				}),
				~tidalRecTempButton3 = Button.new.maxWidth_(24).states_([["3", Color.white, Color.black]])
				.action_({ |v|
				}),

			),

			10,

			HLayout(
				StaticText.new.string_("Trak Preset").fixedHeight_(15).maxWidth_(70).align_(\center),
				~tidalTrackPresetNumBox = NumberBox.new.maxWidth_(24).background_(Color.black).stringColor_(Color.white).normalColor_(Color.white).clipLo_(1).clipHi_(8).align_(\center)
				.action_({ |v|
					~tidalTrackPresetNumber = v.value.asInteger - 1;
				}).valueAction_(1);
			),

			~tidalTrackPresetTextField = TextField.new.maxWidth_(100).background_(Color.black).stringColor_(Color.white).string = "PresetName",
			~tidalTrackPresetListView,
			Button.new/*.string_("Save Tr Preset")*/.states_([["Save Tr Preset", Color.black, Color.green]]).maxWidth_(100) // maxWidth added
			.action_({
				handler.emitEvent(\updateActiveOrbit);
				this.saveTrackPreset;
			}),
			Button.new/*.string_("Load Tr Preset")*/.states_([["Load Tr Preset", Color.blue, Color.white]]).maxWidth_(100) // maxWidth added
			.action_({ |view|
				this.loadTrackPreset;
				handler.emitEvent(\updateUI);
			}),
			20,
			Button.new/*.string_("Delete Tr Preset")*/.states_([["Delete Tr Preset", Color.red, Color.white]]).maxWidth_(100) // maxWidth added
			.action_({
				var pos = ~tidalTrackPresetListView.items.indexOfEqual(~tidalTrackPresetTextField.value ++ ".json");
				// ~presetTrackPath.postln; // Put into a variable
				// presetTrackPath.resolveRelative.postln; // since this line triggers : ERROR: can't resolve relative to an unsaved file.
				// (~presetTrackPath ++ ~tidalTrackPresetTextField.value ++ ".json").postln;
				// .parentLevelPath(2) // from atk-sc3 to go back within the structure
				File.delete(~presetTrackPath ++ ~tidalTrackPresetTextField.value ++ ".json");
				this.prLoadTrackPresetFiles;
				~tidalTrackPresetListView.items_(presetTrackFiles)/*.valueAction_(0)*/ ;
				~tidalTrackPresetListView.valueAction_((pos-1).max(0));
				~tidalPresetTrackSize = ~tidalTrackPresetListView.items.size;
				~tidalPresetLine = (1 .. ~tidalPresetTrackSize /*~tidalTrackPresetListView.items.size*/);
			}),
			20,
			~tidalResetAllButtonView = Button.new/*.string_("Reset All")*/.states_([["Reset All", Color.white, Color.black]]).maxWidth_(100) // maxWidth added
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