# Welcome to ***Live 4 Bubbles !*** &nbsp;&nbsp; <span class="badge-licence"><a href="https://creativecommons.org/licenses/by-nc-sa/4.0/" title="Licence"><img src="https://licensebuttons.net/l/by-nc-sa/3.0/88x31.png" alt="Licence link" /></a></span> &nbsp;&nbsp; <span class="badge-buymeacoffee"><a href="https://ko-fi.com/Live4Life" title="Donate to this project using Buy Me A Coffee"><img src="https://img.shields.io/badge/buy%20me%20a%20coffee-donate-yellow.svg" alt="Buy Me A Coffee donate button" /></a></span> &nbsp;&nbsp; <span class="badge-patreon"><a href="https://patreon.com/Live4Life" title="Donate to this project using Patreon"><img src="https://img.shields.io/badge/patreon-donate-yellow.svg" alt="Patreon donate button" /></a></span>

<p align="center">
<img src="Images/live4bubbles_overview.gif" />
</p>

<div align="center">

| [**Overview**](#-overview) | [**Installation**](#-installation) | [**Setup**](#%EF%B8%8F-setup) | [**References**](#-references) | [**Contribute**](#-contribute) | [**Acknowledgements**](#-acknowledgements) | [**Licence**](#-licence) |

</div>

---

# 📖 Overview

---

**A performance tool for controlling Live Coding Databases from [Tidal Cycles](https://tidalcycles.org/) and [Hydra](https://hydra.ojack.xyz/) with [SuperCollider](https://supercollider.github.io/) GUI and controllers. It is my new life-long project, for as many years as possible until the end of my life.**

*Live 4 Bubbles* is an extension and modified version based on [SuperDirtMixer](https://github.com/thgrund/SuperDirtMixer) from Thomas Grund. But it goes far beyond a mixer for Tidal Cycles - it transforms your live coding setup into a comprehensive performance and control system with control over your rhythmic, spectral and spatial patterns of sound files and SuperCollider synthesis.

### 🎛️ Core Features

**Multi-Track Control System of code execution with dynamic control over dozens of parameters**
- Control up to 8 independent tracks simultaneously. (Above you can see images from a four-track representation).
- Trigger and control Tidal Cycles code lines from SuperCollider GUI to switch between different code patterns on the fly.
- Preset system for instant performance setup and transitions with crossfade between patterns.
- Integration with MIDI controllers for hands-on control.

**Parameter Management:**
- Speed of the pattern or the sound, duration and legato,
- Sound folder and buffer selection for quick navigation through sound libraries during performance,
- Filter & Database of Effects on the sound and the patterns with multiple parameter controls,
- Spatial positioning and orbit management.

---

# 💿 Installation

---

### 📋 Prerequisites

Before installing *Live 4 Bubbles*, you need to have the following installed:

- **[SuperCollider](https://supercollider.github.io/)** - Audio synthesis platform
- **[Tidal Cycles](https://tidalcycles.org/)** - Live coding pattern language
- **[SuperDirt Quark](https://github.com/musikinformatik/SuperDirt)** - SuperCollider synthesizer for Tidal
- **[Pulsar](https://pulsar-edit.dev/)** - Text editor for live coding
- **[pulsar-tidalcycles](https://web.pulsar-edit.dev/packages/tidalcycles)** - Pulsar package for Tidal Cycles integration

You can use my variation of [pulsar-tidalcycles](https://github.com/Xon77/pulsar-tidalcycles) with additional features.

---

### 📥 Step-by-Step Installation

**1. Install SuperDirtMixer**

*Live 4 Bubbles* is built on top of SuperDirtMixer. Follow the instructions in [SuperDirtMixer](https://github.com/thgrund/SuperDirtMixer) to install all [its requirements](https://github.com/thgrund/SuperDirtMixer?tab=readme-ov-file#requirements).

**2. Download *Live 4 Bubbles***

Clone or download the *Live 4 Bubbles* repository to your local machine:

```bash
git clone https://github.com/Xon77/Live4Bubbles.git
```

**3. Install SuperCollider Extensions**

Copy the [`SuperDirtMixerX`](L4B_SC/SuperDirtMixerX/) folder from [`L4B_SC/SuperDirtMixerX/`](L4B_SC/SuperDirtMixerX/) into your SuperCollider extensions folder:

- **macOS**: `~/Library/Application Support/SuperCollider/Extensions/`
- **Linux**: `~/.local/share/SuperCollider/Extensions/`
- **Windows**: `%USERPROFILE%\AppData\Local\SuperCollider\Extensions\`

**4. Setup Sound Database**

**Important**: Your sound folder must be structured following the [Live 4 Life sound database structure](https://github.com/Xon77/L4LSoundsDataBase/tree/main/SoundFolder), except that folder names should use underscores instead of spaces (e.g., `DB_MBass` instead of `DB MBass`).

Example structure:
```
YourSoundFolder/
├── DB_Kick/
│   ├── sound1.wav
│   └── sound2.wav
├── DB_Snare/
├── DB_Bass/
└── ...
```

**5. Configure File Paths**

Edit the initialization files in [`L4B_SC/L4B_SC_Project/`](L4B_SC/L4B_SC_Project/) to match your system paths:
- Open [`_0T_Init_TidalX.scd`](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd) in SuperCollider
- Update the sound folder path to point to your sound database
- Update any other paths as needed for your setup

---

# ⚙️ Setup

---

### 🚀 Quick Start

**1. Start SuperCollider**

Open SuperCollider and evaluate the initialization file:
- Navigate to [`L4B_SC/L4B_SC_Project/`](L4B_SC/L4B_SC_Project/)
- Open [`_0T_Init_TidalX.scd`](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd)
- Evaluate the section in parentheses between [lines 16 and 422](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L16-L422) within the file (Cmd+Enter on macOS, Ctrl+Enter on Windows/Linux)

This will:
- Boot the SuperCollider server
- Load SuperDirt
- Initialize the SuperDirtMixer GUI
- Setup all MIDI connections (if configured)

**2. Launch Pulsar with Tidal Files to create a four-track code execution partition**

Open all Tidal Cycles files from the [`L4B_Tidal/`](L4B_Tidal/) folder in Pulsar:
1. Open [`L4B_Tidal/DJCode1.tidal`](L4B_Tidal/DJCode1.tidal) in tab 1
2. Open [`L4B_Tidal/DJCode2.tidal`](L4B_Tidal/DJCode2.tidal) in tab 2
3. Open [`L4B_Tidal/DJCode3.tidal`](L4B_Tidal/DJCode3.tidal) in tab 3
4. Open [`L4B_Tidal/DJCode4.tidal`](L4B_Tidal/DJCode4.tidal) in tab 4
5. Open [`L4B_Tidal/DJCodeG.tidal`](L4B_Tidal/DJCodeG.tidal) in tab 5 (for global control)
6. Open [`L4B_Tidal/ControlTidal.tidal`](L4B_Tidal/ControlTidal.tidal) in tab 6 for initialization

Other workflows are possible. For example, you can execute code lines by selecting different splits and tabs. (The images above represent four splits of the first tab.)

**3. Initialize Control Variables in Tidal Files**

In [`ControlTidal.tidal`](L4B_Tidal/ControlTidal.tidal), evaluate the following sections in order with Ctrl+Enter (Cmd+Enter on macOS):

1. Evaluate [lines 5 to 1107](L4B_Tidal/ControlTidal.tidal#L5-L1107) - Main control definitions and functions 
2. Evaluate [line 1110](L4B_Tidal/ControlTidal.tidal#L1110) - Initialization of controls and variables
3. Evaluate [line 1113](L4B_Tidal/ControlTidal.tidal#L1113) - Update of definitions and functions of step 1
4. Evaluate [lines 1118 to 1136](L4B_Tidal/ControlTidal.tidal#L1118-L1136) - Final initialization for crossfades

This sets up all control parameters for tempo, duration, sound folders, buffers, effects, and more.

You can begin to play.

---

### 🎹 MIDI Controller Setup

*Live 4 Bubbles* can support multiple MIDI controllers. The mapping has been done for the following controllers:

- [`_Init Midi X2.scd`](L4B_SC/L4B_SC_Project/_Init%20Midi%20X2.scd) - Xone K2 from Allen & Heath
- [`_Init Midi T2.scd`](L4B_SC/L4B_SC_Project/_Init%20Midi%20T2.scd) - MIDI Fighter Twister
- [`_Init Midi LPX.scd`](L4B_SC/L4B_SC_Project/_Init%20Midi%20LPX.scd) - Launchpad X from Novation

To setup MIDI, simply connect your MIDI controller. For the MIDI Fighter Twister, name the controllers depending on how many you have: "MIDI Fighter Twister 1", "MIDI Fighter Twister 2", "MIDI Fighter Twister 3", as you can now connect up to 3 MIDI Fighters together. The controllers are initialized by evaluating [`_0T_Init_TidalX.scd`](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd).

---

### 🎨 Pulsar Styling (Optional)

For a customized Pulsar appearance optimized for *Live 4 Bubbles*:

1. Navigate to [`L4B_Pulsar/`](L4B_Pulsar/)
2. Copy the styles from [`styles.less`](L4B_Pulsar/styles.less)
3. Paste them into your Pulsar stylesheet (Edit → Stylesheet)


---

### 🎛️ Control Parameters

Each of the 8 tracks has access to the following parameters:

**Timing & Structure:**
- `d1` to `d8` - Tempo/rhythm structure
- `1Leg` to `8Leg` - Duration/legato
- `1LegS` to `8LegS` - Duration algorithm switch

**Sound Selection:**
- `1Fol` to `8Fol` - Sound folder selection
- `1Buf` to `8Buf` - Buffer selection within folder
- `1FolN` to `8FolN` - Normalized folder index (0-1)
- `1BufN` to `8BufN` - Normalized buffer index (0-1)

**Playback:**
- `1Rat` to `8Rat` - Playback rate/speed
- `1Ran` to `8Ran` - Random parameter (context-dependent)
- `1Xon` to `8Xon` - Orbit assignment

**Effects (2 chains per track):**
- `1fxs` to `8fxs` - Effect selection (chain 1)
- `1fxx` to `8fxx` - Effect mix (chain 1)
- `1fxt` to `8fxt` - Effect type parameter (chain 1)
- `1fxp` to `8fxp` - Effect parameter 1 (chain 1)
- `1fxv` to `8fxv` - Effect parameter 2 (chain 1)
- `1fxs2` to `8fxs2` - Effect selection (chain 2)
- `1fxx2` to `8fxx2` - Effect mix (chain 2)
- (and so on for chain 2...)

**User Functions:**
- `1fus` to `8fus` - User function selection
- `1fux` to `8fux` - User function mix
- (and similar parameters for user functions)

**Spatial:**
- `1Spa` to `8Spa` - Spatial positioning

---

**Additional documentation will be added in the coming weeks and months.**

---

# 📚 References

---

This project, briefly introduced during Composite Montréal in June 2025 through a 5-minute pitch, has been presented on Saturday, October 18, 2025 as part of a three-hour public experimental workshop at [Eastern Bloc](https://www.easternbloc.ca). The event included a short performance and a participatory exploration session with the audience. I warmly thank Eastern Bloc and [CALQ](https://www.calq.gouv.qc.ca/en/) for their support in making this presentation possible.

---

# 🤝 Contribute

---

If you would like to contribute, please get in touch with me in order to organise further development.

You can also support through donations via [**Ko-fi**](https://ko-fi.com/live4life) or get specific support and courses via [**Patreon**](https://www.patreon.com/live4life). :grin:

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/Live4Life)

<a href="https://www.patreon.com/Live4Life">
	<img src="https://c5.patreon.com/external/logo/become_a_patron_button@2x.png" width="160">
</a>

---

# 🙏 Acknowledgements

---


As [mentioned in my previous project](https://github.com/Xon77/Live4Life?tab=readme-ov-file#acknowledgements) **_Live 4 Life_**, I would have been unable to build this tool without the help of the SuperCollider online community, who always answered my questions and even provided me with some examples of codes and classes. 

I would like to especially thank Thomas Grund, without whom this project would not have come to life—not only thanks to the Quark [SuperDirMixer](https://github.com/thgrund/SuperDirtMixer) but also for implementing the feature allowing the evaluation of Tidal code lines in Atom/Pulsar, which I had suggested on [Github](https://github.com/tidalcycles/pulsar-tidalcycles/issues/119).

Last, but not least, I would like to thank [CALQ](https://www.calq.gouv.qc.ca/en/), who supported me in this project.

---

# 📄 Licence

---

© 2025 - End of the world ∞ Christophe Lengelé

***Live 4 Bubbles*** is an open source software: you can redistribute it and/or modify it under the terms of **Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International license** (CC BY-NC-SA 4.0). 

:warning: **You may not use it for commercial purposes.**

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY. 

**I wish it would be used in the spirit of [*Free Party*](https://en.wikipedia.org/wiki/Free_party)**. Unfortunately, *Free* does not mean free in this commercial world, but invites to contribute to the costs and labor according to one's ability to give. I do not want this tool to be used, by any means, for personal profit.

:warning: **Moreover, I do not want this tool to be used by or in [Société des Arts Technologiques](https://sat.qc.ca)** without my consent, since this organization never helped me in diffusing my spatial creations in their dome despite my previous proposals. (This restriction will be withdrawn only if I have the opportunity to play improvisations in their dome with an appropriate allowance. However, it will surely never happen, since I am no longer interested in sending them applications and I am looking for more open spaces. But, life is unpredictable;)

See the [License](/LICENCE.md) for more details.