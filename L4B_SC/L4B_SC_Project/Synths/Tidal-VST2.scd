// Modification replacing \dexed by ~vstName & "Dexed.vst3" by ~vstNameString

(
var diversions = ();
var vstControlBus = ();
var vstControlBusses = ();
var synths = Dictionary [
	~vstName -> Synth("VST", [id: ~vstName]),
	// \dexed -> Synth("VST", [id: \dexed]),
	// \zebralette2 -> Synth("VST", [id: \zebralette2])
];
var instrument;
var instrumentSynths= ();
var instruments = Dictionary[
	~vstName -> VSTPluginController(synths.at(~vstName), id: ~vstName).open(~vstNameString, editor: true, verbose: false),
	// \dexed -> VSTPluginController(synths.at(\dexed), id: \dexed).open("Dexed.vst3", editor: true, verbose: false),
	// \odin2 -> VSTPluginController(synths.at(\odin2), id: \odin2).open("Odin2.vst3", editor: true, verbose: false),
	// \zebralette2 -> VSTPluginController(synths.at(\zebralette2), id: \zebralette2).open("Zebralette", editor: true, verbose: false)
];


var triggerFunc = { |vstName|
	var oldBusses = Set[];
	var newBusses = Set[];
	var busId;

	var lag = ~lag + (~latency ? 0);
	var latency = lag; // for now
	var sustain = ~sustain = ~sustain.value;
	var freq = ~freq.value;
	var note = (~freq.cpsmidi).round(1).asInteger;
	var velocity = (~amp.value * pow(~gain.min(2) + ~overgain, 4);).linlin(0,1,0,127).asInteger;

	instrument = instruments[vstName];

	// latency = lag
	// ("latency :"+latency).postln;
	// ("lag :"+lag).postln;

	// Switch to the current orbit
	synths.at(vstName).set(
		\out, ~out,
		\pan, currentEnvironment.at(\pan),
	);

	~dirt.server.makeBundle(~latency, {

		currentEnvironment.keysDo { |key|

			if (key.asString.contains("varg"), {

				busId = currentEnvironment.at(key);

				if (busId.asString.contains("c"), {

					if (vstControlBusses.at(busId).isNil, {
						vstControlBusses.put(busId, Set[]);
					});

					vstControlBusses.at(busId).add(vstName);
					vstControlBus.put(currentEnvironment.at(key), key.asString.replace("varg", ""));
					newBusses.add(currentEnvironment.at(key));
				}, {
					instrument.set(key.asString.replace("varg", "").asInteger, currentEnvironment.at(key));
				});
			});

			vstControlBusses.keysDo {|bus|
				if (vstControlBusses.at(bus).includes(vstName), {
					oldBusses.add(bus)
				});
			};
		};

		(newBusses -- oldBusses).do{ |deleteBus|
			vstControlBusses.at(deleteBus).remove(vstName);
		};

	});


	// Comments by Spacechild1 : no sound / how to make it work ?
	// https://github.com/thgrund/TidalVST/issues/4

	// I just noticed that you schedule MIDI messages on the tempo clock:
	thisThread.clock.sched(latency, {
		instruments[vstName].midi.noteOn(0, note, velocity);
	});

	thisThread.clock.sched(sustain + latency, {
		instruments[vstName].midi.noteOff(0, note, velocity);
	});

	// For accurate timing, the MIDI messages should be scheduled as OSC bundles instead. For example:
	/*~dirt.server.makeBundle(latency, {
		instruments[vstName].midi.noteOn(0, note, velocity);
	});

	~dirt.server.makeBundle(sustain + latency, {
		instruments[vstName].midi.noteOff(0, note, velocity);
	});*/


		/* This is plugin specific to switch the presets
	      if (~preset.notNil, {
		  (\type: \vst_midi,
			\vst: instruments[~vstName],
			\midicmd: \control,
			\chan: 0,\ctlNum: 0,\control: 1
		  ).play;

		  (\type: \vst_midi,
			\vst: instruments[~vstName],
			\midicmd: \program,
			\chan: 0,\progNum: ~preset
		  ).play;
		});*/
};

var serverMessage = { |synth|
	[\out, ~out, \sustain, ~sustain].asControlInput.flop.do { |each|
		~dirt.server.sendMsg(\s_new,
			synth,
			-1, // no id
			1, // add action: addToTail
			currentEnvironment.at(\synthGroup), // send to group
			*each.asOSCArgArray // append all other args
		)
	}
};


// Below with ~vstName -> Triggers a MIDI error when Tidal sends info, then use of case
case
{ ~vstName == \dexed }
{
~dirt.soundLibrary.addSynth(\vst,
	(playInside: { |e|
		triggerFunc.value(\dexed /*~vstName*/);
		serverMessage.value(\VST);
	})
);
}
{ ~vstName == \vital }
{
~dirt.soundLibrary.addSynth(\vst,
	(playInside: { |e|
		triggerFunc.value(\vital /*~vstName*/);
		serverMessage.value(\VST);
	})
);
}
{ ~vstName == \surge }
{
~dirt.soundLibrary.addSynth(\vst,
	(playInside: { |e|
		triggerFunc.value(\surge /*~vstName*/);
		serverMessage.value(\VST);
	})
);
};


// Tried to compile a string in the same way as Tidal-VST file -> But does not work
/*
~triggerFunc = triggerFunc;
~serverMessage = serverMessage;

("(~dirt.soundLibrary.addSynth(\\vst,
(playInside: { |e|
~triggerFunc.value(\\"++ ~vstName ++");
~serverMessage.value(\\VST);
})
);)"
).interpret;

// Try to compile a string in the same way as Tidal-VST file -> But does not work
("(~dirt.soundLibrary.addSynth(\\vst,
(playInside: { |e|
triggerFunc.value(\\"++ ~vstName ++");
serverMessage.value(\\VST);
})
);)"
).interpret;
*/


~instruments = instruments;

)