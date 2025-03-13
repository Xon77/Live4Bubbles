/*

StageMaster
- [] GR Anzeige des Compressor auf Summe
- [] peak dB Anzeige auf Master

*/
SuperDirtMixerX {
	var dirt;
	var <>presetPath = "../presets/";
	var <>prMasterBus;
	var <>switchControlButtonEvent;
	var >midiInternalOut;
	var reverbVariableName = \room;
	var reverbNativeSize = 0.95;
	var oscMasterLevelSender;
	var defaultParentEvent, defaultParentEventKeys;
	var freeAction, addRemoteControlAction;

	*new { |dirt|
		^super.newCopyArgs(dirt).init
	}

	/* INITIALIZATION */
	init {
		try {
			"---- initialize SuperDirtMixer ----".postln;
			this.prLoadSynthDefs;

			dirt.server.sync;

			defaultParentEvent = Dictionary.new;

			dirt.startSendRMS;

			dirt.orbits.do({
				|orbit, i|
				orbit.set(\label, "d" ++ (i+1))
			});

			// Additional app state
			currentEnvironment.put(\SuperDirtMixer
				, Dictionary[
					\wasRemoteControlAdded -> false,
					\wasStageMasterSynthCreated -> false,
				]
			);

			"SuperDirtMixer was successfully initialized".postln;
		}
	}

	setOrbitLabels { // Deleted, but I like that to name tracks
		|labels|

		labels.do({
			|label, index|
			dirt.orbits[index].set(\label, label);
		});
	}

	prLoadSynthDefs { |path|
		var filePaths;
		path = path ?? { "../synths".resolveRelative };
		filePaths = pathMatch(standardizePath(path +/+ "*"));

		filePaths.do { |filepath|
			if(filepath.splitext.last == "scd") {
				(dirt:dirt).use { filepath.load }; "loading synthdefs in %\n".postf(filepath)
			}
		}
	}

	/* PUBLIC FUNCTIONS */
	setReverbNativeSize { |size|
		if (reverbVariableName == \room, {
			dirt.set(\size, size);
		});
		reverbNativeSize = size;
	}

	setReverbVariableName { |variableName|
		dirt.orbits.do({arg item;
			if ((variableName == \room).not, {
				item.defaultParentEvent.put(\size, nil);
			}, {
				item.defaultParentEvent.put(\size, reverbNativeSize)
			});

			if ((variableName == reverbVariableName).not , {
				item.defaultParentEvent.put(variableName, item.defaultParentEvent.at(reverbVariableName));
				item.defaultParentEvent.put(reverbVariableName, nil);
			});
		});

		reverbVariableName = variableName;
	}

	setMasterBus { |masterBus|
		this.prMasterBus = masterBus;

		oscMasterLevelSender = {
			var sound = InFeedback.ar(this.prMasterBus, /*2*/ ~dirt.numChannels); // change 2 by numChannels for multichannel
			SendPeakRMS.kr(sound, 10, 3, "/MixerMasterOutLevels")
		}.play;
	}

	disableMasterPeakRMS {
		this.prMasterBus = nil;
		oscMasterLevelSender.free;
	}

	free {
		freeAction.value();
	}

	addRemoteControl {
		addRemoteControlAction.value();
	}


	/* GUI */
	gui {
		/* DECLARE GUI VARIABLES */
		var v;// local machine

		// var window = Window("SuperDirtMixer", Rect(100, 300, 2000, 1000), scroll: true); // Not used

		var eventHandler = EventHandler.new;

		var mixerContainer, fxContainer, settingsContainer, midiContainer, masterContainer;
		var masterUI, mixerUI, utilityUI, equalizerUI, midiControlUI, compressorUI;

		~tidalWindow = Window.new("Live 4 Bubbles (Love & Fuck) ——— All 4 Poutchou!", bounds: Rect(~tidalWindowPosX /*0*/, ~tidalWindowPosY /*80*/, ~tidalWindowSize /*820*/ /*430*/, 1040 /*493*/)).alwaysOnTop_(true);

		mixerContainer = ScrollView(~tidalWindow /*window*/, Rect(10, 10, 380, 500)).background_(Color.gray(0.7)).fixedWidth_(~tidalWindowMixerSize)/*.maxWidth_(~tidalWindowMixerSize)*//*.minWidth_(~tidalWindowMixerSize/*592*/)*/.minHeight_(1032); // Add minWidth & minHeight - other way ?
		fxContainer = ScrollView(~tidalWindow /*window*/, Rect(10, 310, 450, 380)).background_(Color.gray(0.7))/*.minWidth_(1200)*/.minHeight_(380 /*410*/); // Add minWidth & minHeight - other way ?
		settingsContainer = CompositeView(~tidalWindow /*window*/, Rect(0, 0, 135, 500)).background_(Color.gray(0.85));
		midiContainer = CompositeView(~tidalWindow /*window*/, Rect(0, 0, 135, 500)).background_(Color.gray(0.85));
		masterContainer = CompositeView(~tidalWindow /*window*/, Rect(0, 0, 135 /*80*/, 500)).background_(Color.gray(0.85))/*.minWidth_(200).maxWidth_(80)*/.maxWidth_(~tidalWindowMasterSize /*200*/).maxHeight_(1030); // Add maxWidth & maxHeight - other way ?

		masterUI = MasterUIX.new(eventHandler);
		mixerUI = MixerUIX.new(eventHandler, dirt.orbits);
		utilityUI =  UtilityUIX.new(eventHandler, dirt.orbits, presetPath, defaultParentEvent);
		equalizerUI = EqualizerUIX.new(eventHandler, dirt.orbits, dirt.controlBusses, fxContainer);
		midiControlUI = MidiControlUI.new(switchControlButtonEvent, midiInternalOut);
		compressorUI = CompressorUIX.new(eventHandler, dirt.orbits);

		mixerUI.reverbVariableName = reverbVariableName;
		mixerUI.reverbNativeSize = reverbNativeSize;

		/* INIT GUI COMPONENTS */

		mixerUI.createUI(mixerContainer);
		utilityUI.createUI(settingsContainer);
		masterUI.createUI(masterContainer,this.prMasterBus);
		midiControlUI.createUI(midiContainer);
		compressorUI.createUI(fxContainer);

		/*window.drawFunc = {

			mixerContainer.resizeTo(window.bounds.width - 20, window.bounds.height - fxContainer.bounds.height - 20);

			fxContainer.resizeTo(
				window.bounds.width - 20 - settingsContainer.bounds.width
				- midiContainer.bounds.width - masterContainer.bounds.width, fxContainer.bounds.height
			);

			fxContainer.moveTo(10, window.bounds.height - fxContainer.bounds.height);

			settingsContainer.moveTo(window.bounds.width - settingsContainer.bounds.width - 10, window.bounds.height - fxContainer.bounds.height);

			midiContainer.moveTo(
				window.bounds.width - settingsContainer.bounds.width - midiContainer.bounds.width
				- masterContainer.bounds.width - 10, window.bounds.height - fxContainer.bounds.height
			);

			masterContainer.moveTo(
				window.bounds.width - settingsContainer.bounds.width - masterContainer.bounds.width - 10,
				window.bounds.height - fxContainer.bounds.height);
		};*/


		// Create a window
		// window = Window.new("Mixer", Rect(0,0,300,1000), scroll: true);

		~tidalWindowS = ScrollView.new(~tidalWindow, bounds: Rect(-7,-6, ~tidalWindowSize+10 /*830*/ /*686*/ /*436*/, 1040 /*493*/));
		~tidalWindowS.hasBorder = 0;
		~tidalWindowS.resize = 5;

		/*~tidalWindow*/ ~tidalWindowS.canvas = View().layout_(
			VLayout(
				HLayout (
					masterContainer, // masterUI.createUI(this.prMasterBus),
					mixerContainer, // mixerUI.createUI,
					settingsContainer, //utilityUI.createUI
				).spacing_(0),
				2 /*10*/,
				HLayout (
					fxContainer /*[fxContainer, align: \centre]*/,  // equalizerUI.createdUI,
					/*20,*/
					// compressorUI.createUI,
					/*20,
					midiControlUI.createUI,*/
					/*20*/
					// masterUI.createUI(this.prMasterBus),
					/*10,
					utilityUI.createUI*/
				)
			)
		);

		eventHandler.emitEvent(\setActiveOrbit, dirt.orbits[0]);

		freeAction = {
			eventHandler.emitEvent(\releaseAll);
			dirt.stopSendRMS;
		};

		addRemoteControlAction = {
			eventHandler.emitEvent(\addRemoteControl);
		};

		if (currentEnvironment[\SuperDirtMixer][\wasRemoteControlAdded] == false, {
			eventHandler.emitEvent(\addRemoteControl);
			currentEnvironment[\SuperDirtMixer].put(\wasRemoteControlAdded, true);
		});

		// ~tidalWindow.onClose_({ ~tidalFreqScope.kill; ~tidalFreqScope = nil; dirt.stopSendRMS; eventHandler.emitEvent(\releaseAll); ~dirt.free; /*s*/ ~dirt.server.quit; ~tidalX.disableMasterPeakRMS });

		~tidalWindow.onClose_({
			// ~tidalFreqScope.kill; ~tidalFreqScope = nil; // ?
			eventHandler.emitEvent(\destroy);
			// eventHandler.emitEvent(\releaseAll);
			// dirt.stopSendRMS;
			~dirt.free; /*s*/ ~dirt.server.quit; // To keep ?
			// ~tidalX.disableMasterPeakRMS;
		});
		~tidalWindow.front;

		/*window.onClose_({
			eventHandler.emitEvent(\destroy);
			//eventHandler.emitEvent(\releaseAll);
			//dirt.stopSendRMS;
		});
		window.front;*/
	}

}
