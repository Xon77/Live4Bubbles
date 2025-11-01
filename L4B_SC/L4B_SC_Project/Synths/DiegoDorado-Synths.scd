
// From Diego Dorado Github
// https://github.com/diegodorado/tidal-synths
// "I am not sure where the synths came from but they are surely not mine. In the future I will be more careful about this."

/*(
  "Loading synths.".underlined.postln;
  PathName.new(thisProcess.nowExecutingPath.dirname).filesDo{|f|
    (f.extension=="scd").and(f.asAbsolutePath!=thisProcess.nowExecutingPath).if({f.asAbsolutePath.load()},{});
};
)*/


(
SynthDef(\acid309, {|out, sustain=1, pan, accelerate, freq |
	var env1, env2, son, pitch, amp, gate;
	gate=1;
	amp = 0.5;
	pitch = freq.cpsmidi;
	pitch = Lag.kr(pitch, 0.12 * (1-Trig.kr(gate, 0.001)) * gate);
	env1 = EnvGen.ar(Env.new([0, 1.0, 0, 0], [0.001, 2.0, 0.04], [0, -4, -4], 2), gate, amp, doneAction:2);
	env2 = EnvGen.ar(Env.adsr(0.001, 0.8, 0, 0.8, 70, -4), gate);
	son = LFPulse.ar(pitch.midicps, 0.0, 0.51, 2, -1);

	son = RLPF.ar(son, (pitch + env2).midicps, 0.3);

	OffsetOut.ar(out, DirtPan.ar(son, ~dirt.numChannels, pan, env1));

}).add;
);


(
SynthDef(\bass8, {
	arg out = 0, amp = 0.1, gate = 1, pan = 0, freq = 200;
	var sig;
	var osc = Array.newClear(6);
	var env = Array.newClear(6);

	env[0] = EnvGen.kr(Env([0,1,0.051,0],[0.001,0.01,0.8], [4,-8]), 1);
	env[1] = EnvGen.kr(Env([0,1,0.051,0],[0.005,0.5,1.5], [0,-8], releaseNode:2), 1);
	env[2] = EnvGen.kr(Env([0,1,1,0],[0.01,0.01,0.2], [0,0,-4], releaseNode:2), gate);
	env[3] = EnvGen.kr(Env([0,1,0],[0.002,2.8], [0,-4]), 1);
	env[4] = EnvGen.kr(Env([0,1,1,0],[0.001,0.1,0.8], [4,0,-4], releaseNode:2), gate);
	env[5] = EnvGen.kr(Env([0,1,0],[0.001,3.0], [0,-4]), 1);

	freq = (freq / 4) * ((0..1)/1 - 0.5 * 0.0007 + 1);

	osc[0] = SinOsc.ar(freq * 11 + 0) * env[0];
	osc[1] = SinOsc.ar(freq * 6 * ( osc[0] * 2.5 + 1 )) * env[1];
	osc[2] = SinOsc.ar(freq * 2 * 1 + 0) * env[2];
	osc[3] = SinOsc.ar(freq * 1 * ( osc[2] * 2.5 + 1 ) + 0) * env[3];
	osc[4] = SinOsc.ar(freq * 1 * ( osc[1] * 2.5 + 1 ) * (osc[3] * 2.5 + 1)) * env[4];
	osc[5] = SinOsc.ar(freq * 2) * env[5];

	sig = osc * DC.ar([0.0, 0.0, 0.0,  0.0, 0.5, 0.5]);
	sig = sig * 0.5;
	sig = sig.flop.sum;
	sig = sig * EnvGen.ar(\adsr.kr( Env.adsr(0.001,0,1,0.01, 1,-1) ), gate, doneAction:2);
	sig = sig * AmpComp.kr(freq);
	sig = sig.sum;

	Out.ar(out, Pan2.ar(sig, pan, amp));

}).add;
);


// Click due to the interference between the envelope of the synth and the envelope of SuperDirt
(
SynthDef(\cs80leadClick, {
	arg freq=880, amp=0.5, attack=0.75, decay=0.5, sustain=0.8, release=1.0, fatt=0.75, fdecay=0.5, fsus=0.8, frel=1.0,
	cutoff=200, pan=0, dtune=0.002, vibrate=4, vibdepth=0.015, gate=1.0, ratio=1,out=0,cbus=1;
	var env,fenv,vib,ffreq,sig;

	cutoff=In.kr(cbus);
	env=EnvGen.kr(Env.adsr(attack,decay,sustain,release),gate,levelScale:1,doneAction:2);
	fenv=EnvGen.kr(Env.adsr(fatt,fdecay,fsus,frel,curve:2),gate,levelScale:1,doneAction:2);
	vib=SinOsc.kr(vibrate).range(-1*vibdepth,vibdepth)+1;
	freq=Line.kr(freq,freq*ratio,5);
	freq=freq*vib;
	sig=Mix.ar(Saw.ar([freq,freq*(1+dtune)]));
	// keep this below nyquist!!
	ffreq=max(fenv*freq*12,cutoff)+100;
	sig=LPF.ar(sig,ffreq);

	OffsetOut.ar(out, DirtPan.ar(sig, ~dirt.numChannels, pan, env));

}).add;
);

(
SynthDef(\cs80lead, {
	arg freq=880, amp=0.5, attack=0.75, decay=0.5, sustain=0.8, release=1.0, fatt=0.75, fdecay=0.5, fsus=0.8, frel=1.0,
	cutoff=200, pan=0, dtune=0.002, vibrate=4, vibdepth=0.015, gate=1.0, ratio=1,out=0,cbus=1;
	var env,fenv,vib,ffreq,sig;

	cutoff=In.kr(cbus);
	// env=EnvGen.kr(Env.adsr(attack,decay,sustain,release),gate,levelScale:1,doneAction:2);
	// fenv=EnvGen.kr(Env.adsr(fatt,fdecay,fsus,frel,curve:2),gate,levelScale:1,doneAction:2);
	vib=SinOsc.kr(vibrate).range(-1*vibdepth,vibdepth)+1;
	freq=Line.kr(freq,freq*ratio,5);
	freq=freq*vib;
	sig=Mix.ar(Saw.ar([freq,freq*(1+dtune)]));
	// keep this below nyquist!!
	ffreq=max(freq*12,cutoff)+100;
	sig=LPF.ar(sig,ffreq);

	OffsetOut.ar(out, DirtPan.ar(sig, ~dirt.numChannels, pan));

}).add;
);


(
SynthDef(\fmtribe, {|out, pan, accelerate, freq, gate,
	attack=0.1,
	decay=0.3,
	sustain=0.7,
	release=0.2,
	mattack=0.5,
	mdecay=0.5,
	msustain=0.7,
	mrelease=0.1,
	waveform=0 |

	var mod,modEnv,car,env,sig,clip;
	var modFreq = 50;
	var clipAmount = 0.1;
	freq = freq.cpsmidi;

	modEnv = EnvGen.ar(Env.adsr(mattack, mdecay, msustain, mrelease));
	mod = SinOsc.ar(modFreq) * modEnv;
	car = SinOsc.ar(freq + mod);
	env = EnvGen.ar(Env.adsr(attack, decay, sustain, release), doneAction: 2);

	clip = clipAmount * 500;
	sig = (car * clip).clip(-0.97, 0.97) * 0.751;
	OffsetOut.ar(out, DirtPan.ar(sig, ~dirt.numChannels, pan, env));

}).add;
);


/*(
fork{
	SynthDef(\basicFM, {|out = 0, gate = 1, amp = 1, carFreq = 1000, modFreq = 100, modAmount = 2000, clipAmount = 0.1|
		var modEnv = EnvGen.ar(Env.adsr(0.5, 0.5, 0.7, 0.1, peakLevel: modAmount), gate);
		var mod = SinOsc.ar(modFreq) * modEnv;
		var car = SinOsc.ar(carFreq + mod);
		var ampEnv = EnvGen.ar(Env.adsr(0.1, 0.3, 0.7, 0.2, peakLevel: amp), gate, doneAction: 2);
		var clip = clipAmount * 500;
		Out.ar(out, (car * ampEnv * clip).clip(-0.7, 0.7) * 0.1);
	}).add;

	s.sync;

	Pbind(*[
		instrument: \basicFM,
		out: [0, 1],
		dur: Pseq([1/7, 1/3] / 1.2, inf),
		sustain: Pkey(\dur),
		amp: 1/4,
		carFreq: Pseq([1, 1, 1, 2] * 350, inf),
		modFreq: Pkey(\carFreq) * (1/8),
		modAmount: Pseq([2, 1, 0.5, 2, 1, 0.5, 2, 1, 2, 1, 0.5, 2, 1, 0.5, 2, 4] * 500, inf),
		clipAmount: Pseq([Pn(0.1, 32), Pn(1, 32)], inf)
	]).play;
}
)*/


(
//https://sccode.org/1-4YY
SynthDef(\pbass, {
	arg out = 0, pan = 0, freq = 200;
	var subfreq = freq / 2;

	// Envelopes
	var subenv = EnvGen.kr(Env.perc(0, 1), doneAction:2);
	var env = EnvGen.kr(Env.perc(0, 0.5));

	// Component synthesis
	var pluck = Pluck.ar(PinkNoise.ar, 1, 0.2, subfreq.reciprocal) * subenv * 2;
	var tri = VarSaw.ar(freq) * env;
	var sin = SinOsc.ar(freq) * env;
	var sub = (SinOsc.ar([subfreq, subfreq - 2, subfreq + 2]).sum * subenv).tanh;
	var click = RLPF.ar(Impulse.ar(0), [2000, 8000], 1).sum * 1000;

	// Initial signal
	var sig = pluck + tri + sub + click;

	// Resonant LPFs
	sig = RLPF.ar(sig, XLine.ar(freq * 100, freq * 10, 0.15));
	sig = sig + (MoogFF.ar(sig, freq * 20, 2.5) * 0.1);

	// EQ resulting signal
	sig = BPeakEQ.ar(sig, 400, 0.5, -9);
	sig = BPeakEQ.ar(sig, 2000, 0.5, 6);
	sig = BHiShelf.ar(sig, 8000, 1, 3);
	sig = BPeakEQ.ar(sig, 200, 1, 3);

	// Apply another envelope to dampen a bit more
	sig = sig * XLine.kr(1, 0.6, 0.1);

	// Tanh distortion / limiting
	sig = (sig * 1).tanh;

	// Another round of signal coloring, using another RLPF
	// and sine components
	sig = sig + RLPF.ar(sig, XLine.ar(freq * 100, freq * 10, 0.15)) + sin + sub;

	// Another round of tanh distortion / limiting
	sig = (sig / 2.3).tanh;

	// Another resonant LPF
	sig = MoogFF.ar(sig, XLine.ar(freq*150, freq*30, 0.1), 0.1);

	OffsetOut.ar(out, DirtPan.ar(sig, ~dirt.numChannels, pan, env));

}).add;
);


(
SynthDef(\rhodes, {|out, sustain=1, pan, accelerate, freq |
  var sig, in, n = 6, max = 0.04, min = 0.01, delay, pitch, detune, hammer, amp, env;
  amp = 0.9;

	freq = freq.cpsmidi;
	hammer = Decay2.ar(Impulse.ar(0.001), 0.008, 0.04, LFNoise2.ar([2000,4000].asSpec.map(amp), 0.25));
	sig = Mix.ar(Array.fill(3, { arg i;
			detune = #[-0.04, 0, 0.03].at(i);
			delay = (1/(freq + detune).midicps);
			CombL.ar(hammer, delay, delay, 50 * amp)
		}) );

	sig = HPF.ar(sig,50);
    env = EnvGen.ar(Env.perc(0.0001,sustain, amp * 4, -1), doneAction:2);

    OffsetOut.ar(out, DirtPan.ar(sig, ~dirt.numChannels, pan, env));

}).add;
)