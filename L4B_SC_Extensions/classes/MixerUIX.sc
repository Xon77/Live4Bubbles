MixerUIX : UIFactories {
	var activeOrbit;
	var handler;
	var <orbitLevelIndicators;
	var >reverbVariableName, >reverbNativeSize;
	var orbits;
	var guiElements;
	var tidalNetAddr;
	var defaultParentEvent;

	var panListener;

	*new { | initHandler, initOrbits|
		^super.new.init(initHandler, initOrbits);
	}

	init { |initHandler, initOrbits|
		handler = initHandler;
		orbits = initOrbits;
		orbitLevelIndicators = Array.new(orbits.size);
		tidalNetAddr = NetAddr.new("127.0.0.1", 6010);
		guiElements = Array.new(orbits.size);
		reverbNativeSize = 0;

		/*defaultParentEvent = [ // To delete ?
			\seqLine, 0, \ryt, 0, \leg, 1, \legS, 0, \buf, 0, \bufS, 0, \buf2, 0, \buf2R, 0, \fxs, 0, \fxs2, 0, \fxx, 0, \fxx2, 0, \fxt, 0, \fxt2, 0, \fxp, 0, \fxp2, 0, \fxv, 0, \fxv2, 0, \fus, 0, \fus2, 0, \fux, 0, \fux2, 0, \fut, 0, \fut2, 0, \fup, 0, \fup2, 0, \fuv, 0, \fuv2, 0, \pan, 0.5, \masterGain, 1.0, \mute, 0, reverbVariableName.asSymbol, 0.0
		];*/

		if (orbits.isNil.not, {
			var globalEffects = GlobalEffects.new;

			activeOrbit = orbits[0];
			// this.setOrbits(defaultParentEvent); // To delete ?

			orbits.do { |orbit|
				globalEffects.addGlobalEffect(orbit, GlobalDirtEffect(\dirt_master_mix, [\masterGain, \gainControlLag]), true);
			};
		});

		reverbVariableName = \room;

		if (handler.isNil.not, {
			handler.subscribe(this, \resetAll);
			handler.subscribe(this, \updateUI);
			handler.subscribe(this, \updateTrackUI); // added
			handler.subscribe(this, \releaseAll);
			handler.subscribe(this, \addRemoteControl);
		});

		this.addSeqLineListener;
		this.addRytListener; // deleted in the original, but necessary here ?
		this.addBufListener;
		this.addBuf2Listener;
		this.addFxsListener;
		this.addFxs2Listener;
		this.addFxpListener;
		this.addFxp2Listener;
		this.addFxtListener;
		this.addFxt2Listener;
		this.addFxvListener;
		this.addFxv2Listener;
		this.addFusListener;
		this.addFus2Listener;
		this.addFupListener;
		this.addFup2Listener;
		this.addFutListener;
		this.addFut2Listener;
		this.addFuvListener;
		this.addFuv2Listener;

		/*this.addMeterResponseOSCFunc;
		this.addPanListener;
		this.addGainListener;
		this.addReverbListener;
		this.addRemoteControlListener;*/
	}

	handleEvent { |eventName, eventData|
		if (eventName == \updateUI, {
			orbits.do({|item|
				// guiElements[item.orbitIndex][\preset][\value]./*value_*/valueAction_(item.get(\preset));

				// guiElements[item.orbitIndex][\seqLine][\element]./*value_*/valueAction_(item.get(\seqLine).linlin(1,~tidalEvalLine[0].last,0,1)); // updated by value
				guiElements[item.orbitIndex][\seqLine][\value]./*value_*/valueAction_(item.get(\seqLine));
				// item.get(\seqLine).postln;

				guiElements[item.orbitIndex][\rytS][\value]./*value_*/valueAction_(item.get(\rytS));
				// guiElements[item.orbitIndex][\ryt][\element]./*value_*/valueAction_(item.get(\ryt)); // updated by value
				guiElements[item.orbitIndex][\ryt][\value]./*value_*/valueAction_(item.get(\ryt));
				guiElements[item.orbitIndex][\legS][\value]./*value_*/valueAction_(item.get(\legS));
				// guiElements[item.orbitIndex][\leg][\element]./*value_*/valueAction_(item.get(\leg).curvelin(0,20,0,1,4)); // updated by value
				guiElements[item.orbitIndex][\leg][\value]./*value_*/valueAction_(item.get(\leg));
				guiElements[item.orbitIndex][\bufS][\value]./*value_*/valueAction_(item.get(\bufS));
				// guiElements[item.orbitIndex][\buf][\element]./*value_*/valueAction_(item.get(\buf)); // updated by value
				guiElements[item.orbitIndex][\buf][\value]./*value_*/valueAction_(item.get(\buf));
				// guiElements[item.orbitIndex][\buf][\label].string_/*value_*/(~arrayOfFolderNames4Tidal[item.get(\buf)].split($_)[0]/*item.get(\label)*/); // updated by value
				// guiElements[item.orbitIndex][\buf][\label2].string_/*value_*/(~arrayOfFolderNames4Tidal[item.get(\buf)].split($_)[1]/*item.get(\label2)*/); // updated by value
				// guiElements[item.orbitIndex][\buf2][\element]./*value_*/valueAction_(item.get(\buf2)); // updated by value
				guiElements[item.orbitIndex][\buf2][\value]./*value_*/valueAction_(item.get(\buf2));
				// guiElements[item.orbitIndex][\fxs][\element].value_(item.get(\fxs));
				guiElements[item.orbitIndex][\fxs][\value]./*value_*/valueAction_(item.get(\fxs));
				// guiElements[item.orbitIndex][\fxs2][\element].value_(item.get(\fxs2));
				guiElements[item.orbitIndex][\fxs2][\value]./*value_*/valueAction_(item.get(\fxs2));
				guiElements[item.orbitIndex][\fxx][\value]./*value_*/valueAction_(item.get(\fxx));
				guiElements[item.orbitIndex][\fxx2][\value]./*value_*/valueAction_(item.get(\fxx2));
				// guiElements[item.orbitIndex][\fxt][\element]./*value_*/valueAction_(item.get(\fxt)); // updated by value
				guiElements[item.orbitIndex][\fxt][\value]./*value_*/valueAction_(item.get(\fxt));
				// guiElements[item.orbitIndex][\fxt2][\element]./*value_*/valueAction_(item.get(\fxt2)); // updated by value
				guiElements[item.orbitIndex][\fxt2][\value]./*value_*/valueAction_(item.get(\fxt2));
				// guiElements[item.orbitIndex][\fxp][\element]./*value_*/valueAction_(item.get(\fxp)); // updated by value
				guiElements[item.orbitIndex][\fxp][\value]./*value_*/valueAction_(item.get(\fxp));
				// guiElements[item.orbitIndex][\fxp2][\element]./*value_*/valueAction_(item.get(\fxp2)); // updated by value
				guiElements[item.orbitIndex][\fxp2][\value]./*value_*/valueAction_(item.get(\fxp2));
				// guiElements[item.orbitIndex][\fxv][\element]./*value_*/valueAction_(item.get(\fxv)); // updated by value
				guiElements[item.orbitIndex][\fxv][\value]./*value_*/valueAction_(item.get(\fxv));
				// guiElements[item.orbitIndex][\fxv2][\element]./*value_*/valueAction_(item.get(\fxv2)); // updated by value
				guiElements[item.orbitIndex][\fxv2][\value]./*value_*/valueAction_(item.get(\fxv2));
				// guiElements[item.orbitIndex][\fus][\element].value_(item.get(\fus));
				guiElements[item.orbitIndex][\fus][\value]./*value_*/valueAction_(item.get(\fus));
				guiElements[item.orbitIndex][\fux][\value]./*value_*/valueAction_(item.get(\fux));
				// guiElements[item.orbitIndex][\fus2][\element].value_(item.get(\fus2));
				guiElements[item.orbitIndex][\fus2][\value]./*value_*/valueAction_(item.get(\fus2));
				guiElements[item.orbitIndex][\fux2][\value]./*value_*/valueAction_(item.get(\fux2));
				// guiElements[item.orbitIndex][\fut][\element]./*value_*/valueAction_(item.get(\fut)); // updated by value
				guiElements[item.orbitIndex][\fut][\value]./*value_*/valueAction_(item.get(\fut));
				// guiElements[item.orbitIndex][\fut2][\element].value_(item.get(\fut2)); // updated by value
				guiElements[item.orbitIndex][\fut2][\value]./*value_*/valueAction_(item.get(\fut2));
				// guiElements[item.orbitIndex][\fup][\element]./*value_*/valueAction_(item.get(\fup)); // updated by value
				guiElements[item.orbitIndex][\fup][\value]./*value_*/valueAction_(item.get(\fup));
				// guiElements[item.orbitIndex][\fup2][\element]./*value_*/valueAction_(item.get(\fup2)); // updated by value
				guiElements[item.orbitIndex][\fup2][\value]./*value_*/valueAction_(item.get(\fup2));
				// guiElements[item.orbitIndex][\fuv][\element]./*value_*/valueAction_(item.get(\fuv)); // updated by value
				guiElements[item.orbitIndex][\fuv][\value]./*value_*/valueAction_(item.get(\fuv));
				// guiElements[item.orbitIndex][\fuv2][\element]./*value_*/valueAction_(item.get(\fuv2)); // updated by value
				guiElements[item.orbitIndex][\fuv2][\value]./*value_*/valueAction_(item.get(\fuv2));

				// in conflict with label of Buf
				// guiElements[item.orbitIndex][\orbitLabel][\element].string_(item.get(\label));
				// guiElements[item.orbitIndex][\contextMenuLabel][\element].string_(item.get(\label));

				guiElements[item.orbitIndex][\pan][\element].value_(item.get(\pan));
				guiElements[item.orbitIndex][\pan][\value].value_(item.get(\pan));

				guiElements[item.orbitIndex][\masterGain][\element].value_((item.get(\masterGain) + 1).curvelin(1,3, 0,1, curve: 3));
				guiElements[item.orbitIndex][\masterGain][\value].value_(item.get(\masterGain));
				guiElements[item.orbitIndex][\mute][\value]./*value_*/valueAction_(item.get(\mute)); // added & valueAction to be taken into account
				guiElements[item.orbitIndex][\reverb][\element].value_(item.get(reverbVariableName));

				guiElements[item.orbitIndex][\orbitLabel][\element].background = (Color.fromHexString(item.get(\color)));
				guiElements[item.orbitIndex][\colorPicker][\element].setColorFromHexString(item.get(\color));

			});
		});

		if (eventName == \updateTrackUI, {
			// orbits.postln;
			// var orbitNb = ~orbitUpdateNb; // orbitNb.postln;
			orbits[~orbitUpdateNb /*orbitNb*/].do({|item|

				// item.postln;
				// i.postln;

				// guiElements[item.orbitIndex][\preset][\value]./*value_*/valueAction_(item.get(\preset));

				// guiElements[item.orbitIndex][\seqLine][\element]./*value_*/valueAction_(item.get(\seqLine).linlin(1,~tidalEvalLine[0].last,0,1)); // updated by value
				guiElements[~orbitUpdateNb][\seqLine][\value]./*value_*/valueAction_(item.get(\seqLine));
				~tidalEvalPos[~orbitUpdateNb] = (item.get(\seqLine)) -1;
				// item.get(\seqLine).postln;

				guiElements[~orbitUpdateNb][\rytS][\value]./*value_*/valueAction_(item.get(\rytS));
				guiElements[~orbitUpdateNb][\ryt][\value]./*value_*/valueAction_(item.get(\ryt));
				guiElements[~orbitUpdateNb][\legS][\value]./*value_*/valueAction_(item.get(\legS));
				guiElements[~orbitUpdateNb][\leg][\value]./*value_*/valueAction_(item.get(\leg));
				guiElements[~orbitUpdateNb][\bufS][\value]./*value_*/valueAction_(item.get(\bufS));
				guiElements[~orbitUpdateNb][\buf][\value]./*value_*/valueAction_(item.get(\buf));
				guiElements[~orbitUpdateNb][\buf2][\value]./*value_*/valueAction_(item.get(\buf2));
				guiElements[~orbitUpdateNb][\fxs][\value]./*value_*/valueAction_(item.get(\fxs));
				guiElements[~orbitUpdateNb][\fxs2][\value]./*value_*/valueAction_(item.get(\fxs2));
				guiElements[~orbitUpdateNb][\fxx][\value]./*value_*/valueAction_(item.get(\fxx));
				guiElements[~orbitUpdateNb][\fxx2][\value]./*value_*/valueAction_(item.get(\fxx2));
				guiElements[~orbitUpdateNb][\fxt][\value]./*value_*/valueAction_(item.get(\fxt));
				guiElements[~orbitUpdateNb][\fxt2][\value]./*value_*/valueAction_(item.get(\fxt2));
				guiElements[~orbitUpdateNb][\fxp][\value]./*value_*/valueAction_(item.get(\fxp));
				guiElements[~orbitUpdateNb][\fxp2][\value]./*value_*/valueAction_(item.get(\fxp2));
				guiElements[~orbitUpdateNb][\fxv][\value]./*value_*/valueAction_(item.get(\fxv));
				guiElements[~orbitUpdateNb][\fxv2][\value]./*value_*/valueAction_(item.get(\fxv2));
				guiElements[~orbitUpdateNb][\fus][\value]./*value_*/valueAction_(item.get(\fus));
				guiElements[~orbitUpdateNb][\fux][\value]./*value_*/valueAction_(item.get(\fux));
				guiElements[~orbitUpdateNb][\fus2][\value]./*value_*/valueAction_(item.get(\fus2));
				guiElements[~orbitUpdateNb][\fux2][\value]./*value_*/valueAction_(item.get(\fux2));
				guiElements[~orbitUpdateNb][\fut][\value]./*value_*/valueAction_(item.get(\fut));
				guiElements[~orbitUpdateNb][\fut2][\value]./*value_*/valueAction_(item.get(\fut2));
				guiElements[~orbitUpdateNb][\fup][\value]./*value_*/valueAction_(item.get(\fup));
				guiElements[~orbitUpdateNb][\fup2][\value]./*value_*/valueAction_(item.get(\fup2));
				guiElements[~orbitUpdateNb][\fuv][\value]./*value_*/valueAction_(item.get(\fuv));
				guiElements[~orbitUpdateNb][\fuv2][\value]./*value_*/valueAction_(item.get(\fuv2));

				// in conflict with label of Buf
				// guiElements[orbitNb][\orbitLabel][\element].string_(item.get(\label));
				// guiElements[orbitNb][\contextMenuLabel][\element].string_(item.get(\label));

				guiElements[~orbitUpdateNb][\pan][\element].value_/*valueAction_*/(item.get(\pan));
				guiElements[~orbitUpdateNb][\pan][\value].value_/*valueAction_*/(item.get(\pan));
				// item.set(\pan, (item.get(\pan)));
				// (item.get(\pan)).postln;

				/*guiElements[orbitNb][\masterGain][\element].value_((item.get(\masterGain) + 1).curvelin(1,3, 0,1, curve: 3));
				guiElements[orbitNb][\masterGain][\value].value_(item.get(\masterGain));
				guiElements[orbitNb][\mute][\value]./*value_*/valueAction_(item.get(\mute)); // added & valueAction to be taken into account
				guiElements[orbitNb][\reverb][\element].value_(item.get(reverbVariableName));

				guiElements[orbitNb][\orbitLabel][\element].background = (Color.fromHexString(item.get(\color)));
				guiElements[orbitNb][\colorPicker][\element].setColorFromHexString(item.get(\color));*/

			});
		});

		if (eventName == \resetAll, {
			orbits.do({|item|
				guiElements[item.orbitIndex][\preset][\value].valueAction_(1);
				// guiElements[item.orbitIndex][\seqLine][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\seqLine][\value].valueAction_(0);
				guiElements[item.orbitIndex][\rytS][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\ryt][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\ryt][\value].valueAction_(0);
				guiElements[item.orbitIndex][\legS][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\leg][\element].valueAction_(1.curvelin(0,20,0,1,4)); // updated by value
				guiElements[item.orbitIndex][\leg][\value].valueAction_(1);
				guiElements[item.orbitIndex][\bufS][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\buf][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\buf][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\buf][\label].string_(~arrayOfFolderNames4Tidal[0].split($_)[0]); // updated by value
				// guiElements[item.orbitIndex][\buf][\label2].string_(~arrayOfFolderNames4Tidal[0].split($_)[1]); // updated by value
				// guiElements[item.orbitIndex][\buf2][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\buf2][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fxs][\element].valueAction_(0);
				guiElements[item.orbitIndex][\fxs][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fxs2][\element].valueAction_(0);
				guiElements[item.orbitIndex][\fxs2][\value].valueAction_(0);
				guiElements[item.orbitIndex][\fxx][\value].valueAction_(0);
				guiElements[item.orbitIndex][\fxx2][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fxt][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fxt][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fxt2][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fxt2][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fxp][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fxp][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fxp2][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fxp2][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fxv][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fxv][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fxv2][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fxv2][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fus][\element].valueAction_(0);
				guiElements[item.orbitIndex][\fus][\value].valueAction_(0);
				guiElements[item.orbitIndex][\fux][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fus2][\element].valueAction_(0);
				guiElements[item.orbitIndex][\fus2][\value].valueAction_(0);
				guiElements[item.orbitIndex][\fux2][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fut][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fut][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fut2][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fut2][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fup][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fup][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fup2][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fup2][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fuv][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fuv][\value].valueAction_(0);
				// guiElements[item.orbitIndex][\fuv2][\element].valueAction_(0); // updated by value
				guiElements[item.orbitIndex][\fuv2][\value].valueAction_(0);

				// guiElements[item.orbitIndex][\orbitLabel][\element].string_("d" ++ (item.orbitIndex + 1)); // to change or uncomment - already defined
				// guiElements[item.orbitIndex][\contextMenuLabel][\element].string_("d" ++ (item.orbitIndex + 1)); // Impact ? -> acts on the name of the track in the color picker window

				guiElements[item.orbitIndex][\pan][\element].value_/*valueAction_*/(0.5);
				guiElements[item.orbitIndex][\pan][\value].value_/*valueAction_*/(0.5);
				guiElements[item.orbitIndex][\masterGain][\element].value_(2.curvelin(1,3,0,1, curve: 3));
				guiElements[item.orbitIndex][\masterGain][\value].valueAction_(1.0);
				guiElements[item.orbitIndex][\mute][\value].valueAction_(0);
				guiElements[item.orbitIndex][\reverb][\element].valueAction_(0.0);

				guiElements[item.orbitIndex][\orbitLabel][\element].background = Color.fromHexString("#D9D9D9");
				guiElements[item.orbitIndex][\colorPicker][\element].setColorFromHexString("#D9D9D9");

				item.set(\pan, 0.5);
				item.set(\masterGain, 1.0);
				item.set(\color, "#D9D9D9");
				// item.set(\label, "d" ++ (item.orbitIndex + 1)); // to change or uncomment - already defined
				item.set(reverbVariableName.asSymbol, 0.0);
				// Where is the action on EQ of the track selected in \resetAll ?
			});
		});

		if (eventName == \releaseAll, {
			var globalEffects = GlobalEffects.new;
			orbits.do({|orbit| globalEffects.releaseGlobalEffect(orbit, \dirt_master_mix )});
		});

		if (eventName == \addRemoteControl, {
			this.addRemoteControlListener;
			this.addMeterResponseOSCFunc;
		});
	}

	setOrbits { |pairs|
		pairs.pairsDo { |key, val|
			orbits.do({
				| orbit |
				if (orbit.defaultParentEvent[key].isNil, {
					orbit.set(key, val);
				});
			})
		};
	}

	// taken from UtilityUIX but modified to take the value of the preset as an argument - how to take a function from another class file
	loadTrackPresetK { |presetNb, trackNb|

		// var defaultTrackEvents = JSONlib.convertToSC(File.readAllString(("../presetsTr/" /*presetTrackPath*/ ++ ~tidalTrackPresetListView.items[presetNb] /*~tidalTrackPresetListView.item*/ /*presetTrackFile*/).resolveRelative, "r"));

		// Apparemment problème avec resolveRelative - to Define XXX
		var defaultTrackEvents = JSONlib.convertToSC(File.readAllString(("/Users/xon/Library/Application Support/SuperCollider/Extensions/SuperDirtMixerX/presetsTr/" ++ ~tidalTrackPresetListView.items[presetNb].split($.)[1].stripWhiteSpace/*.replace(" ")*/ ++ ".json" /*~tidalTrackPresetListView.item*/ /*presetTrackFile*/)/*.resolveRelative*/, "r"));

		// ("/Users/xon/Library/Application Support/SuperCollider/Extensions/SuperDirtMixerX/presetsTr/" + ~tidalTrackPresetListView.items[presetNb].split($. )[1].stripWhiteSpace/*.replace(" ")*/ ++ ".json").postln;

		// var defaultTrackEvents2 = ~tidalPresetsData[presetNb].copy;
		// ~tidalPresetsData[0].class

		/*defaultTrackEvents2.class.postln;
		defaultTrackEvents2.postln;
		defaultTrackEvents.class.postln;
		defaultTrackEvents.postln;*/

		defaultTrackEvents /*~tidalPresetsData[presetNb]*/ .do({
			arg defaultEvent /*, index*/;
			// defaultEvent.postln;

			/*defaultParentEvent.keysValuesDo*/
			[\seqLine, \ryt, \rytS, \leg, \legS, \bufS, \buf, \buf2, \buf2R, \fxs, \fxs2, \fxx, \fxx2, \fxt, \fxt2, \fxp, \fxp2, \fxv, \fxv2, \fus, \fus2, \fux, \fux2, \fut, \fut2, \fup, \fup2, \fuv, \fuv2, \pan /*, \masterGain, \mute, reverbVariableName.asSymbol*/
			].do({|key, val| // key.postln;
				if (defaultEvent.keys.includes(key) == false, {defaultEvent.put(key, val)})
			});

			orbits[trackNb /*~tidalTrackPresetNumber*/ /*~tidalCurrentOrbit*/].set(*defaultEvent.asPairs);
		});

		handler.emitEvent(\presetLoaded, /*~tidalPresetsData[presetNb]*/ defaultTrackEvents);

	}

	createUI {
		| container |
		var orbitMixerViews = Array.new((orbits.size * 2) - 1);

		defaultParentEvent = [
			\preset, 1, \seqLine, 1, \ryt, 0, \rytS, 0, \leg, 1.curvelin(0,20,0,1,4), \legS, 0, \bufS, 0, \buf, 0, \buf2, 0, \buf2R, 0, \fxs, 0, \fxs2, 0, \fxx, 0, \fxx2, 0, \fxt, 0, \fxt2, 0, \fxp, 0, \fxp2, 0, \fxv, 0, \fxv2, 0, \fus, 0, \fus2, 0, \fux, 0, \fux2, 0, \fut, 0, \fut2, 0, \fup, 0, \fup2, 0, \fuv, 0, \fuv2, 0, \pan, 0.5, \masterGain, 1.0, \mute, 0, reverbVariableName.asSymbol, 0.0, \color, "#D9D9D9", \label, "" // changed compared to the original
		];

		/*defaultParentEvent = [
			\pan, 0.5, \masterGain, 1.0, reverbVariableName.asSymbol, 0.0, \color, "#D9D9D9", \label, ""
	    ];*/

		this.setOrbits(defaultParentEvent);

		if (reverbVariableName.asSymbol == \room, {
			defaultParentEvent.add(\size).add(reverbNativeSize)
		});

		handler.emitEvent(\extendDefaultParentEvent, defaultParentEvent);

		(0..(orbits.size - 1)).do({
			arg item;
			var baseIndex = item * 2;

			orbitMixerViews.insert(baseIndex,
				this.createMixerUIComponent(
					orbits[item]
					, container
					, reverbVariableName
			));

			if ( (item == (orbits.size - 1)).not, {orbitMixerViews.insert(baseIndex + 1, 15 /*60*/)}); // original ok ?
		});

		container/*.maxWidth_(300)*/.onResize = { // .maxWidth_(300) to show the 4 first tracks - no longer necessary
			container.children.do({
				|compositeView|
				// compositeView.resizeTo(125, (container.bounds.height - 15).min(460)); // original
				compositeView.resizeTo(70 /*125*/, (container.bounds.height - 15)/*.min(460)*/);
			});
		};
	}

	createMixerUIComponent { |orbit, container, reverbVariableName|
		var composite = CompositeView.new(container, Rect((orbit.orbitIndex * 73 /*130*/) + 5, 5, 100, 440)); // Changed

		var contextMenuLabel;
		var text = orbit.get(\label);
		var orbitLabelView = StaticText.new.string_(text).font_(Font/*.monospace*/("Verdana",14,true)).minWidth_(44 /*100*/).align_(\center); // Changed

		var colorPicker = ColorPickerUI.new({|color|
			orbitLabelView.background = color;
			orbit.defaultParentEvent.put(\color, color.hexString);
		});

		var orbitElements = guiElements[orbit.orbitIndex];

		// Big additions

		var presetKnob = Knob().value_(orbit.get(\preset)).centered_(false).color_([Color.red,Color.red,Color.yellow,Color.yellow])
		// mode & step does not work ???
		// .mode_(\vertcial).step_(1/(~tidalTrackPresetListView.items.size-1)).keystep_(1/(~tidalTrackPresetListView.items.size-1))
		.action_({|a|
			~presetNb[orbit.orbitIndex] = (((~tidalTrackPresetListView.items.size-1)*a.value)+1).asInteger;
			if (~presetNb[orbit.orbitIndex] != ~previousPresetNb[orbit.orbitIndex], {
				//~presetNb[orbit.orbitIndex].postln;
				orbit.set(\preset,~presetNb[orbit.orbitIndex]);
				~orbitUpdateNb = orbit.orbitIndex;
				presetNumBox.valueAction_(~presetNb[orbit.orbitIndex]);
				// this.loadTrackPresetK(~presetNb[orbit.orbitIndex]-1, orbit.orbitIndex); // not necessary - triggered by the NumBox
				// handler.emitEvent(\updateUI); // updates all orbits or tracks - Too much
				// handler.emitEvent(\updateTrackUI); // tries to update only the track // not necessary - triggered by the NumBox
				~previousPresetNb[orbit.orbitIndex] = (((~tidalTrackPresetListView.items.size-1)*a.value)+1).asInteger;
			});
			// Why the code below has steps ??? but it also generates too much action
			/*var preset = (((~tidalTrackPresetListView.items.size-1)*a.value)+1).asInteger;
			orbit.set(\preset,preset);
			presetNumBox.valueAction_(preset);
			this.loadTrackPreset(preset-1);
			~presetNb[orbit.orbitIndex].postln;
			handler.emitEvent(\updateUI);*/
		});

		var presetNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.red).font_(Font.monospace(14,true)).maxWidth_(44).maxHeight_(15)
		.decimals_(0)
		.clipLo_(1).clipHi_(~tidalTrackPresetListView.items.size).align_(\center)
		.step_(1).scroll_step_(1).value_(orbit.get(\preset)).action_({|a|
			presetKnob.value_((a.value-1)/(~tidalTrackPresetListView.items.size-1));
			orbit.set(\preset,a.value.asInteger);
			~orbitUpdateNb = orbit.orbitIndex;
			// ~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', orbit.orbitIndex%4, \row, a.value, \column, 1);
			this.loadTrackPresetK((a.value-1).asInteger, orbit.orbitIndex);
			handler.emitEvent(\updateTrackUI /*\updateUI*/);
			presetLabelView.string_((~tidalTrackPresetListView.items[(a.value-1).asInteger].split($.)[1]));
		});

		var presetLabelView = StaticText.new.string_(~tidalTrackPresetListView.items[0].split($.)[1]).maxWidth_(65).font_(Font.sansSerif(10)/*Font("Arial", 9)*/).align_(\center);

		var seqLineKnob = Knob().value_(orbit.get(\seqLine)).centered_(false).color_([Color.blue,Color.blue,Color.yellow,Color.yellow]).action_({|a|
			var line = ~tidalEvalLine[orbit.orbitIndex][(~tidalEvalLine[orbit.orbitIndex].size-1)*a.value];
			// line.postln;
			orbit.set(\seqLine,line);
			seqLineNumBox.valueAction_(line);
			// ~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', orbit.orbitIndex /*%4*/, \row, line /*a.value*/, \column, 1);
		});

		var seqLineNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue).font_(Font.monospace(14,true)).maxWidth_(44).maxHeight_(15)
		.decimals_(0)
		.clipLo_(~tidalEvalLine[orbit.orbitIndex][0]).clipHi_(~tidalEvalLine[orbit.orbitIndex][~tidalEvalLine[orbit.orbitIndex].size-1]).align_(\center)
		.step_(1).scroll_step_(1).value_(orbit.get(\seqLine)).action_({|a|
			seqLineKnob.value_((a.value-~tidalEvalLine[orbit.orbitIndex][0])/(~tidalEvalLine[orbit.orbitIndex].size-1));
			orbit.set(\seqLine,a.value.asInteger);
			~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', orbit.orbitIndex /*%4*/, \row, a.value, \column, 1);
		});

		var rytSNumBox = NumberBox().normalColor_(Color.blue).background_(Color.new255(255, 165, 0)).font_(Font.monospace(13,true)).maxWidth_(25).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(2).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\rytS,a.value);
			~patternSpeedSwitch[orbit.orbitIndex] = a.value;
			case
			{~patternSpeedSwitch[orbit.orbitIndex] == 0}
			{~patternSpeeds[orbit.orbitIndex] = (0..64)}
			{~patternSpeedSwitch[orbit.orbitIndex] == 1}
			{~patternSpeeds[orbit.orbitIndex] = [0, 1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64]}
			{~patternSpeedSwitch[orbit.orbitIndex] == 2}
			{~patternSpeeds[orbit.orbitIndex] = [0, 1, 2, 4, 8, 16, 32, 64]};
			~patternSpeedsSize[orbit.orbitIndex] = ~patternSpeeds[orbit.orbitIndex].size-1;
		});

		var rytKnob = Knob().value_(orbit.get(\ryt)).centered_(false).color_([Color.new255(255, 165, 0),Color.new255(255, 165, 0),Color.yellow,Color.blue]).action_({|a|
			var ryt = ~patternSpeeds[orbit.orbitIndex][~patternSpeedsSize[orbit.orbitIndex]*a.value];
			// var ryt = ~patternSpeeds[orbit.orbitIndex][~patternSpeedsSize[orbit.orbitIndex]]*a.value;
			// var ryt = a.value*64;
			if (a.value == 0, {~tidalGuiElements[orbit.orbitIndex][\ryt][\value].normalColor_(Color.red)}, {~tidalGuiElements[orbit.orbitIndex][\ryt][\value].normalColor_(Color.blue)});
			orbit.set(\ryt, ryt);
			rytNumBox.value_(ryt);
			tidalNetAddr.sendMsg("/ctrl", ["d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8"][orbit.orbitIndex], ryt);
		});

		var rytNumBox = NumberBox().normalColor_(Color.blue).background_(Color.new255(255, 165, 0)).font_(Font.monospace(13,true)).maxWidth_(44).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~patternSpeeds[orbit.orbitIndex].last).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\ryt))*/.action_({|a|
			if (a.value < 1, {a.normalColor_(Color.red)}, {a.normalColor_(Color.blue)});
			orbit.set(\ryt,a.value.asInteger);
			rytKnob.value_(
				(a.value / ~patternSpeeds[orbit.orbitIndex][~patternSpeedsSize[orbit.orbitIndex]])/*.postln*/;

				/*~patternSpeeds[orbit.orbitIndex][~patternSpeedsSize[orbit.orbitIndex]*a.value] / 64
				~patternSpeeds[0][~patternSpeedsSize[0] * 0.5]  / 64
				~patternSpeeds[0][~patternSpeedsSize[0]] * 0.5
				~patternSpeedsSize[0]*/

			);
			// if (~patternSpeedSwitch[orbit.orbitIndex] == 1, {rytKnob.value_(a.value/~patternSpeedsSize[orbit.orbitIndex])},{rytKnob.value_(a.value/64)});
			tidalNetAddr.sendMsg("/ctrl", ["d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8"][orbit.orbitIndex], a.value.asInteger);
		}).normalColor_(Color.red);

		var legKnob = Knob().value_(orbit.get(\leg)).centered_(false).color_([Color.new255(139, 58, 58),Color.new255(139, 58, 58),Color.yellow,Color.yellow]).action_({|a|
			var leg = a.value.lincurve(0,1,0,20,4);
			orbit.set(\leg, leg);
			legNumBox.value_(leg);
			tidalNetAddr.sendMsg("/ctrl", ["1Leg", "2Leg", "3Leg", "4Leg", "5Leg", "6Leg", "7Leg", "8Leg"][orbit.orbitIndex], leg);
		}).value_(1.curvelin(0,20,0,1,4));

		var legNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.new255(139, 58, 58)).font_(Font.monospace(13,true)).maxWidth_(44).maxHeight_(15)
		.decimals_(2)
		.clipLo_(0).clipHi_(20).align_(\center)
		.step_(0.01).scroll_step_(0.01)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\leg,a.value);
			legKnob.value_(a.value.curvelin(0,20,0,1,4););
			tidalNetAddr.sendMsg("/ctrl", ["1Leg", "2Leg", "3Leg", "4Leg", "5Leg", "6Leg", "7Leg", "8Leg"][orbit.orbitIndex], a.value.asInteger);
		}).value_(1);

		var legSNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.new255(139, 58, 58)).font_(Font.monospace(13,true)).maxWidth_(25).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(9).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\legS,a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1LegS", "2LegS", "3LegS", "4LegS", "5LegS", "6LegS", "7LegS", "8LegS"][orbit.orbitIndex], a.value.asInteger);
		});

		var bufKnob = Knob().value_(orbit.get(\buf)).centered_(false).color_([Color.red(0.7),Color.red(0.7),Color.yellow,Color.yellow]).action_({|a| var bufString;
			orbit.set(\buf,(~arrayOfFolderNames4TidalSize[orbit.orbitIndex]*a.value).asInteger);
			bufNumBox.value_((~arrayOfFolderNames4TidalSize[orbit.orbitIndex]*a.value).asInteger);
			bufString = ~arrayOfFolderNames4Tidal[~arrayOfFolderNames4TidalSize[orbit.orbitIndex]*a.value].split($_);
			orbit.set(\label, bufString[0]);
			orbit.set(\label2, bufString[1]);
			bufLabelView.string_(bufString[0]);
			bufLabelView2.string_(bufString[1]);
			buf2NumBox.clipHi_(~tidalFolderSizes[(~arrayOfFolderNames4TidalSize[orbit.orbitIndex]*a.value).asInteger]);
			// (~tidalFolderSizes[(~arrayOfFolderNames4TidalSize[orbit.orbitIndex]*a.value).asInteger]).postln;
			// (~arrayOfFolderNames4TidalSize[orbit.orbitIndex]*a.value).asInteger.postln;
			buf2NumBox.value_((buf2Knob.value*~tidalFolderSizes[(~arrayOfFolderNames4TidalSize[orbit.orbitIndex]*a.value).asInteger]).asInteger);
			tidalNetAddr.sendMsg("/ctrl", ["1Fol", "2Fol", "3Fol", "4Fol", "5Fol", "6Fol", "7Fol", "8Fol"][orbit.orbitIndex], ~arrayOfFolderNames4Tidal[(a.value*~arrayOfFolderNames4TidalSize[orbit.orbitIndex]).asInteger]);
		});

		var bufNumBox = NumberBox().normalColor_(Color.white).background_(Color.red(0.7)).font_(Font.monospace(13,true)).maxWidth_(25).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~arrayOfFolderNames4TidalSize[orbit.orbitIndex]).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a| var bufString;
			orbit.set(\buf,a.value.asInteger);
			bufString = ~arrayOfFolderNames4Tidal[a.value].split($_);
			orbit.set(\label, bufString[0]);
			orbit.set(\label2, bufString[1]);
			bufLabelView.string_(bufString[0]);
			bufLabelView2.string_(bufString[1]);
			bufKnob.valueAction_(a.value/~arrayOfFolderNames4TidalSize[orbit.orbitIndex]);
			// buf2NumBox.clipHi_(~tidalFolderSizes[a.value]);
			/// buf2NumBox.value_((buf2Knob.value*~tidalFolderSizes[a.value]).asInteger);
			// Retiré car valueAction sur bufKnob
			// tidalNetAddr.sendMsg("/ctrl", ["1Fol", "2Fol", "3Fol", "4Fol", "5Fol", "6Fol", "7Fol", "8Fol"][orbit.orbitIndex], ~arrayOfFolderNames4Tidal[a.value]);
		});

		var bufSNumBox = NumberBox().normalColor_(Color.white).background_(Color.red(0.7)).font_(Font.monospace(13,true)).maxWidth_(25).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(4).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\bufS,a.value);
			~tidalFolderSizeSwitch[orbit.orbitIndex] = a.value;
			case
			{~tidalFolderSizeSwitch[orbit.orbitIndex] == 0}
			{~arrayOfFolderNames4TidalSize[orbit.orbitIndex] = ~arrayOfFolderNames4TidalSizeS0}
			{~tidalFolderSizeSwitch[orbit.orbitIndex] == 1}
			{~arrayOfFolderNames4TidalSize[orbit.orbitIndex] = ~arrayOfFolderNames4TidalSizeS1}
			{~tidalFolderSizeSwitch[orbit.orbitIndex] == 2}
			{~arrayOfFolderNames4TidalSize[orbit.orbitIndex] = ~arrayOfFolderNames4TidalSizeS2}
			{~tidalFolderSizeSwitch[orbit.orbitIndex] == 3}
			{~arrayOfFolderNames4TidalSize[orbit.orbitIndex] = ~arrayOfFolderNames4TidalSizeS3}
			{~tidalFolderSizeSwitch[orbit.orbitIndex] == 4}
			{~arrayOfFolderNames4TidalSize[orbit.orbitIndex] = ~arrayOfFolderNames4TidalSizeS4};
		}).valueAction_(0);

		var buf2Knob = Knob()/*.maxWidth_(44)*/.value_(orbit.get(\buf2)).centered_(false).color_([Color.magenta(0.7),Color.magenta(0.7),Color.yellow,Color.yellow]).action_({|a|
			var val = a.value*~tidalFolderSizes[orbit.get(\buf)?0];
			orbit.set(\buf2R, a.value);
			orbit.set(\buf2, val);
			buf2NumBox.value_(val);
			tidalNetAddr.sendMsg("/ctrl", ["1Buf", "2Buf", "3Buf", "4Buf", "5Buf", "6Buf", "7Buf", "8Buf"][orbit.orbitIndex], val);
		});

		var buf2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.magenta(0.7)).font_(Font.monospace(13,true)).maxWidth_(44).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~tidalFolderSizes[orbit.get(\buf)?0]).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\buf2, a.value);
			orbit.set(\bu2fR, a.value/~tidalFolderSizes[orbit.get(\buf)]);
			buf2Knob.value_(a.value/~tidalFolderSizes[orbit.get(\buf)?0]);
			tidalNetAddr.sendMsg("/ctrl", ["1Buf", "2Buf", "3Buf", "4Buf", "5Buf", "6Buf", "7Buf", "8Buf"][orbit.orbitIndex], a.value);
		});

		var bufLabelView = StaticText.new.string_((~arrayOfFolderNames4Tidal[0].split($_)[0])).maxWidth_(15).font_(Font("Arial", 10)).align_(\left);

		var bufLabelView2 = StaticText.new.string_((~arrayOfFolderNames4Tidal[0].split($_)[1])).maxWidth_(45).font_(Font.sansSerif(10)/*Font("Arial", 9)*/).align_(\centre);

		var fxsNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.green(0.6)).font_(Font.monospace(10,true)).maxWidth_(16).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~tidalnumWhenS).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fxs,a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fxs", "2fxs", "3fxs", "4fxs", "5fxs", "6fxs", "7fxs", "8fxs"][orbit.orbitIndex], a.value);
		}).value_(1);

		var fxs2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.green(0.3)).font_(Font.monospace(10,true)).maxWidth_(16).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~tidalnumWhenS).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fxs2,a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fxs2", "2fxs2", "3fxs2", "4fxs2", "5fxs2", "6fxs2", "7fxs2", "8fxs2"][orbit.orbitIndex], a.value);
		}).value_(1);

		var fxxNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.green(0.6)).font_(Font.monospace(10,true)).maxWidth_(16).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~tidalnumSwitchX).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fxx,a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fxx", "2fxx", "3fxx", "4fxx", "5fxx", "6fxx", "7fxx", "8fxx"][orbit.orbitIndex], a.value);
		});

		var fxx2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.green(0.3)).font_(Font.monospace(10,true)).maxWidth_(16).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~tidalnumSwitchX).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fxx2,a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fxx2", "2fxx2", "3fxx2", "4fxx2", "5fxx2", "6fxx2", "7fxx2", "8fxx2"][orbit.orbitIndex], a.value);
		});

		var fxtKnob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fxt)).centered_(false).color_([Color.green(0.6),Color.green(0.6),Color.yellow,Color.yellow]).action_({|a|
			var fxt = a.value;
			orbit.set(\fxt, fxt);
			fxtNumBox.value_(fxt);
			tidalNetAddr.sendMsg("/ctrl", ["1fxt", "2fxt", "3fxt", "4fxt", "5fxt", "6fxt", "7fxt", "8fxt"][orbit.orbitIndex], a.value);
		});

		var fxtNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.green(0.6)).font_(Font.monospace(10,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(2)
		.clipLo_(0).clipHi_(1).align_(\center)
		.step_(0.01).scroll_step_(0.01)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fxt,a.value);
			fxtKnob.value_(a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fxt", "2fxt", "3fxt", "4fxt", "5fxt", "6fxt", "7fxt", "8fxt"][orbit.orbitIndex], a.value);
		});

		var fxt2Knob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fxt2)).centered_(false).color_([Color.green(0.3),Color.green(0.3),Color.yellow,Color.yellow]).action_({|a|
			var fxt2 = a.value;
			orbit.set(\fxt2, fxt2);
			fxt2NumBox.value_(fxt2);
			tidalNetAddr.sendMsg("/ctrl", ["1fxt2", "2fxt2", "3fxt2", "4fxt2", "5fxt2", "6fxt2", "7fxt2", "8fxt2"][orbit.orbitIndex], a.value);
		});

		var fxt2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.green(0.3)).font_(Font.monospace(10,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(2)
		.clipLo_(0).clipHi_(1).align_(\center)
		.step_(0.01).scroll_step_(0.01)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fxt2,a.value);
			fxt2Knob.value_(a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fxt2", "2fxt2", "3fxt2", "4fxt2", "5fxt2", "6fxt2", "7fxt2", "8fxt2"][orbit.orbitIndex], a.value);
		});

		var fxpKnob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fxp)).centered_(false).color_([Color.green(0.6),Color.green(0.6),Color.yellow,Color.yellow]).action_({|a|
			var fxp = ~tidalnumEffects*a.value;
			orbit.set(\fxp, fxp);
			fxpNumBox.value_(fxp);
			tidalNetAddr.sendMsg("/ctrl", ["1fxp", "2fxp", "3fxp", "4fxp", "5fxp", "6fxp", "7fxp", "8fxp"][orbit.orbitIndex], a.value);
		});

		var fxpNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.green(0.6)).font_(Font.monospace(12,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(0)
		.clipLo_(1).clipHi_(~tidalnumEffects).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fxp,a.value.asInteger);
			fxpKnob.value_(a.value/~tidalnumEffects);
			tidalNetAddr.sendMsg("/ctrl", ["1fxp", "2fxp", "3fxp", "4fxp", "5fxp", "6fxp", "7fxp", "8fxp"][orbit.orbitIndex], a.value/~tidalnumEffects);
		});

		var fxp2Knob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fxp2)).centered_(false).color_([Color.green(0.3),Color.green(0.3),Color.yellow,Color.yellow]).action_({|a|
			var fxp2 = ~tidalnumEffects*a.value;
			orbit.set(\fxp2, fxp2);
			fxp2NumBox.value_(fxp2);
			tidalNetAddr.sendMsg("/ctrl", ["1fxp2", "2fxp2", "3fxp2", "4fxp2", "5fxp2", "6fxp2", "7fxp2", "8fxp2"][orbit.orbitIndex], a.value);
		});

		var fxp2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.green(0.3)).font_(Font.monospace(12,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(0)
		.clipLo_(1).clipHi_(~tidalnumEffects).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fxp2,a.value.asInteger);
			fxp2Knob.value_(a.value/~tidalnumEffects);
			tidalNetAddr.sendMsg("/ctrl", ["1fxp2", "2fxp2", "3fxp2", "4fxp2", "5fxp2", "6fxp2", "7fxp2", "8fxp2"][orbit.orbitIndex], a.value/~tidalnumEffects);
		});

		var fxvKnob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fxv)).centered_(false).color_([Color.green(0.6),Color.green(0.6),Color.yellow,Color.yellow]).action_({|a|
			var fxv = a.value;
			orbit.set(\fxv, fxv);
			fxvNumBox.value_(fxv);
			tidalNetAddr.sendMsg("/ctrl", ["1fxv", "2fxv", "3fxv", "4fxv", "5fxv", "6fxv", "7fxv", "8fxv"][orbit.orbitIndex], a.value);
		});

		var fxvNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.green(0.6)).font_(Font.monospace(10,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(2)
		.clipLo_(0).clipHi_(1).align_(\center)
		.step_(0.01).scroll_step_(0.01)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fxv,a.value);
			fxvKnob.value_(a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fxv", "2fxv", "3fxv", "4fxv", "5fxv", "6fxv", "7fxv", "8fxv"][orbit.orbitIndex], a.value);
		});

		var fxv2Knob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fxv2)).centered_(false).color_([Color.green(0.3),Color.green(0.3),Color.yellow,Color.yellow]).action_({|a|
			var fxv2 = a.value;
			orbit.set(\fxv2, fxv2);
			fxv2NumBox.value_(fxv2);
			tidalNetAddr.sendMsg("/ctrl", ["1fxv2", "2fxv2", "3fxv2", "4fxv2", "5fxv2", "6fxv2", "7fxv2", "8fxv2"][orbit.orbitIndex], a.value);
		});

		var fxv2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.green(0.3)).font_(Font.monospace(10,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(2)
		.clipLo_(0).clipHi_(1).align_(\center)
		.step_(0.01).scroll_step_(0.01)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fxv2,a.value);
			fxv2Knob.value_(a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fxv2", "2fxv2", "3fxv2", "4fxv2", "5fxv2", "6fxv2", "7fxv2", "8fxv2"][orbit.orbitIndex], a.value);
		});

		var fusNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue(1)).font_(Font.monospace(10,true)).maxWidth_(16).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~tidalnumWhenS).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fus,a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fus", "2fus", "3fus", "4fus", "5fus", "6fus", "7fus", "8fus"][orbit.orbitIndex], a.value);
		}).value_(1);

		var fus2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue(0.3)).font_(Font.monospace(10,true)).maxWidth_(16).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~tidalnumWhenS).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fus2,a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fus2", "2fus2", "3fus2", "4fus2", "5fus2", "6fus2", "7fus2", "8fus2"][orbit.orbitIndex], a.value);
		}).value_(1);

		var fuxNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue(1)).font_(Font.monospace(10,true)).maxWidth_(16).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~tidalnumSwitchX).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fux,a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fux", "2fux", "3fux", "4fux", "5fux", "6fux", "7fux", "8fux"][orbit.orbitIndex], a.value);
		});

		var fux2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue(0.3)).font_(Font.monospace(10,true)).maxWidth_(16).maxHeight_(15)
		.decimals_(0)
		.clipLo_(0).clipHi_(~tidalnumSwitchX).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fux2,a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fux2", "2fux2", "3fux2", "4fux2", "5fux2", "6fux2", "7fux2", "8fux2"][orbit.orbitIndex], a.value);
		});

		var futKnob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fut)).centered_(false).color_([Color.blue(1),Color.blue(1),Color.yellow,Color.yellow]).action_({|a|
			var fut = a.value;
			orbit.set(\fut, fut);
			futNumBox.value_(fut);
			tidalNetAddr.sendMsg("/ctrl", ["1fut", "2fut", "3fut", "4fut", "5fut", "6fut", "7fut", "8fut"][orbit.orbitIndex], a.value);
		});

		var futNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue(1)).font_(Font.monospace(10,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(2)
		.clipLo_(0).clipHi_(1).align_(\center)
		.step_(0.01).scroll_step_(0.01)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fut,a.value);
			futKnob.value_(a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fut", "2fut", "3fut", "4fut", "5fut", "6fut", "7fut", "8fut"][orbit.orbitIndex], a.value);
		});

		var fut2Knob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fut2)).centered_(false).color_([Color.blue(0.3),Color.blue(0.3),Color.yellow,Color.yellow]).action_({|a|
			var fut2 = a.value;
			orbit.set(\fut2, fut2);
			fut2NumBox.value_(fut2);
			tidalNetAddr.sendMsg("/ctrl", ["1fut2", "2fut2", "3fut2", "4fut2", "5fut2", "6fut2", "7fut2", "8fut2"][orbit.orbitIndex], a.value);
		});

		var fut2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue(0.3)).font_(Font.monospace(10,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(2)
		.clipLo_(0).clipHi_(1).align_(\center)
		.step_(0.01).scroll_step_(0.01)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fut2,a.value);
			fut2Knob.value_(a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fut2", "2fut2", "3fut2", "4fut2", "5fut2", "6fut2", "7fut2", "8fut2"][orbit.orbitIndex], a.value);
		});

		var fupKnob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fup)).centered_(false).color_([Color.blue(1),Color.blue(1),Color.yellow,Color.yellow]).action_({|a|
			var fup = (~tidalnumFunctions*a.value).round(1);
			orbit.set(\fup, fup);
			fupNumBox.value_(fup);
			tidalNetAddr.sendMsg("/ctrl", ["1fup", "2fup", "3fup", "4fup", "5fup", "6fup", "7fup", "8fup"][orbit.orbitIndex], a.value);
		});

		var fupNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue(1)).font_(Font.monospace(12,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(0)
		.clipLo_(1).clipHi_(~tidalnumFunctions).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fup,a.value.asInteger);
			fupKnob.value_(a.value/~tidalnumFunctions); // (a.value/~tidalnumFunctions).postln;
			tidalNetAddr.sendMsg("/ctrl", ["1fup", "2fup", "3fup", "4fup", "5fup", "6fup", "7fup", "8fup"][orbit.orbitIndex], a.value/~tidalnumFunctions);
		});

		var fup2Knob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fup2)).centered_(false).color_([Color.blue(0.3),Color.blue(0.3),Color.yellow,Color.yellow]).action_({|a|
			var fup2 = (~tidalnumFunctions*a.value).round(1);
			orbit.set(\fup2, fup2);
			fup2NumBox.value_(fup2);
			tidalNetAddr.sendMsg("/ctrl", ["1fup2", "2fup2", "3fup2", "4fup2", "5fup2", "6fup2", "7fup2", "8fup2"][orbit.orbitIndex], a.value);
		});

		var fup2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue(0.3)).font_(Font.monospace(12,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(0)
		.clipLo_(1).clipHi_(~tidalnumFunctions).align_(\center)
		.step_(1).scroll_step_(1)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fup2,a.value.asInteger);
			fup2Knob.value_(a.value/~tidalnumFunctions);
			tidalNetAddr.sendMsg("/ctrl", ["1fup2", "2fup2", "3fup2", "4fup2", "5fup2", "6fup2", "7fup2", "8fup2"][orbit.orbitIndex], a.value/~tidalnumFunctions);
		});

		var fuvKnob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fuv)).centered_(false).color_([Color.blue(1),Color.blue(1),Color.yellow,Color.yellow]).action_({|a|
			var fuv = a.value;
			orbit.set(\fuv, fuv);
			fuvNumBox.value_(fuv);
			tidalNetAddr.sendMsg("/ctrl", ["1fuv", "2fuv", "3fuv", "4fuv", "5fuv", "6fuv", "7fuv", "8fuv"][orbit.orbitIndex], a.value);
		});

		var fuvNumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue(1)).font_(Font.monospace(10,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(2)
		.clipLo_(0).clipHi_(1).align_(\center)
		.step_(0.01).scroll_step_(0.01)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fuv,a.value);
			fuvKnob.value_(a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fuv", "2fuv", "3fuv", "4fuv", "5fuv", "6fuv", "7fuv", "8fuv"][orbit.orbitIndex], a.value);
		});

		var fuv2Knob = Knob().maxWidth_(30).maxHeight_(25).value_(orbit.get(\fuv2)).centered_(false).color_([Color.blue(0.3),Color.blue(0.3),Color.yellow,Color.yellow]).action_({|a|
			var fuv2 = a.value;
			orbit.set(\fuv2, fuv2);
			fuv2NumBox.value_(fuv2);
			tidalNetAddr.sendMsg("/ctrl", ["1fuv2", "2fuv2", "3fuv2", "4fuv2", "5fuv2", "6fuv2", "7fuv2", "8fuv2"][orbit.orbitIndex], a.value);
		});

		var fuv2NumBox = NumberBox().normalColor_(Color.yellow).background_(Color.blue(0.3)).font_(Font.monospace(10,true)).maxWidth_(28).maxHeight_(15)
		.decimals_(2)
		.clipLo_(0).clipHi_(1).align_(\center)
		.step_(0.01).scroll_step_(0.01)/*.value_(orbit.get(\buf))*/.action_({|a|
			orbit.set(\fuv2,a.value);
			fuv2Knob.value_(a.value);
			tidalNetAddr.sendMsg("/ctrl", ["1fuv2", "2fuv2", "3fuv2", "4fuv2", "5fuv2", "6fuv2", "7fuv2", "8fuv2"][orbit.orbitIndex], a.value);
		});

		var panKnob = Knob().value_(orbit.get(\pan)).centered_(true).action_({|a|
			orbit.set(\pan,a.value);
			panNumBox.value_(a.value);
			// orbit.get(\pan).postln;
		});

		var panNumBox = NumberBox().maxHeight_(16).maxWidth_(44) // maxHeight & maxWidth added
		.decimals_(2)
		.clipLo_(0).clipHi_(1).align_(\center)
		.scroll_step_(0.1).value_(orbit.get(\pan)).action_({|a|
			panKnob.value_(a.value);
			orbit.set(\pan,a.value);
			// orbit.get(\pan).postln;
		});

		var gainSlider = Slider.new.fixedWidth_(30).value_((orbit.get(\masterGain) + 1).curvelin(1,3,0,1, curve: 3)).action_({|a|
			orbit.set(\masterGain,a.value.lincurve(0, 1.0, 1,3, curve: 3) - 1);
			gainNumBox.value_(a.value.lincurve(0, 1.0, 1,3, curve: 3)-1);
		});

		var gainNumBox = NumberBox().maxHeight_(16).maxWidth_(44) // maxHeight & maxWidth added
		.decimals_(2)
		.clipLo_(0).clipHi_(2).align_(\center)
		.scroll_step_(0.1).value_(orbit.get(\masterGain)).action_({|a|
			gainSlider.value_((a.value + 1).curvelin(1, 3, 0, 1, curve: 3));
			orbit.set(\masterGain,a.value);
		});

		// TO DO - Initialize with the Track nb
		var eqButton = Button.new.string_("FX").maxWidth_(44).action_({ |a| // maxWidth added
			// Save EQ values before switching to the new eq orbit
			handler.emitEvent(\setActiveOrbit, orbit);
			~tidalCurrentOrbit = orbit.orbitIndex; // Added
			activeOrbit = orbit;
			guiElements.do({arg item, i; item[\eq][\element].states_([["FX"+(i+1), Color.black, Color.white]])}); // Changed
			a.states_([["FX"+(orbit.orbitIndex+1), Color.white, Color.new255(238, 180, 34)]]); // Changed
		});

		var reverbKnob =  Knob().value_(orbit.get(reverbVariableName)).action_({|a|
			orbit.set(reverbVariableName,a.value);
		});

		var muteButton = Button.new.maxWidth_(20) // Kept
		.states_([["M", Color.black, Color.white], ["M", Color.white, Color.red]])
		.value_(orbit.get(\mute))
		.action_({ |a|
			orbit.set(\mute, a.value);
			if(a.value == 0) { tidalNetAddr.sendMsg("/unmute",orbit.orbitIndex + 1) };
			if(a.value == 1) { tidalNetAddr.sendMsg("/mute",orbit.orbitIndex + 1) };
		});

		var contextMenuLabelView = TextView.new.string_(orbit.get(\label)).fixedHeight_(30);

		var newOrbitElements = Dictionary.newFrom([
			\orbitLabel,  Dictionary.newFrom([\element, orbitLabelView])
			, \contextMenuLabel, Dictionary.newFrom([\element, contextMenuLabelView])

			, \preset, Dictionary.newFrom([\element, presetKnob, \value, presetNumBox])
			, \seqLine, Dictionary.newFrom([\element, seqLineKnob, \value, seqLineNumBox])
			, \ryt, Dictionary.newFrom([\element, rytKnob, \value, rytNumBox])
			, \rytS, Dictionary.newFrom([\value, rytSNumBox])
			, \leg, Dictionary.newFrom([\element, legKnob, \value, legNumBox])
			, \legS, Dictionary.newFrom([\value, legSNumBox])
			, \buf, Dictionary.newFrom([\element, bufKnob, \value, bufNumBox, \label, bufLabelView, \label2, bufLabelView2])
			, \bufS, Dictionary.newFrom([\value, bufSNumBox])
			, \buf2, Dictionary.newFrom([\element, buf2Knob, \value, buf2NumBox])
			, \fxs, Dictionary.newFrom([\value, fxsNumBox])
			, \fxs2, Dictionary.newFrom([\value, fxs2NumBox])
			, \fxx, Dictionary.newFrom([\value, fxxNumBox])
			, \fxx2, Dictionary.newFrom([\value, fxx2NumBox])
			, \fxt, Dictionary.newFrom([\element, fxtKnob, \value, fxtNumBox])
			, \fxt2, Dictionary.newFrom([\element, fxt2Knob, \value, fxt2NumBox])
			, \fxp, Dictionary.newFrom([\element, fxpKnob, \value, fxpNumBox])
			, \fxp2, Dictionary.newFrom([\element, fxp2Knob, \value, fxp2NumBox])
			, \fxv, Dictionary.newFrom([\element, fxvKnob, \value, fxvNumBox])
			, \fxv2, Dictionary.newFrom([\element, fxv2Knob, \value, fxv2NumBox])
			, \fus, Dictionary.newFrom([\value, fusNumBox])
			, \fus2, Dictionary.newFrom([\value, fus2NumBox])
			, \fux, Dictionary.newFrom([\value, fuxNumBox])
			, \fux2, Dictionary.newFrom([\value, fux2NumBox])
			, \fut, Dictionary.newFrom([\element, futKnob, \value, futNumBox])
			, \fut2, Dictionary.newFrom([\element, fut2Knob, \value, fut2NumBox])
			, \fup, Dictionary.newFrom([\element, fupKnob, \value, fupNumBox])
			, \fup2, Dictionary.newFrom([\element, fup2Knob, \value, fup2NumBox])
			, \fuv, Dictionary.newFrom([\element, fuvKnob, \value, fuvNumBox])
			, \fuv2, Dictionary.newFrom([\element, fuv2Knob, \value, fuv2NumBox])

			, \pan, Dictionary.newFrom([\element, panKnob, \value, panNumBox])
			, \masterGain, Dictionary.newFrom([\element, gainSlider, \value, gainNumBox ])
			, \reverb, Dictionary.newFrom([\element, reverbKnob])
			, \eq, Dictionary.newFrom([\element, eqButton])
			, \mute, Dictionary.newFrom([\value, muteButton])
			, \orbitWrapper, Dictionary.newFrom([\element, composite])
			, \colorPicker, Dictionary.newFrom([\element, colorPicker])
		]);

		~tidalGuiElements = guiElements.add(newOrbitElements);

		// To withdraw when Server.merter is ON
		this.orbitLevelIndicators.add(Array.fill(~dirt.numChannels, {LevelIndicator.new.fixedWidth_(/*12*/ 32 / ~dirt.numChannels).drawsPeak_(true).warning_(0.9).critical_(1.0)}));

		if (orbit == activeOrbit,
			{eqButton.states_([["FX", Color.white, Color.new255(238, 180, 34)]])},
			{eqButton.states_([["FX", Color.black, Color.gray(0.9) /*Color.white*/]])});


		// CompositeView.new(container, Rect((orbit.orbitIndex * 73 /*130*/) + 5, 5, 100, 440))/*.minHeight_(930)*/. // To delete
		composite.layout_(VLayout(
			HLayout(orbitLabelView),
			5,
			HLayout(presetLabelView),
			HLayout(presetNumBox),
			presetKnob,
			HLayout(seqLineNumBox),
			seqLineKnob,
			HLayout(rytSNumBox),
			HLayout(rytNumBox),
			rytKnob,
			HLayout(legSNumBox),
			HLayout(legNumBox),
			legKnob,
			HLayout(bufSNumBox),
			HLayout(bufNumBox, bufLabelView).spacing_(0),
			HLayout(bufLabelView2),
			bufKnob,
			HLayout(buf2NumBox),
			buf2Knob,
			HLayout(fxsNumBox, 8, fxs2NumBox),
			HLayout(fxtNumBox,fxt2NumBox),
			HLayout(fxtKnob,fxt2Knob),
			HLayout(fxxNumBox, 8, fxx2NumBox),
			HLayout(fxpNumBox,fxp2NumBox),
			HLayout(fxpKnob,fxp2Knob),
			HLayout(fxvNumBox,fxv2NumBox),
			HLayout(fxvKnob,fxv2Knob),
			HLayout(fusNumBox, 8, fus2NumBox),
			HLayout(futNumBox,fut2NumBox),
			HLayout(futKnob,fut2Knob),
			HLayout(fuxNumBox, 8, fux2NumBox),
			HLayout(fupNumBox,fup2NumBox),
			HLayout(fupKnob,fup2Knob),
			HLayout(fuvNumBox,fuv2NumBox),
			HLayout(fuvKnob,fuv2Knob),
			panKnob,
			HLayout(panNumBox),
			HLayout(
				this.orbitLevelIndicators[orbit.orbitIndex][0],
				this.orbitLevelIndicators[orbit.orbitIndex][1],
				if (/*~dirt.numChannels*/ ~tidalOutputChannels > 2, {this.orbitLevelIndicators[orbit.orbitIndex][2]},{0}),
				if (/*~dirt.numChannels*/ ~tidalOutputChannels > 3, {this.orbitLevelIndicators[orbit.orbitIndex][3]},{0}),
				if (/*~dirt.numChannels*/ ~tidalOutputChannels > 4, {this.orbitLevelIndicators[orbit.orbitIndex][4]},{0}),
				if (/*~dirt.numChannels*/ ~tidalOutputChannels > 5, {this.orbitLevelIndicators[orbit.orbitIndex][5]},{0}),
				if (/*~dirt.numChannels*/ ~tidalOutputChannels > 6, {this.orbitLevelIndicators[orbit.orbitIndex][6]},{0}),
				if (/*~dirt.numChannels*/ ~tidalOutputChannels > 7, {this.orbitLevelIndicators[orbit.orbitIndex][7]},{0}),
				gainSlider
			).spacing_(1),
			HLayout(gainNumBox),
			// StaticText.new.string_("FX - Reverb").align_(\center),
			reverbKnob,
			HLayout(muteButton
				/*Button.new.maxWidth_(20)
				.states_([["M", Color.black, Color.white], ["M", Color.white, Color.blue]])
				.action_({
				|view|
				if(view.value == 0) { tidalNetAddr.sendMsg("/unmute",orbit.orbitIndex + 1) };
				if(view.value == 1) { tidalNetAddr.sendMsg("/mute",orbit.orbitIndex + 1) };
				})*/,
				Button.new.maxWidth_(20).states_([["S", Color.black, Color.white], ["S", Color.white, Color.blue]]).action_({
					|view|
					if(view.value == 0) { tidalNetAddr.sendMsg("/unsolo",orbit.orbitIndex + 1) };
					if(view.value == 1) { tidalNetAddr.sendMsg("/solo",orbit.orbitIndex + 1) };
				}),
			).spacing_(1),
			HLayout(eqButton),
		).spacing_(0).margins_(0)
		).background_(Color.gray(0.85));

		contextMenuLabelView.keyUpAction =  {|tv|
			orbitLabelView.string_(tv.string);
			orbit.set(\label, tv.string);
		};

		// To uncomment to not get the ColorPicker
		/*composite.setContextMenuActions(
			CustomViewAction(contextMenuLabelView),
			CustomViewAction(colorPicker.createUI().minHeight_(300).minWidth_(300))
		);*/

	}

	/*
	* ADD OSC LISTENERS
	*/
	addMeterResponseOSCFunc {
		OSCFunc({ |msg|
			{
				try {
					var indicators = orbitLevelIndicators[msg[2]];

					(0..(~dirt.numChannels - 1)).do({ |item|
						var baseIndex = ((item + 1) * 2) + 1;
						var rms = msg[baseIndex + 1].ampdb.linlin(-80, 0, 0, 1);
						var peak = msg[baseIndex].ampdb.linlin(-80, 0, 0, 1, \min);

						indicators[item].value = rms;
						indicators[item].peakLevel = peak;
					});

				} { |error| };
			}.defer;
		}, ("/rms")).fix;
	}

	// Big Additions to put below in addRemoteControlListener XXX ?

	addSeqLineListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\seqLine, value/*.linlin(0,1,0,100)*/);
			guiElements[orbitIndex][\seqLine][\element].value_((orbits.at(orbitIndex).get(\seqLine)-~tidalEvalLine[orbitIndex][0])/(~tidalEvalLine[orbitIndex].size-1));
			guiElements[orbitIndex][\seqLine][\value].value_(orbits.at(orbitIndex).get(\seqLine));
		}.defer;
	}, ("/SuperDirtMixer/seqLine"), recvPort: 57120).fix;
	}

	addRytListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\ryt, value/*.linlin(0,1,0,100)*/);
			guiElements[orbitIndex][\ryt][\element].value_(value/64);
			guiElements[orbitIndex][\ryt][\value].value_(orbits.at(orbitIndex).get(\ryt));
			tidalNetAddr.sendMsg("/ctrl", ["d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8"][orbitIndex], value);
		}.defer;
	}, ("/SuperDirtMixer/ryt"), recvPort: 57120).fix;
	}

	addLegListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\leg, value/*.linlin(0,1,0,100)*/);
			guiElements[orbitIndex][\leg][\element].value_(value.curvelin(0,20,0,1,4););
			guiElements[orbitIndex][\leg][\value].value_(orbits.at(orbitIndex).get(\leg));
			tidalNetAddr.sendMsg("/ctrl", ["1Leg", "2Leg", "3Leg", "4Leg", "5Leg", "6Leg", "7Leg", "8Leg"][orbitIndex], value);
		}.defer;
	}, ("/SuperDirtMixer/leg"), recvPort: 57120).fix;
	}

	addBufListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];
			var bufString = bufString = ~arrayOfFolderNames4Tidal[value].split($_);

			orbits.at(orbitIndex).set(\buf, /*(value*~arrayOfFolderNames4Tidal.size).asInteger*/ value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\buf][\element].value_(value/~arrayOfFolderNames4TidalSize[orbitIndex]);
			guiElements[orbitIndex][\buf][\value].value_(orbits.at(orbitIndex).get(\buf));
			guiElements[orbitIndex][\buf][\label].string_(bufString[0]);
			guiElements[orbitIndex][\buf][\label2].string_(bufString[1]);

			// orbits.at(orbitIndex).get(\buf2R).postln;
			guiElements[orbitIndex][\buf2][\value].clipHi_(~tidalFolderSizes[(~arrayOfFolderNames4TidalSize[orbitIndex]*(value/~arrayOfFolderNames4TidalSize[orbitIndex])).asInteger]);
			guiElements[orbitIndex][\buf2][\value].value_(~tidalFolderSizes[(~arrayOfFolderNames4TidalSize[orbitIndex]*(value/~arrayOfFolderNames4TidalSize[orbitIndex])).asInteger]*(orbits.at(orbitIndex).get(\buf2R)));

			tidalNetAddr.sendMsg("/ctrl", ["1Fol", "2Fol", "3Fol", "4Fol", "5Fol", "6Fol", "7Fol", "8Fol"][orbitIndex], ~arrayOfFolderNames4Tidal[value]);
		}.defer;
	}, ("/SuperDirtMixer/buf"), recvPort: 57120).fix;
	}

	addBuf2Listener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\buf2, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\buf2][\element].value_(value/~tidalFolderSizes[orbits.at(orbitIndex).get(\buf)?0]);
			guiElements[orbitIndex][\buf2][\value].value_(orbits.at(orbitIndex).get(\buf2));
			tidalNetAddr.sendMsg("/ctrl", ["1Buf", "2Buf", "3Buf", "4Buf", "5Buf", "6Buf", "7Buf", "8Buf"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/buf2"), recvPort: 57120).fix;
	}

	addFxsListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fxs, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fxs][\value].value_(orbits.at(orbitIndex).get(\fxs));
			tidalNetAddr.sendMsg("/ctrl", ["1fxs", "2fxs", "3fxs", "4fxs", "5fxs", "6fxs", "7fxs", "8fxs"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fxs"), recvPort: 57120).fix;
	}

	addFxs2Listener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fxs2, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fxs2][\value].value_(orbits.at(orbitIndex).get(\fxs2));
			tidalNetAddr.sendMsg("/ctrl", ["1fxs2", "2fxs2", "3fxs2", "4fxs2", "5fxs2", "6fxs2", "7fxs2", "8fxs2"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fxs2"), recvPort: 57120).fix;
	}

	addFxtListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fxt, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fxt][\element].value_(value);
			guiElements[orbitIndex][\fxt][\value].value_(orbits.at(orbitIndex).get(\fxt));
			tidalNetAddr.sendMsg("/ctrl", ["1fxt", "2fxt", "3fxt", "4fxt", "5fxt", "6fxt", "7fxt", "8fxt"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fxt"), recvPort: 57120).fix;
	}

	addFxt2Listener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fxt2, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fxt2][\element].value_(value);
			guiElements[orbitIndex][\fxt2][\value].value_(orbits.at(orbitIndex).get(\fxt2));
			tidalNetAddr.sendMsg("/ctrl", ["1fxt2", "2fxt2", "3fxt2", "4fxt2", "5fxt2", "6fxt2", "7fxt2", "8fxt2"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fxt2"), recvPort: 57120).fix;
	}

	addFxpListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fxp, value*~tidalnumEffects) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fxp][\element].value_(value);
			guiElements[orbitIndex][\fxp][\value].value_(orbits.at(orbitIndex).get(\fxp));
			tidalNetAddr.sendMsg("/ctrl", ["1fxp", "2fxp", "3fxp", "4fxp", "5fxp", "6fxp", "7fxp", "8fxp"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fxp"), recvPort: 57120).fix;
	}

	addFxp2Listener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fxp2, value*~tidalnumEffects) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fxp2][\element].value_(value);
			guiElements[orbitIndex][\fxp2][\value].value_(orbits.at(orbitIndex).get(\fxp2));
			tidalNetAddr.sendMsg("/ctrl", ["1fxp2", "2fxp2", "3fxp2", "4fxp2", "5fxp2", "6fxp2", "7fxp2", "8fxp2"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fxp2"), recvPort: 57120).fix;
	}

	addFxvListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fxv, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fxv][\element].value_(value);
			guiElements[orbitIndex][\fxv][\value].value_(orbits.at(orbitIndex).get(\fxv));
			tidalNetAddr.sendMsg("/ctrl", ["1fxv", "2fxv", "3fxv", "4fxv", "5fxv", "6fxv", "7fxv", "8fxv"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fxv"), recvPort: 57120).fix;
	}

	addFxv2Listener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fxv2, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fxv2][\element].value_(value);
			guiElements[orbitIndex][\fxv2][\value].value_(orbits.at(orbitIndex).get(\fxv2));
			tidalNetAddr.sendMsg("/ctrl", ["1fxv2", "2fxv2", "3fxv2", "4fxv2", "5fxv2", "6fxv2", "7fxv2", "8fxv2"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fxv2"), recvPort: 57120).fix;
	}

	addFusListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fus, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fus][\value].value_(orbits.at(orbitIndex).get(\fus));
			tidalNetAddr.sendMsg("/ctrl", ["1fus", "2fus", "3fus", "4fus", "5fus", "6fus", "7fus", "8fus"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fus"), recvPort: 57120).fix;
	}

	addFus2Listener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fus2, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fus2][\value].value_(orbits.at(orbitIndex).get(\fus2));
			tidalNetAddr.sendMsg("/ctrl", ["1fus2", "2fus2", "3fus2", "4fus2", "5fus2", "6fus2", "7fus2", "8fus2"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fus2"), recvPort: 57120).fix;
	}

	addFutListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fut, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fut][\element].value_(value);
			guiElements[orbitIndex][\fut][\value].value_(orbits.at(orbitIndex).get(\fut));
			tidalNetAddr.sendMsg("/ctrl", ["1fut", "2fut", "3fut", "4fut", "5fut", "6fut", "7fut", "8fut"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fut"), recvPort: 57120).fix;
	}

	addFut2Listener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fut2, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fut2][\element].value_(value);
			guiElements[orbitIndex][\fut2][\value].value_(orbits.at(orbitIndex).get(\fut2));
			tidalNetAddr.sendMsg("/ctrl", ["1fut2", "2fut2", "3fut2", "4fut2", "5fut2", "6fut2", "7fut2", "8fut2"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fut2"), recvPort: 57120).fix;
	}

	addFupListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fup, value*~tidalnumFunctions) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fup][\element].value_(value);
			guiElements[orbitIndex][\fup][\value].value_(orbits.at(orbitIndex).get(\fup));
			tidalNetAddr.sendMsg("/ctrl", ["1fup", "2fup", "3fup", "4fup", "5fup", "6fup", "7fup", "8fup"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fup"), recvPort: 57120).fix;
	}

	addFup2Listener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fup2, value*~tidalnumFunctions) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fup2][\element].value_(value);
			guiElements[orbitIndex][\fup2][\value].value_(orbits.at(orbitIndex).get(\fup2));
			tidalNetAddr.sendMsg("/ctrl", ["1fup2", "2fup2", "3fup2", "4fup2", "5fup2", "6fup2", "7fup2", "8fup2"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fup2"), recvPort: 57120).fix;
	}

	addFuvListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fuv, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fuv][\element].value_(value);
			guiElements[orbitIndex][\fuv][\value].value_(orbits.at(orbitIndex).get(\fuv));
			tidalNetAddr.sendMsg("/ctrl", ["1fuv", "2fuv", "3fuv", "4fuv", "5fuv", "6fuv", "7fuv", "8fuv"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fuv"), recvPort: 57120).fix;
	}

	addFuv2Listener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\fuv2, value) /*.linlin(0,1,0,100)*/;
			guiElements[orbitIndex][\fuv2][\element].value_(value);
			guiElements[orbitIndex][\fuv2][\value].value_(orbits.at(orbitIndex).get(\fuv2));
			tidalNetAddr.sendMsg("/ctrl", ["1fuv2", "2fuv2", "3fuv2", "4fuv2", "5fuv2", "6fuv2", "7fuv2", "8fuv2"][orbitIndex], value);

		}.defer;
	}, ("/SuperDirtMixer/fuv2"), recvPort: 57120).fix;
	}

	/*addPanListener { OSCFunc ({|msg| // To delete
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\pan, value.linlin(0,1,0,1.0));
			guiElements[orbitIndex][\pan][\element].value_(orbits.at(orbitIndex).get(\pan));
			guiElements[orbitIndex][\pan][\value].value_(orbits.at(orbitIndex).get(\pan));
		}.defer;
	}, ("/SuperDirtMixer/pan"), recvPort: 57120).fix;
	}

	addGainListener { OSCFunc ({|msg|
		{
			var orbitIndex = msg[1];
			var value     = msg[2];

			orbits.at(orbitIndex).set(\masterGain, value.linlin(0,2,0,2));
			guiElements[orbitIndex][\masterGain][\element].value_((orbits.at(orbitIndex).get(\masterGain) + 1).curvelin(1,3, 0,1, curve: 3));
			guiElements[orbitIndex][\masterGain][\value].value_(orbits.at(orbitIndex).get(\masterGain));
		}.defer;
	}, ("/SuperDirtMixer/masterGain"), recvPort: 57120).fix;
	}

	addReverbListener {

		^OSCFunc ({|msg|
			{
				try {
					var orbitIndex = msg[1];
					var value      = msg[2];

					orbits.at(orbitIndex).set(reverbVariableName, value.linlin(0,1,0,1.0));
					guiElements[orbitIndex][\reverb][\element].value_(orbits.at(orbitIndex).get(reverbVariableName));

				} { |error| };

			}.defer;
		}, ("/SuperDirtMixer/reverb"), recvPort: 57120).fix;
	}*/

	addRemoteControlListener { OSCFunc ({|msg| {
		var event = ();
		var orbitIndex;

		event.putPairs(msg[1..]);

		orbitIndex = event.at(\orbit);

		if ((orbits.at(orbitIndex).isNil).not, {
			if (event.at(reverbVariableName).isNil.not, {
				orbits.at(orbitIndex).set(reverbVariableName, event.at(reverbVariableName).linlin(0,1,0,1.0));
				guiElements[orbitIndex][\reverb][\element].value_(orbits.at(orbitIndex).get(reverbVariableName));
			});

			/*if (event.at(\buf).isNil.not, {
			orbits.at(orbitIndex).set(\buf, event.at(\buf).linlin(0,1,0,1.0));
			guiElements[orbitIndex][\buf][\element].value_(orbits.at(orbitIndex).get(\buf));
			guiElements[orbitIndex][\buf][\value].value_(orbits.at(orbitIndex).get(\buf));
			});*/

			if (event.at(\pan).isNil.not, {
				orbits.at(orbitIndex).set(\pan, event.at(\pan).linlin(0,1,0,1.0));
				guiElements[orbitIndex][\pan][\element].value_(orbits.at(orbitIndex).get(\pan));
				guiElements[orbitIndex][\pan][\value].value_(orbits.at(orbitIndex).get(\pan));
			});

			if (event.at(\masterGain).isNil.not, {
				orbits.at(orbitIndex).set(\masterGain, event.at(\masterGain).linlin(0,2,0,2));
				guiElements[orbitIndex][\masterGain][\element].value_((orbits.at(orbitIndex).get(\masterGain) + 1).curvelin(1,3, 0,1, curve: 3));
				guiElements[orbitIndex][\masterGain][\value].value_(orbits.at(orbitIndex).get(\masterGain));
			});

			if (event.at(\label).isNil.not, {
				orbits.at(orbitIndex).set(\label, event.at(\label));
				guiElements[orbitIndex][\orbitLabel][\element].string_(orbits.at(orbitIndex).get(\label));
				guiElements[orbitIndex][\contextMenuLabel][\element].string_(orbits.at(orbitIndex).get(\label));
			});

			if (event.at(\color).isNil.not, {
				orbits.at(orbitIndex).set(\color, event.at(\color));
				guiElements[orbitIndex][\colorPicker][\element].setColorFromHexString(event.at(\color).asString);
				guiElements[orbitIndex][\orbitLabel][\element].background = event.at(\color).asString;
			});

		});

	}.defer;
	}, ("/SuperDirtMixer"), recvPort: 57121).fix;
	}

}