-- /Users/xon/.cabal/share/aarch64-osx-ghc-9.2.2/tidal-1.9.5/BootTidal.hs -- Boot file by default
-- /Users/xon/.cabal/store/ghc-9.2.2/tdl-1.9.5-6bf6d67f/share/BootTidal.hs -- Other Boot file
-- /Users/xon/Desktop/Live_Coding/Tidal/ExtraSynths/_BootTidal.hs -- The Custom Boot file path
-- Option Use Boot file in Current directory no longer available ?


:set -XOverloadedStrings
:set prompt ""

import Sound.Tidal.Context
import Sound.Tidal.Params -- Impact ?


-- Below necessary for SETUP_250225.tidal at the end with lots of abbreviations & algos
import Sound.Tidal.Pattern
import Sound.Tidal.Time
import Sound.Tidal.Chords -- add
import Data.Maybe (mapMaybe)
import Data.List (sortBy)
import Control.Applicative


import System.IO (hSetEncoding, stdout, utf8)


-- https://github.com/timcowlishaw/tidal_experiments/blob/main/BootTidal.hs
import System.IO.Unsafe (unsafePerformIO) -- necessary for Tim Cowlishaw extra utilities - why ?
import Control.Monad (liftM) -- necessary for Tim Cowlishaw extra utilities - why ?

hSetEncoding stdout utf8


-- https://tidalcycles.org/docs/configuration/boot-tidal

-- total latency = oLatency + cFrameTimespan => 0.07 below (for 0.2, put 0.15 in oLatency)
-- By default oLatency = 0.1 or 0.05 & cFrameTimespan = 1/20
-- In BootTidal.hs you can configure cFrameTimespan, which is by default 1/20, which means tidal calculates in chunks of one 20th of a second. This is a balance between smaller values (e.g. 1/30) that spread the load better and improve interactivity (i.e. decrease latency), and larger values (e.g. 1/10) which might (or might not) decrease the overall load. I haven't spent much time tweaking this..
-- Also in there is oLatency, decrease that for greater interactivity, or increase it to increase latency but iron out 'spikes', e.g. if you see superdirt complain of late messages.

-- XXXXXX
-- tidal <- startTidal (superdirtTarget {oLatency = 0.001, oAddress = "127.0.0.1", oPort = 57120}) (defaultConfig {cFrameTimespan = 1/20, cProcessAhead = 2/10}) -- Retrait du port rajouté dans SuperDirtMixer plus bas

tidal <- startTidal (superdirtTarget {oLatency = 0.05, oAddress = "127.0.0.1", oPort = 57120}) (defaultConfig {cCtrlAddr = "0.0.0.0", cCtrlPort = 6010, cVerbose = True, cFrameTimespan = 1/20, cProcessAhead = 2/10})
-- ,but  adding cProcessAhead = 1/10 or below 1/20 triggers more late messages - Why ? -- No cProcessAhead is ok or a minimum of 2/10


-- Add event highlighting
-- let editorTarget = Target {oName = "editor", oAddress = "127.0.0.1", oPort = 6013, oLatency = 0.02, oSchedule = Pre BundleStamp, oWindow = Nothing, oHandshake = False, oBusPort = Nothing }
-- let editorShape = OSCContext "/editor/highlights"

-- tidal <- startStream (defaultConfig {cFrameTimespan = 1/50}) [(superdirtTarget {oLatency = 0.2}, [superdirtShape]), (editorTarget, [editorShape])]


-- tidalInst <- mkTidalWith [(superdirtTarget { oLatency = 0.01 }, [superdirtShape])] (setFrameTimespan (1/50) $ setProcessAhead (1/20) defaultConfig)
-- tidal <- startTidal (superdirtTarget {oLatency = 0.05, oAddress = "127.0.0.1", oPort = 57120}) (defaultConfig {cCtrlAddr = "0.0.0.0", cCtrlPort = 6010, cVerbose = True, cFrameTimespan = 1/30, cProcessAhead = 0.05})

-- s.latency serves the same function as cProcessAhead
-- Process Ahead: This parameter controls how far ahead Tidal will start processing events. It might need to be adjusted when a high latency value is set. Adjust this value if you get late messages in SuperCollider. The default is 0.3 seconds.
-- I suspect that this only matters in situations where SuperCollider is scheduling/sequencing its own patterns of events. If the messages that SuperDirt receive have timestamps, then it passes that timing information to server.makeBundle, whose documentation suggests that the bundle will be sent immediately (ignoring s.latency).

-- For Tidal VST as in the doc : https://github.com/thgrund/TidalVST
-- let vstTarget = Target {oName = "hydra", oAddress = "127.0.0.1", oHandshake = True, oPort = 3337, oBusPort = Just 3338, oLatency = 0.1, oSchedule = Pre BundleStamp, oWindow = Nothing}
-- tidal <- startStream (defaultConfig {cFrameTimespan = 1/20}) [(superdirtTarget {oLatency = 0.15}, [superdirtShape]),(vstTarget, [superdirtShape]) ]


-- Final version
-- Définition de la cible VST
-- let vstTarget = Target { oName = "vst", oAddress = "127.0.0.1", oHandshake = True, oPort = 3337, oBusPort = Just 3338, oLatency = 0.1, oSchedule = Pre BundleStamp, oWindow = Nothing}

-- Démarrage du stream avec SuperDirt et VST
-- tidal <- startStream (defaultConfig { cCtrlAddr = "0.0.0.0", cCtrlPort = 6010, cVerbose = True, cFrameTimespan = 1/30}) [ (superdirtTarget {oLatency = 0.1, oAddress = "127.0.0.1", oPort = 57120}, [superdirtShape]), (vstTarget, [superdirtShape]) ]



-- How to set the sound browser dynamically ?
-- setSoundFolder = "/Users/xon/Library/Application Support/SuperCollider/downloaded-quarks/Dirt-Samples/"


-- XXXXXX
:{
let superDirtMixerTarget = Target {
        oName = "superDirtMixer", oAddress = "localhost"
        , oPort = 57121, oLatency = 0.2, oSchedule = Live
        , oWindow = Nothing, oHandshake = False
        , oBusPort = Nothing }
    oscplay = OSC "/SuperDirtMixer" Named {requiredArgs = ["orbit"]}
    oscmap = [(superDirtMixerTarget, [oscplay])]
:}

-- superDirtMixer <- startStream (defaultConfig {cCtrlPort = 6010}) oscmap
-- tidal <- startStream (defaultConfig {cCtrlPort = 6010}) oscmap

:{
{-
let x1 = streamReplace superDirtMixer 1 . (|< orbit 0)
    x2 = streamReplace superDirtMixer 2 . (|< orbit 1)
    x3 = streamReplace superDirtMixer 3 . (|< orbit 2)
    x4 = streamReplace superDirtMixer 4 . (|< orbit 3)
    x5 = streamReplace superDirtMixer 5 . (|< orbit 4)
    x6 = streamReplace superDirtMixer 6 . (|< orbit 5)
    x7 = streamReplace superDirtMixer 7 . (|< orbit 6)
    x8 = streamReplace superDirtMixer 8 . (|< orbit 7)
    x9 = streamReplace superDirtMixer 9 . (|< orbit 8)
    x10 = streamReplace superDirtMixer 10 . (|< orbit 9)
    x11 = streamReplace superDirtMixer 11 . (|< orbit 10)
    x12 = streamReplace superDirtMixer 12 . (|< orbit 11)
    x13 = streamReplace superDirtMixer 13 . (|< orbit 12)
    x14 = streamReplace superDirtMixer 14 . (|< orbit 13)
-- This is needed to silence all patterns from both streams
    hushS = do
           streamHush tidal
           streamHush superDirtMixer
-}
:}



:{
{-
-- https://tidalcycles.org/docs/configuration/MIDIOSC/osc
-- https://github.com/kindohm/tidal-osc-console-display

-- Cible OSC pour envoyer des messages à l'adresse "/osc"
let target = Target
      { oName = "osc_target"          -- Un nom convivial pour l'affichage
      , oAddress = "127.0.0.1"        -- L'adresse locale (localhost)
      , oPort = 57121                 -- Port d'écoute de la cible (différent de SuperDirt)
      , oLatency = 0.1                -- Latence ajoutée pour réduire les décalages
      , oSchedule = Live              -- Envoi en temps réel
      , oBusPort = Nothing            -- Valeur par défaut
      , oWindow = Nothing             -- Valeur par défaut
      , oHandshake = False            -- Valeur par défaut
      }
-- Message OSC à envoyer à l'adresse "/osc" sans paramètres
    -- oscMessage = OSC "/osc" (ArgList [("s", Nothing)])
    oscMessage = OSC "/osc" Named {requiredArgs = ["s"]}
-- Mappage entre la cible OSC et le message à envoyer
    oscmap = [(target, [oscMessage])]
-- Lancer le flux avec la cible OSC et envoyer le message
    main :: IO ()
    main = do
      putStrLn "Envoi du message OSC à /osc..."
      stream <- startStream defaultConfig oscmap
      -- d = streamReplace stream
      putStrLn "Message envoyé."
      return ()
-}
:}



:{
let only = (hush >>)
    p = streamReplace tidal
    hush = streamHush tidal
    panic = do hush
               once $ sound "superpanic"
    list = streamList tidal
    mute = streamMute tidal
    unmute = streamUnmute tidal
    unmuteAll = streamUnmuteAll tidal
    unsoloAll = streamUnsoloAll tidal
    solo = streamSolo tidal
    unsolo = streamUnsolo tidal
    once = streamOnce tidal
    first = streamFirst tidal
    asap = once
    nudgeAll = streamNudgeAll tidal
    all = streamAll tidal
    resetCycles = streamResetCycles tidal
    setCycle = streamSetCycle tidal
    setcps = asap . cps
    getcps = streamGetcps tidal
    getnow = streamGetnow tidal
    xfade i = transition tidal True (Sound.Tidal.Transition.xfadeIn 4) i
    xfadeIn i t = transition tidal True (Sound.Tidal.Transition.xfadeIn t) i
    histpan i t = transition tidal True (Sound.Tidal.Transition.histpan t) i
    wait i t = transition tidal True (Sound.Tidal.Transition.wait t) i
    waitT i f t = transition tidal True (Sound.Tidal.Transition.waitT f t) i
    jump i = transition tidal True (Sound.Tidal.Transition.jump) i
    jumpIn i t = transition tidal True (Sound.Tidal.Transition.jumpIn t) i
    jumpIn' i t = transition tidal True (Sound.Tidal.Transition.jumpIn' t) i
    jumpMod i t = transition tidal True (Sound.Tidal.Transition.jumpMod t) i
    jumpMod' i t p = transition tidal True (Sound.Tidal.Transition.jumpMod' t p) i
    mortal i lifespan release = transition tidal True (Sound.Tidal.Transition.mortal lifespan release) i
    interpolate i = transition tidal True (Sound.Tidal.Transition.interpolate) i
    interpolateIn i t = transition tidal True (Sound.Tidal.Transition.interpolateIn t) i
    clutch i = transition tidal True (Sound.Tidal.Transition.clutch) i
    clutchIn i t = transition tidal True (Sound.Tidal.Transition.clutchIn t) i
    anticipate i = transition tidal True (Sound.Tidal.Transition.anticipate) i
    anticipateIn i t = transition tidal True (Sound.Tidal.Transition.anticipateIn t) i
    forId i t = transition tidal False (Sound.Tidal.Transition.mortalOverlay t) i
    d1 = p 1 . (|< orbit 0)
    d2 = p 2 . (|< orbit 1)
    d3 = p 3 . (|< orbit 2)
    d4 = p 4 . (|< orbit 3)
    d5 = p 5 . (|< orbit 4)
    d6 = p 6 . (|< orbit 5)
    d7 = p 7 . (|< orbit 6)
    d8 = p 8 . (|< orbit 7)
    d9 = p 9 . (|< orbit 8)
    d10 = p 10 . (|< orbit 9)
    d11 = p 11 . (|< orbit 10)
    d12 = p 12 . (|< orbit 11)
    d13 = p 13
    d14 = p 14
    d15 = p 15
    d16 = p 16
    sel = select
    shiftBy num x = (num ~>) $ x -- pour la randomness
    shift x = shiftBy 1 $ x
    shrand n = shiftBy n $ rand
    --
    -- Tim Cowlishaw extra utilities:
    {-
    bpm2cps x = x / 60 / 4
    cps2bpm x = x * 4 * 60
    setbpm = setcps . bpm2cps
    getbpm = liftM cps2bpm $ getcps
    muteMultiple = mapM_ mute
    soloMultiple = mapM_ unsolo
    unmuteMultiple = mapM_ unmute
    unsoloMultiple = mapM_ unsolo
    -}
    -- Below Tim Cowlishaw already included in FM and called by script below :
    -- /Users/xon/Desktop/Live_Coding/Tidal/ExtraSynths/FM-Synths-params.hs
    {-
    fmamp op = pF ("amp" ++ show op)
    fmratio op = pF ("ratio" ++ show op)
    fmdetune op = pF ("detune" ++ show op)
    fmmod opa opb = pF ("mod" ++ show opa ++ show opb)
    fmfeedback = pF "feedback"
    fmeglevel op step = pF ("eglevel" ++ show op ++ show step)
    fmegrate op step = pF ("egrate" ++ show op ++ show step)
    -}
:}

:{
let getState = streamGet tidal
    setI = streamSetI tidal
    setF = streamSetF tidal
    setS = streamSetS tidal
    setR = streamSetR tidal
    setB = streamSetB tidal
:}

-- lessDense function https://club.tidalcycles.org/t/limit-events-based-on-delta-time/3121
-- To test
import Data.List(sortOn)
:{
densityFilter:: Eq a => Double -> [Event a] -> [Event a]
densityFilter density events = foldl (fi density) events [0..length events -1]
                      where fi density es n | length es > n = filter (\e -> e == es!!n || abs ((eventPartStart e) - (eventPartStart (es!!n))) >= toRational density ) es
                                            | otherwise = es

lessDense :: Eq a => Double -> Pattern a -> Pattern a
lessDense density p = p {query = (densityFilter density). sortOn whole . query p}
:}



:{
-- SuperDirtMixer related functions
-- Equalizer
let hiPassFreq = pF "hiPassFreq"
    hiPassRq   = pF "hiPassRq"
    hiPassBypass   = pF "hiPassBypass"
    loShelfFreq = pF "loShelfFreq"
    loShelfGain = pF "loShelfGain"
    loShelfRs   = pF "loShelfRs"
    loShelfBypass   = pF "loShelfBypass"
    loPeakFreq  = pF "loPeakFreq"
    loPeakGain  = pF "loPeakGain"
    loPeakRq    = pF "loPeakRq"
    loPeakBypass    = pF "loPeakBypass"
    midPeakFreq = pF "midPeakFreq"
    midPeakGain = pF "midPeakGain"
    midPeakRq   = pF "midPeakRq"
    midPeakBypass   = pF "midPeakBypass"
    hiPeakFreq  = pF "hiPeakFreq"
    hiPeakGain  = pF "hiPeakGain"
    hiPeakRq    = pF "hiPeakRq"
    hiPeakBypass    = pF "hiPeakBypass"
    hiShelfFreq = pF "hiShelfFreq"
    hiShelfGain = pF "hiShelfGain"
    hiShelfRs   = pF "hiShelfRs"
    hiShelfBypass   = pF "hiShelfBypass"
    loPassFreq = pF "loPassFreq"
    loPassRq   = pF "loPassRq"
    loPassBypass   = pF "loPassBypass"
-- Compressor
    cpAttack = pF "cpAttack"
    cpRelease = pF "cpRelease"
    cpThresh = pF "cpThresh"
    cpTrim = pF "cpTrim"
    cpGain = pF "cpGain"
    cpRatio = pF "cpRatio"
    cpLookahead = pF "cpLookahead"
    cpSaturate = pF "cpSaturate"
    cpHpf = pF "cpHpf"
    cpKnee = pF "cpKnee"
    cpBias = pF "cpBias"
-- Mixer
    masterGain = pF "masterGain"
:}


:script /Users/xon/Desktop/Live_Coding/Tidal/ExtraSynths/FM-Synths-params.hs
:script /Users/xon/Desktop/Live_Coding/Tidal/ExtraSynths/Mi-UGens-params.hs
:script /Users/xon/Desktop/Live_Coding/Tidal/ExtraSynths/ExtraSynths-params.hs
-- :script /Users/xon/Desktop/Live_Coding/Tidal/ExtraSynths/VST-params.hs


-- Script de Geikha
-- https://github.com/geikha/BUROCRACIA_JAJA/blob/main/SETUP_250225.tidal
:script /Users/xon/Desktop/Live_Coding/Tidal/G_Setup_250225.tidal


:set prompt "tidal> "
:set prompt-cont ""

default (Pattern String, Integer, Double)
