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

**A performance tool on controlling Live Coding Data Bases from [Tidal Cycles](https://tidalcycles.org/) or [Hydra](https://hydra.ojack.xyz/) with [SuperCollider](https://supercollider.github.io/) GUI or controllers.**

Live 4 Bubbles is an extension and modified version based on [SuperDirMixer](https://github.com/thgrund/SuperDirtMixer) from Thomas Grund. But it goes far beyond a simple mixer for Tidal Cycles - it transforms your live coding setup into a comprehensive performance system with unprecedented control over your sonic palette.

### 🎛️ Core Features

**Multi-Track Control System**
- Control up to 8 independent tracks simultaneously
- Real-time tempo and duration control per track
- Dynamic sound folder and buffer selection
- Advanced playback control (speed, direction, effects)

**Flexible Code Execution**
- Trigger and control Tidal Cycles code lines from SuperCollider GUI
- Switch between different code patterns on the fly
- Save and recall presets for instant performance setup
- Integration with MIDI controllers for hands-on control

**Advanced Parameter Management**
- Control tempo (BPM) independently per track
- Adjust duration and legato per sound
- Select sound folders and specific buffers from your database
- Apply effects with multiple parameter controls (2 effect chains per track)
- Spatial positioning and orbit management

**Database-Driven Sound Selection**
- Organize sounds in a structured folder hierarchy
- Quick navigation through sound libraries during performance
- Normalized control values for seamless automation
- Compatible with the [Live 4 Life sound database structure](https://github.com/Xon77/L4LSoundsDataBase)

**Performance-Ready**
- Preset system for quick setup and transitions
- MIDI controller integration (with multiple mapping options)
- CrossFade between patterns
- Real-time visual feedback in SuperCollider GUI

---

# 💿 Installation

---

### 📋 Prerequisites

Before installing Live 4 Bubbles, you need to have the following installed:

- **[SuperCollider](https://supercollider.github.io/)** - Audio synthesis platform
- **[Tidal Cycles](https://tidalcycles.org/)** - Live coding pattern language
- **[SuperDirt Quark](https://github.com/musikinformatik/SuperDirt)** - SuperCollider synthesizer for Tidal
- **[Pulsar](https://pulsar-edit.dev/)** (or Atom) - Text editor for live coding
- **[pulsar-tidalcycles](https://web.pulsar-edit.dev/packages/tidalcycles)** - Pulsar package for Tidal Cycles integration

---

### 📥 Step-by-Step Installation

**1. Install SuperDirtMixer**

Live 4 Bubbles is built on top of SuperDirtMixer. Follow the instructions in [SuperDirtMixer](https://github.com/thgrund/SuperDirtMixer) to install all [its requirements](https://github.com/thgrund/SuperDirtMixer?tab=readme-ov-file#requirements).

**2. Download Live 4 Bubbles**

Clone or download the Live 4 Bubbles repository to your local machine:

```bash
git clone https://github.com/Xon77/Live4Bubbles.git
```

**3. Install SuperCollider Extensions**

Copy the `SuperDirtMixerX` folder from `L4B_SC/SuperDirtMixerX/` into your SuperCollider extensions folder:

- **macOS**: `~/Library/Application Support/SuperCollider/Extensions/`
- **Linux**: `~/.local/share/SuperCollider/Extensions/`
- **Windows**: `%USERPROFILE%\AppData\Local\SuperCollider\Extensions\`

**4. Setup Sound Database**

Your sound folder must be structured following the [Live 4 Life sound database structure](https://github.com/Xon77/L4LSoundsDataBase/tree/main/SoundFolder).

**Important**: Folder names should use underscores instead of spaces (e.g., `DB_MBass` instead of `DB MBass`).

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

Edit the initialization files in `L4B_SC/L4B_SC_Project/` to match your system paths:
- Open `_0T_Init_TidalX.scd` in SuperCollider
- Update the sound folder path to point to your sound database
- Update any other paths as needed for your setup

---

# ⚙️ Setup

---

### 🚀 Quick Start

**1. Start SuperCollider**

Open SuperCollider and evaluate the initialization file:
- Navigate to `L4B_SC/L4B_SC_Project/`
- Open `_0T_Init_TidalX.scd`
- Evaluate the entire file (Cmd+A then Cmd+Enter on macOS, Ctrl+A then Ctrl+Enter on Windows/Linux)

This will:
- Boot the SuperCollider server
- Load SuperDirt
- Initialize the SuperDirtMixer GUI
- Setup all MIDI connections (if configured)

**2. Launch Pulsar with Tidal Files**

Open your Tidal Cycles files in Pulsar:

1. Open `L4B_Tidal/ControlTidal.tidal` first
2. Evaluate the 3 initialization steps at the beginning of the file
3. Open `L4B_Tidal/DJCode1.tidal` in tab 1
4. Open `L4B_Tidal/DJCode2.tidal` in tab 2
5. Open `L4B_Tidal/DJCode3.tidal` in tab 3
6. Open `L4B_Tidal/DJCode4.tidal` in tab 4
7. Open `L4B_Tidal/DJCodeG.tidal` in tab 5 (for global control)

**3. Initialize Control Variables**

In `ControlTidal.tidal`, evaluate the initialization function:

```haskell
initializeControls 8  -- Initialize controls for 8 tracks
```

This sets up all control parameters for tempo, duration, sound folders, buffers, effects, and more.

---

### 🎹 MIDI Controller Setup

Live 4 Bubbles supports multiple MIDI controller configurations:

**Available MIDI Initialization Files:**
- `_Init Midi X2.scd` - Main MIDI mapping (recommended)
- `_Init Midi T2.scd` - Alternative MIDI mapping
- `_Init Midi LPX.scd` - Launchpad X mapping

To setup MIDI:
1. Connect your MIDI controller
2. Open the appropriate MIDI initialization file in SuperCollider
3. Update the MIDI device names to match your controller
4. Evaluate the file to activate MIDI control

---

### 🎨 Pulsar Styling (Optional)

For a customized Pulsar appearance optimized for Live 4 Bubbles:

1. Navigate to `L4B_Pulsar/`
2. Copy the styles from `styles.less`
3. Paste them into your Pulsar stylesheet (Edit → Stylesheet)

---

### 📂 File Organization

**Key Files:**
- **ControlTidal.tidal** - Main control definitions and initialization
- **DJCode1-4.tidal** - Individual track patterns and code
- **DJCodeG.tidal** - Global controls and effects
- **_0T_Init_TidalX.scd** - SuperCollider initialization
- **PresetsTrax.txt** - Preset configurations for quick recall

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

### 📖 Usage Tips

**Starting a Performance:**
1. Load your preset from `PresetsTrax.txt` or create your own
2. Set initial sound folders for each track
3. Adjust tempo and duration parameters
4. Start triggering code lines from Pulsar
5. Use MIDI controllers or GUI for real-time parameter control

**Workflow Recommendations:**
- Use `DJCode1-4` for main pattern variations
- Use `DJCodeG` for global effects and transitions
- Save your favorite combinations as presets
- Organize sound folders by type for quick navigation

**Additional documentation will be added in the coming weeks and months.**

---

# 📚 References

---

This project, briefly introduced during Composite Montréal in June 2025 through a 5-minute pitch, has been presented on Saturday, October 18, 2025 as part of a three-hour public experimental workshop at [Eastern Bloc](https://www.easternbloc.ca). The event included a short performance and a participatory exploration session with the audience. I warmly thank Eastern Bloc and [CALQ](https://www.calq.gouv.qc.ca/en/) for their support in making this presentation possible.

---

# 🤝 Contribute

---

If you would like to contribute, please get in touch with me in order to organise further development.

You can also support thanks to donations via [**Ko-fi**](https://ko-fi.com/live4life) or get specific support and courses via [**Patreon**](https://www.patreon.com/live4life)</mark>. :grin:

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

:warning: **Moreover, I do not want that this tool to be used by or in [Société des Arts Technologiques](https://sat.qc.ca)** without my consent, since this organisation never helped me in diffusing my spatial creations in their dome despite my previous proposals. (This restriction will be withdrawn only if I have the opportunity to play improvisations in their dome with an appropriate allowance. However, it will surely never happen, since I am no longer interested to send them applications and I am looking for more open spaces. But, Life is unpredictable;)

See the [License](/LICENCE.md) for more details.