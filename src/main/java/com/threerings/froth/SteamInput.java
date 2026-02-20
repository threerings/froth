package com.threerings.froth;

/**
 * Represents the Steam Input interface. Steam Input is a flexible input API that supports
 * over three hundred devices including all common variants of Xbox, Playstation, Nintendo
 * Switch Pro, and Steam Controllers.
 */
public class SteamInput
{
    /** The maximum number of connected controllers supported. */
    public static final int MAX_COUNT = 16;

    /** The maximum number of active action set layers. */
    public static final int MAX_ACTIVE_LAYERS = 16;

    /** The maximum number of origins for a single action. */
    public static final int MAX_ORIGINS = 8;

    /** When sending an option to all controllers. */
    public static final long HANDLE_ALL_CONTROLLERS = -1L; // UINT64_MAX

    /** The input source modes. */
    public enum InputSourceMode
    {
        // Note: ordinals correspond to native EInputSourceMode values. Do not reorder!
        NONE, DPAD, BUTTONS, FOUR_BUTTONS, ABSOLUTE_MOUSE, RELATIVE_MOUSE,
        JOYSTICK_MOVE, JOYSTICK_MOUSE, JOYSTICK_CAMERA, SCROLL_WHEEL,
        TRIGGER, TOUCH_MENU, MOUSE_JOYSTICK, MOUSE_REGION, RADIAL_MENU,
        SINGLE_BUTTON, SWITCHES,
        ;
    }

    /** The Steam input device types. */
    public enum InputType
    {
        // Note: ordinals correspond to native ESteamInputType values. Do not reorder!
        UNKNOWN,
        STEAM_CONTROLLER,
        XBOX_360_CONTROLLER,
        XBOX_ONE_CONTROLLER,
        GENERIC_GAMEPAD,
        PS4_CONTROLLER,
        APPLE_MFI_CONTROLLER,
        ANDROID_CONTROLLER,
        SWITCH_JOYCON_PAIR,
        SWITCH_JOYCON_SINGLE,
        SWITCH_PRO_CONTROLLER,
        MOBILE_TOUCH,
        PS3_CONTROLLER,
        PS5_CONTROLLER,
        STEAM_DECK_CONTROLLER,
        ;
    }

    /** Sizes for PNG glyphs. */
    public enum GlyphSize
    {
        // Note: ordinals correspond to native ESteamInputGlyphSize values. Do not reorder!
        /** 32x32 pixels. */
        SMALL,
        /** 128x128 pixels. */
        MEDIUM,
        /** 256x256 pixels. */
        LARGE,
        ;
    }

    /** Flags for {@link #setLEDColor}. */
    public enum LEDFlag
    {
        // Note: ordinals correspond to native ESteamInputLEDFlag values. Do not reorder!
        SET_COLOR,
        RESTORE_USER_DEFAULT,
        ;
    }

    /** The controller pads for legacy haptic pulse methods. */
    public enum ControllerPad
    {
        // Note: ordinals correspond to native ESteamControllerPad values. Do not reorder!
        LEFT,
        RIGHT,
        ;
    }

    /**
     * Represents the current state of a digital action.
     */
    public static final class DigitalActionData
    {
        /** Whether the action is currently pressed. */
        public boolean state;

        /** Whether the action is currently available to be bound in the active action set. */
        public boolean active;

        @Override
        public String toString ()
        {
            return "DigitalActionData{state=" + state + ", active=" + active + "}";
        }

        /** Internal mechanism for populating this class from the native method. */
        private void setValues (boolean state, boolean active)
        {
            this.state = state;
            this.active = active;
        }
    }

    /**
     * Represents the current state of an analog action.
     */
    public static final class AnalogActionData
    {
        /** The type of data coming from this action. */
        public InputSourceMode mode;

        /** The current state of this action (x axis). */
        public float x;

        /** The current state of this action (y axis). */
        public float y;

        /** Whether the action is currently available to be bound in the active action set. */
        public boolean active;

        @Override
        public String toString ()
        {
            return "AnalogActionData{mode=" + mode + ", x=" + x + ", y=" + y +
                ", active=" + active + "}";
        }

        /** Internal mechanism for populating this class from the native method. */
        private void setValues (int mode, float x, float y, boolean active)
        {
            InputSourceMode[] modes = InputSourceMode.values();
            this.mode = (mode >= 0 && mode < modes.length) ? modes[mode] : InputSourceMode.NONE;
            this.x = x;
            this.y = y;
            this.active = active;
        }
    }

    /**
     * Represents raw motion data from a controller.
     */
    public static final class MotionData
    {
        /** Gyro quaternion components. */
        public float rotQuatX, rotQuatY, rotQuatZ, rotQuatW;

        /** Positional acceleration components. */
        public float posAccelX, posAccelY, posAccelZ;

        /** Angular velocity components. */
        public float rotVelX, rotVelY, rotVelZ;

        @Override
        public String toString ()
        {
            return "MotionData{rotQuat=(" + rotQuatX + ", " + rotQuatY + ", " +
                rotQuatZ + ", " + rotQuatW + "), posAccel=(" + posAccelX + ", " +
                posAccelY + ", " + posAccelZ + "), rotVel=(" + rotVelX + ", " +
                rotVelY + ", " + rotVelZ + ")}";
        }

        /** Internal mechanism for populating this class from the native method. */
        private void setValues (
            float rotQuatX, float rotQuatY, float rotQuatZ, float rotQuatW,
            float posAccelX, float posAccelY, float posAccelZ,
            float rotVelX, float rotVelY, float rotVelZ)
        {
            this.rotQuatX = rotQuatX;
            this.rotQuatY = rotQuatY;
            this.rotQuatZ = rotQuatZ;
            this.rotQuatW = rotQuatW;
            this.posAccelX = posAccelX;
            this.posAccelY = posAccelY;
            this.posAccelZ = posAccelZ;
            this.rotVelX = rotVelX;
            this.rotVelY = rotVelY;
            this.rotVelZ = rotVelZ;
        }
    }

    // ---- Lifecycle ----

    /**
     * Initializes the Steam Input interface. Must be called before any other SteamInput methods.
     *
     * @param explicitlyCallRunFrame if true, you must manually call {@link #runFrame} each frame;
     * otherwise Steam Input will be updated when {@link SteamAPI#runCallbacks} is called.
     * @return true on success.
     */
    public static boolean init (boolean explicitlyCallRunFrame)
    {
        _initialized = SteamAPI.isSteamRunning() && nInit(explicitlyCallRunFrame);
        return _initialized;
    }

    /**
     * Shuts down the Steam Input interface.
     *
     * @return true on success.
     */
    public static boolean shutdown ()
    {
        if (_initialized && nShutdown()) {
            _initialized = false;
            return true;
        }
        return false;
    }

    /**
     * Sets the absolute path to the Input Action Manifest file containing the in-game actions
     * and file paths to the official configurations.
     *
     * @return true on success.
     */
    public static boolean setInputActionManifestFilePath (String path)
    {
        if (path == null) {
            throw new NullPointerException("path");
        }
        return _initialized && nSetInputActionManifestFilePath(path);
    }

    /**
     * Synchronizes API state with the latest Steam Input action data available. This is performed
     * automatically by {@link SteamAPI#runCallbacks}, but for the absolute lowest possible
     * latency, you can call this directly before reading controller state.
     */
    public static void runFrame ()
    {
        if (_initialized) {
            nRunFrame();
        }
    }

    /**
     * Waits on an IPC event from Steam sent when there is new data to be fetched from
     * the data drop. Useful for games with a dedicated input thread.
     *
     * @param waitForever if true, waits indefinitely for data.
     * @param timeout timeout in milliseconds if not waiting forever.
     * @return true when data was received before the timeout expires.
     */
    public static boolean waitForData (boolean waitForever, int timeout)
    {
        return _initialized && nBWaitForData(waitForever, timeout);
    }

    /**
     * Returns true if new data has been received since the last time action data was accessed
     * via {@link #getDigitalActionData} or {@link #getAnalogActionData}.
     */
    public static boolean newDataAvailable ()
    {
        return _initialized && nBNewDataAvailable();
    }

    // ---- Controllers ----

    /**
     * Enumerates currently connected Steam Input enabled devices.
     *
     * @param handlesOut an array of at least {@link #MAX_COUNT} elements to receive the handles.
     * @return the number of handles written to handlesOut.
     */
    public static int getConnectedControllers (long[] handlesOut)
    {
        if (handlesOut == null) {
            throw new NullPointerException("handlesOut");
        }
        return _initialized ? nGetConnectedControllers(handlesOut) : 0;
    }

    /**
     * Enables {@code SteamInputDeviceConnected_t} and {@code SteamInputDeviceDisconnected_t}
     * callbacks. Each controller that is already connected will generate a device connected
     * callback when you enable them.
     */
    public static void enableDeviceCallbacks ()
    {
        if (_initialized) {
            nEnableDeviceCallbacks();
        }
    }

    // ---- Action Sets ----

    /**
     * Looks up the handle for an Action Set. Best to do this once on startup, and store the
     * handles for all future API calls.
     *
     * @param actionSetName the name of the action set (e.g. "Menu", "Walk", "Drive").
     * @return the handle, or 0 if not found.
     */
    public static long getActionSetHandle (String actionSetName)
    {
        if (actionSetName == null) {
            throw new NullPointerException("actionSetName");
        }
        return _initialized ? nGetActionSetHandle(actionSetName) : 0;
    }

    /**
     * Reconfigures the controller to use the specified action set. This is cheap, and can be
     * safely called repeatedly.
     *
     * @param inputHandle the controller handle, or {@link #HANDLE_ALL_CONTROLLERS} for all.
     * @param actionSetHandle the action set handle obtained from {@link #getActionSetHandle}.
     */
    public static void activateActionSet (long inputHandle, long actionSetHandle)
    {
        if (_initialized) {
            nActivateActionSet(inputHandle, actionSetHandle);
        }
    }

    /**
     * Returns the current action set handle for the specified controller.
     */
    public static long getCurrentActionSet (long inputHandle)
    {
        return _initialized ? nGetCurrentActionSet(inputHandle) : 0;
    }

    // ---- Action Set Layers ----

    /**
     * Activates an action set layer for the specified controller.
     */
    public static void activateActionSetLayer (long inputHandle, long actionSetLayerHandle)
    {
        if (_initialized) {
            nActivateActionSetLayer(inputHandle, actionSetLayerHandle);
        }
    }

    /**
     * Deactivates an action set layer for the specified controller.
     */
    public static void deactivateActionSetLayer (long inputHandle, long actionSetLayerHandle)
    {
        if (_initialized) {
            nDeactivateActionSetLayer(inputHandle, actionSetLayerHandle);
        }
    }

    /**
     * Deactivates all action set layers for the specified controller.
     */
    public static void deactivateAllActionSetLayers (long inputHandle)
    {
        if (_initialized) {
            nDeactivateAllActionSetLayers(inputHandle);
        }
    }

    /**
     * Enumerates currently active action set layers for the specified controller.
     *
     * @param inputHandle the controller handle.
     * @param handlesOut an array of at least {@link #MAX_ACTIVE_LAYERS} elements.
     * @return the number of handles written to handlesOut.
     */
    public static int getActiveActionSetLayers (long inputHandle, long[] handlesOut)
    {
        if (handlesOut == null) {
            throw new NullPointerException("handlesOut");
        }
        return _initialized ? nGetActiveActionSetLayers(inputHandle, handlesOut) : 0;
    }

    // ---- Digital Actions ----

    /**
     * Looks up the handle for a digital action. Best to do this once on startup.
     *
     * @return the handle, or 0 if not found.
     */
    public static long getDigitalActionHandle (String actionName)
    {
        if (actionName == null) {
            throw new NullPointerException("actionName");
        }
        return _initialized ? nGetDigitalActionHandle(actionName) : 0;
    }

    /**
     * Returns the current state of the supplied digital game action.
     *
     * @param inputHandle the controller handle.
     * @param digitalActionHandle the digital action handle.
     * @param data the data object to populate with the result.
     * @return true if the data was successfully populated.
     */
    public static boolean getDigitalActionData (
        long inputHandle, long digitalActionHandle, DigitalActionData data)
    {
        if (data == null) {
            throw new NullPointerException("data");
        }
        return _initialized && nGetDigitalActionData(inputHandle, digitalActionHandle, data);
    }

    /**
     * Gets the origin(s) for a digital action within an action set.
     *
     * @param inputHandle the controller handle.
     * @param actionSetHandle the action set handle.
     * @param digitalActionHandle the digital action handle.
     * @param originsOut an array of at least {@link #MAX_ORIGINS} elements.
     * @return the number of origins written to originsOut.
     */
    public static int getDigitalActionOrigins (
        long inputHandle, long actionSetHandle, long digitalActionHandle, int[] originsOut)
    {
        if (originsOut == null) {
            throw new NullPointerException("originsOut");
        }
        return _initialized ?
            nGetDigitalActionOrigins(inputHandle, actionSetHandle, digitalActionHandle,
                originsOut) : 0;
    }

    /**
     * Returns a localized string for the user-facing action name corresponding to the
     * specified digital action handle.
     */
    public static String getStringForDigitalActionName (long digitalActionHandle)
    {
        return _initialized ? nGetStringForDigitalActionName(digitalActionHandle) : null;
    }

    // ---- Analog Actions ----

    /**
     * Looks up the handle for an analog action. Best to do this once on startup.
     *
     * @return the handle, or 0 if not found.
     */
    public static long getAnalogActionHandle (String actionName)
    {
        if (actionName == null) {
            throw new NullPointerException("actionName");
        }
        return _initialized ? nGetAnalogActionHandle(actionName) : 0;
    }

    /**
     * Returns the current state of the supplied analog game action.
     *
     * @param inputHandle the controller handle.
     * @param analogActionHandle the analog action handle.
     * @param data the data object to populate with the result.
     * @return true if the data was successfully populated.
     */
    public static boolean getAnalogActionData (
        long inputHandle, long analogActionHandle, AnalogActionData data)
    {
        if (data == null) {
            throw new NullPointerException("data");
        }
        return _initialized && nGetAnalogActionData(inputHandle, analogActionHandle, data);
    }

    /**
     * Gets the origin(s) for an analog action within an action set.
     *
     * @param inputHandle the controller handle.
     * @param actionSetHandle the action set handle.
     * @param analogActionHandle the analog action handle.
     * @param originsOut an array of at least {@link #MAX_ORIGINS} elements.
     * @return the number of origins written to originsOut.
     */
    public static int getAnalogActionOrigins (
        long inputHandle, long actionSetHandle, long analogActionHandle, int[] originsOut)
    {
        if (originsOut == null) {
            throw new NullPointerException("originsOut");
        }
        return _initialized ?
            nGetAnalogActionOrigins(inputHandle, actionSetHandle, analogActionHandle,
                originsOut) : 0;
    }

    /**
     * Returns a localized string for the user-facing action name corresponding to the
     * specified analog action handle.
     */
    public static String getStringForAnalogActionName (long analogActionHandle)
    {
        return _initialized ? nGetStringForAnalogActionName(analogActionHandle) : null;
    }

    /**
     * Stops analog momentum for the action if it is a mouse action in trackball mode.
     */
    public static void stopAnalogActionMomentum (long inputHandle, long analogActionHandle)
    {
        if (_initialized) {
            nStopAnalogActionMomentum(inputHandle, analogActionHandle);
        }
    }

    // ---- Glyphs and Strings ----

    /**
     * Gets a local path to a PNG file for the provided origin's glyph.
     *
     * @param origin the action origin (from {@link #getDigitalActionOrigins} or similar).
     * @param size the desired glyph size.
     * @param flags style flags (see ESteamInputGlyphStyle in the Steamworks SDK).
     * @return the file path, or null if unavailable.
     */
    public static String getGlyphPNGForActionOrigin (int origin, GlyphSize size, int flags)
    {
        if (size == null) {
            throw new NullPointerException("size");
        }
        return _initialized ? nGetGlyphPNGForActionOrigin(origin, size.ordinal(), flags) : null;
    }

    /**
     * Gets a local path to an SVG file for the provided origin's glyph.
     *
     * @param origin the action origin.
     * @param flags style flags.
     * @return the file path, or null if unavailable.
     */
    public static String getGlyphSVGForActionOrigin (int origin, int flags)
    {
        return _initialized ? nGetGlyphSVGForActionOrigin(origin, flags) : null;
    }

    /**
     * Gets a local path to an older, Big Picture Mode-style PNG file for a particular origin.
     *
     * @param origin the action origin.
     * @return the file path, or null if unavailable.
     */
    public static String getGlyphForActionOriginLegacy (int origin)
    {
        return _initialized ? nGetGlyphForActionOriginLegacy(origin) : null;
    }

    /**
     * Returns a localized string for the specified action origin.
     */
    public static String getStringForActionOrigin (int origin)
    {
        return _initialized ? nGetStringForActionOrigin(origin) : null;
    }

    /**
     * Returns a localized string for the specified Xbox controller origin.
     */
    public static String getStringForXboxOrigin (int origin)
    {
        return _initialized ? nGetStringForXboxOrigin(origin) : null;
    }

    /**
     * Gets a local path to art for on-screen glyph for a particular Xbox controller origin.
     */
    public static String getGlyphForXboxOrigin (int origin)
    {
        return _initialized ? nGetGlyphForXboxOrigin(origin) : null;
    }

    /**
     * Gets the equivalent action origin for a given Xbox controller origin.
     *
     * @return the equivalent action origin, or 0 (None) if unavailable.
     */
    public static int getActionOriginFromXboxOrigin (long inputHandle, int xboxOrigin)
    {
        return _initialized ? nGetActionOriginFromXboxOrigin(inputHandle, xboxOrigin) : 0;
    }

    /**
     * Converts an origin to another controller type. For inputs not present on the other
     * controller type this will return 0 (None).
     */
    public static int translateActionOrigin (InputType destinationType, int sourceOrigin)
    {
        if (destinationType == null) {
            throw new NullPointerException("destinationType");
        }
        return _initialized ?
            nTranslateActionOrigin(destinationType.ordinal(), sourceOrigin) : 0;
    }

    // ---- Motion Data ----

    /**
     * Returns raw motion data from the specified controller.
     *
     * @param inputHandle the controller handle.
     * @param data the data object to populate with the result.
     * @return true if the data was successfully populated.
     */
    public static boolean getMotionData (long inputHandle, MotionData data)
    {
        if (data == null) {
            throw new NullPointerException("data");
        }
        return _initialized && nGetMotionData(inputHandle, data);
    }

    // ---- Haptics and LEDs ----

    /**
     * Triggers a vibration event on supported controllers. Steam will translate these commands
     * into haptic pulses for Steam Controllers.
     *
     * @param inputHandle the controller handle.
     * @param leftSpeed the left motor speed (0-65535).
     * @param rightSpeed the right motor speed (0-65535).
     */
    public static void triggerVibration (long inputHandle, int leftSpeed, int rightSpeed)
    {
        if (_initialized) {
            nTriggerVibration(inputHandle, leftSpeed, rightSpeed);
        }
    }

    /**
     * Triggers a vibration event on supported controllers including Xbox trigger impulse rumble.
     *
     * @param inputHandle the controller handle.
     * @param leftSpeed the left motor speed (0-65535).
     * @param rightSpeed the right motor speed (0-65535).
     * @param leftTriggerSpeed the left trigger motor speed (0-65535).
     * @param rightTriggerSpeed the right trigger motor speed (0-65535).
     */
    public static void triggerVibrationExtended (
        long inputHandle, int leftSpeed, int rightSpeed,
        int leftTriggerSpeed, int rightTriggerSpeed)
    {
        if (_initialized) {
            nTriggerVibrationExtended(inputHandle, leftSpeed, rightSpeed,
                leftTriggerSpeed, rightTriggerSpeed);
        }
    }

    /**
     * Sends a haptic pulse, works on Steam Deck and Steam Controller devices.
     *
     * @param inputHandle the controller handle.
     * @param hapticLocation the location bitmask (1=left, 2=right, 3=both).
     * @param intensity the intensity of the haptic pulse.
     * @param gainDB the gain in dB.
     * @param otherIntensity the intensity of the other channel.
     * @param otherGainDB the gain in dB of the other channel.
     */
    public static void triggerSimpleHapticEvent (
        long inputHandle, int hapticLocation,
        int intensity, int gainDB, int otherIntensity, int otherGainDB)
    {
        if (_initialized) {
            nTriggerSimpleHapticEvent(inputHandle, hapticLocation,
                intensity, gainDB, otherIntensity, otherGainDB);
        }
    }

    /**
     * Sets the controller LED color on supported controllers.
     *
     * @param inputHandle the controller handle.
     * @param colorR red component (0-255).
     * @param colorG green component (0-255).
     * @param colorB blue component (0-255).
     * @param flag the LED flag controlling behavior.
     */
    public static void setLEDColor (
        long inputHandle, int colorR, int colorG, int colorB, LEDFlag flag)
    {
        if (flag == null) {
            throw new NullPointerException("flag");
        }
        if (_initialized) {
            nSetLEDColor(inputHandle, colorR, colorG, colorB, flag.ordinal());
        }
    }

    /**
     * Triggers a haptic pulse on a Steam Controller. If you are approximating rumble you may
     * want to use {@link #triggerVibration} instead.
     *
     * @param inputHandle the controller handle.
     * @param targetPad the target pad.
     * @param durationMicroSec the pulse duration in microseconds.
     */
    public static void legacyTriggerHapticPulse (
        long inputHandle, ControllerPad targetPad, int durationMicroSec)
    {
        if (targetPad == null) {
            throw new NullPointerException("targetPad");
        }
        if (_initialized) {
            nLegacyTriggerHapticPulse(inputHandle, targetPad.ordinal(), durationMicroSec);
        }
    }

    /**
     * Triggers a repeated haptic pulse on a Steam Controller.
     *
     * @param inputHandle the controller handle.
     * @param targetPad the target pad.
     * @param durationMicroSec the on-pulse duration in microseconds.
     * @param offMicroSec the off duration in microseconds.
     * @param repeat the number of repetitions.
     * @param flags reserved for future use (pass 0).
     */
    public static void legacyTriggerRepeatedHapticPulse (
        long inputHandle, ControllerPad targetPad,
        int durationMicroSec, int offMicroSec, int repeat, int flags)
    {
        if (targetPad == null) {
            throw new NullPointerException("targetPad");
        }
        if (_initialized) {
            nLegacyTriggerRepeatedHapticPulse(inputHandle, targetPad.ordinal(),
                durationMicroSec, offMicroSec, repeat, flags);
        }
    }

    // ---- Utility ----

    /**
     * Invokes the Steam overlay and brings up the binding screen.
     *
     * @param inputHandle the controller handle.
     * @return true if the overlay was successfully shown.
     */
    public static boolean showBindingPanel (long inputHandle)
    {
        return _initialized && nShowBindingPanel(inputHandle);
    }

    /**
     * Returns the input type for a particular controller handle.
     *
     * @return the input type, or {@link InputType#UNKNOWN} if not initialized or not found.
     */
    public static InputType getInputTypeForHandle (long inputHandle)
    {
        if (!_initialized) {
            return InputType.UNKNOWN;
        }
        int type = nGetInputTypeForHandle(inputHandle);
        InputType[] types = InputType.values();
        return (type >= 0 && type < types.length) ? types[type] : InputType.UNKNOWN;
    }

    /**
     * Returns the associated controller handle for the specified emulated gamepad index.
     *
     * @return the controller handle, or 0 if not associated with Steam Input.
     */
    public static long getControllerForGamepadIndex (int index)
    {
        return _initialized ? nGetControllerForGamepadIndex(index) : 0;
    }

    /**
     * Returns the associated gamepad index for the specified controller, or -1 if not
     * associated with an XInput index.
     */
    public static int getGamepadIndexForController (long inputHandle)
    {
        return _initialized ? nGetGamepadIndexForController(inputHandle) : -1;
    }

    /**
     * Gets the binding revision for a given device.
     *
     * @param inputHandle the controller handle.
     * @param revisionOut an array of at least 2 elements to receive [major, minor].
     * @return false if the handle was not valid or if a mapping is not yet loaded.
     */
    public static boolean getDeviceBindingRevision (long inputHandle, int[] revisionOut)
    {
        if (revisionOut == null || revisionOut.length < 2) {
            throw new IllegalArgumentException("revisionOut must have at least 2 elements");
        }
        return _initialized && nGetDeviceBindingRevision(inputHandle, revisionOut);
    }

    /**
     * Gets the Steam Remote Play session ID associated with a device, or 0 if none.
     */
    public static int getRemotePlaySessionID (long inputHandle)
    {
        return _initialized ? nGetRemotePlaySessionID(inputHandle) : 0;
    }

    /**
     * Gets a bitmask of the Steam Input Configuration types opted in for the current session.
     */
    public static int getSessionInputConfigurationSettings ()
    {
        return _initialized ? nGetSessionInputConfigurationSettings() : 0;
    }

    // ---- Native methods ----

    /** Native: init. */
    protected static native boolean nInit (boolean explicitlyCallRunFrame);

    /** Native: shutdown. */
    protected static native boolean nShutdown ();

    /** Native: setInputActionManifestFilePath. */
    protected static native boolean nSetInputActionManifestFilePath (String path);

    /** Native: runFrame. */
    protected static native void nRunFrame ();

    /** Native: bWaitForData. */
    protected static native boolean nBWaitForData (boolean waitForever, int timeout);

    /** Native: bNewDataAvailable. */
    protected static native boolean nBNewDataAvailable ();

    /** Native: getConnectedControllers. */
    protected static native int nGetConnectedControllers (long[] handlesOut);

    /** Native: enableDeviceCallbacks. */
    protected static native void nEnableDeviceCallbacks ();

    /** Native: getActionSetHandle. */
    protected static native long nGetActionSetHandle (String actionSetName);

    /** Native: activateActionSet. */
    protected static native void nActivateActionSet (long inputHandle, long actionSetHandle);

    /** Native: getCurrentActionSet. */
    protected static native long nGetCurrentActionSet (long inputHandle);

    /** Native: activateActionSetLayer. */
    protected static native void nActivateActionSetLayer (
        long inputHandle, long actionSetLayerHandle);

    /** Native: deactivateActionSetLayer. */
    protected static native void nDeactivateActionSetLayer (
        long inputHandle, long actionSetLayerHandle);

    /** Native: deactivateAllActionSetLayers. */
    protected static native void nDeactivateAllActionSetLayers (long inputHandle);

    /** Native: getActiveActionSetLayers. */
    protected static native int nGetActiveActionSetLayers (long inputHandle, long[] handlesOut);

    /** Native: getDigitalActionHandle. */
    protected static native long nGetDigitalActionHandle (String actionName);

    /** Native: getDigitalActionData. */
    protected static native boolean nGetDigitalActionData (
        long inputHandle, long digitalActionHandle, DigitalActionData data);

    /** Native: getDigitalActionOrigins. */
    protected static native int nGetDigitalActionOrigins (
        long inputHandle, long actionSetHandle, long digitalActionHandle, int[] originsOut);

    /** Native: getStringForDigitalActionName. */
    protected static native String nGetStringForDigitalActionName (long digitalActionHandle);

    /** Native: getAnalogActionHandle. */
    protected static native long nGetAnalogActionHandle (String actionName);

    /** Native: getAnalogActionData. */
    protected static native boolean nGetAnalogActionData (
        long inputHandle, long analogActionHandle, AnalogActionData data);

    /** Native: getAnalogActionOrigins. */
    protected static native int nGetAnalogActionOrigins (
        long inputHandle, long actionSetHandle, long analogActionHandle, int[] originsOut);

    /** Native: getStringForAnalogActionName. */
    protected static native String nGetStringForAnalogActionName (long analogActionHandle);

    /** Native: stopAnalogActionMomentum. */
    protected static native void nStopAnalogActionMomentum (
        long inputHandle, long analogActionHandle);

    /** Native: getMotionData. */
    protected static native boolean nGetMotionData (long inputHandle, MotionData data);

    /** Native: getGlyphPNGForActionOrigin. */
    protected static native String nGetGlyphPNGForActionOrigin (
        int origin, int size, int flags);

    /** Native: getGlyphSVGForActionOrigin. */
    protected static native String nGetGlyphSVGForActionOrigin (int origin, int flags);

    /** Native: getGlyphForActionOrigin_Legacy. */
    protected static native String nGetGlyphForActionOriginLegacy (int origin);

    /** Native: getStringForActionOrigin. */
    protected static native String nGetStringForActionOrigin (int origin);

    /** Native: getStringForXboxOrigin. */
    protected static native String nGetStringForXboxOrigin (int origin);

    /** Native: getGlyphForXboxOrigin. */
    protected static native String nGetGlyphForXboxOrigin (int origin);

    /** Native: getActionOriginFromXboxOrigin. */
    protected static native int nGetActionOriginFromXboxOrigin (
        long inputHandle, int xboxOrigin);

    /** Native: translateActionOrigin. */
    protected static native int nTranslateActionOrigin (int destinationType, int sourceOrigin);

    /** Native: triggerVibration. */
    protected static native void nTriggerVibration (
        long inputHandle, int leftSpeed, int rightSpeed);

    /** Native: triggerVibrationExtended. */
    protected static native void nTriggerVibrationExtended (
        long inputHandle, int leftSpeed, int rightSpeed,
        int leftTriggerSpeed, int rightTriggerSpeed);

    /** Native: triggerSimpleHapticEvent. */
    protected static native void nTriggerSimpleHapticEvent (
        long inputHandle, int hapticLocation,
        int intensity, int gainDB, int otherIntensity, int otherGainDB);

    /** Native: setLEDColor. */
    protected static native void nSetLEDColor (
        long inputHandle, int colorR, int colorG, int colorB, int flags);

    /** Native: legacy_TriggerHapticPulse. */
    protected static native void nLegacyTriggerHapticPulse (
        long inputHandle, int targetPad, int durationMicroSec);

    /** Native: legacy_TriggerRepeatedHapticPulse. */
    protected static native void nLegacyTriggerRepeatedHapticPulse (
        long inputHandle, int targetPad,
        int durationMicroSec, int offMicroSec, int repeat, int flags);

    /** Native: showBindingPanel. */
    protected static native boolean nShowBindingPanel (long inputHandle);

    /** Native: getInputTypeForHandle. */
    protected static native int nGetInputTypeForHandle (long inputHandle);

    /** Native: getControllerForGamepadIndex. */
    protected static native long nGetControllerForGamepadIndex (int index);

    /** Native: getGamepadIndexForController. */
    protected static native int nGetGamepadIndexForController (long inputHandle);

    /** Native: getDeviceBindingRevision. */
    protected static native boolean nGetDeviceBindingRevision (
        long inputHandle, int[] revisionOut);

    /** Native: getRemotePlaySessionID. */
    protected static native int nGetRemotePlaySessionID (long inputHandle);

    /** Native: getSessionInputConfigurationSettings. */
    protected static native int nGetSessionInputConfigurationSettings ();

    /** Are we initialized? */
    protected static boolean _initialized;
}
