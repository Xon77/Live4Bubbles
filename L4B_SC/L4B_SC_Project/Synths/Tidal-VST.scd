// Modification creating a string compilation to be able to change ~vstName

("(
SynthDef(\\VST, { |out, pan = 0, n = 0, id = \\"++ ~vstName ++"|
	var sound;

	var env = EnvGen.kr(Env.asr(0.1, 1, 0.1), gate: 1, doneAction:2);
    // var env = EnvGen.ar(Env.asr(0.2, 1, 0.2), gate: 1, doneAction:2);
    // var env = Line.ar(0, 1, 1, doneAction:2);

	sound = In.ar(out, ~dirt.numChannels);

	// These are vst instruments
	sound = VSTPlugin.ar(sound, ~dirt.numChannels, id: \\"++ ~vstName ++") * env;

	// sound = VSTPlugin.ar(sound, ~dirt.numChannels, id: \zebralette2);
	// Here you could define more instruments

    Out.ar(out,
		DirtPan.ar(sound, ~dirt.numChannels, pan)
	)
}).add;
)")
/*.asCompileString.postln*/
.interpret;


// If you can hear audio clicks while playing with your custom SuperCollider synths, try the following:
/*
- rewrite your SynthDef the Tidal way (see above).
- raise the legato value in your pattern.
- increase the fadeTime parameter in SuperDirt:
~dirt.orbits[3].set(\fadeTime, 0.01);
~dirt.orbits[4].set(\fadeTime, 0.1);*
*/


/*( // ERROR: syntax error, unexpected '~'
SynthDef("VST", { |out, pan = 0, n = 0, id = ~vstName|
	var sound;

	var env = EnvGen.kr(Env.asr(0.1, 1, 0.1), gate: 1, doneAction:2);

	sound = In.ar(out, ~dirt.numChannels);

	// These are vst instruments
	sound = VSTPlugin.ar(sound, ~dirt.numChannels, id: ~vstName) * env;

	// sound = VSTPlugin.ar(sound, ~dirt.numChannels, id: \zebralette2);
	// Here you could define more instruments
	Out.ar(out,
		DirtPan.ar(sound, ~dirt.numChannels, pan)
	)
}).add;
)*/


/*( // Original
SynthDef("VST", { |out, pan = 0, n = 0, id = \dexed|
	var sound;

	var env = EnvGen.kr(Env.asr(0.1, 1, 0.1), gate: 1, doneAction:2);

	sound = In.ar(out, ~dirt.numChannels);

	// These are vst instruments
	sound = VSTPlugin.ar(sound, ~dirt.numChannels, id: \dexed) * env;

	// sound = VSTPlugin.ar(sound, ~dirt.numChannels, id: \zebralette2);
	// Here you could define more instruments
	Out.ar(out,
		DirtPan.ar(sound, ~dirt.numChannels, pan)
	)
}).add;
)*/
